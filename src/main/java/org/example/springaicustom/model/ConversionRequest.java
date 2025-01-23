package org.example.springaicustom.model;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonClassDescription("Currency converter API request")
public record ConversionRequest(@JsonProperty(required = true, value = "have")
                                   @JsonPropertyDescription("Currency the person who want to make the exchange currently hold. Must be 3-character currency code (e.g. USD).") String have,
                                @JsonProperty(required = true) @JsonPropertyDescription("Currency the person who want to make the exchange want to convert to. Must be 3-character currency code (e.g. USD).") String want,
                                @JsonProperty(required = true) @JsonPropertyDescription("Amount of currency to convert.") String amount) {
}
