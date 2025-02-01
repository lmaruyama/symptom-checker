package com.healthly.symptomchecker.dto;

import java.util.Map;

public record AssessmentResultResponse(String condition, Map<String, Double> probabilities) {
}
