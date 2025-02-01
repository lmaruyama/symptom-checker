package com.healthly.symptomchecker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AssessmentStartResponse(
        @JsonProperty("assessment_id") String assessmentId,
        @JsonProperty("next_question_id") String nextQuestionId) {
}
