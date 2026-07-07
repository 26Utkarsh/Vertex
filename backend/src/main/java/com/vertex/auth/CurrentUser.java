package com.vertex.auth;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUser {
    public UUID id(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}

