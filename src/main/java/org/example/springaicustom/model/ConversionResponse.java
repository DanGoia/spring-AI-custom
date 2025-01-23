package org.example.springaicustom.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record ConversionResponse(@JsonPropertyDescription("Old Amount") String oldAmount,
                                 @JsonPropertyDescription("Old Currency") String oldCurrency,
                                 @JsonPropertyDescription("New Currency") String newCurrency,
                                 @JsonPropertyDescription("New Ammount") String newAmount) {
}
