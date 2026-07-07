package com.vertex.collector.hn;

record HackerNewsStory(
        int id,
        String by,
        int descendants,
        int score,
        long time,
        String title,
        String url
) {
}

