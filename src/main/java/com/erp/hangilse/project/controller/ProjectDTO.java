package com.erp.hangilse.project.controller;

import com.erp.hangilse.project.domain.StatusEnum;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
        private String contents;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class createProjectMetaDTO {
        private List<String> types;
        private Map<Long, String> clients;
        private Map<Long, String> accounts;
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
        private String contents;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class projectFilterInfoDTO {
        private LocalDate startDate;
        private LocalDate endDate;
        @Email(message = "not email form")
        private String email;
        private String name;
        private String status;
        private String type;
        private String accountName;
        private String clientName;
        private Set<String> tags;
    }

    @Data
    @NoArgsConstructor
    public static class projectListInfoDTO {
        private Long id;
        private String name;
        private String type;
        private StatusEnum status;
        private LocalDate createTime;
        private LocalDate completeTime;
        private LocalDate endTime;
        private LocalDate targetTime;
        private String clientName;
        private String assignee;

        public projectListInfoDTO(Long id, String name, String type, StatusEnum status, LocalDate createTime, LocalDate completeTime, LocalDate endTime, LocalDate targetTime, String clientName, String assignee) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.status = status;
            this.createTime = createTime;
            this.completeTime = completeTime;
            this.endTime = endTime;
            this.targetTime = targetTime;
            this.clientName = clientName;
            this.assignee = assignee;
        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class createCommentDTO {
        @NotBlank(message = "projectId is Mandatory")
        private Long projectId;
        private Long parentId;
        @NotBlank(message = "accountId is Mandatory")
        private Long accountId;
        @NotBlank(message = "contents is Mandatory")
        private String contents;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class updateCommentDTO {
        @NotBlank(message = "commentId is Mandatory")
        private Long commentId;
        @NotBlank(message = "contents is Mandatory")
        private String contents;
    }
}
