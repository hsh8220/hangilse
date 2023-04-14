package com.erp.hangilse.client.controller;

import com.erp.hangilse.ControllerTest;
import com.erp.hangilse.account.domain.Authority;
import com.erp.hangilse.client.domain.Client;
import com.erp.hangilse.client.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientControllerTest extends ControllerTest {

    @MockBean
    ClientService clientService;

    @Test
    void getClientByFilter() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        List<Client> clients = new ArrayList<>();

        clients.add(Client.builder().name("client1").type("test").build());
        clients.add(Client.builder().name("client2").type("test").build());

        final PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createTime");
        Page<Client> clientPage = new PageImpl(clients, pageable, 10);

        ClientDTO.clientFilterInfoDTO filter = new ClientDTO.clientFilterInfoDTO();
        filter.setType("test");

        given(clientService.getClientByFilter(any(), any())).willReturn(clientPage);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/client/search?page=0&size=10&direction=DESC").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getClientById() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        Client client = Client.builder().name("client1").type("test").build();

        given(clientService.getClientById(123l)).willReturn(client);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/client/123").headers(httpHeaders));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void saveClient() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        ClientDTO.createClientDTO dto = new ClientDTO.createClientDTO();
        dto.setName("client1");
        dto.setType("test");

        Client client = Client.builder().name("client1").type("test").build();

        given(clientService.saveClient(dto)).willReturn(client);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/client").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


    }

    @Test
    void updateClient() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        ClientDTO.updateClientDTO dto = new ClientDTO.updateClientDTO();
        dto.setId(123l);
        dto.setName("client2");
        dto.setType("test");

        Client client = Client.builder().name("client2").type("test").build();

        given(clientService.updateClient(dto)).willReturn(client);

        // when
        ResultActions result = mockMvc.perform(put("/api/v1/client").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteProject() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/client/123").headers(httpHeaders));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
