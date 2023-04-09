package com.erp.hangilse.account.controller;

import com.erp.hangilse.ControllerTest;
import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.account.domain.Authority;
import com.erp.hangilse.account.service.LoginService;
import com.erp.hangilse.global.security.JwtAuthToken;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class LoginControllerTest extends ControllerTest {

    @MockBean
    LoginService loginService;

    @Test
    public void login() throws Exception {

        //given
        AccountDTO.loginDTO loginDTO = new AccountDTO.loginDTO();
        loginDTO.setEmail("test@aaa.bbb");
        loginDTO.setPassword("1234");

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .name("test")
                .password("1234")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();
        account.setActivated(true);

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.createAuthToken("test@aaa.bbb",Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()), "access", expiredDate);
        JwtAuthToken refreshToken = jwtAuthTokenProvider.createAuthToken("test@aaa.bbb",Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()), "refresh", expiredDate);


        given(loginService.login(loginDTO.getEmail(), loginDTO.getPassword())).willReturn(Optional.ofNullable(account));
        given(loginService.createAuthToken(account)).willReturn(jwtAuthToken);
        given(loginService.createRefreshToken(account)).willReturn(refreshToken);

        //when
        ResultActions result = mockMvc.perform(post("/login/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value(jwtAuthToken.getToken()));
    }

    @Test
    public void checkDuplicateId() throws Exception {

        //given
        given(accountService.checkDuplicateEmail("test@aaa.bbb")).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(get("/login/emailCheck/test@aaa.bbb"));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("true"));
    }

    @Test
    public void checkDuplicateName() throws Exception {

        //given
        given(accountService.checkDuplicateName("test")).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(get("/login/nameCheck/test"));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("true"));
    }

    @Test
    public void createAccount() throws Exception {

        //given
        AccountDTO.CreateAccountDTO dto = new AccountDTO.CreateAccountDTO();
        dto.setEmail("test@aaa.bbb");
        dto.setPassword("1234");
        dto.setName("test");

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .password("1234")
                .name("test")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ENGINEER").build()))
                .build();
        account.setId(123);

        given(accountService.checkDuplicateEmail(dto.getEmail())).willReturn(true);
        given(accountService.checkDuplicateName(dto.getName())).willReturn(true);
        given(accountService.addAccount(any())).willReturn(account);

        //when
        ResultActions result = mockMvc.perform(post("/login/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("JOIN_SUCCESS"));


    }

    @Test
    public void refreshToken() throws Exception {
        //given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.createAuthToken("test@aaa.bbb",authorities, "access", expiredDate);
        JwtAuthToken refreshToken = jwtAuthTokenProvider.createAuthToken("test@aaa.bbb",authorities, "refresh", expiredDate);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", jwtAuthToken.getToken());
        tokens.put("refresh", refreshToken.getToken());

        AccountDTO.refreshTokenDTO dto = new AccountDTO.refreshTokenDTO();
        dto.setRefreshToken(refreshToken.getToken());

        given(loginService.refreshAccessToken(any())).willReturn(tokens);

        //when
        ResultActions result = mockMvc.perform(post("/login/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
