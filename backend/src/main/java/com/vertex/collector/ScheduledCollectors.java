package com.vertex.collector;

import com.vertex.config.VertexProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledCollectors {
    private final CollectorOrchestrator collectorOrchestrator;
    private final VertexProperties properties;

    public ScheduledCollectors(CollectorOrchestrator collectorOrchestrator, VertexProperties properties) {
        this.collectorOrchestrator = collectorOrchestrator;
        this.properties = properties;
    }

    @Scheduled(cron = "0 0 */6 * * *", zone = "UTC")
    void collect() {
        if (properties.collectorEnabled()) {
            collectorOrchestrator.runAll();
        }
    }
}

