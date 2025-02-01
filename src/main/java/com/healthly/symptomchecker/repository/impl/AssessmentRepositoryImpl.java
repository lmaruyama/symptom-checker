package com.healthly.symptomchecker.repository.impl;

import com.healthly.symptomchecker.entity.Assessment;
import com.healthly.symptomchecker.repository.AssessmentRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class AssessmentRepositoryImpl implements AssessmentRepository {

    private static final Logger log = LoggerFactory.getLogger(AssessmentRepositoryImpl.class);
    private final DynamoDbTable<Assessment> assessmentTable;

    @Override
    public Assessment save(Assessment assessment) {
        log.info("Saving assessment {}", assessment);
        assessmentTable.putItem(assessment);
        return assessment;
    }

    @Override
    public Optional<Assessment> findById(String assessmentId) {
        if (!StringUtils.hasText(assessmentId)) {
            log.warn("Attempt to search assessment by id but no value has been provided");
            return Optional.empty();
        }
        return Optional.ofNullable(
                assessmentTable.getItem(
                        r -> r.key(Key.builder().partitionValue(assessmentId).build())));
    }
}
