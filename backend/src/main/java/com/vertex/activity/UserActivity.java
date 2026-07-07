package com.vertex.activity;

import com.vertex.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "user_activities")
public class UserActivity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 48)
    private UserActivityAction action;

    @Column(name = "item_id")
    private UUID itemId;

    @Column(nullable = false, columnDefinition = "text")
    private String detail;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> metadata = new LinkedHashMap<>();

    @Column(nullable = false)
    private Instant createdAt;

    protected UserActivity() {
    }

    public UserActivity(User user, UserActivityAction action, UUID itemId, String detail, Map<String, Object> metadata, Instant createdAt) {
        this.user = user;
        this.action = action;
        this.itemId = itemId;
        this.detail = detail;
        this.metadata = new LinkedHashMap<>(metadata);
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UserActivityAction getAction() {
        return action;
    }

    public UUID getItemId() {
        return itemId;
    }

    public String getDetail() {
        return detail;
    }

    public Map<String, Object> getMetadata() {
        return Map.copyOf(metadata);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
