package com.vertex.item;

import com.vertex.item.dto.ItemResponse;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    public ItemResponse toResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getSource(),
                item.getTitle(),
                item.getUrl(),
                item.getSummary(),
                item.getTags(),
                item.getScore(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getMetadata()
        );
    }
}

