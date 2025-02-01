package com.healthly.symptomchecker.data;

import com.healthly.symptomchecker.entity.Condition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.healthly.symptomchecker.data.Disease.COMMON_COLD;
import static com.healthly.symptomchecker.data.Disease.COVID_19;
import static com.healthly.symptomchecker.data.Disease.HAY_FEVER;
import static java.util.Map.entry;

public class Data {

    private Data() {}

    public static final Set<Symptom> symptoms = Arrays.stream(Symptom.values()).collect(Collectors.toSet());

    public static final List<Condition> conditions = List.of(
            new Condition(HAY_FEVER, 0.3,
                    Map.ofEntries(
                        entry(Symptom.SNEEZING, 0.9),
                        entry(Symptom.RUNNY_NOSE, 0.85),
                        entry(Symptom.NASAL_CONGESTION, 0.75),
                        entry(Symptom.COUGH, 0.1),
                        entry(Symptom.FEVER, 0.0),
                        entry(Symptom.SORE_THROAT, 0.05),
                        entry(Symptom.LOSS_OF_SMELL_OR_TASTE, 0.05),
                        entry(Symptom.HEADACHE, 0.3),
                        entry(Symptom.FATIGUE, 0.2),
                        entry(Symptom.WATERY_OR_ITCHY_EYES, 0.95),
                        entry(Symptom.SHORTNESS_OF_BREATH, 0.05)
                    )
            ),
            new Condition(COVID_19, 0.2,
                    Map.ofEntries(
                        entry(Symptom.SNEEZING, 0.1),
                        entry(Symptom.RUNNY_NOSE, 0.2),
                        entry(Symptom.NASAL_CONGESTION, 0.4),
                        entry(Symptom.COUGH, 0.7),
                        entry(Symptom.FEVER, 0.85),
                        entry(Symptom.SORE_THROAT, 0.65),
                        entry(Symptom.LOSS_OF_SMELL_OR_TASTE, 0.8),
                        entry(Symptom.HEADACHE, 0.6),
                        entry(Symptom.FATIGUE, 0.75),
                        entry(Symptom.WATERY_OR_ITCHY_EYES, 0.05),
                        entry(Symptom.SHORTNESS_OF_BREATH, 0.5)
                    )
            ),
            new Condition(COMMON_COLD, 0.5,
                    Map.ofEntries(
                            Map.entry(Symptom.SNEEZING, 0.7),
                            Map.entry(Symptom.RUNNY_NOSE, 0.8),
                            Map.entry(Symptom.NASAL_CONGESTION, 0.85),
                            Map.entry(Symptom.COUGH, 0.6),
                            Map.entry(Symptom.FEVER, 0.1),
                            Map.entry(Symptom.SORE_THROAT, 0.75),
                            Map.entry(Symptom.LOSS_OF_SMELL_OR_TASTE, 0.05),
                            Map.entry(Symptom.HEADACHE, 0.4),
                            Map.entry(Symptom.FATIGUE, 0.3),
                            Map.entry(Symptom.WATERY_OR_ITCHY_EYES, 0.1),
                            Map.entry(Symptom.SHORTNESS_OF_BREATH, 0.05)
                    )
            )
    );
}
