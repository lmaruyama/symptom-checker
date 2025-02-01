package com.healthly.symptomchecker.data;

public enum Disease {

    HAY_FEVER("Hay fever"),
    COVID_19("COVID-19"),
    COMMON_COLD("Common cold");

    private String description;

    Disease(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
