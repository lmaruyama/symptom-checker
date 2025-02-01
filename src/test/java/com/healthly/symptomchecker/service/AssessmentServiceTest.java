package com.healthly.symptomchecker.service;

import com.healthly.symptomchecker.data.PossibleAnswer;
import com.healthly.symptomchecker.data.Symptom;
import com.healthly.symptomchecker.entity.Assessment;
import com.healthly.symptomchecker.repository.AssessmentRepository;
import com.healthly.symptomchecker.service.impl.AssessmentServiceImpl;
import com.healthly.symptomchecker.service.impl.ComputeBayesianProbabilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class AssessmentServiceTest {

    AssessmentService assessmentService;
    AssessmentRepository assessmentRepository;

    @BeforeEach
    void setUp() {
        assessmentRepository = new AssessmentRepositoryStub();
        assessmentService = new AssessmentServiceImpl(assessmentRepository, new ComputeBayesianProbabilities());
    }

    @Test
    void whenStartingANewAssessment_shouldCreateTheAssessmentAndReturnTheNextQuestion() {
        final String userId = UUID.randomUUID().toString();
        final List<Symptom> symptoms = List.of(Symptom.SNEEZING, Symptom.NASAL_CONGESTION);
        Assessment newAssessment = new Assessment();
        newAssessment.setUserId(userId);
        newAssessment.getConfirmedSymptoms().addAll(symptoms);

        final Assessment assessment = assessmentService.startAssessment(userId, symptoms);

        assertThat(assessment).isNotNull();
        assertThat(assessment.getAssessmentId()).isNotNull();
        assertThat(assessment.getUserId()).isEqualTo(userId);
        assertThat(assessment.getConfirmedSymptoms()).hasSize(symptoms.size());
        assertThat(assessment.getConfirmedSymptoms()).containsAll(symptoms);
        assertThat(assessment.getAskedQuestions()).isNotEmpty();
        assertThat(assessment.getQuestionCount()).isZero();

        // related to the next question
        assertThat(assessment.getAskedQuestions()).isNotEmpty();
        assertThat(assessment.getQuestionCount()).isZero();
        assertThat(assessment.getConfirmedSymptoms()).hasSize(symptoms.size());
    }

    @Test
    void whenAskingAQuestion_withAnswerIsNo_shouldNotRepeatTheQuestion() {
        final String userId = UUID.randomUUID().toString();
        final List<Symptom> symptoms = List.of(Symptom.SNEEZING, Symptom.NASAL_CONGESTION);
        Assessment newAssessment = new Assessment();
        newAssessment.setUserId(userId);
        newAssessment.getConfirmedSymptoms().addAll(symptoms);

        List<Symptom> askedQuestions = newAssessment.getAskedQuestions();
        assertThat(askedQuestions).isEmpty();

        final Assessment assessment = assessmentService.startAssessment(userId, symptoms);

        askedQuestions = assessment.getAskedQuestions();
        assertThat(askedQuestions).hasSize(1);

        final Optional<Symptom> firstQuestionOptional = askedQuestions.stream().findFirst();
        assertThat(firstQuestionOptional).isPresent();
        assertThat(symptoms).doesNotContain(firstQuestionOptional.get());
        Symptom firstQuestion = firstQuestionOptional.get();

        final String assessmentId = assessment.getAssessmentId();
        Optional<Symptom> nextQuestion = assessmentService.processAnswer(assessmentId, firstQuestion, PossibleAnswer.NO);
        assertThat(askedQuestions).hasSize(2);
        assertThat(nextQuestion).isPresent();
        assertThat(askedQuestions).contains(nextQuestion.get());
        assertThat(symptoms).doesNotContain(nextQuestion.get());
    }

    @Test
    void whenAskingAQuestion_withAnswerIsYes_shouldAddItToTheConfirmedSymptoms() {
        final String userId = UUID.randomUUID().toString();
        final List<Symptom> symptoms = List.of(Symptom.SNEEZING, Symptom.NASAL_CONGESTION);
        Assessment newAssessment = new Assessment();
        newAssessment.setUserId(userId);
        newAssessment.getConfirmedSymptoms().addAll(symptoms);

        final Assessment assessment = assessmentService.startAssessment(userId, symptoms);

        final List<Symptom> askedQuestions = assessment.getAskedQuestions();
        final Optional<Symptom> firstQuestionOptional = askedQuestions.stream().findFirst();
        assertThat(firstQuestionOptional).isPresent();
        Symptom firstQuestion = firstQuestionOptional.get();

        final String assessmentId = assessment.getAssessmentId();
        Optional<Symptom> nextQuestion = assessmentService.processAnswer(assessmentId, firstQuestion, PossibleAnswer.YES);
        assertThat(askedQuestions).hasSize(2);
        assertThat(nextQuestion).isPresent();
        assertThat(askedQuestions).contains(nextQuestion.get());
        assertThat(assessment.getConfirmedSymptoms()).contains(firstQuestion);
    }

    @Test
    void shouldReturnTheResult() {
        final String userId = UUID.randomUUID().toString();
        final List<Symptom> symptoms = List.of(Symptom.SNEEZING, Symptom.NASAL_CONGESTION);
        Assessment newAssessment = new Assessment();
        newAssessment.setUserId(userId);
        newAssessment.getConfirmedSymptoms().addAll(symptoms);

        final Assessment assessment = assessmentService.startAssessment(userId, symptoms);
        final String assessmentId = assessment.getAssessmentId();

        final List<Symptom> askedQuestions = assessment.getAskedQuestions();
        final Optional<Symptom> firstQuestionOptional = askedQuestions.stream().findFirst();
        assertThat(firstQuestionOptional).isPresent();
        Symptom firstQuestion = firstQuestionOptional.get();

        assessmentService.processAnswer(assessmentId, firstQuestion, PossibleAnswer.YES);

        final Map<String, Double> result = assessmentService.getResult(assessmentId);
        assertThat(result).isNotEmpty();
        System.out.println(result);
    }
}
