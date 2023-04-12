package com.erp.hangilse.project.controller;

import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.client.domain.Client;
import com.erp.hangilse.global.domain.Tag;
import com.erp.hangilse.project.domain.StatusEnum;
import com.querydsl.core.annotations.QueryProjection;
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
        private String contents;
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
        private String name;
        private String status;
        private String type;
        private String clientName;
        private Set<String> tags;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class selectProjectListDTO {
        private long id;
        private String name;
        private String type;
        private StatusEnum status;
        private LocalDate createTime;
        private LocalDate completeTime;
        private LocalDate endTime;
        private LocalDate targetTime;
        private Client client;
        private Account account;
        private Set<Account> watchers;
        private Set<Tag> tags;
        private Double cost;
        @QueryProjection
        public selectProjectListDTO(long id, String name, String type, StatusEnum status, LocalDate createTime, LocalDate completeTime, LocalDate endTime, LocalDate targetTime) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.status = status;
            this.createTime = createTime;
            this.completeTime = completeTime;
            this.endTime = endTime;
            this.targetTime = targetTime;
//            this.client = client;
//            this.account = account;
//            this.watchers = watchers;
//            this.tags = tags;
//            this.cost = cost;
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
