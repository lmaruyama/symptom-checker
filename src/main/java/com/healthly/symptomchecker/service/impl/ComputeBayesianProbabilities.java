package com.healthly.symptomchecker.service.impl;

import com.healthly.symptomchecker.data.Disease;
import com.healthly.symptomchecker.data.Symptom;
import com.healthly.symptomchecker.entity.Condition;
import com.healthly.symptomchecker.service.ComputeProbabilities;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Implements Bayes' Theorem
 *
 * P(Disease|Symptoms) = P(Symptoms|Disease) x P(Disease) / P(Symptom)
 *
 * observedSymptoms = {COUGH, WATERY_OR_ITCHY_EYES, NASAL_CONGESTION}
 *
 * conditions = [
 *     new Condition(HAY_FEVER, 0.3, { SNEEZING: 0.1, RUNNY_NOSE: 0.95, NASAL_CONGESTION: 0.75 }),
 *     new Condition(COVID_19, 0.2, { SNEEZING: 0.7, RUNNY_NOSE: 0.05, NASAL_CONGESTION: 0.4 }),
 *     new Condition(COMMON_COLD, 0.5, { SNEEZING: 0.6, RUNNY_NOSE: 0.1, NASAL_CONGESTION: 0.85 })
 * ]
 *
 * Calculation for each disease
 * HAY_FEVER = 0.3 × 0.1 × 0.95 × 0.75 = 0.021375
 * COVID_19 = 0.2 × 0.7 × 0.05 × 0.4 = 0.0028
 * COMMON_COLD = 0.5 × 0.6 × 0.1 × 0.85 = 0.0255
 *
 * Total Probability = 0.021375 + 0.0028 + 0.0255 = 0.049675
 *
 * Normalization
 * P(HAY_FEVER∣Symptoms) = 0.021375 / 0.049675 = 0.43
 * P(COVID_19∣Symptoms) = 0.0028 / 0.049675 = 0.056
 * P(COMMON_COLD∣Symptoms) = 0.0255 / 0.049675 = 0.514
 */
@Component
public class ComputeBayesianProbabilities implements ComputeProbabilities {

    @Override
    public Map<Disease, Double> compute(Set<Symptom> observedSymptoms, List<Condition> conditions) {
        Map<Disease, Double> probabilities = new HashMap<>();
        double totalProbability = 0;

        for (Condition condition : conditions) {
            double likelihood = condition.prevalence();
            for (Symptom symptom : observedSymptoms) {
                likelihood *= condition.symptomProbabilities().getOrDefault(symptom, 1.0);
            }
            probabilities.put(condition.disease(), likelihood);
            totalProbability += likelihood;
        }

        if (totalProbability > 0) {
            for (Map.Entry<Disease, Double> entry : probabilities.entrySet()) {
                probabilities.put(entry.getKey(), entry.getValue() / totalProbability);
            }
        }

        return probabilities;
    }
}
