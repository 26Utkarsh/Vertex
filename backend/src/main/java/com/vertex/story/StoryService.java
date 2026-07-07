package com.vertex.story;

import com.vertex.common.PageResponse;
import com.vertex.item.Item;
import com.vertex.item.ItemRepository;
import com.vertex.item.SourceType;
import com.vertex.story.dto.StoryResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class StoryService {
    private static final double STORY_THRESHOLD = 0.85;

    private final StoryRepository storyRepository;
    private final ItemRepository itemRepository;
    private final EmbeddingService embeddingService;
    private final Clock clock;

    public StoryService(StoryRepository storyRepository, ItemRepository itemRepository, EmbeddingService embeddingService, Clock clock) {
        this.storyRepository = storyRepository;
        this.itemRepository = itemRepository;
        this.embeddingService = embeddingService;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public PageResponse<StoryResponse> list() {
        return PageResponse.from(storyRepository.findAllByOrderByScoreDesc(PageRequest.of(0, 10)).map(this::toResponse));
    }

    @Transactional
    public int rebuild() {
        Instant now = Instant.now(clock);
        List<Item> items = itemRepository.findByCreatedAtAfter(
                now.minus(10, ChronoUnit.DAYS),
                PageRequest.of(0, 80, Sort.by(Sort.Direction.DESC, "score"))
        ).getContent();
        List<Story> stories = buildStories(items, now);
        storyRepository.deleteAll();
        storyRepository.saveAll(stories);
        return stories.size();
    }

    private List<Story> buildStories(List<Item> items, Instant now) {
        List<EmbeddedItem> embeddedItems = items.stream()
                .map(item -> new EmbeddedItem(item, embeddingService.embed(item.getTitle() + " " + item.getSummary())))
                .toList();
        List<Story> stories = new ArrayList<>();
        Set<UUID> used = new LinkedHashSet<>();
        for (EmbeddedItem seed : embeddedItems) {
            if (used.contains(seed.item().getId())) {
                continue;
            }
            List<Item> group = new ArrayList<>();
            group.add(seed.item());
            for (EmbeddedItem candidate : embeddedItems) {
                if (seed.item().getId().equals(candidate.item().getId()) || seed.item().getSource() == candidate.item().getSource()) {
                    continue;
                }
                if (similarity(seed, candidate) > STORY_THRESHOLD) {
                    group.add(candidate.item());
                }
            }
            if (group.size() >= 2 && group.stream().map(Item::getSource).distinct().count() >= 2) {
                group.forEach(item -> used.add(item.getId()));
                stories.add(toStory(group, now));
            }
        }
        return stories.stream()
                .sorted(Comparator.comparingDouble(Story::getScore).reversed())
                .limit(10)
                .toList();
    }

    private Story toStory(List<Item> group, Instant now) {
        Item lead = group.stream().max(Comparator.comparingDouble(Item::getScore)).orElseThrow();
        List<UUID> itemIds = group.stream().map(Item::getId).toList();
        List<SourceType> sources = group.stream().map(Item::getSource).distinct().toList();
        double score = group.stream().mapToDouble(Item::getScore).max().orElse(0.0);
        return new Story(lead.getTitle(), itemIds, sources, score, now);
    }

    private StoryResponse toResponse(Story story) {
        return new StoryResponse(story.getId(), story.getTitle(), story.getItemIds(), story.getSources(), story.getScore(), story.getCreatedAt(), story.getUpdatedAt());
    }

    private double similarity(EmbeddedItem left, EmbeddedItem right) {
        if (normalizedUrl(left.item()).equals(normalizedUrl(right.item()))) {
            return 1.0;
        }
        return embeddingService.cosine(left.embedding(), right.embedding());
    }

    private String normalizedUrl(Item item) {
        return item.getUrl().toLowerCase().replace("https://", "").replace("http://", "").replace("www.", "").replaceAll("/$", "");
    }

    private record EmbeddedItem(Item item, double[] embedding) {
    }
}
