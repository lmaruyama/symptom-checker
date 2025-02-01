package com.healthly.symptomchecker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthly.symptomchecker.data.Disease;
import com.healthly.symptomchecker.data.Symptom;
import com.healthly.symptomchecker.entity.Assessment;
import com.healthly.symptomchecker.dto.AssessmentAnswerRequest;
import com.healthly.symptomchecker.dto.AssessmentAnswerResponse;
import com.healthly.symptomchecker.dto.AssessmentResultResponse;
import com.healthly.symptomchecker.dto.AssessmentStartRequest;
import com.healthly.symptomchecker.dto.AssessmentStartResponse;
import com.healthly.symptomchecker.data.PossibleAnswer;
import com.healthly.symptomchecker.service.AssessmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssessmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AssessmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AssessmentService assessmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void startAssessment_mustReturnTheAssessmentIdAndTheTheNextQuestion() throws Exception {
        final String userId = UUID.randomUUID().toString();
        final List<Symptom> symptoms = Arrays.asList(Symptom.SNEEZING, Symptom.NASAL_CONGESTION);
        final Symptom fever = Symptom.FEVER;
        final String nextQuestion = fever.description();

        final Assessment assessment = new Assessment();
        final String assessmentId = UUID.randomUUID().toString();
        assessment.setAssessmentId(assessmentId);
        assessment.getAskedQuestions().add(fever);

        AssessmentStartResponse response = new AssessmentStartResponse(assessmentId, nextQuestion);
        when(assessmentService.startAssessment(userId, symptoms)).thenReturn(assessment);

        AssessmentStartRequest request = new AssessmentStartRequest(userId, symptoms);
        mockMvc.perform(
                post("/assessment/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void answeringQuestion_mustReturnTheNextQuestion() throws Exception {
        final String assessmentId = UUID.randomUUID().toString();
        final Symptom questionId = Symptom.FEVER;
        final PossibleAnswer answer = PossibleAnswer.YES;
        AssessmentAnswerRequest request = new AssessmentAnswerRequest(questionId.description(), answer);

        final Symptom nextQuestion = Symptom.SORE_THROAT;
        AssessmentAnswerResponse response = new AssessmentAnswerResponse(nextQuestion.description());

        when(assessmentService.processAnswer(assessmentId, questionId, answer))
                        .thenReturn(Optional.of(nextQuestion));

        mockMvc.perform(
                post("/assessment/%s/answer".formatted(assessmentId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void answeringQuestion_whenNoFurtherQuestionRequired_MustReturnNull() throws Exception {
        final String assessmentId = UUID.randomUUID().toString();
        final Symptom questionId = Symptom.FEVER;
        final PossibleAnswer answer = PossibleAnswer.YES;
        final AssessmentAnswerRequest request = new AssessmentAnswerRequest(questionId.description(), answer);

        final AssessmentAnswerResponse response = new AssessmentAnswerResponse(null);

        when(assessmentService.processAnswer(assessmentId, questionId, answer))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        post("/assessment/%s/answer".formatted(assessmentId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void gettingTheResult_mustReturnConditionAndProbabilitiesForAllConditions() throws Exception {
        final String assessmentId = UUID.randomUUID().toString();

        String condition = Disease.COMMON_COLD.description();
        Map<String, Double> probabilities = Map.of(
                Disease.COMMON_COLD.description(), 0.63,
                Disease.COVID_19.description(), 0.25,
                Disease.HAY_FEVER.description(), 0.17
        );
        final AssessmentResultResponse response = new AssessmentResultResponse(condition, probabilities);

        when(assessmentService.getResult(assessmentId)).thenReturn(probabilities);

        mockMvc.perform(get("/assessment/%s/result".formatted(assessmentId)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
