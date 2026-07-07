package com.vertex.user;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final Clock clock = Clock.fixed(Instant.parse("2026-07-07T00:00:00Z"), ZoneOffset.UTC);
    private final UserService userService = new UserService(userRepository, clock);

    @Test
    void createsGoogleUserWhenMissing() {
        when(userRepository.findByGoogleId("google-123")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.findOrCreateGoogleUser("dev@example.com", "google-123");

        assertThat(user.getEmail()).isEqualTo("dev@example.com");
        assertThat(user.getGoogleId()).isEqualTo("google-123");
        assertThat(user.getCreatedAt()).isEqualTo(Instant.parse("2026-07-07T00:00:00Z"));
    }
}

