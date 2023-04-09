package com.erp.hangilse.global.security;

import com.erp.hangilse.account.domain.Authority;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthToken {
    @Getter
    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String TOKEN_TYPE = "type";

    JwtAuthToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    JwtAuthToken(String id, Set<Authority> authorities, String type, Date expiredDate, Key key) {
        this.key = key;
        this.token = createJwtAuthToken(id, authorities, type, expiredDate).get();
    }

    public boolean validate() {
        return getData() != null && getData().get("type").equals("access");
    }

    public Claims getData() {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (SecurityException e) {
            log.error("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid.");
        }
        return null;
    }

    private Optional<String> createJwtAuthToken(String id, Set<Authority> authorities, String type, Date expiredDate) {
        String authorityNames = authorities.stream()
                .map(Authority::getAuthorityName)
                .collect(Collectors.joining(","));

        String token = Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, authorityNames)
                .claim(TOKEN_TYPE, type)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact();

        return Optional.ofNullable(token);
    }
}
