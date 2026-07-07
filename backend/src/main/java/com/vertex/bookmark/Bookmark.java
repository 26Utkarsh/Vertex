package com.vertex.bookmark;

import com.vertex.item.Item;
import com.vertex.user.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "bookmarks")
public class Bookmark {
    @EmbeddedId
    private BookmarkId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    private Instant createdAt;

    protected Bookmark() {
    }

    public Bookmark(User user, Item item, Instant createdAt) {
        this.user = user;
        this.item = item;
        this.createdAt = createdAt;
        this.id = new BookmarkId(user.getId(), item.getId());
    }

    public BookmarkId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Item getItem() {
        return item;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

