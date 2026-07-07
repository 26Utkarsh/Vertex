package com.vertex.bookmark;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class BookmarkId implements Serializable {
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "item_id")
    private UUID itemId;

    protected BookmarkId() {
    }

    public BookmarkId(UUID userId, UUID itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getItemId() {
        return itemId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BookmarkId bookmarkId)) {
            return false;
        }
        return Objects.equals(userId, bookmarkId.userId) && Objects.equals(itemId, bookmarkId.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, itemId);
    }
}

