CREATE TABLE items (
    id UUID PRIMARY KEY,
    source VARCHAR(32) NOT NULL,
    title TEXT NOT NULL,
    url TEXT NOT NULL,
    summary TEXT NOT NULL,
    tags JSONB NOT NULL DEFAULT '[]'::jsonb,
    score DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    metadata JSONB NOT NULL DEFAULT '{}'::jsonb,
    search_vector TSVECTOR GENERATED ALWAYS AS (
        to_tsvector('english', coalesce(title, '') || ' ' || coalesce(summary, ''))
    ) STORED,
    CONSTRAINT items_source_url_unique UNIQUE (source, url)
);

CREATE INDEX items_source_score_idx ON items (source, score DESC);
CREATE INDEX items_score_idx ON items (score DESC);
CREATE INDEX items_search_vector_idx ON items USING GIN (search_vector);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE,
    google_id VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE bookmarks (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    item_id UUID NOT NULL REFERENCES items(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (user_id, item_id)
);

CREATE INDEX bookmarks_item_idx ON bookmarks (item_id);

