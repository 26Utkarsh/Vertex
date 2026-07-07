package com.vertex.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static final int MAX_REQUESTS_PER_MINUTE = 180;
    private static final int MAX_INTERNAL_REQUESTS_PER_MINUTE = 24;
    private static final long WINDOW_SECONDS = 60L;
    private final Map<String, Window> windows = new ConcurrentHashMap<>();
    private final Clock clock;

    public RateLimitFilter(Clock clock) {
        this.clock = clock;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/") || "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        cleanup();
        String key = clientKey(request);
        int limit = request.getRequestURI().startsWith("/api/internal/") ? MAX_INTERNAL_REQUESTS_PER_MINUTE : MAX_REQUESTS_PER_MINUTE;
        Window window = windows.compute(key, (ignored, existing) -> {
            Instant now = Instant.now(clock);
            if (existing == null || existing.expiresAt().isBefore(now)) {
                return new Window(new AtomicInteger(1), now.plusSeconds(WINDOW_SECONDS));
            }
            existing.count().incrementAndGet();
            return existing;
        });
        if (window.count().get() > limit) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Too many requests\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String clientKey(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        String ip = forwardedFor == null || forwardedFor.isBlank()
                ? request.getRemoteAddr()
                : forwardedFor.split(",")[0].trim();
        return request.getRequestURI().startsWith("/api/internal/") ? "internal:" + ip : "public:" + ip;
    }

    private void cleanup() {
        Instant now = Instant.now(clock);
        Iterator<Map.Entry<String, Window>> iterator = windows.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue().expiresAt().isBefore(now)) {
                iterator.remove();
            }
        }
    }

    private record Window(AtomicInteger count, Instant expiresAt) {
    }
}

