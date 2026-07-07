package com.vertex.bookmark;

import com.vertex.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
    List<Bookmark> findAllByUserOrderByCreatedAtDesc(User user);
}

