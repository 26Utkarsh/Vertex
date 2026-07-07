package com.vertex.weekly;

import com.vertex.item.Item;
import com.vertex.item.ItemMapper;
import com.vertex.item.ItemRepository;
import com.vertex.item.dto.ItemResponse;
import com.vertex.weekly.dto.WeeklyDiffResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WeeklyService {
    private final WeeklySnapshotRepository weeklySnapshotRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final Clock clock;

    public WeeklyService(WeeklySnapshotRepository weeklySnapshotRepository, ItemRepository itemRepository, ItemMapper itemMapper, Clock clock) {
        this.weeklySnapshotRepository = weeklySnapshotRepository;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.clock = clock;
    }

    @Transactional
    public WeeklySnapshot snapshotCurrentWeek() {
        LocalDate week = currentWeek();
        List<UUID> topIds = topItems().stream().map(Item::getId).toList();
        return weeklySnapshotRepository.save(new WeeklySnapshot(week, topIds, Instant.now(clock)));
    }

    @Transactional(readOnly = true)
    public WeeklyDiffResponse diff() {
        LocalDate week = currentWeek();
        List<Item> current = topItems();
        Set<UUID> currentIds = current.stream().map(Item::getId).collect(Collectors.toSet());
        WeeklySnapshot previous = weeklySnapshotRepository.findById(week.minusWeeks(1)).orElse(null);
        Set<UUID> previousIds = previous == null ? Set.of() : Set.copyOf(previous.getItemIds());
        List<ItemResponse> newItems = current.stream()
                .filter(item -> !previousIds.contains(item.getId()))
                .map(itemMapper::toResponse)
                .toList();
        List<ItemResponse> droppedItems = previous == null
                ? List.of()
                : itemRepository.findAllById(previous.getItemIds()).stream()
                .filter(item -> !currentIds.contains(item.getId()))
                .map(itemMapper::toResponse)
                .toList();
        return new WeeklyDiffResponse(week, newItems, droppedItems);
    }

    private List<Item> topItems() {
        return itemRepository.findAll(PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "score", "createdAt"))).getContent();
    }

    private LocalDate currentWeek() {
        return LocalDate.now(clock.withZone(ZoneOffset.UTC)).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
}

