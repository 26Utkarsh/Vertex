package com.vertex.collector;

import com.vertex.item.SourceType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RankingServiceTest {
    private final RankingService rankingService = new RankingService();

    @Test
    void givesHigherScoreToFreshActiveItems() {
        Instant now = Instant.parse("2026-07-07T00:00:00Z");
        CollectedItem fresh = item(now.minusSeconds(3600), 1000, 250);
        CollectedItem stale = item(now.minusSeconds(60 * 60 * 72), 100, 5);

        assertThat(rankingService.score(fresh, now)).isGreaterThan(rankingService.score(stale, now));
    }

    @Test
    void explainsWhyFreshPopularItemsRanked() {
        Instant now = Instant.parse("2026-07-07T00:00:00Z");

        assertThat(rankingService.reason(item(now.minusSeconds(3600), 1000, 250), now))
                .isEqualTo("High growth + fresh");
    }

    private CollectedItem item(Instant publishedAt, int popularity, int activity) {
        return new CollectedItem(
                SourceType.GITHUB,
                "owner/repo",
                "https://github.com/owner/repo",
                "Useful developer project",
                List.of("github"),
                publishedAt,
                popularity,
                activity,
                Map.of()
        );
    }
}
