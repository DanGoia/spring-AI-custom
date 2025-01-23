package org.example.springaicustom.controllers;

import lombok.RequiredArgsConstructor;
import org.example.springaicustom.model.Answer;
import org.example.springaicustom.model.Question;
import org.example.springaicustom.services.OpenAIWeatherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConversionController {
    private final OpenAIWeatherService openAIWeatherService;

    @PostMapping(value = "/convert")
    public Answer convert(@RequestBody Question question) {
        return openAIWeatherService.getAnswer(question);
    }
}
