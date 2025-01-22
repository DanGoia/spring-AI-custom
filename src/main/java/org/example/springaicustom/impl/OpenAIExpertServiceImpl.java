package org.example.springaicustom.impl;

import lombok.RequiredArgsConstructor;
import org.example.springaicustom.model.Answer;
import org.example.springaicustom.model.Question;
import org.example.springaicustom.services.OpenAIExpertService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAIExpertServiceImpl implements OpenAIExpertService {
    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    @Value("classpath:templates/get-developer-prompt.st")
    private Resource getDeveloperPrompt;

    @Value("classpath:templates/get-developer-interview-respond-formated.st")
    private Resource getDeveloperRespondFormatedPrompt;

    @Value("classpath:templates/rag-prompt.st")
    private Resource ragTemplatePrompt;

    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();
        ChatResponse response = chatModel.call(prompt);
        return new Answer(response.getResult().getOutput().getContent());
    }
}
