package com.vertex.activity;

import com.vertex.activity.dto.UserActivityResponse;
import com.vertex.auth.CurrentUser;
import com.vertex.common.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private final ActivityService activityService;
    private final CurrentUser currentUser;

    public ActivityController(ActivityService activityService, CurrentUser currentUser) {
        this.activityService = activityService;
        this.currentUser = currentUser;
    }

    @GetMapping
    PageResponse<UserActivityResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return activityService.list(currentUser.id(jwt));
    }

    @PostMapping("/items/{itemId}/view")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void viewItem(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID itemId) {
        activityService.record(currentUser.id(jwt), UserActivityAction.VIEW_ITEM, itemId, "Viewed item detail", Map.of());
    }
}
