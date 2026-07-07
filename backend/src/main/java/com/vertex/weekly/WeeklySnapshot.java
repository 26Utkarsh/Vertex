package com.vertex.weekly;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "weekly_snapshots")
public class WeeklySnapshot {
    @Id
    @Column(name = "snapshot_week")
    private LocalDate snapshotWeek;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<UUID> itemIds = new ArrayList<>();

    @Column(nullable = false)
    private Instant createdAt;

    protected WeeklySnapshot() {
    }

    public WeeklySnapshot(LocalDate snapshotWeek, List<UUID> itemIds, Instant createdAt) {
        this.snapshotWeek = snapshotWeek;
        this.itemIds = new ArrayList<>(itemIds);
        this.createdAt = createdAt;
    }

    public LocalDate getSnapshotWeek() {
        return snapshotWeek;
    }

    public List<UUID> getItemIds() {
        return List.copyOf(itemIds);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

