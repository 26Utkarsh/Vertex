package com.vertex.collector;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class RankingService {
    public double score(CollectedItem item, Instant now) {
        double starsGrowth = Math.log10(Math.max(item.popularity(), 0) + 1) * 25.0;
        double ageHours = Math.max(Duration.between(item.publishedAt(), now).toHours(), 0);
        double recency = Math.max(0.0, 100.0 - (ageHours * 2.0));
        double activity = Math.log10(Math.max(item.activity(), 0) + 1) * 25.0;
        return round((starsGrowth * 0.4) + (recency * 0.3) + (activity * 0.3));
    }

    public String reason(CollectedItem item, Instant now) {
        double ageHours = Math.max(Duration.between(item.publishedAt(), now).toHours(), 0);
        boolean fresh = ageHours <= 24;
        boolean popular = item.popularity() >= 250;
        boolean active = item.activity() >= 50;
        if (popular && fresh) {
            return "High growth + fresh";
        }
        if (active && fresh) {
            return "Active discussion + recent";
        }
        if (popular) {
            return "Strong source momentum";
        }
        if (fresh) {
            return "Recent and gaining attention";
        }
        return "Balanced score across growth and activity";
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
