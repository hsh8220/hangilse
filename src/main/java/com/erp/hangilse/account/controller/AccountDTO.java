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
        @NotNull
        private Set<Authority> authorities;

        public Account toEntity() {
            Account account = Account.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .authorities(authorities)
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
