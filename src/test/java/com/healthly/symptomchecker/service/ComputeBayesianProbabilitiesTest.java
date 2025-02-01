package com.healthly.symptomchecker.service;

import com.healthly.symptomchecker.data.Disease;
import com.healthly.symptomchecker.data.Symptom;
import com.healthly.symptomchecker.entity.Condition;
import com.healthly.symptomchecker.service.impl.ComputeBayesianProbabilities;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.healthly.symptomchecker.data.Disease.COMMON_COLD;
import static com.healthly.symptomchecker.data.Disease.COVID_19;
import static com.healthly.symptomchecker.data.Disease.HAY_FEVER;
import static java.util.Map.entry;

class ComputeBayesianProbabilitiesTest {

    static final Set<Symptom> symptoms = Set.of(Symptom.COUGH, Symptom.WATERY_OR_ITCHY_EYES, Symptom.NASAL_CONGESTION);
    static final List<Condition> conditions = List.of(
            new Condition(HAY_FEVER, 0.3,
                    Map.ofEntries(
                            entry(Symptom.NASAL_CONGESTION, 0.75),
                            entry(Symptom.COUGH, 0.1),
                            entry(Symptom.WATERY_OR_ITCHY_EYES, 0.95)
                    )
            ),
            new Condition(COVID_19, 0.2,
                    Map.ofEntries(
                            entry(Symptom.NASAL_CONGESTION, 0.4),
                            entry(Symptom.COUGH, 0.7),
                            entry(Symptom.WATERY_OR_ITCHY_EYES, 0.05)
                    )
            ),
            new Condition(COMMON_COLD, 0.5,
                    Map.ofEntries(
                            Map.entry(Symptom.NASAL_CONGESTION, 0.85),
                            Map.entry(Symptom.COUGH, 0.6),
                            Map.entry(Symptom.WATERY_OR_ITCHY_EYES, 0.1)
                    )
            )
    );

    private ComputeProbabilities computeProbabilities;

    @BeforeEach
    void setUp() {
        computeProbabilities = new ComputeBayesianProbabilities();
    }

    @Test
    void computeBayesianProbabilitiesTest() {
        final Map<Disease, Double> compute = computeProbabilities.compute(symptoms, conditions);

        Assertions.assertThat(compute).isNotNull();
        Assertions.assertThat(compute.get(HAY_FEVER)).isCloseTo(0.43029, Offset.offset(0.0001));
        Assertions.assertThat(compute.get(COVID_19)).isCloseTo(0.05636, Offset.offset(0.0001));
        Assertions.assertThat(compute.get(COMMON_COLD)).isCloseTo(0.51334, Offset.offset(0.0001));
    }
}
