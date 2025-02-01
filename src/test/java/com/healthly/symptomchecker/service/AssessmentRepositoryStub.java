package com.healthly.symptomchecker.service;

import com.healthly.symptomchecker.entity.Assessment;
import com.healthly.symptomchecker.repository.AssessmentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AssessmentRepositoryStub implements AssessmentRepository {

    private static final Map<String, Assessment> assessments = new HashMap<>();

    @Override
    public Assessment save(Assessment assessment) {
        if (assessment == null) {
            throw new IllegalArgumentException("Assessment cannot be null");
        }

        final String assessmentId = assessment.getAssessmentId();
        assessments.put(assessmentId, assessment);

        return assessment;
    }

    @Override
    public Optional<Assessment> findById(String assessmentId) {
        if (assessments.containsKey(assessmentId)) {
            return Optional.of(assessments.get(assessmentId));
        }

        return Optional.empty();
    }
}
