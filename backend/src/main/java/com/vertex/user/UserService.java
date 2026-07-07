package com.vertex.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Clock clock;

    public UserService(UserRepository userRepository, Clock clock) {
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Transactional
    public User findOrCreateGoogleUser(String email, String googleId) {
        return userRepository.findByGoogleId(googleId)
                .orElseGet(() -> userRepository.save(new User(email, googleId, Instant.now(clock))));
    }

    @Transactional(readOnly = true)
    public User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user was not found"));
    }
}

