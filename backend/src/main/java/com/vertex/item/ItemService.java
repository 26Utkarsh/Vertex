package com.vertex.item;

import com.vertex.common.PageResponse;
import com.vertex.item.dto.ItemResponse;
import com.vertex.skip.UserSkip;
import com.vertex.skip.UserSkipRepository;
import com.vertex.user.User;
import com.vertex.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private static final int MAX_PAGE_SIZE = 50;

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final UserSkipRepository userSkipRepository;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper, UserService userService, UserSkipRepository userSkipRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.userService = userService;
        this.userSkipRepository = userSkipRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<ItemResponse> list(SourceType source, int page, int size) {
        Pageable pageable = pageable(page, size);
        Page<Item> items = source == null
                ? itemRepository.findAll(pageable)
                : itemRepository.findAllBySource(source, pageable);
        return PageResponse.from(items.map(itemMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public ItemResponse get(UUID id) {
        return itemRepository.findById(id)
                .map(itemMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    @Transactional(readOnly = true)
    public PageResponse<ItemResponse> listPersonalized(UUID userId, SourceType source, int page, int size) {
        User user = userService.requireUser(userId);
        Set<String> suppressedTags = userSkipRepository.findAllByUserAndSkipCountGreaterThan(user, 3).stream()
                .map(UserSkip::getTag)
                .collect(Collectors.toSet());
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        int safePage = Math.max(page, 0);
        Pageable scanPageable = PageRequest.of(0, Math.min((safePage + 1) * safeSize + 100, 200), Sort.by(Sort.Direction.DESC, "score", "createdAt"));
        List<Item> scanned = (source == null ? itemRepository.findAll(scanPageable) : itemRepository.findAllBySource(source, scanPageable)).getContent();
        List<Item> ranked = scanned.stream()
                .sorted(Comparator.comparingDouble((Item item) -> adjustedScore(item, suppressedTags)).reversed().thenComparing(Item::getCreatedAt, Comparator.reverseOrder()))
                .toList();
        int fromIndex = Math.min(safePage * safeSize, ranked.size());
        int toIndex = Math.min(fromIndex + safeSize, ranked.size());
        Page<ItemResponse> pageResponse = new PageImpl<>(
                ranked.subList(fromIndex, toIndex).stream().map(itemMapper::toResponse).toList(),
                PageRequest.of(safePage, safeSize),
                ranked.size()
        );
        return PageResponse.from(pageResponse);
    }

    @Transactional(readOnly = true)
    public PageResponse<ItemResponse> search(String query, SourceType source, int page, int size) {
        if (query == null || query.trim().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search query must be at least 2 characters");
        }
        Page<Item> items = itemRepository.search(query.trim(), source, pageable(page, size));
        return PageResponse.from(items.map(itemMapper::toResponse));
    }

    private Pageable pageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "score", "createdAt"));
    }

    private double adjustedScore(Item item, Set<String> suppressedTags) {
        boolean suppressed = item.getTags().stream()
                .map(tag -> tag.toLowerCase(Locale.ROOT))
                .anyMatch(suppressedTags::contains);
        return suppressed ? item.getScore() * 0.45 : item.getScore();
    }
}
