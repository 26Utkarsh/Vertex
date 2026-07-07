package com.vertex.activity;

import com.vertex.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {
    List<UserActivity> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
