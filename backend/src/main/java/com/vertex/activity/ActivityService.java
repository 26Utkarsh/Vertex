package com.vertex.activity;

import com.vertex.activity.dto.UserActivityResponse;
import com.vertex.common.PageResponse;
import com.vertex.user.User;
import com.vertex.user.UserService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ActivityService {
    private final UserActivityRepository repository;
    private final UserService userService;
    private final Clock clock;

    public ActivityService(UserActivityRepository repository, UserService userService, Clock clock) {
        this.repository = repository;
        this.userService = userService;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public PageResponse<UserActivityResponse> list(UUID userId) {
        User user = userService.requireUser(userId);
        List<UserActivityResponse> activities = repository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(0, 50)).stream()
                .map(this::toResponse)
                .toList();
        return PageResponse.from(new PageImpl<>(activities, PageRequest.of(0, Math.max(activities.size(), 1)), activities.size()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(UUID userId, UserActivityAction action, UUID itemId, String detail, Map<String, Object> metadata) {
        User user = userService.requireUser(userId);
        repository.save(new UserActivity(user, action, itemId, detail, metadata, Instant.now(clock)));
    }

    private UserActivityResponse toResponse(UserActivity activity) {
        return new UserActivityResponse(
                activity.getId(),
                activity.getAction(),
                activity.getItemId(),
                activity.getDetail(),
                activity.getMetadata(),
                activity.getCreatedAt()
        );
    }
}
