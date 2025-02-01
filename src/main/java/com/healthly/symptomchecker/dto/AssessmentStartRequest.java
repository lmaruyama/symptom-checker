package com.healthly.symptomchecker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healthly.symptomchecker.data.Symptom;

import java.util.List;

public record AssessmentStartRequest (
        @JsonProperty("user_id") String userId,
        @JsonProperty("initial_symptoms") List<Symptom> symptoms) {
}
