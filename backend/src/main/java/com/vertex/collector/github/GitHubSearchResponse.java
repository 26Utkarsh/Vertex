package com.vertex.collector.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

record GitHubSearchResponse(List<GitHubRepository> items) {
}

record GitHubRepository(
        @JsonProperty("full_name") String fullName,
        @JsonProperty("html_url") String htmlUrl,
        String description,
        String language,
        @JsonProperty("stargazers_count") int stargazersCount,
        @JsonProperty("forks_count") int forksCount,
        @JsonProperty("open_issues_count") int openIssuesCount,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("pushed_at") String pushedAt
) {
}

record GitHubCommit(String sha) {
}
