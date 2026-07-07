CREATE TABLE daily_briefs (
    brief_date DATE PRIMARY KEY,
    summary TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE user_skips (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    tag VARCHAR(128) NOT NULL,
    skip_count INTEGER NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    PRIMARY KEY (user_id, tag)
);

CREATE TABLE stories (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    item_ids JSONB NOT NULL,
    sources JSONB NOT NULL,
    score DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE weekly_snapshots (
    snapshot_week DATE PRIMARY KEY,
    item_ids JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

