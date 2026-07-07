package com.vertex.story;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoryRepository extends JpaRepository<Story, UUID> {
    Page<Story> findAllByOrderByScoreDesc(Pageable pageable);
}

