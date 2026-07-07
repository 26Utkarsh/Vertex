package com.vertex.collector;

import com.vertex.activity.ActivityService;
import com.vertex.activity.UserActivityAction;
import com.vertex.auth.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/collectors")
public class RefreshController {
    private final CollectorOrchestrator collectorOrchestrator;
    private final ActivityService activityService;
    private final CurrentUser currentUser;

    public RefreshController(CollectorOrchestrator collectorOrchestrator, ActivityService activityService, CurrentUser currentUser) {
        this.collectorOrchestrator = collectorOrchestrator;
        this.activityService = activityService;
        this.currentUser = currentUser;
    }

    @PostMapping("/refresh")
    CollectorRunResult refresh(@AuthenticationPrincipal Jwt jwt) {
        CollectorRunResult result = collectorOrchestrator.runAll();
        activityService.record(
                currentUser.id(jwt),
                UserActivityAction.REFRESH_SIGNALS,
                null,
                "Refreshed GitHub, arXiv, and Hacker News signals",
                Map.of("writes", result.writes())
        );
        return result;
    }
}
