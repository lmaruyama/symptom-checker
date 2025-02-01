package com.healthly.symptomchecker.entity;

import com.healthly.symptomchecker.data.Disease;
import com.healthly.symptomchecker.data.Symptom;

import java.util.Map;

public record Condition(
        Disease disease,
        double prevalence,
        Map<Symptom, Double> symptomProbabilities) {

}
