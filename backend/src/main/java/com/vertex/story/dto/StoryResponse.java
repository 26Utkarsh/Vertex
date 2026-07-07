package com.vertex.story.dto;

import com.vertex.item.SourceType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record StoryResponse(UUID id, String title, List<UUID> itemIds, List<SourceType> sources, double score, Instant createdAt, Instant updatedAt) {
}

