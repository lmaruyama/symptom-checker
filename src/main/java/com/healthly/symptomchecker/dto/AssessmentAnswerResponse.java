package com.healthly.symptomchecker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AssessmentAnswerResponse (@JsonProperty("next_question_id") String nextQuestionId) {
}
