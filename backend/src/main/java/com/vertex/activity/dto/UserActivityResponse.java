package com.vertex.activity.dto;

import com.vertex.activity.UserActivityAction;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record UserActivityResponse(
        UUID id,
        UserActivityAction action,
        UUID itemId,
        String detail,
        Map<String, Object> metadata,
        Instant createdAt
) {
}
