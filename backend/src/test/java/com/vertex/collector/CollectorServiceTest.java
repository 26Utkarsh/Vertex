package com.vertex.collector;

import com.vertex.ai.Summarizer;
import com.vertex.item.Item;
import com.vertex.item.ItemRepository;
import com.vertex.item.SourceType;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CollectorServiceTest {
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final RankingService rankingService = new RankingService();
    private final Summarizer summarizer = new Summarizer() {
        @Override
        public String summarize(CollectedItem item, com.vertex.ai.SummaryDepth depth) {
            return depth.name() + " summary";
        }

        @Override
        public String summarizeBrief(List<com.vertex.item.Item> items) {
            return "Brief";
        }
    };
    private final Clock clock = Clock.fixed(Instant.parse("2026-07-07T00:00:00Z"), ZoneOffset.UTC);
    private final CollectorService collectorService = new CollectorService(itemRepository, rankingService, summarizer, clock);

    @Test
    void persistsCollectedItemsThroughRepository() {
        when(itemRepository.findBySourceAndUrl(SourceType.GITHUB, "https://github.com/owner/repo")).thenReturn(Optional.empty());
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        int writes = collectorService.persist(List.of(new CollectedItem(
                SourceType.GITHUB,
                "owner/repo",
                "https://github.com/owner/repo",
                "Raw text",
                List.of("github"),
                Instant.parse("2026-07-06T00:00:00Z"),
                100,
                20,
                Map.of("stars", 100)
        )));

        assertThat(writes).isEqualTo(1);
        verify(itemRepository).save(any(Item.class));
    }
}
