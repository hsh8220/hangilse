package com.erp.hangilse.account.controller;

import com.erp.hangilse.account.service.AccountService;
import com.erp.hangilse.global.CommonResponse;
import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.account.service.LoginService;
import com.erp.hangilse.global.security.JwtAuthToken;
import com.erp.hangilse.global.security.JwtAuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final LoginService loginService;
    private final AccountService accountService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @GetMapping
    public ResponseEntity<Account> getAccountByEmail(@RequestHeader(value = "x-auth-token") String token) {

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token);
        String email = (String) jwtAuthToken.getData().get("sub");

        Account account = accountService.getAccountByEmail(email);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(account);
    }

    @PostMapping("/passwdCheck")
    public ResponseEntity<CommonResponse> checkPasswd(@RequestHeader(value = "x-auth-token") String token,
                                                      @RequestBody AccountDTO.AccountPwCheckDTO dto) {

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token);
        String email = (String) jwtAuthToken.getData().get("sub");

        CommonResponse response;
        try {
            Optional<Account> account = loginService.login(email, dto.getPassword());
            response = CommonResponse.builder()
                    .code("CHECK_PASSWORD")
                    .status(200)
                    .message("true")
                    .build();
        }catch (Exception e) {
            response = CommonResponse.builder()
                    .code("CHECK_PASSWORD")
                    .status(200)
                    .message("false")
                    .build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PutMapping
    public ResponseEntity<Account> updateAccountByEmail(@RequestHeader(value = "x-auth-token") String token,
                                                        @RequestBody AccountDTO.UpdateAccountDTO dto) {

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token);
        String email = (String) jwtAuthToken.getData().get("sub");

        Account account_rst = null;
        if(dto.getNewPassword() != null && !dto.getNewPassword().equals("")) {
            account_rst = accountService.changePassword(email, dto.getNewPassword());
        }
        if(dto.getNewName() != null && !dto.getNewName().equals("")) {
            Account account = accountService.getAccountByEmail(email);
            account.setName(dto.getNewName());
            account_rst = accountService.saveAccount(account);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(account_rst);

    }

    @DeleteMapping
    public ResponseEntity<CommonResponse> deleteAccountByEmail(@RequestHeader(value = "x-auth-token") String token) {

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token);
        String email = (String) jwtAuthToken.getData().get("sub");

        long id = accountService.deleteAccountByEmail(email);

        CommonResponse response = CommonResponse.builder()
                .code("DELETE ACCOUNT SUCCESS")
                .status(200)
                .message("DELETE ACCOUNT SUCCESS")
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
