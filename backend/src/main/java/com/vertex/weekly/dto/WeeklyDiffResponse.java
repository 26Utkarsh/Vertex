package com.vertex.weekly.dto;

import com.vertex.item.dto.ItemResponse;

import java.time.LocalDate;
import java.util.List;

public record WeeklyDiffResponse(LocalDate week, List<ItemResponse> newItems, List<ItemResponse> droppedItems) {
}

