package com.healthly.symptomchecker.data;

import com.healthly.symptomchecker.exception.InvalidSymptomException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum Symptom {
    SNEEZING("Sneezing"),
    RUNNY_NOSE("Runny nose"),
    NASAL_CONGESTION("Nasal congestion"),
    COUGH("Cough"),
    FEVER("Fever"),
    SORE_THROAT("Sore throat"),
    LOSS_OF_SMELL_OR_TASTE("Loss of smell or taste"),
    HEADACHE("Headache"),
    FATIGUE("Fatigue"),
    WATERY_OR_ITCHY_EYES("Watery or itchy eyes"),
    SHORTNESS_OF_BREATH("Shortness of breath");

    private final String description;

    Symptom(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    public static Symptom getSymptom(String description) {
        return Arrays.stream(Symptom.values())
                .filter(symptom -> symptom.description.equals(description))
                .findFirst()
                .orElseThrow(() ->
                        new InvalidSymptomException(
                                "[%s] is not a valid symptom.".formatted(description)));
    }

    public static Set<Symptom> getSymptoms(List<String> description) {
        Set<Symptom> symptoms = new HashSet<>();
        description.forEach(d -> symptoms.add(getSymptom(d)));
        return symptoms;
    }
}
