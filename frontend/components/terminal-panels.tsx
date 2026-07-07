import type { Item, SourceType } from "@/lib/types";
import { sourceLabel } from "@/lib/api";
import { scoreTone } from "@/lib/format";

export function DeskHeader({
  eyebrow,
  title,
  meta
}: Readonly<{
  eyebrow: string;
  title: string;
  meta: string;
}>) {
  return (
    <section className="terminal-panel p-4">
      <div className="flex flex-col justify-between gap-3 lg:flex-row lg:items-end">
        <div>
          <p className="metric-font text-[12px] font-black uppercase ui-accent">{eyebrow}</p>
          <h1 className="mt-1 text-3xl font-black leading-tight tracking-tight ui-text sm:text-4xl">{title}</h1>
        </div>
        <div className="metric-font grid grid-cols-3 gap-2 text-[11px] font-bold uppercase ui-muted sm:min-w-96">
          <div className="surface-strong rounded-lg p-3">
            <div className="ui-faint">Mode</div>
            <div className="mt-1 ui-accent-2">Live</div>
          </div>
          <div className="surface-strong rounded-lg p-3">
            <div className="ui-faint">Model</div>
            <div className="mt-1 ui-accent">V1 Rank</div>
          </div>
          <div className="surface-strong rounded-lg p-3">
            <div className="ui-faint">Window</div>
            <div className="mt-1 ui-text">{meta}</div>
          </div>
        </div>
      </div>
    </section>
  );
}

export function SignalRail({ items }: Readonly<{ items: Item[] }>) {
  const topScore = items.length === 0 ? 0 : Math.max(...items.map((item) => item.score));
  const sourceCounts = countSources(items);
  return (
    <aside className="flex flex-col gap-3">
      <section className="terminal-panel rounded-xl p-4">
        <h2 className="metric-font text-[12px] font-black uppercase ui-faint">Market depth</h2>
        <div className={`metric-font mt-3 text-5xl font-black tabular-nums ${scoreTone(topScore)}`}>{topScore.toFixed(1)}</div>
        <p className="metric-font mt-1 text-[11px] font-bold uppercase ui-faint">Top active score</p>
      </section>
      <section className="terminal-panel rounded-xl p-4">
        <h2 className="metric-font text-[12px] font-black uppercase ui-faint">Source tape</h2>
        <div className="mt-3 space-y-2">
          {(Object.keys(sourceCounts) as SourceType[]).map((source) => (
            <div className="metric-font grid grid-cols-[1fr_42px] items-center gap-3 text-xs" key={source}>
              <span className="font-bold uppercase ui-muted">{sourceLabel(source)}</span>
              <span className="text-right font-black tabular-nums ui-accent">{sourceCounts[source]}</span>
              <span className="col-span-2 h-1.5 rounded-full bg-[color:var(--border-soft)]">
                <span className="block h-1.5 rounded-full bg-[color:var(--accent-2)]" style={{ width: `${items.length === 0 ? 0 : (sourceCounts[source] / items.length) * 100}%` }} />
              </span>
            </div>
          ))}
        </div>
      </section>
      <section className="terminal-panel rounded-xl p-4">
        <h2 className="metric-font text-[12px] font-black uppercase ui-faint">Rank weights</h2>
        <dl className="metric-font mt-3 space-y-2 text-xs">
          <div className="flex justify-between">
            <dt className="ui-faint">Growth</dt>
            <dd className="font-black ui-accent-2">40%</dd>
          </div>
          <div className="flex justify-between">
            <dt className="ui-faint">Recency</dt>
            <dd className="font-black ui-accent">30%</dd>
          </div>
          <div className="flex justify-between">
            <dt className="ui-faint">Activity</dt>
            <dd className="font-black ui-accent-3">30%</dd>
          </div>
        </dl>
      </section>
    </aside>
  );
}

function countSources(items: Item[]): Record<SourceType, number> {
  return items.reduce<Record<SourceType, number>>(
    (counts, item) => ({
      ...counts,
      [item.source]: counts[item.source] + 1
    }),
    { GITHUB: 0, ARXIV: 0, HACKER_NEWS: 0 }
  );
}
