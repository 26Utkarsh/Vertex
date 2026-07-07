package com.vertex.collector.github;

import com.vertex.collector.CollectedItem;
import com.vertex.config.VertexProperties;
import com.vertex.item.SourceType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GitHubCollector {
    private final RestClient restClient;
    private final VertexProperties properties;

    public GitHubCollector(RestClient restClient, VertexProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public List<CollectedItem> collect() {
        LocalDate since = LocalDate.now(ZoneOffset.UTC).minusDays(7);
        RestClient.RequestHeadersSpec<?> request = restClient.get()
                .uri("https://api.github.com/search/repositories?q=created:>{since}&sort=stars&order=desc&per_page=30", since);
        if (properties.githubToken() != null && !properties.githubToken().isBlank()) {
            request = request.header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.githubToken());
        }
        GitHubSearchResponse response = request.retrieve().body(GitHubSearchResponse.class);
        if (response == null || response.items() == null) {
            return List.of();
        }
        return response.items().stream().map(this::map).toList();
    }

    private CollectedItem map(GitHubRepository repository) {
        Instant createdAt = Instant.parse(repository.createdAt());
        Map<String, Object> metadata = new LinkedHashMap<>();
        HealthSignals healthSignals = healthSignals(repository.fullName());
        metadata.put("fullName", repository.fullName());
        metadata.put("language", repository.language());
        metadata.put("stars", repository.stargazersCount());
        metadata.put("forks", repository.forksCount());
        metadata.put("openIssues", repository.openIssuesCount());
        metadata.put("pushedAt", repository.pushedAt());
        metadata.put("commit_frequency_delta", healthSignals.commitFrequencyDelta());
        metadata.put("commitsLast30Days", healthSignals.currentWindowCommits());
        metadata.put("commitsPrior30Days", healthSignals.previousWindowCommits());
        if (healthSignals.declining()) {
            metadata.put("health_flag", "declining");
        }
        return new CollectedItem(
                SourceType.GITHUB,
                repository.fullName(),
                repository.htmlUrl(),
                "%s. Language: %s. Stars: %d. Forks: %d.".formatted(
                        repository.description() == null ? repository.fullName() : repository.description(),
                        repository.language() == null ? "unknown" : repository.language(),
                        repository.stargazersCount(),
                        repository.forksCount()
                ),
                repository.language() == null ? List.of("github") : List.of("github", repository.language()),
                createdAt,
                repository.stargazersCount(),
                repository.forksCount() + repository.openIssuesCount(),
                metadata
        );
    }

    private HealthSignals healthSignals(String fullName) {
        String[] parts = fullName.split("/", 2);
        if (parts.length != 2) {
            return HealthSignals.unknown();
        }
        Instant now = Instant.now();
        int current = commitCount(parts[0], parts[1], now.minusSeconds(30L * 24L * 60L * 60L), now);
        int previous = commitCount(parts[0], parts[1], now.minusSeconds(60L * 24L * 60L * 60L), now.minusSeconds(30L * 24L * 60L * 60L));
        double delta = previous == 0 ? 0.0 : ((double) current - previous) / previous;
        return new HealthSignals(current, previous, Math.round(delta * 100.0) / 100.0, previous > 0 && current < previous * 0.5);
    }

    private int commitCount(String owner, String repo, Instant since, Instant until) {
        try {
            RestClient.RequestHeadersSpec<?> request = restClient.get()
                    .uri("https://api.github.com/repos/{owner}/{repo}/commits?since={since}&until={until}&per_page=1",
                            owner, repo, since.toString(), until.toString());
            if (properties.githubToken() != null && !properties.githubToken().isBlank()) {
                request = request.header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.githubToken());
            }
            ResponseEntity<GitHubCommit[]> response = request.retrieve().toEntity(GitHubCommit[].class);
            String linkHeader = response.getHeaders().getFirst(HttpHeaders.LINK);
            Integer lastPage = parseLastPage(linkHeader);
            if (lastPage != null) {
                return lastPage;
            }
            GitHubCommit[] body = response.getBody();
            return body == null ? 0 : body.length;
        } catch (RestClientException exception) {
            return 0;
        }
    }

    private Integer parseLastPage(String linkHeader) {
        if (linkHeader == null || linkHeader.isBlank()) {
            return null;
        }
        for (String part : linkHeader.split(",")) {
            if (part.contains("rel=\"last\"")) {
                int pageIndex = part.indexOf("page=");
                if (pageIndex < 0) {
                    return null;
                }
                int start = pageIndex + "page=".length();
                int end = start;
                while (end < part.length() && Character.isDigit(part.charAt(end))) {
                    end++;
                }
                return Integer.parseInt(part.substring(start, end));
            }
        }
        return null;
    }

    private record HealthSignals(int currentWindowCommits, int previousWindowCommits, double commitFrequencyDelta, boolean declining) {
        static HealthSignals unknown() {
            return new HealthSignals(0, 0, 0.0, false);
        }
    }
}
