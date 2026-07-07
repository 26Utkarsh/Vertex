package com.vertex.story;

import com.vertex.common.PageResponse;
import com.vertex.story.dto.StoryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stories")
public class StoryController {
    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping
    PageResponse<StoryResponse> list() {
        return storyService.list();
    }
}

