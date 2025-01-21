package org.example.springaicustom.impl;

import org.example.springaicustom.model.GetDeveloperRequest;
import org.example.springaicustom.model.GetResponse;
import org.example.springaicustom.services.OpenAIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OpenAIServiceImplTest {
    @Autowired
    OpenAIService openAIService;
    @Test
    void getJsonAnswer() {
        GetResponse answer= openAIService.getJsonAnswer(new GetDeveloperRequest(List.of("Java")));
        System.out.println("Retriving answer: " + answer);
    }
}