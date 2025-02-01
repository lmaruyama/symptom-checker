package com.healthly.symptomchecker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healthly.symptomchecker.data.PossibleAnswer;

public record AssessmentAnswerRequest (
        @JsonProperty("question_id") String questionId,
        @JsonProperty("response") PossibleAnswer response) {
}
