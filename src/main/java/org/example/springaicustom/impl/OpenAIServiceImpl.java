package org.example.springaicustom.impl;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService {
    private final ChatModel chatModel;

    @Value("classpath:templates/get-developer-prompt.st")
    private Resource getDeveloperPrompt;

    @Value("classpath:templates/get-developer-interview-respond-formated.st")
    private Resource getDeveloperRespondFormatedPrompt;


    public OpenAIServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
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
}
