package com.healthly.symptomchecker.entity;

import com.healthly.symptomchecker.data.Symptom;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.util.ArrayList;
import java.util.List;

import static com.healthly.symptomchecker.data.SecondaryPartitionKeys.ASSESSMENT_USER_INDEX;

@DynamoDbBean
@ToString
@EqualsAndHashCode
@Setter
public class Assessment {

    private String assessmentId;
    private String userId;
    private List<Symptom> confirmedSymptoms;
    private List<Symptom> askedQuestions;
    private int questionCount;

    public Assessment() {
        this.confirmedSymptoms = new ArrayList<>();
        this.askedQuestions = new ArrayList<>();
        this.questionCount = 0;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("assessment_id")
    public String getAssessmentId() {
        return assessmentId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = ASSESSMENT_USER_INDEX)
    @DynamoDbAttribute("user_id")
    public String getUserId() {
        return userId;
    }

    @DynamoDbAttribute("confirmed_symptoms")
    public List<Symptom> getConfirmedSymptoms() {
        return confirmedSymptoms;
    }

    @DynamoDbAttribute("asked_questions")
    public List<Symptom> getAskedQuestions() {
        return askedQuestions;
    }

    @DynamoDbAttribute("question_count")
    public int getQuestionCount() {
        return questionCount;
    }

    public void incrementQuestionCount() {
        questionCount++;
    }

    public void addAskedQuestion(Symptom nextQuestion) {
        askedQuestions.add(nextQuestion);
    }

    public void addSymptom(Symptom symptom) {
        confirmedSymptoms.add(symptom);
    }
}


