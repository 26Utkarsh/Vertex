package com.vertex.skip;

import com.vertex.auth.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/skips")
public class SkipController {
    private final SkipService skipService;
    private final CurrentUser currentUser;

    public SkipController(SkipService skipService, CurrentUser currentUser) {
        this.skipService = skipService;
        this.currentUser = currentUser;
    }

    @PostMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void skip(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID itemId) {
        skipService.skip(currentUser.id(jwt), itemId);
    }
}

