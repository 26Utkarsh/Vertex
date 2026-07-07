package com.vertex.collector;

import com.vertex.collector.arxiv.ArxivCollector;
import com.vertex.collector.github.GitHubCollector;
import com.vertex.collector.hn.HackerNewsCollector;
import com.vertex.story.StoryService;
import org.springframework.stereotype.Service;

@Service
public class CollectorOrchestrator {
    private final GitHubCollector gitHubCollector;
    private final ArxivCollector arxivCollector;
    private final HackerNewsCollector hackerNewsCollector;
    private final CollectorService collectorService;
    private final StoryService storyService;

    public CollectorOrchestrator(GitHubCollector gitHubCollector, ArxivCollector arxivCollector, HackerNewsCollector hackerNewsCollector, CollectorService collectorService, StoryService storyService) {
        this.gitHubCollector = gitHubCollector;
        this.arxivCollector = arxivCollector;
        this.hackerNewsCollector = hackerNewsCollector;
        this.collectorService = collectorService;
        this.storyService = storyService;
    }

    public CollectorRunResult runGithub() {
        return new CollectorRunResult(collectorService.persist(gitHubCollector.collect()));
    }

    public CollectorRunResult runArxiv() {
        return new CollectorRunResult(collectorService.persist(arxivCollector.collect()));
    }

    public CollectorRunResult runHackerNews() {
        return new CollectorRunResult(collectorService.persist(hackerNewsCollector.collect()));
    }

    public CollectorRunResult runAll() {
        int writes = 0;
        writes += runGithub().writes();
        writes += runArxiv().writes();
        writes += runHackerNews().writes();
        storyService.rebuild();
        return new CollectorRunResult(writes);
    }

    public CollectorRunResult rebuildStories() {
        return new CollectorRunResult(storyService.rebuild());
    }
}
