package com.vertex.story;

import com.vertex.item.SourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stories")
public class Story {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, columnDefinition = "text")
    private String title;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<UUID> itemIds = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<SourceType> sources = new ArrayList<>();

    @Column(nullable = false)
    private double score;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Story() {
    }

    public Story(String title, List<UUID> itemIds, List<SourceType> sources, double score, Instant createdAt) {
        this.title = title;
        this.itemIds = new ArrayList<>(itemIds);
        this.sources = new ArrayList<>(sources);
        this.score = score;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<UUID> getItemIds() {
        return List.copyOf(itemIds);
    }

    public List<SourceType> getSources() {
        return List.copyOf(sources);
    }

    public double getScore() {
        return score;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

