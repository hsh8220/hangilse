package com.erp.hangilse.board.controller;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;

public class BoardDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class createBoardDTO {
        private String type;
        private String level;
        private String contents;
        private Set<String> tags;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class updateBoardDTO {
        @NotBlank(message = "Id is Mandatory")
        private Long id;
        private String type;
        private String level;
        private String contents;
        private Set<String> tags;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BoardFilterInfoDTO {
        private LocalDate startDate;
        private LocalDate endDate;
        private String type;
        private String level;
        private String accountName;
        private Set<String> tags;
    }
}
