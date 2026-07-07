package com.vertex.bookmark;

import com.vertex.activity.ActivityService;
import com.vertex.activity.UserActivityAction;
import com.vertex.common.PageResponse;
import com.vertex.item.Item;
import com.vertex.item.ItemMapper;
import com.vertex.item.ItemRepository;
import com.vertex.item.dto.ItemResponse;
import com.vertex.user.User;
import com.vertex.user.UserService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final Clock clock;
    private final ActivityService activityService;

    public BookmarkService(BookmarkRepository bookmarkRepository, ItemRepository itemRepository, UserService userService, ItemMapper itemMapper, Clock clock, ActivityService activityService) {
        this.bookmarkRepository = bookmarkRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.itemMapper = itemMapper;
        this.clock = clock;
        this.activityService = activityService;
    }

    @Transactional(readOnly = true)
    public PageResponse<ItemResponse> list(UUID userId) {
        User user = userService.requireUser(userId);
        List<ItemResponse> items = bookmarkRepository.findAllByUserOrderByCreatedAtDesc(user).stream()
                .map(Bookmark::getItem)
                .map(itemMapper::toResponse)
                .toList();
        return PageResponse.from(new PageImpl<>(items, PageRequest.of(0, Math.max(items.size(), 1)), items.size()));
    }

    @Transactional
    public void add(UUID userId, UUID itemId) {
        User user = userService.requireUser(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        BookmarkId id = new BookmarkId(user.getId(), item.getId());
        if (!bookmarkRepository.existsById(id)) {
            bookmarkRepository.save(new Bookmark(user, item, Instant.now(clock)));
            activityService.record(user.getId(), UserActivityAction.BOOKMARK_ADD, item.getId(), "Saved bookmark: " + item.getTitle(), Map.of("source", item.getSource().name()));
        }
    }

    @Transactional
    public void remove(UUID userId, UUID itemId) {
        User user = userService.requireUser(userId);
        BookmarkId id = new BookmarkId(user.getId(), itemId);
        if (!bookmarkRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bookmark not found");
        }
        bookmarkRepository.deleteById(id);
        activityService.record(user.getId(), UserActivityAction.BOOKMARK_REMOVE, itemId, "Removed bookmark", Map.of());
    }
}
