package com.vertex.brief;

import com.vertex.ai.Summarizer;
import com.vertex.brief.dto.DailyBriefResponse;
import com.vertex.item.Item;
import com.vertex.item.ItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DailyBriefService {
    private final DailyBriefRepository dailyBriefRepository;
    private final ItemRepository itemRepository;
    private final Summarizer summarizer;
    private final Clock clock;

    public DailyBriefService(DailyBriefRepository dailyBriefRepository, ItemRepository itemRepository, Summarizer summarizer, Clock clock) {
        this.dailyBriefRepository = dailyBriefRepository;
        this.itemRepository = itemRepository;
        this.summarizer = summarizer;
        this.clock = clock;
    }

    @Transactional
    public DailyBriefResponse today() {
        LocalDate today = LocalDate.now(clock.withZone(ZoneOffset.UTC));
        DailyBrief brief = dailyBriefRepository.findById(today)
                .orElseGet(() -> dailyBriefRepository.save(createBrief(today)));
        return new DailyBriefResponse(brief.getBriefDate(), brief.getSummary(), brief.getCreatedAt());
    }

    private DailyBrief createBrief(LocalDate today) {
        Instant now = Instant.now(clock);
        List<Item> items = itemRepository.findByCreatedAtAfter(
                now.minus(24, ChronoUnit.HOURS),
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "score", "createdAt"))
        ).getContent();
        String summary = summarizer.summarizeBrief(items);
        return new DailyBrief(today, summary, now);
    }
}

