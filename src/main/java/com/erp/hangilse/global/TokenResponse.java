package com.erp.hangilse.global;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private String message;
    private String code;
    private int status;
    private String accessToken;
    private String refreshToken;
}
