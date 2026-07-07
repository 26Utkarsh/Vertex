"use client";

import { useEffect, useState } from "react";
import { BookmarkButton } from "@/components/bookmark-button";
import { SkipButton } from "@/components/skip-button";
import { EmptyState, ErrorState, LoadingState } from "@/components/states";
import { getItem, recordItemView, sourceLabel } from "@/lib/api";
import { getToken } from "@/lib/auth";
import { fullDate, hostFromUrl, scoreTone, sourceTone } from "@/lib/format";
import { stringMetadata } from "@/lib/metadata";
import type { Item } from "@/lib/types";

export function ItemDetailPage({ id }: Readonly<{ id: string }>) {
  const [item, setItem] = useState<Item | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [reloadToken, setReloadToken] = useState(0);
  const [depth, setDepth] = useState<"senior" | "junior">("senior");

  useEffect(() => {
    let active = true;
    setLoading(true);
    setError(null);
    getItem(id)
      .then((result) => {
        if (active) {
          setItem(result);
          const token = getToken();
          if (token !== null) {
            recordItemView(token, result.id).catch(() => undefined);
          }
        }
      })
      .catch((unknownError: unknown) => {
        if (active) {
          setError(unknownError instanceof Error ? unknownError.message : "Item failed to load.");
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
  }, [id, reloadToken]);

  if (loading) {
    return <LoadingState label="Loading item" />;
  }
  if (error) {
    return <ErrorState message={error} onRetry={() => setReloadToken((value) => value + 1)} />;
  }
  if (item === null) {
    return <EmptyState title="Item not found" detail="The item may have been removed or the link is invalid." />;
  }
  const summary = depth === "junior"
    ? stringMetadata(item.metadata, "summary_junior") ?? item.summary
    : stringMetadata(item.metadata, "summary_senior") ?? item.summary;
  const rankReason = stringMetadata(item.metadata, "rank_reason") ?? "Ranked by growth, recency, and activity";
  const healthFlag = stringMetadata(item.metadata, "health_flag");

  return (
    <article className="grid gap-4 xl:grid-cols-[minmax(0,1fr)_340px]">
      <section className="terminal-panel rounded-xl p-5 sm:p-7">
        <div className="metric-font flex flex-wrap items-center gap-2 text-[11px] font-black uppercase">
          <span className={`rounded-full border px-3 py-1 ${sourceTone(item.source)}`}>{sourceLabel(item.source)}</span>
          <span className="ui-faint">{hostFromUrl(item.url)}</span>
          {healthFlag === "declining" ? <span className="rounded-full border border-[color:var(--danger)] px-3 py-1 ui-danger">Declining repo health</span> : null}
        </div>
        <h1 className="mt-5 text-3xl font-black leading-tight tracking-tight ui-text sm:text-4xl">{item.title}</h1>
        <div className="mt-5 inline-flex rounded-full border border-[color:var(--border)] bg-[color:var(--panel-strong)] p-1">
          {(["senior", "junior"] as const).map((option) => (
            <button
              className={`soft-focus metric-font rounded-full px-4 py-2 text-xs font-black uppercase ${depth === option ? "bg-[color:var(--accent)] text-[color:var(--bg)]" : "ui-muted"}`}
              type="button"
              key={option}
              onClick={() => setDepth(option)}
            >
              {option}
            </button>
          ))}
        </div>
        <div className="mt-6 border-y border-[color:var(--border-soft)] py-5">
          <p className="text-base leading-8 ui-muted sm:text-lg">{summary}</p>
          <p className="metric-font mt-4 text-[11px] font-black uppercase ui-faint">Why ranked here: {rankReason}</p>
        </div>
        <div className="mt-5 flex flex-wrap gap-2">
          {item.tags.map((tag) => (
            <span className="metric-font rounded-full border border-[color:var(--border-soft)] bg-[color:var(--panel-strong)] px-3 py-1.5 text-[11px] font-black uppercase ui-faint" key={tag}>
              {tag}
            </span>
          ))}
        </div>
        <a className="soft-focus metric-font mt-8 inline-flex rounded-full border border-[color:var(--accent-2)] bg-[color:var(--accent-2)] px-5 py-2.5 text-xs font-black uppercase text-[color:var(--bg)]" href={item.url} rel="noreferrer" target="_blank">
          Open source
        </a>
      </section>
      <aside className="flex flex-col gap-3">
        <section className="terminal-panel rounded-xl p-4">
          <h2 className="metric-font text-[12px] font-black uppercase ui-faint">Signal card</h2>
          <div className={`metric-font mt-3 text-5xl font-black tabular-nums ${scoreTone(item.score)}`}>{item.score.toFixed(1)}</div>
          <div className="metric-font mt-1 text-[11px] font-black uppercase ui-faint">Composite score</div>
          <div className="mt-5">
            <BookmarkButton itemId={item.id} />
          </div>
          <div className="mt-3">
            <SkipButton itemId={item.id} />
          </div>
        </section>
        <section className="terminal-panel rounded-xl p-4">
          <h2 className="metric-font text-[12px] font-black uppercase ui-faint">Timeline</h2>
          <dl className="metric-font mt-3 space-y-3 text-xs">
            <div className="flex justify-between gap-4">
              <dt className="ui-faint">Created</dt>
              <dd className="text-right font-bold ui-text">{fullDate(item.createdAt)}</dd>
            </div>
            <div className="flex justify-between gap-4">
              <dt className="ui-faint">Updated</dt>
              <dd className="text-right font-bold ui-text">{fullDate(item.updatedAt)}</dd>
            </div>
          </dl>
        </section>
        <section className="terminal-panel rounded-xl p-4">
          <h2 className="metric-font text-[12px] font-black uppercase ui-faint">Metadata</h2>
          <div className="metric-font mt-3 max-h-72 overflow-auto rounded-lg border border-[color:var(--border-soft)] bg-[color:var(--panel-strong)] p-3 text-[11px] leading-5 ui-muted">
            <pre>{JSON.stringify(item.metadata, null, 2)}</pre>
          </div>
        </section>
      </aside>
    </article>
  );
}
