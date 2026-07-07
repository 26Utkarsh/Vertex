"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { EmptyState, ErrorState, LoadingState } from "@/components/states";
import { DeskHeader } from "@/components/terminal-panels";
import { listActivity } from "@/lib/api";
import { getToken } from "@/lib/auth";
import { fullDate } from "@/lib/format";
import type { UserActivity } from "@/lib/types";

const ACTION_LABELS: Record<UserActivity["action"], string> = {
  LOGIN: "Login",
  VIEW_ITEM: "Viewed",
  BOOKMARK_ADD: "Saved",
  BOOKMARK_REMOVE: "Removed",
  SKIP_ITEM: "Skipped",
  REFRESH_SIGNALS: "Refresh"
};

export function ActivityPage() {
  const [activities, setActivities] = useState<UserActivity[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const token = getToken();
    if (token === null) {
      setError("Login is required to view your activity history.");
      setLoading(false);
      return;
    }
    listActivity(token)
      .then((page) => setActivities(page.content))
      .catch((unknownError: unknown) => setError(unknownError instanceof Error ? unknownError.message : "Activity failed to load."))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="flex flex-col gap-4">
      <DeskHeader eyebrow="Private history" title="Activity ledger" meta={`${activities.length} events`} />
      {loading ? <LoadingState label="Loading activity" /> : null}
      {!loading && error ? <ErrorState message={error} /> : null}
      {!loading && !error && activities.length === 0 ? <EmptyState title="No activity yet" detail="Refresh, save, skip, or open items while logged in to build your private ledger." /> : null}
      {!loading && !error && activities.length > 0 ? (
        <section className="terminal-panel rounded-xl p-3">
          <div className="metric-font hidden grid-cols-[120px_1fr_180px] border-b border-[color:var(--border-soft)] px-3 pb-3 text-[11px] font-black uppercase ui-faint md:grid">
            <span>Action</span>
            <span>Detail</span>
            <span className="text-right">Time</span>
          </div>
          <div className="divide-y divide-[color:var(--border-soft)]">
            {activities.map((activity) => (
              <article className="grid gap-2 px-3 py-4 md:grid-cols-[120px_1fr_180px] md:items-center" key={activity.id}>
                <span className="metric-font text-xs font-black uppercase ui-accent">{ACTION_LABELS[activity.action]}</span>
                <div>
                  <p className="text-sm font-semibold ui-text">{activity.detail}</p>
                  {activity.itemId ? (
                    <Link className="metric-font mt-1 inline-flex text-[11px] font-black uppercase ui-accent-2" href={`/items/${activity.itemId}`}>
                      Open item
                    </Link>
                  ) : null}
                </div>
                <time className="metric-font text-xs font-bold ui-faint md:text-right" dateTime={activity.createdAt}>{fullDate(activity.createdAt)}</time>
              </article>
            ))}
          </div>
        </section>
      ) : null}
    </div>
  );
}
