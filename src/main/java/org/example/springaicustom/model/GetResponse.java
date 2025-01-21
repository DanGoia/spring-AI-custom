package org.example.springaicustom.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record GetResponse(
        @JsonPropertyDescription("This is a list of the languages that were asked in the question") List<String> languages,
        @JsonPropertyDescription("This is the list of links that are related to the question topics") List<String> topicRelatedLinks,
        @JsonPropertyDescription("This is the the average salary range for the Junior developer position") String averageJuniorSalary,
        @JsonPropertyDescription("This is the the average salary range for the Mid developer position") String averageMidSalary,
        @JsonPropertyDescription("This is the the average salary range for the Senior developer position") String averageSeniorSalary) {
}
