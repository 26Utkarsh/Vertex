package com.vertex.bookmark;

import com.vertex.auth.CurrentUser;
import com.vertex.common.PageResponse;
import com.vertex.item.dto.ItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final CurrentUser currentUser;

    public BookmarkController(BookmarkService bookmarkService, CurrentUser currentUser) {
        this.bookmarkService = bookmarkService;
        this.currentUser = currentUser;
    }

    @GetMapping
    PageResponse<ItemResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return bookmarkService.list(currentUser.id(jwt));
    }

    @PostMapping("/{itemId}")
    @ResponseStatus(HttpStatus.CREATED)
    void add(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID itemId) {
        bookmarkService.add(currentUser.id(jwt), itemId);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void remove(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID itemId) {
        bookmarkService.remove(currentUser.id(jwt), itemId);
    }
}

