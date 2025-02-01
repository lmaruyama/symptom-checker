package com.healthly.symptomchecker.service;

import com.healthly.symptomchecker.data.Symptom;
import com.healthly.symptomchecker.entity.Assessment;
import com.healthly.symptomchecker.data.PossibleAnswer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AssessmentService {
    Assessment startAssessment(String userId, List<Symptom> symptoms);

    Optional<Symptom> processAnswer(String assessmentId,
                                    Symptom questionId, PossibleAnswer response);

    Map<String, Double> getResult(String assessmentId);
}
