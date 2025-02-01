package com.healthly.symptomchecker.service.impl;

import com.healthly.symptomchecker.data.Data;
import com.healthly.symptomchecker.data.Disease;
import com.healthly.symptomchecker.data.PossibleAnswer;
import com.healthly.symptomchecker.data.Symptom;
import com.healthly.symptomchecker.entity.Assessment;
import com.healthly.symptomchecker.entity.Condition;
import com.healthly.symptomchecker.exception.AssessmentNotFoundException;
import com.healthly.symptomchecker.repository.AssessmentRepository;
import com.healthly.symptomchecker.service.AssessmentService;
import com.healthly.symptomchecker.service.ComputeProbabilities;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.healthly.symptomchecker.data.PossibleAnswer.YES;

@Service
@AllArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private static final Logger log = LoggerFactory.getLogger(AssessmentServiceImpl.class);
    private static final int QUESTION_THRESHOLD = 3;
    private final AssessmentRepository assessmentRepository;
    private final ComputeProbabilities computeProbabilities;

    /**
     * @param userId The user initiating the assessment.
     * @param symptoms  The initial symptoms reported by the user.
     * @return The Assessment
     */
    @Override
    public Assessment startAssessment(String userId, List<Symptom> symptoms) {

        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("User ID is required but not provided");
        }

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(UUID.randomUUID().toString());
        assessment.setUserId(userId);
        assessment.getConfirmedSymptoms().addAll(symptoms);
        final Optional<Symptom> nextQuestionOptional = suggestNextQuestion(assessment);
        nextQuestionOptional.ifPresent(assessment::addAskedQuestion);
        assessmentRepository.save(assessment);
        return assessment;
    }

    /**
     * @param assessmentId The unique ID of the assessment.
     * @param questionId The symptom being answered.
     * @param response The user's answer ("yes" / "no").
     * @return The possible symptom based on the user's answer
     */
    @Override
    public Optional<Symptom> processAnswer(
            String assessmentId, Symptom questionId, PossibleAnswer response) {

        Assessment assessment = retrieveAssessment(assessmentId);

        // if reaches the maximum number of questions,
        // it terminates the questionnaire
        if (assessment.getQuestionCount() > QUESTION_THRESHOLD) {
            log.info("The max number of questions {} has been reached for the Assessment {}"
                    , QUESTION_THRESHOLD, assessmentId);
            return Optional.empty();
        }

        if (YES.equals(response)) {
            assessment.addSymptom(questionId);
        }
        assessment.incrementQuestionCount();

        final Optional<Symptom> optionalSymptom = suggestNextQuestion(assessment);
        optionalSymptom.ifPresent(assessment::addAskedQuestion);
        assessmentRepository.save(assessment);
        return optionalSymptom;
    }

    @Override
    public Map<String, Double> getResult(String assessmentId) {
        Assessment assessment = retrieveAssessment(assessmentId);
        Set<Symptom> confirmedSymptoms = new HashSet<>(assessment.getConfirmedSymptoms());
        final Map<Disease, Double> computed =
                computeProbabilities.compute(confirmedSymptoms, getConditions());

        return computed.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().description(),
                        Map.Entry::getValue
                ));
    }

    private Assessment retrieveAssessment(String assessmentId) {
        return assessmentRepository
                .findById(assessmentId)
                .orElseThrow(AssessmentNotFoundException::new);
    }

    private Optional<Symptom> suggestNextQuestion(Assessment assessment) {
        final Set<Symptom> confirmedSymptoms = new HashSet<>(assessment.getConfirmedSymptoms());
        final Map<Disease, Double> diseases =
                computeProbabilities.compute(confirmedSymptoms, getConditions());

        final Optional<Map.Entry<Disease, Double>> mostLikelyCondition
                = diseases.entrySet().stream().max(Map.Entry.comparingByValue());

        // in case there is possible condition identified
        // it will select the next most relevant symptom based on the reported symptoms
        if (mostLikelyCondition.isPresent()) {
            final Disease disease = mostLikelyCondition.get().getKey();
            final Optional<Condition> condition =
                    getConditions().stream()
                            .filter(c -> disease.equals(c.disease()))
                            .findFirst();

            if (condition.isPresent()) {
                final Map<Symptom, Double> symptomDoubleMap =
                        new EnumMap<>(condition.get().symptomProbabilities());
                assessment.getConfirmedSymptoms().forEach(symptomDoubleMap.keySet()::remove);
                assessment.getAskedQuestions().forEach(symptomDoubleMap.keySet()::remove);

                Map.Entry<Symptom, Double> nextQuestionMap = Collections.max(
                        symptomDoubleMap.entrySet(), Map.Entry.comparingByValue()
                );

                return Optional.of(nextQuestionMap.getKey());
            }
        }

        Set<Symptom> remainingSymptoms = getSymptoms();
        assessment.getConfirmedSymptoms().forEach(remainingSymptoms::remove);
        assessment.getAskedQuestions().forEach(remainingSymptoms::remove);

        if (remainingSymptoms.isEmpty()) return Optional.empty();

        return Optional.ofNullable(remainingSymptoms.iterator().next());
    }

    private List<Condition> getConditions() {
        return Data.conditions;
    }

    private Set<Symptom> getSymptoms() {
        return Data.symptoms;
    }
}
