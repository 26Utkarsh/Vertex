package com.vertex.collector;

import com.vertex.item.SourceType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record CollectedItem(
        SourceType source,
        String title,
        String url,
        String rawText,
        List<String> tags,
        Instant publishedAt,
        int popularity,
        int activity,
        Map<String, Object> metadata
) {
}

