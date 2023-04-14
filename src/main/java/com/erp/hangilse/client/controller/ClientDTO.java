package com.erp.hangilse.client.controller;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;

public class ClientDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class createClientDTO {
        @NotBlank(message = "Name is Mandatory")
        private String name;
        @NotBlank(message = "Type is Mandatory")
        private String type;
        private String address;
        private Set<String> tags;
        private String contents;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class updateClientDTO {
        @NotBlank(message = "Id is Mandatory")
        private Long id;
        private String name;
        private String type;
        private String address;
        private Set<String> tags;
        private String contents;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class clientFilterInfoDTO {
        private LocalDate startDate;
        private LocalDate endDate;
        private String name;
        private String type;
        private String address;
        private Set<String> tags;
    }
}
