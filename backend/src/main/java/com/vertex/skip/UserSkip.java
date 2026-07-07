package com.vertex.skip;

import com.vertex.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "user_skips")
public class UserSkip {
    @EmbeddedId
    private UserSkipId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int skipCount;

    @Column(nullable = false)
    private Instant updatedAt;

    protected UserSkip() {
    }

    public UserSkip(User user, String tag, Instant updatedAt) {
        this.id = new UserSkipId(user.getId(), tag);
        this.user = user;
        this.skipCount = 0;
        this.updatedAt = updatedAt;
    }

    public String getTag() {
        return id.getTag();
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void increment(Instant updatedAt) {
        this.skipCount++;
        this.updatedAt = updatedAt;
    }
}

