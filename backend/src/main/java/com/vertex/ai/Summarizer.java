package com.vertex.ai;

import com.vertex.collector.CollectedItem;
import com.vertex.item.Item;

import java.util.List;

public interface Summarizer {
    String summarize(CollectedItem item, SummaryDepth depth);

    String summarizeBrief(List<Item> items);
}
