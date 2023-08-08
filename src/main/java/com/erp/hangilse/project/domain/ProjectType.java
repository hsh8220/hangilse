package com.erp.hangilse.project.domain;

public enum ProjectType {
    Design("설계"),
    Review("검토"),
    Demolition("철거");

    private final String value;

    ProjectType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
