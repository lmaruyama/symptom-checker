package com.healthly.symptomchecker.service;

import com.healthly.symptomchecker.data.Disease;
import com.healthly.symptomchecker.data.Symptom;
import com.healthly.symptomchecker.entity.Condition;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ComputeProbabilities {

    Map<Disease, Double> compute(Set<Symptom> observedSymptoms, List<Condition> conditions);
}
