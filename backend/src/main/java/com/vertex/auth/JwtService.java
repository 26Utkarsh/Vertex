package com.vertex.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.vertex.config.VertexProperties;
import com.vertex.user.User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
    public static final String HMAC_ALGORITHM = "HmacSHA256";

    private final VertexProperties properties;

    public JwtService(VertexProperties properties) {
        this.properties = properties;
    }

    public String createToken(User user) {
        byte[] secret = properties.jwtSecret().getBytes(StandardCharsets.UTF_8);
        if (secret.length < 32) {
            throw new IllegalStateException("JWT_SECRET must be at least 32 bytes");
        }
        Instant now = Instant.now();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .issuer("vertex")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(7, ChronoUnit.DAYS)))
                .build();
        try {
            JWSSigner signer = new MACSigner(secret);
            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
            jwt.sign(signer);
            return jwt.serialize();
        } catch (JOSEException exception) {
            throw new IllegalStateException("Failed to create JWT", exception);
        }
    }
}

