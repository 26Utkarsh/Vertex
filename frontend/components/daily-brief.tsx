"use client";

import { useEffect, useState } from "react";
import { todayBrief } from "@/lib/api";
import type { DailyBrief as DailyBriefType } from "@/lib/types";

export function DailyBrief() {
  const [brief, setBrief] = useState<DailyBriefType | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let active = true;
    todayBrief()
      .then((result) => {
        if (active) {
          setBrief(result);
        }
      })
      .catch((unknownError: unknown) => {
        if (active) {
          setError(unknownError instanceof Error ? unknownError.message : "Brief unavailable");
        }
      });
    return () => {
      active = false;
    };
  }, []);

  return (
    <section className="terminal-panel reveal-in rounded-xl p-5">
      <div className="flex flex-col gap-2 sm:flex-row sm:items-start sm:justify-between">
        <div>
          <p className="metric-font text-[12px] font-black uppercase ui-accent">Today's Brief</p>
          <h2 className="mt-1 text-xl font-black ui-text">What matters right now</h2>
        </div>
        <span className="metric-font rounded-full border border-[color:var(--border)] px-3 py-1 text-[11px] font-black uppercase ui-faint">
          {brief?.date ?? "Loading"}
        </span>
      </div>
      <p className="mt-4 text-sm leading-7 ui-muted">
        {brief?.summary ?? (error ? `Brief unavailable: ${error}` : "Generating today's cross-source developer brief...")}
      </p>
    </section>
  );
}

