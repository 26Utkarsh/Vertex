package com.vertex.skip;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class UserSkipId implements Serializable {
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "tag")
    private String tag;

    protected UserSkipId() {
    }

    public UserSkipId(UUID userId, String tag) {
        this.userId = userId;
        this.tag = tag;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserSkipId that)) {
            return false;
        }
        return Objects.equals(userId, that.userId) && Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, tag);
    }
}

