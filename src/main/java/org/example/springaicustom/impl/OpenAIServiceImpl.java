package org.example.springaicustom.impl;

import org.example.springaicustom.model.Answer;
import org.example.springaicustom.model.GetDeveloperRequest;
import org.example.springaicustom.model.GetResponse;
import org.example.springaicustom.model.Question;
import org.example.springaicustom.services.OpenAIService;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class OpenAIServiceImpl implements OpenAIService {
    private final ChatModel chatModel;
    private final SimpleVectorStore simpleVectorStore;
    private final VectorStore milvusConfigVectorStore;

    @Value("classpath:templates/get-developer-prompt.st")
    private Resource getDeveloperPrompt;

    @Value("classpath:templates/get-developer-interview-respond-formated.st")
    private Resource getDeveloperRespondFormatedPrompt;

    @Value("classpath:templates/rag-prompt.st")
    private Resource ragTemplatePrompt;

    @Value("classpath:templates/system-message.st")
    private Resource systemTemplatePrompt;

    @Autowired
    public OpenAIServiceImpl(ChatModel chatModel, @Qualifier("simpleVectorStore") SimpleVectorStore simpleVectorStore, @Qualifier("vectorStore") VectorStore milvusConfigVectorStore) {
        this.chatModel = chatModel;
        this.simpleVectorStore = simpleVectorStore;
        this.milvusConfigVectorStore = milvusConfigVectorStore;
    }

    @Override
    public GetResponse getJsonAnswer(GetDeveloperRequest topic) {
        BeanOutputConverter<GetResponse> converter = new BeanOutputConverter<>(GetResponse.class);
        String format = converter.getFormat();

        //Prints the Json format schema
        System.out.println(format);

        PromptTemplate promptTemplate = new PromptTemplate(getDeveloperPrompt);
        Prompt prompt = promptTemplate.create(Map.of("topic", topic.topic().toString(),
                "format", format));
        ChatResponse response = chatModel.call(prompt);
        return converter.convert(response.getResult().getOutput().getContent());
    }

    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();
        ChatResponse response = chatModel.call(prompt);
        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public Answer getDeveloperAnswer(GetDeveloperRequest topic) {
        PromptTemplate promptTemplate = new PromptTemplate(getDeveloperRespondFormatedPrompt);
        Prompt prompt = promptTemplate.create(Map.of("topic", topic.topic().toString()));
        ChatResponse response = chatModel.call(prompt);
        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public Answer getFormatedDeveloperAnswer(GetDeveloperRequest topic) {
        PromptTemplate promptTemplate = new PromptTemplate(getDeveloperRespondFormatedPrompt);
        Prompt prompt = promptTemplate.create(Map.of("topicRelatedLinks", topic.topic().toString()));
        ChatResponse response = chatModel.call(prompt);
        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public Answer getRagAnswer(Question question) {
        List<Document> documentList = simpleVectorStore.similaritySearch(SearchRequest.builder()
                .query(question.question()).topK(4).build());

        if (null != documentList && !documentList.isEmpty()) {
            List<String> contentList = documentList.stream().
                    map(Document::getFormattedContent).
                    toList();
            PromptTemplate promptTemplate = new PromptTemplate(ragTemplatePrompt);
            Prompt prompt = promptTemplate.create(Map.of("input", question.question(),
                    "documents", String.join("\n", contentList)));
            ChatResponse response = chatModel.call(prompt);

            return new Answer(response.getResult().getOutput().getContent());
        }
        return new Answer("empty heads !!!!!");
    }

    @Override
    public Answer getMilvusRagAnswer(Question question) {
        PromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemTemplatePrompt);
        Message systemMessage = systemPromptTemplate.createMessage();

        List<Document> documents = milvusConfigVectorStore.similaritySearch(SearchRequest.builder()
                .query(question.question()).topK(5).build());
        List<String> contentList = documents.stream().map(Document::getContent).toList();

        PromptTemplate promptTemplate = new PromptTemplate(ragTemplatePrompt);
        Message userMessage = promptTemplate.createMessage(Map.of("input", question.question(), "documents",
                String.join("\n", contentList)));

        ChatResponse response = chatModel.call(new Prompt(List.of(systemMessage, userMessage)));

        return new Answer(response.getResult().getOutput().getContent());
    }
}
