package com.erp.hangilse.global;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse {
    private String message;
    private String code;
    private int status;
}
