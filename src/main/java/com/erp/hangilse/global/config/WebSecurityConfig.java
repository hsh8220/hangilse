package com.erp.hangilse.global.config;

import com.erp.hangilse.account.domain.AuthorityEnum;
import com.erp.hangilse.global.exception.ExceptionHandlerFilter;
import com.erp.hangilse.global.security.JwtAccessDeniedHandler;
import com.erp.hangilse.global.security.JwtAuthTokenProvider;
import com.erp.hangilse.global.security.JwtAuthenticationEntryPoint;
import com.erp.hangilse.global.security.JwtConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final JwtAuthenticationEntryPoint authenticationErrorHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final CorsFilter corsFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(authenticationErrorHandler)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/profile").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/docs/**").permitAll()
                .antMatchers("/api/v1/**").hasAnyAuthority(AuthorityEnum.ROLE_ENGINEER.toString(), AuthorityEnum.ROLE_OWNER.toString())

                .anyRequest().authenticated()

                .and()
                .apply(securityConfigurerAdapter());
    }

//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring()
//                .antMatchers(HttpMethod.OPTIONS, "/**")
//
//                // allow anonymous resource requests
//                .antMatchers(
//                        "/",
//                        "/h2-console/**"
//                );
//    }

    private JwtConfigurer securityConfigurerAdapter() {
        return new JwtConfigurer(jwtAuthTokenProvider, exceptionHandlerFilter);
    }
}
