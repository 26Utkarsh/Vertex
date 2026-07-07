package com.vertex.brief.dto;

import java.time.Instant;
import java.time.LocalDate;

public record DailyBriefResponse(LocalDate date, String summary, Instant createdAt) {
}

