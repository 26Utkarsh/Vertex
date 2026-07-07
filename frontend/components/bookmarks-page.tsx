"use client";

import { useEffect, useState } from "react";
import { ItemCard } from "@/components/item-card";
import { EmptyState, ErrorState, LoadingState } from "@/components/states";
import { DeskHeader, SignalRail } from "@/components/terminal-panels";
import { getToken } from "@/lib/auth";
import { listBookmarks } from "@/lib/api";
import type { Item } from "@/lib/types";

export function BookmarksPage() {
  const [items, setItems] = useState<Item[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [reloadToken, setReloadToken] = useState(0);

  useEffect(() => {
    const token = getToken();
    if (token === null) {
      setError("Login is required to view bookmarks.");
      setLoading(false);
      return;
    }
    setLoading(true);
    setError(null);
    listBookmarks(token)
      .then((page) => setItems(page.content))
      .catch((unknownError: unknown) => setError(unknownError instanceof Error ? unknownError.message : "Bookmarks failed to load."))
      .finally(() => setLoading(false));
  }, [reloadToken]);

  return (
    <div className="flex flex-col gap-4">
      <DeskHeader eyebrow="Saved" title="Bookmark blotter" meta={`${items.length} saved`} />
      <div className="grid gap-4 xl:grid-cols-[minmax(0,1fr)_320px]">
        <section className="flex flex-col gap-2">
          {loading ? <LoadingState label="Loading bookmarks" /> : null}
          {!loading && error ? <ErrorState message={error} onRetry={() => setReloadToken((value) => value + 1)} /> : null}
          {!loading && !error && items.length === 0 ? (
            <EmptyState title="No saved rows" detail="Saved intelligence rows will appear in this blotter." />
          ) : null}
          {!loading && !error && items.length > 0 ? (
            <>
              {items.map((item) => (
                <ItemCard item={item} key={item.id} />
              ))}
            </>
          ) : null}
        </section>
        <SignalRail items={items} />
      </div>
    </div>
  );
}
