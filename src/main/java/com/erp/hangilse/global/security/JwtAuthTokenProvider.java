package com.erp.hangilse.global.security;

import com.erp.hangilse.account.domain.Authority;
import com.erp.hangilse.global.exception.TokenValidFailedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthTokenProvider {
    private final Key key;
    private static final String AUTHORITIES_KEY = "auth";

    public JwtAuthTokenProvider(String base64Secret) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtAuthToken createAuthToken(String id, Set<Authority> authorities, String type, Date expiredDate) {
        return new JwtAuthToken(id, authorities, type, expiredDate, key);
    }

    public JwtAuthToken convertAuthToken(String token) {
        return new JwtAuthToken(token, key);
    }

    public Authentication getAuthentication(JwtAuthToken authToken) {

        if(authToken.validate()) {
            Claims claims = authToken.getData();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            User principal = new User(claims.getSubject(), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);

        } else {
            throw new TokenValidFailedException();
        }
    }
}
