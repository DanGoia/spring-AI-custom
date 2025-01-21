package org.example.springaicustom.controllers;

import org.example.springaicustom.model.Answer;
import org.example.springaicustom.model.GetDeveloperRequest;
import org.example.springaicustom.model.GetResponse;
import org.example.springaicustom.model.Question;
import org.example.springaicustom.services.OpenAIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

    private final OpenAIService openAIService;


    public QuestionController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return openAIService.getAnswer(question);
    }

    @PostMapping("/ask-developer")
    public Answer askDeveloperQuestion(@RequestBody GetDeveloperRequest topic) {
        return openAIService.getDeveloperAnswer(topic);
    }

    @PostMapping("/ask-developer-formated-response")
    public Answer askDeveloperFormatedQuestion(@RequestBody GetDeveloperRequest topic) {
        return openAIService.getFormatedDeveloperAnswer(topic);
    }

    @PostMapping("/ask-developer-json")
    public GetResponse askDeveloperForJsonResponse(@RequestBody GetDeveloperRequest topic) {
        return openAIService.getJsonAnswer(topic);
    }
}
