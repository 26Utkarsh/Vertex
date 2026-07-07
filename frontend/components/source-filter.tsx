import type { SourceType } from "@/lib/types";

export type SourceFilterValue = SourceType | "ALL";

const SOURCES: SourceFilterValue[] = ["ALL", "GITHUB", "ARXIV", "HACKER_NEWS"];

export function SourceFilter({
  value,
  onChange
}: Readonly<{
  value: SourceFilterValue;
  onChange: (source: SourceFilterValue) => void;
}>) {
  return (
    <div className="grid grid-cols-2 gap-2 sm:flex sm:flex-wrap" role="tablist" aria-label="Source filter">
      {SOURCES.map((source) => (
        <button
          key={source}
          className={`soft-focus metric-font min-h-10 rounded-full border px-4 py-2 text-left text-[11px] font-black uppercase transition ${
            value === source
              ? "border-[color:var(--accent)] bg-[color:var(--accent)] text-[color:var(--bg)] shadow-[0_10px_26px_rgba(255,177,61,0.18)]"
              : "border-[color:var(--border)] bg-[color:var(--panel-strong)] ui-muted hover:border-[color:var(--accent-3)] hover:text-[color:var(--accent-3)]"
          }`}
          type="button"
          role="tab"
          aria-selected={value === source}
          onClick={() => onChange(source)}
        >
          {source === "ALL" ? "ALL SOURCES" : source.replaceAll("_", " ")}
        </button>
      ))}
    </div>
  );
}
