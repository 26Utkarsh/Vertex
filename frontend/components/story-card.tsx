import type { Story } from "@/lib/types";
import { sourceLabel } from "@/lib/api";
import { scoreTone, sourceTone } from "@/lib/format";

export function StoryCard({ story }: Readonly<{ story: Story }>) {
  return (
    <article className="terminal-row reveal-in rounded-xl p-4 sm:p-5">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
        <div className="min-w-0">
          <div className="metric-font flex flex-wrap gap-2 text-[11px] font-black uppercase">
            {story.sources.map((source) => (
              <span className={`rounded-full border px-2 py-1 ${sourceTone(source)}`} key={source}>
                {sourceLabel(source)}
              </span>
            ))}
          </div>
          <h2 className="mt-3 text-xl font-black leading-snug ui-text">{story.title}</h2>
          <p className="mt-2 text-sm leading-6 ui-muted">
            Merged story across {story.itemIds.length} related signals. Open the source items below to inspect each angle.
          </p>
        </div>
        <div className={`metric-font text-3xl font-black tabular-nums ${scoreTone(story.score)}`}>{story.score.toFixed(1)}</div>
      </div>
      <div className="mt-4 flex flex-wrap gap-2">
        {story.itemIds.map((itemId, index) => (
          <a className="soft-focus metric-font rounded-full border border-[color:var(--border)] px-3 py-1.5 text-[11px] font-black uppercase ui-muted hover:text-[color:var(--accent)]" href={`/items/${itemId}`} key={itemId}>
            Source {index + 1}
          </a>
        ))}
      </div>
    </article>
  );
}

