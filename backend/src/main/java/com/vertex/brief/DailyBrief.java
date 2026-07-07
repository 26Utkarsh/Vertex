package com.vertex.brief;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "daily_briefs")
public class DailyBrief {
    @Id
    @Column(name = "brief_date")
    private LocalDate briefDate;

    @Column(nullable = false, columnDefinition = "text")
    private String summary;

    @Column(nullable = false)
    private Instant createdAt;

    protected DailyBrief() {
    }

    public DailyBrief(LocalDate briefDate, String summary, Instant createdAt) {
        this.briefDate = briefDate;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    public LocalDate getBriefDate() {
        return briefDate;
    }

    public String getSummary() {
        return summary;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

