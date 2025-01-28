package org.example.springaicustom.impl;

import org.example.springaicustom.model.ConversionRequest;
import org.example.springaicustom.model.ConversionResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.util.function.Function;

public class ConversionFunction implements Function<ConversionRequest, ConversionResponse> {
    public static final String CONVERT_CURRENCY_API_URL = "https://api.api-ninjas.com/v1/convertcurrency";

    private final String apiNinjasKey;

    public ConversionFunction(String apiNinjasKey) {
        this.apiNinjasKey = apiNinjasKey;
    }

    @Override
    public ConversionResponse apply(ConversionRequest conversionRequest) {
        RestClient restClient = RestClient.builder()
                .baseUrl(CONVERT_CURRENCY_API_URL)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Key", apiNinjasKey);
                    httpHeaders.set("Accept", "application/json");
                    httpHeaders.set("Content-Type", "application/json");
                }).build();

        return restClient.get().uri(uriBuilder -> {
            System.out.println("Building URI for currency convert request: " + conversionRequest);
            createUrl(conversionRequest, uriBuilder);
            return uriBuilder.build();
        }).retrieve().body(ConversionResponse.class);
    }

    private  void createUrl(ConversionRequest conversionRequest, UriBuilder uriBuilder) {
        uriBuilder.queryParam("have", conversionRequest.have());

        if (conversionRequest.want() != null && !conversionRequest.want().isBlank()) {
            uriBuilder.queryParam("want", conversionRequest.want());
        }
        if (conversionRequest.amount() != null && !conversionRequest.amount().isBlank()) {
            uriBuilder.queryParam("amount", conversionRequest.amount());
        }
    }
}
