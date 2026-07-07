package com.vertex.auth;

import com.vertex.activity.ActivityService;
import com.vertex.activity.UserActivityAction;
import com.vertex.config.VertexProperties;
import com.vertex.user.User;
import com.vertex.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Component
public class GoogleOAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtService jwtService;
    private final VertexProperties properties;
    private final ActivityService activityService;

    public GoogleOAuthSuccessHandler(UserService userService, JwtService jwtService, VertexProperties properties, ActivityService activityService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.properties = properties;
        this.activityService = activityService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (!(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            throw new ServletException("Google OAuth did not return an OIDC user");
        }
        String email = oidcUser.getEmail();
        String googleId = oidcUser.getSubject();
        User user = userService.findOrCreateGoogleUser(email, googleId);
        activityService.record(user.getId(), UserActivityAction.LOGIN, null, "Signed in with Google", Map.of("email", email));
        String token = jwtService.createToken(user);
        String redirect = UriComponentsBuilder.fromUriString(properties.frontendUrl())
                .path("/auth/callback")
                .queryParam("token", token)
                .build()
                .toUriString();
        response.sendRedirect(redirect);
    }
}
