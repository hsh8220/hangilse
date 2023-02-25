package com.erp.hangilse.global.security;

import com.erp.hangilse.global.exception.ExceptionHandlerFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtAuthTokenProvider jwtAuthTokenProvider;
    private ExceptionHandlerFilter exceptionHandlerFilter;

    public JwtConfigurer(JwtAuthTokenProvider jwtAuthTokenProvider, ExceptionHandlerFilter exceptionHandlerFilter) {
        this.jwtAuthTokenProvider = jwtAuthTokenProvider;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(jwtAuthTokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(exceptionHandlerFilter, JwtFilter.class);
    }
}
