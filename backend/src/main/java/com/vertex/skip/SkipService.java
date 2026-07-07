package com.vertex.skip;

import com.vertex.activity.ActivityService;
import com.vertex.activity.UserActivityAction;
import com.vertex.item.Item;
import com.vertex.item.ItemRepository;
import com.vertex.user.User;
import com.vertex.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class SkipService {
    private final UserSkipRepository userSkipRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final Clock clock;
    private final ActivityService activityService;

    public SkipService(UserSkipRepository userSkipRepository, ItemRepository itemRepository, UserService userService, Clock clock, ActivityService activityService) {
        this.userSkipRepository = userSkipRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.clock = clock;
        this.activityService = activityService;
    }

    @Transactional
    public void skip(UUID userId, UUID itemId) {
        User user = userService.requireUser(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        Instant now = Instant.now(clock);
        for (String tag : item.getTags()) {
            String normalized = tag.toLowerCase(Locale.ROOT);
            UserSkipId id = new UserSkipId(user.getId(), normalized);
            UserSkip userSkip = userSkipRepository.findById(id)
                    .orElseGet(() -> new UserSkip(user, normalized, now));
            userSkip.increment(now);
            userSkipRepository.save(userSkip);
        }
        activityService.record(user.getId(), UserActivityAction.SKIP_ITEM, item.getId(), "Marked not interested: " + item.getTitle(), Map.of("tags", item.getTags()));
    }
}
