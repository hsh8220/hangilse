package com.erp.hangilse.account.controller;


import com.erp.hangilse.ControllerTest;
import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.account.domain.Authority;
import com.erp.hangilse.account.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AccountControllerTest extends ControllerTest {

    @MockBean
    LoginService loginService;

    @Test
    public void getAccount() throws Exception {

        //given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .password("1234")
                .name("test")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();

        given(accountService.getAccountByEmail("test@aaa.bbb")).willReturn(account);

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/account").headers(httpHeaders));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void AccountPwCheck() throws Exception {

        //given
        AccountDTO.AccountPwCheckDTO dto = new AccountDTO.AccountPwCheckDTO();
        dto.setPassword("1234");

        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .password("1234")
                .name("test")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();

        given(loginService.login("test@aaa.bbb", dto.getPassword())).willReturn(Optional.ofNullable(account));

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/account/passwdCheck").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void updateAccount() throws Exception {

        //given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .password("1234")
                .name("test")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();

        Account account_rst = Account.builder()
                .email("test@aaa.bbb")
                .password("4567")
                .name("hangilse")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();
        account_rst.setId(123);
        account_rst.setActivated(true);

        AccountDTO.UpdateAccountDTO dto = new AccountDTO.UpdateAccountDTO();
        dto.setNewPassword("4567");
        dto.setNewName("hangilse");

        given(accountService.getAccountByEmail("test@aaa.bbb")).willReturn(account);
        given(accountService.changePassword("test@aaa.bbb", account.getName())).willReturn(account);

        account.setName(dto.getNewName());
        given(accountService.saveAccount(account)).willReturn(account_rst);


        //when
        ResultActions result = mockMvc.perform(put("/api/v1/account").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteAccount() throws Exception {

        //given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/account").headers(httpHeaders));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}