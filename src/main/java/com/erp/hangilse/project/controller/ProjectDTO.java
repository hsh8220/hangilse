package com.erp.hangilse.project.controller;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;

public class ProjectDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class createProjectDTO {
        @NotBlank(message = "Name is Mandatory")
        private String name;
        @NotBlank(message = "Type is Mandatory")
        private String type;
        private LocalDate targetTime;
        private Long clientId;
        private Long accountId;
        private Set<Long> watcherIds;
        private Set<String> tags;
        private Double cost;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class updateProjectDTO {
        @NotBlank(message = "Id is Mandatory")
        private Long id;
        private String name;
        private String type;
        private String status;
        private LocalDate targetTime;
        private Long clientId;
        private Long accountId;
        private Set<Long> watcherIds;
        private Set<String> tags;
        private Double cost;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class projectFilterInfo {
        private LocalDate startDate;
        private LocalDate endDate;
        private String name;
        private String status;
        private String type;
        private String clientName;
        private Set<String> tags;
    }
}
