package com.vertex.collector;

import com.vertex.config.VertexProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/internal/collectors")
public class CollectorController {
    private static final String INTERNAL_API_KEY_HEADER = "X-Internal-Api-Key";

    private final CollectorOrchestrator collectorOrchestrator;
    private final VertexProperties properties;

    public CollectorController(CollectorOrchestrator collectorOrchestrator, VertexProperties properties) {
        this.collectorOrchestrator = collectorOrchestrator;
        this.properties = properties;
    }

    @PostMapping("/github")
    CollectorRunResult github(HttpServletRequest request) {
        requireInternalKey(request);
        return collectorOrchestrator.runGithub();
    }

    @PostMapping("/arxiv")
    CollectorRunResult arxiv(HttpServletRequest request) {
        requireInternalKey(request);
        return collectorOrchestrator.runArxiv();
    }

    @PostMapping("/hacker-news")
    CollectorRunResult hackerNews(HttpServletRequest request) {
        requireInternalKey(request);
        return collectorOrchestrator.runHackerNews();
    }

    @PostMapping("/all")
    CollectorRunResult all(HttpServletRequest request) {
        requireInternalKey(request);
        return collectorOrchestrator.runAll();
    }

    @PostMapping("/stories")
    CollectorRunResult stories(HttpServletRequest request) {
        requireInternalKey(request);
        return collectorOrchestrator.rebuildStories();
    }

    private void requireInternalKey(HttpServletRequest request) {
        String provided = request.getHeader(INTERNAL_API_KEY_HEADER);
        if (provided == null || !provided.equals(properties.internalApiKey())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid internal API key");
        }
    }
}
