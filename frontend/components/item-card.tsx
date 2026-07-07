import Link from "next/link";
import type { Item } from "@/lib/types";
import { sourceLabel } from "@/lib/api";
import { compactDate, hostFromUrl, scoreTone, sourceTone } from "@/lib/format";
import { stringMetadata } from "@/lib/metadata";

export function ItemCard({ item }: Readonly<{ item: Item }>) {
  const rankReason = stringMetadata(item.metadata, "rank_reason") ?? "Ranked by growth, recency, and activity";
  const healthFlag = stringMetadata(item.metadata, "health_flag");
  return (
    <article className="terminal-row reveal-in grid gap-4 rounded-xl p-4 sm:p-5 lg:grid-cols-[96px_1fr_minmax(140px,190px)]">
      <div className="metric-font flex items-start justify-between gap-3 lg:block">
        <div className={`text-3xl font-black tabular-nums ${scoreTone(item.score)}`}>{item.score.toFixed(1)}</div>
        <div className="mt-1 text-[11px] font-bold uppercase ui-faint">Score</div>
      </div>
      <div className="min-w-0">
        <div className="metric-font flex flex-wrap items-center gap-2 text-[11px] font-black uppercase">
          <span className={`rounded-full border px-2 py-1 ${sourceTone(item.source)}`}>{sourceLabel(item.source)}</span>
          <span className="ui-faint">{hostFromUrl(item.url)}</span>
          <span className="ui-faint">{compactDate(item.createdAt)}</span>
          {healthFlag === "declining" ? (
            <span className="rounded-full border border-[color:var(--danger)] px-2 py-1 ui-danger">Declining repo health</span>
          ) : null}
        </div>
        <Link className="soft-focus block rounded-lg" href={`/items/${item.id}`}>
          <h2 className="mt-3 line-clamp-2 text-lg font-black leading-snug ui-text transition hover:text-[color:var(--accent)] sm:text-xl">{item.title}</h2>
        </Link>
        <p className="mt-2 line-clamp-3 text-sm leading-6 ui-muted">{item.summary}</p>
        <p className="metric-font mt-2 text-[11px] font-bold uppercase ui-faint">Why ranked here: {rankReason}</p>
        <div className="mt-3 flex flex-wrap gap-2">
          <Link className="soft-focus metric-font rounded-full border border-[color:var(--accent)] px-3 py-1.5 text-[11px] font-black uppercase ui-accent" href={`/items/${item.id}`}>
            View detail
          </Link>
          <a className="soft-focus metric-font rounded-full border border-[color:var(--border)] px-3 py-1.5 text-[11px] font-black uppercase ui-muted hover:text-[color:var(--accent-2)]" href={item.url} target="_blank" rel="noreferrer">
            Open source
          </a>
        </div>
      </div>
      <div className="flex flex-wrap content-start gap-1 lg:justify-end">
        {item.tags.slice(0, 5).map((tag) => (
          <span className="metric-font rounded-full border border-[color:var(--border-soft)] bg-[color:var(--panel-strong)] px-2 py-1 text-[11px] font-bold uppercase ui-faint" key={tag}>
            {tag}
          </span>
        ))}
      </div>
    </article>
  );
}
