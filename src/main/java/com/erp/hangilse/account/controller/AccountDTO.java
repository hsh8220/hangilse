package com.erp.hangilse.account.controller;

import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.account.domain.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class AccountDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class loginDTO {
        @NotNull
        @Email
        private String email;

        @NotNull
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AccountPwCheckDTO {

        @NotNull
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateAccountDTO {

        @NotNull
        @Email
        private String email;

        @NotNull
        private String password;

        @NotNull
        private String name;

        public Account toEntity() {
            Account account = Account.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .authorities(Set.of(Authority.builder().authorityName("ROLE_ENGINEER").build())) //초기 생성 권한은 무조건 엔지니어, 이후 관리자가 role에 맞게 조정하는 것으로
                    .build();

            return account;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateAccountDTO {

        private String newPassword;

        private String newName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class resendAuthCodeDTO {

        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class IdentifyAccountDTO {

        @NotNull
        @Email
        private String email;

        @NotNull
        private String authCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class refreshTokenDTO {
        private String refreshToken;
    }

}
