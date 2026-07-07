"use client";

import { useEffect, useState } from "react";
import { DailyBrief } from "@/components/daily-brief";
import { ItemCard } from "@/components/item-card";
import { SourceFilter, type SourceFilterValue } from "@/components/source-filter";
import { EmptyState, ErrorState, LoadingState } from "@/components/states";
import { StoryCard } from "@/components/story-card";
import { DeskHeader, SignalRail } from "@/components/terminal-panels";
import { listItems, listPersonalizedItems, listStories, refreshSignals } from "@/lib/api";
import { getToken } from "@/lib/auth";
import type { Item, Story } from "@/lib/types";

export function FeedPage() {
  const [source, setSource] = useState<SourceFilterValue>("ALL");
  const [items, setItems] = useState<Item[]>([]);
  const [stories, setStories] = useState<Story[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [refreshNote, setRefreshNote] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [reloadToken, setReloadToken] = useState(0);
  const [authenticated, setAuthenticated] = useState(false);

  useEffect(() => {
    let active = true;
    setLoading(true);
    setError(null);
    const token = getToken();
    setAuthenticated(token !== null);
    const itemRequest = token === null ? listItems(source) : listPersonalizedItems(token, source);
    Promise.allSettled([itemRequest, source === "ALL" ? listStories() : Promise.resolve({ content: [] as Story[], page: 0, size: 0, totalElements: 0, totalPages: 0 })])
      .then((page) => {
        if (active) {
          const [itemsResult, storiesResult] = page;
          if (itemsResult.status === "fulfilled") {
            setItems(itemsResult.value.content);
          } else {
            throw itemsResult.reason;
          }
          setStories(storiesResult.status === "fulfilled" ? storiesResult.value.content : []);
        }
      })
      .catch((unknownError: unknown) => {
        if (active) {
          setError(unknownError instanceof Error ? unknownError.message : "Feed failed to load.");
        }
      })
      .finally(() => {
        if (active) {
          setLoading(false);
        }
      });
    return () => {
      active = false;
    };
  }, [source, reloadToken]);

  function pullFreshSignals() {
    const token = getToken();
    if (token === null) {
      setRefreshNote("Login is required to refresh live collectors.");
      return;
    }
    setRefreshing(true);
    setRefreshNote(null);
    refreshSignals(token)
      .then((result) => {
        setRefreshNote(`Refresh complete: ${result.writes} rows written or updated.`);
        setReloadToken((value) => value + 1);
      })
      .catch((unknownError: unknown) => setRefreshNote(unknownError instanceof Error ? unknownError.message : "Refresh failed."))
      .finally(() => setRefreshing(false));
  }

  return (
    <div className="flex flex-col gap-4">
      <DeskHeader eyebrow="Developer intelligence" title="Ranked signal monitor" meta={`${items.length} items`} />
      <DailyBrief />

      <div className="grid gap-4 xl:grid-cols-[minmax(0,1fr)_320px]">
        <section className="flex min-w-0 flex-col gap-4">
          <div className="terminal-panel flex flex-col justify-between gap-4 rounded-xl p-4 lg:flex-row lg:items-center">
            <div className="metric-font grid grid-cols-1 gap-2 text-[11px] font-black uppercase ui-muted sm:w-[30rem] sm:grid-cols-3">
              <div className="surface-strong rounded-lg p-3">
                <div className="ui-faint">Coverage</div>
                <div className="mt-1 ui-text">V1 Sources</div>
              </div>
              <div className="surface-strong rounded-lg p-3">
                <div className="ui-faint">Sort</div>
                <div className="mt-1 ui-accent">Score DESC</div>
              </div>
              <div className="surface-strong rounded-lg p-3">
                <div className="ui-faint">Refresh</div>
                <div className="mt-1 ui-accent-2">Manual</div>
              </div>
            </div>
            <SourceFilter value={source} onChange={setSource} />
          </div>
          <div className="terminal-panel flex flex-col gap-3 rounded-xl p-4 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <div className="metric-font text-[11px] font-black uppercase ui-faint">Live refresh</div>
              <p className="mt-1 text-sm ui-muted">{authenticated ? "Pull fresh GitHub, arXiv, and Hacker News signals into your feed." : "Login to unlock manual source refresh and activity history."}</p>
              {refreshNote ? <p className="metric-font mt-2 text-[11px] font-black uppercase ui-accent">{refreshNote}</p> : null}
            </div>
            <button
              className="soft-focus metric-font rounded-full border border-[color:var(--accent-2)] bg-[color:var(--accent-2)] px-5 py-2.5 text-xs font-black uppercase text-[color:var(--bg)] disabled:cursor-not-allowed disabled:opacity-50"
              type="button"
              onClick={pullFreshSignals}
              disabled={refreshing}
            >
              {refreshing ? "Pulling..." : "Refresh sources"}
            </button>
          </div>

          {loading ? <LoadingState label="Loading feed" /> : null}
          {!loading && error ? <ErrorState message={error} onRetry={() => setReloadToken((value) => value + 1)} /> : null}
          {!loading && !error && items.length === 0 ? (
            <EmptyState title="No active signals" detail="Collector output will appear here as ranked terminal rows." />
          ) : null}
          {!loading && !error && items.length > 0 ? (
            <section className="flex flex-col gap-2">
              {stories.length > 0 ? (
                <div className="mb-2 flex flex-col gap-2">
                  {stories.map((story) => (
                    <StoryCard story={story} key={story.id} />
                  ))}
                </div>
              ) : null}
              <div className="metric-font hidden grid-cols-[96px_1fr_minmax(140px,190px)] px-4 text-[11px] font-black uppercase ui-faint lg:grid">
                <span>Score</span>
                <span>Signal</span>
                <span className="text-right">Tags</span>
              </div>
              {items.map((item) => (
                <ItemCard item={item} key={item.id} />
              ))}
            </section>
          ) : null}
        </section>

        <SignalRail items={items} />
      </div>
    </div>
  );
}
