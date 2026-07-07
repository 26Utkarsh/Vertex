package com.vertex.collector;

import com.vertex.ai.Summarizer;
import com.vertex.ai.SummaryDepth;
import com.vertex.item.Item;
import com.vertex.item.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CollectorService {
    private final ItemRepository itemRepository;
    private final RankingService rankingService;
    private final Summarizer summarizer;
    private final Clock clock;

    public CollectorService(ItemRepository itemRepository, RankingService rankingService, Summarizer summarizer, Clock clock) {
        this.itemRepository = itemRepository;
        this.rankingService = rankingService;
        this.summarizer = summarizer;
        this.clock = clock;
    }

    @Transactional
    public int persist(List<CollectedItem> collectedItems) {
        Instant now = Instant.now(clock);
        int writes = 0;
        for (CollectedItem collectedItem : collectedItems) {
            double score = rankingService.score(collectedItem, now);
            String seniorSummary = summarizer.summarize(collectedItem, SummaryDepth.SENIOR);
            String juniorSummary = summarizer.summarize(collectedItem, SummaryDepth.JUNIOR);
            Map<String, Object> enrichedMetadata = new LinkedHashMap<>(collectedItem.metadata());
            enrichedMetadata.put("rank_reason", rankingService.reason(collectedItem, now));
            enrichedMetadata.put("summary_senior", seniorSummary);
            enrichedMetadata.put("summary_junior", juniorSummary);
            Item item = itemRepository.findBySourceAndUrl(collectedItem.source(), collectedItem.url())
                    .orElseGet(() -> new Item(
                            collectedItem.source(),
                            collectedItem.title(),
                            collectedItem.url(),
                            seniorSummary,
                            collectedItem.tags(),
                            score,
                            collectedItem.publishedAt(),
                            enrichedMetadata
                    ));
            if (item.getId() != null) {
                item.refresh(collectedItem.title(), seniorSummary, collectedItem.tags(), score, enrichedMetadata, now);
            }
            itemRepository.save(item);
            writes++;
        }
        return writes;
    }
}
