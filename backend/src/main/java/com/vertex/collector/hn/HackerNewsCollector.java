package com.vertex.collector.hn;

import com.vertex.collector.CollectedItem;
import com.vertex.item.SourceType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class HackerNewsCollector {
    private final RestClient restClient;

    public HackerNewsCollector(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<CollectedItem> collect() {
        Integer[] ids = restClient.get()
                .uri("https://hacker-news.firebaseio.com/v0/topstories.json")
                .retrieve()
                .body(Integer[].class);
        if (ids == null) {
            return List.of();
        }
        return Arrays.stream(ids)
                .limit(30)
                .map(this::fetchStory)
                .filter(Objects::nonNull)
                .filter(story -> story.url() != null && !story.url().isBlank())
                .map(this::map)
                .toList();
    }

    private HackerNewsStory fetchStory(Integer id) {
        return restClient.get()
                .uri("https://hacker-news.firebaseio.com/v0/item/{id}.json", id)
                .retrieve()
                .body(HackerNewsStory.class);
    }

    private CollectedItem map(HackerNewsStory story) {
        Instant publishedAt = Instant.ofEpochSecond(story.time());
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("hnId", story.id());
        metadata.put("by", story.by());
        metadata.put("comments", story.descendants());
        metadata.put("score", story.score());
        metadata.put("discussionUrl", "https://news.ycombinator.com/item?id=" + story.id());
        return new CollectedItem(
                SourceType.HACKER_NEWS,
                story.title(),
                story.url(),
                "HN story by %s with %d points and %d comments: %s".formatted(
                        story.by(),
                        story.score(),
                        story.descendants(),
                        story.title()
                ),
                List.of("hacker-news", "tech-news"),
                publishedAt,
                story.score(),
                story.descendants(),
                metadata
        );
    }
}
