package com.vertex.brief;

import com.vertex.brief.dto.DailyBriefResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/brief")
public class DailyBriefController {
    private final DailyBriefService dailyBriefService;

    public DailyBriefController(DailyBriefService dailyBriefService) {
        this.dailyBriefService = dailyBriefService;
    }

    @GetMapping("/today")
    DailyBriefResponse today() {
        return dailyBriefService.today();
    }
}

