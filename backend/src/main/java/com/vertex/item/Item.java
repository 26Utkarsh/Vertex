package com.vertex.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "items", uniqueConstraints = @UniqueConstraint(name = "items_source_url_unique", columnNames = {"source", "url"}))
public class Item {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SourceType source;

    @Column(nullable = false, columnDefinition = "text")
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String url;

    @Column(nullable = false, columnDefinition = "text")
    private String summary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false)
    private double score;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> metadata = new LinkedHashMap<>();

    protected Item() {
    }

    public Item(SourceType source, String title, String url, String summary, List<String> tags, double score, Instant createdAt, Map<String, Object> metadata) {
        this.source = source;
        this.title = title;
        this.url = url;
        this.summary = summary;
        this.tags = new ArrayList<>(tags);
        this.score = score;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.metadata = new LinkedHashMap<>(metadata);
    }

    public UUID getId() {
        return id;
    }

    public SourceType getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getTags() {
        return List.copyOf(tags);
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

    public Map<String, Object> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    public void refresh(String title, String summary, List<String> tags, double score, Map<String, Object> metadata, Instant updatedAt) {
        this.title = title;
        this.summary = summary;
        this.tags = new ArrayList<>(tags);
        this.score = score;
        this.metadata = new LinkedHashMap<>(metadata);
        this.updatedAt = updatedAt;
    }
}
