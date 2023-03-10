package com.erp.hangilse;

import com.erp.hangilse.account.domain.Authority;
import com.erp.hangilse.account.service.AccountService;
import com.erp.hangilse.global.security.JwtAuthTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.Set;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtAuthTokenProvider jwtAuthTokenProvider;

    @MockBean
    protected JavaMailSender mailSender;

    @MockBean
    protected AccountService accountService;

    protected String makeJwtAuthToken(Set<Authority> authorities, Date expiredDate) {
        return jwtAuthTokenProvider.createAuthToken("test@aaa.bbb", Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()), "access", expiredDate).getToken();
    }


    @BeforeEach
    void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("http")
                        .withHost("hangilse.com")
                        .withPort(80)
                )
                .alwaysDo(document("{method-name}"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())))
                .build();
    }
}
