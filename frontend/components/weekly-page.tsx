"use client";

import { useEffect, useState } from "react";
import { ItemCard } from "@/components/item-card";
import { EmptyState, ErrorState, LoadingState } from "@/components/states";
import { DeskHeader } from "@/components/terminal-panels";
import { weeklyDiff } from "@/lib/api";
import type { WeeklyDiff } from "@/lib/types";

export function WeeklyPage() {
  const [diff, setDiff] = useState<WeeklyDiff | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let active = true;
    weeklyDiff()
      .then((result) => {
        if (active) {
          setDiff(result);
        }
      })
      .catch((unknownError: unknown) => {
        if (active) {
          setError(unknownError instanceof Error ? unknownError.message : "Weekly diff failed to load.");
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
  }, []);

  return (
    <div className="flex flex-col gap-4">
      <DeskHeader eyebrow="Weekly Diff" title="Top 20 movement" meta={diff?.week ?? "Current week"} />
      {loading ? <LoadingState label="Loading weekly movement" /> : null}
      {!loading && error ? <ErrorState message={error} /> : null}
      {!loading && !error && diff ? (
        <div className="grid gap-4 xl:grid-cols-2">
          <section className="flex flex-col gap-3">
            <h2 className="metric-font text-sm font-black uppercase ui-accent-2">New to top 20</h2>
            {diff.newItems.length === 0 ? <EmptyState title="No new entrants" detail="The current top 20 is unchanged versus the previous weekly snapshot." /> : diff.newItems.map((item) => <ItemCard item={item} key={item.id} />)}
          </section>
          <section className="flex flex-col gap-3">
            <h2 className="metric-font text-sm font-black uppercase ui-danger">Dropped off</h2>
            {diff.droppedItems.length === 0 ? <EmptyState title="No drop-offs" detail="Dropped items will appear here once a previous weekly snapshot exists." /> : diff.droppedItems.map((item) => <ItemCard item={item} key={item.id} />)}
          </section>
        </div>
      ) : null}
    </div>
  );
}

