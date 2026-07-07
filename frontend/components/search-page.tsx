"use client";

import { FormEvent, useState } from "react";
import { ItemCard } from "@/components/item-card";
import { SourceFilter, type SourceFilterValue } from "@/components/source-filter";
import { EmptyState, ErrorState, LoadingState } from "@/components/states";
import { DeskHeader } from "@/components/terminal-panels";
import { searchItems } from "@/lib/api";
import type { Item } from "@/lib/types";

const SUGGESTED_QUERIES = ["agents", "local llm", "open source", "robotics", "security", "rag"];

export function SearchPage() {
  const [query, setQuery] = useState("");
  const [source, setSource] = useState<SourceFilterValue>("ALL");
  const [items, setItems] = useState<Item[]>([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const [error, setError] = useState<string | null>(null);

  function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (query.trim().length < 2) {
      setError("Search query must be at least 2 characters.");
      return;
    }
    setLoading(true);
    setSearched(true);
    setError(null);
    searchItems(query.trim(), source)
      .then((page) => setItems(page.content))
      .catch((unknownError: unknown) => setError(unknownError instanceof Error ? unknownError.message : "Search failed."))
      .finally(() => setLoading(false));
  }

  function runSuggestedSearch(nextQuery: string) {
    setQuery(nextQuery);
    setLoading(true);
    setSearched(true);
    setError(null);
    searchItems(nextQuery, source)
      .then((page) => setItems(page.content))
      .catch((unknownError: unknown) => setError(unknownError instanceof Error ? unknownError.message : "Search failed."))
      .finally(() => setLoading(false));
  }

  return (
    <div className="flex flex-col gap-4">
      <DeskHeader eyebrow="Find signals" title="Cross-source query console" meta={`${items.length} hits`} />
      <form className="terminal-panel flex flex-col gap-4 rounded-xl p-4 sm:p-5" onSubmit={submit}>
        <label className="metric-font text-[12px] font-black uppercase ui-faint" htmlFor="search">
          Query string
        </label>
        <div className="flex flex-col gap-3 sm:flex-row">
          <input
            className="soft-focus min-h-12 flex-1 rounded-xl border border-[color:var(--border)] bg-[color:var(--panel-strong)] px-4 text-base font-semibold ui-text placeholder:text-[color:var(--faint)]"
            id="search"
            value={query}
            onChange={(event) => setQuery(event.target.value)}
            placeholder="vector databases, agents, compilers..."
          />
          <button className="soft-focus metric-font rounded-full border border-[color:var(--accent)] bg-[color:var(--accent)] px-6 py-3 text-xs font-black uppercase text-[color:var(--bg)]" type="submit">
            Execute
          </button>
        </div>
        <SourceFilter value={source} onChange={setSource} />
        <div className="flex flex-wrap gap-2">
          {SUGGESTED_QUERIES.map((suggestion) => (
            <button
              className="soft-focus metric-font rounded-full border border-[color:var(--border)] px-3 py-1.5 text-[11px] font-black uppercase ui-muted hover:text-[color:var(--accent)]"
              type="button"
              key={suggestion}
              onClick={() => runSuggestedSearch(suggestion)}
            >
              {suggestion}
            </button>
          ))}
        </div>
      </form>
      {searched && !loading && !error ? (
        <div className="metric-font text-xs font-black uppercase ui-faint">{items.length} visible matches</div>
      ) : null}
      {loading ? <LoadingState label="Searching" /> : null}
      {!loading && error ? <ErrorState message={error} /> : null}
      {!loading && !error && !searched ? <EmptyState title="Awaiting query" detail="Matching intelligence rows will stream into this console." /> : null}
      {!loading && !error && searched && items.length === 0 ? <EmptyState title="No matches" detail="No ranked rows matched the current query and source filter." /> : null}
      {!loading && !error && items.length > 0 ? (
        <section className="flex flex-col gap-2">
          {items.map((item) => (
            <ItemCard item={item} key={item.id} />
          ))}
        </section>
      ) : null}
    </div>
  );
}
