package com.vertex.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(nullable = false, unique = true)
    private String googleId;

    @Column(nullable = false)
    private Instant createdAt;

    protected User() {
    }

    public User(String email, String googleId, Instant createdAt) {
        this.email = email;
        this.googleId = googleId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

