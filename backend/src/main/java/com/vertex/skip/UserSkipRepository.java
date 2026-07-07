package com.vertex.skip;

import com.vertex.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkipRepository extends JpaRepository<UserSkip, UserSkipId> {
    List<UserSkip> findAllByUserAndSkipCountGreaterThan(User user, int skipCount);
}

