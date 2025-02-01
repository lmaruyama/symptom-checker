package com.healthly.symptomchecker.controller;

import com.healthly.symptomchecker.data.Symptom;
import com.healthly.symptomchecker.entity.Assessment;
import com.healthly.symptomchecker.dto.AssessmentAnswerRequest;
import com.healthly.symptomchecker.dto.AssessmentAnswerResponse;
import com.healthly.symptomchecker.dto.AssessmentResultResponse;
import com.healthly.symptomchecker.dto.AssessmentStartRequest;
import com.healthly.symptomchecker.dto.AssessmentStartResponse;
import com.healthly.symptomchecker.service.AssessmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/assessment")
@AllArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    AssessmentStartResponse start(@RequestBody AssessmentStartRequest request) {
        Assessment assessment = assessmentService.startAssessment(request.userId(), request.symptoms());
        Optional<Symptom> symptom = assessment.getAskedQuestions().stream().findFirst();
        String nextQuestion = symptom.map(Symptom::description).orElse(null);
        return new AssessmentStartResponse(assessment.getAssessmentId(), nextQuestion);
    }

    @PostMapping("/{assessment_id}/answer")
    AssessmentAnswerResponse answer(@PathVariable("assessment_id") String assessmentId, @RequestBody AssessmentAnswerRequest request) {
        Symptom askedSymptom = Symptom.getSymptom(request.questionId());

        final Optional<Symptom> symptom = assessmentService.processAnswer(assessmentId, askedSymptom, request.response());
        String nextQuestion = null;
        if (symptom.isPresent()) {
            nextQuestion = symptom.get().description();
        }
        return new AssessmentAnswerResponse(nextQuestion);
    }

    @GetMapping("/{assessment_id}/result")
    AssessmentResultResponse result(@PathVariable("assessment_id") String assessmentId) {
        Map<String, Double> probabilities = assessmentService.getResult(assessmentId);

        Optional<Map.Entry<String, Double>> maxEntry = probabilities.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        String condition = maxEntry.map(Map.Entry::getKey)
                .orElse("It was not possible to determine your condition");

        return new AssessmentResultResponse(condition, probabilities);
    }
}
