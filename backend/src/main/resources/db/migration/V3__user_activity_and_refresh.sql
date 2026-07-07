create table user_activities (
    id uuid primary key,
    user_id uuid not null references users(id) on delete cascade,
    action varchar(48) not null,
    item_id uuid null references items(id) on delete set null,
    detail text not null,
    metadata jsonb not null default '{}'::jsonb,
    created_at timestamptz not null
);

create index user_activities_user_created_idx on user_activities(user_id, created_at desc);
create index user_activities_item_idx on user_activities(item_id);
