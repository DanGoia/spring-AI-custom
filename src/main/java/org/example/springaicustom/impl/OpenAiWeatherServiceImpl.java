package org.example.springaicustom.impl;

import lombok.RequiredArgsConstructor;
import org.example.springaicustom.model.Answer;
import org.example.springaicustom.model.Question;
import org.example.springaicustom.model.ConversionRequest;
import org.example.springaicustom.model.ConversionResponse;
import org.example.springaicustom.services.OpenAIWeatherService;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OpenAiWeatherServiceImpl implements OpenAIWeatherService {
    @Value("${customaiapp.weather.apiNinjasKey}")
    private String apiNinjasKey;

    private final OpenAiChatModel openAiChatModel;

    @Override
    public Answer getAnswer(Question question) {
        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("Converter",new ConversionFunction(apiNinjasKey))
                        .inputType(ConversionRequest.class)
                        .description("Calculate the conversion for an exchange rate")
                        .responseConverter(response -> {
                            String schema = ModelOptionsUtils.getJsonSchema(ConversionResponse.class, false);
                            String json = ModelOptionsUtils.toJsonString(response);
                            return schema + "\n" + json;
                        })
                        .build()))
                .build();

        Message userMessage = new PromptTemplate(question.question()).createMessage();

        var response = openAiChatModel.call(new Prompt(List.of(userMessage), promptOptions));

        return new Answer(response.getResult().getOutput().getContent());
    }
}
