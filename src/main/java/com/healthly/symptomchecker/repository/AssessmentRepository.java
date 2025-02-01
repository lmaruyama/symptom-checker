package com.healthly.symptomchecker.repository;

import com.healthly.symptomchecker.entity.Assessment;

import java.util.Optional;

public interface AssessmentRepository {
    Assessment save(Assessment assessment);

    Optional<Assessment> findById(String assessmentId);
}
