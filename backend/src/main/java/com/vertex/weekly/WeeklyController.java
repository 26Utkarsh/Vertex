package com.vertex.weekly;

import com.vertex.config.VertexProperties;
import com.vertex.weekly.dto.WeeklyDiffResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class WeeklyController {
    private static final String INTERNAL_API_KEY_HEADER = "X-Internal-Api-Key";
    private final WeeklyService weeklyService;
    private final VertexProperties properties;

    public WeeklyController(WeeklyService weeklyService, VertexProperties properties) {
        this.weeklyService = weeklyService;
        this.properties = properties;
    }

    @GetMapping("/weekly")
    WeeklyDiffResponse diff() {
        return weeklyService.diff();
    }

    @PostMapping("/internal/weekly/snapshot")
    void snapshot(HttpServletRequest request) {
        String provided = request.getHeader(INTERNAL_API_KEY_HEADER);
        if (provided == null || !provided.equals(properties.internalApiKey())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid internal API key");
        }
        weeklyService.snapshotCurrentWeek();
    }
}

