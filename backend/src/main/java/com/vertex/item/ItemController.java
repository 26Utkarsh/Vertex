package com.vertex.item;

import com.vertex.common.PageResponse;
import com.vertex.item.dto.ItemResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import com.vertex.auth.CurrentUser;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api")
public class ItemController {
    private final ItemService itemService;
    private final CurrentUser currentUser;

    public ItemController(ItemService itemService, CurrentUser currentUser) {
        this.itemService = itemService;
        this.currentUser = currentUser;
    }

    @GetMapping("/items")
    PageResponse<ItemResponse> list(
            @RequestParam(required = false) SourceType source,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size
    ) {
        return itemService.list(source, page, size);
    }

    @GetMapping("/items/{id}")
    ItemResponse get(@PathVariable UUID id) {
        return itemService.get(id);
    }

    @GetMapping("/items/personalized")
    PageResponse<ItemResponse> personalized(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) SourceType source,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size
    ) {
        return itemService.listPersonalized(currentUser.id(jwt), source, page, size);
    }

    @GetMapping("/search")
    PageResponse<ItemResponse> search(
            @RequestParam String q,
            @RequestParam(required = false) SourceType source,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size
    ) {
        return itemService.search(q, source, page, size);
    }
}
