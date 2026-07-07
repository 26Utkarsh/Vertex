package com.vertex.auth;

import com.vertex.user.User;
import com.vertex.user.UserService;
import com.vertex.user.dto.UserResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final CurrentUser currentUser;
    private final UserService userService;

    public AuthController(CurrentUser currentUser, UserService userService) {
        this.currentUser = currentUser;
        this.userService = userService;
    }

    @GetMapping("/me")
    UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        User user = userService.requireUser(currentUser.id(jwt));
        return new UserResponse(user.getId(), user.getEmail());
    }
}

