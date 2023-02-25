package com.erp.hangilse.account.controller;

import com.erp.hangilse.account.service.AccountService;
import com.erp.hangilse.global.CommonResponse;
import com.erp.hangilse.global.TokenResponse;
import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.account.domain.AccountStat;
import com.erp.hangilse.account.domain.repository.AccountStatRepository;
import com.erp.hangilse.account.service.LoginService;
import com.erp.hangilse.account.service.MailService;
import com.erp.hangilse.global.security.JwtAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final MailService mailService;
    private final AccountService accountService;
    private final AccountStatRepository accountStatRepository;

    @PostMapping("/sign")
    public ResponseEntity<TokenResponse> login(@RequestBody AccountDTO.loginDTO loginDTO) {

        Optional<Account> account = loginService.login(loginDTO.getEmail(), loginDTO.getPassword());

        TokenResponse response;
        if (!account.get().isActivated()) {
            response = TokenResponse.builder()
                    .code("LOGIN_SUCCESS")
                    .status(200)
                    .message("Account not activated")
                    .accessToken("")
                    .refreshToken("")
                    .build();
        } else {
            Account user = account.get();
            accountService.saveAccount(user);
            JwtAuthToken jwtAuthToken = loginService.createAuthToken(account.get());
            JwtAuthToken refreshToken = loginService.createRefreshToken(account.get());
            response = TokenResponse.builder()
                    .code("LOGIN_SUCCESS")
                    .status(200)
                    .message("LOGIN_SUCCESS")
                    .accessToken(jwtAuthToken.getToken())
                    .refreshToken(refreshToken.getToken())
                    .build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);

    }

    @GetMapping("/emailCheck/{email}")
    public ResponseEntity<CommonResponse> checkDuplicateEmail(@PathVariable String email) {

        CommonResponse response = CommonResponse.builder()
                .code("CHECK_ID")
                .status(200)
                .message(String.valueOf(accountService.checkDuplicateEmail(email)))
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/nameCheck/{name}")
    public ResponseEntity<CommonResponse> checkDuplicateName(@PathVariable String name) {

        CommonResponse response = CommonResponse.builder()
                .code("CHECK_NAME")
                .status(200)
                .message(String.valueOf(accountService.checkDuplicateName(name)))
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/join")
    public ResponseEntity<CommonResponse> createAccount(@RequestBody AccountDTO.CreateAccountDTO dto) throws MessagingException {

        CommonResponse response;
        if (accountService.checkDuplicateEmail(dto.getEmail()) && accountService.checkDuplicateName(dto.getName())) {
            Account account = dto.toEntity();
            account.setActivated(true);
            Account account_rst = accountService.addAccount(account);

            response = CommonResponse.builder()
                    .code("JOIN_SUCCESS")
                    .status(200)
                    .message("JOIN_SUCCESS")
                    .build();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } else {
            response = CommonResponse.builder()
                    .code("DUPLICATED_ACCOUNT")
                    .status(400)
                    .message("DUPLICATED_ACCOUNT")
                    .build();

            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody AccountDTO.refreshTokenDTO dto) {
        Map<String, String> tokens = loginService.refreshAccessToken(dto.getRefreshToken());

        TokenResponse response = TokenResponse.builder()
                .code("REFRESH ACCESS TOKEN")
                .status(200)
                .message("REFRESH ACCESS TOKEN")
                .accessToken(tokens.get("access"))
                .refreshToken(tokens.get("refresh"))
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
