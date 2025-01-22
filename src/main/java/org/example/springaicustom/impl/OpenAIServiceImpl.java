package org.example.springaicustom.impl;

import lombok.RequiredArgsConstructor;
import org.example.springaicustom.model.Answer;
import org.example.springaicustom.model.GetDeveloperRequest;
import org.example.springaicustom.model.GetResponse;
import org.example.springaicustom.model.Question;
import org.example.springaicustom.services.OpenAIService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements OpenAIService {
    private final ChatModel chatModel;
    private final SimpleVectorStore vectorStore;

    @Value("classpath:templates/get-developer-prompt.st")
    private Resource getDeveloperPrompt;

    @Value("classpath:templates/get-developer-interview-respond-formated.st")
    private Resource getDeveloperRespondFormatedPrompt;

    @Value("classpath:templates/rag-prompt.st")
    private Resource ragTemplatePrompt;

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
        List<Document> documentList = vectorStore.similaritySearch(SearchRequest.builder()
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
}
