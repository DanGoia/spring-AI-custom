package org.example.springaicustom.impl;

import org.example.springaicustom.services.OpenAIService;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class OpenAIServiceImplTest {
    @Autowired
    OpenAIService openAIService;
    @Test
    void getAnswer() {
        String answer= openAIService.getAnswer("4 + 4 = ?");
        System.out.println("Retriving answer: " + answer);
    }
}