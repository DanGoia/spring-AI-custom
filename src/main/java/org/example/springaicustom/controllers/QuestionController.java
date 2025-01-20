package org.example.springaicustom.controllers;

import org.example.springaicustom.model.Answer;
import org.example.springaicustom.model.Question;
import org.example.springaicustom.services.OpenAIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

    private final OpenAIService openAIService;


    public QuestionController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/ask")
    public Answer askQuestion(Question question) {
        return openAIService.getAnswer(question);
    }
}
