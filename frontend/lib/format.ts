import type { SourceType } from "@/lib/types";

export function scoreTone(score: number): string {
  if (score >= 70) {
    return "ui-success";
  }
  if (score >= 45) {
    return "ui-accent";
  }
  return "ui-accent-3";
}

export function sourceTone(source: SourceType): string {
  const tones: Record<SourceType, string> = {
    GITHUB: "border-[color:var(--accent-2)] ui-accent-2",
    ARXIV: "border-[color:var(--accent-3)] ui-accent-3",
    HACKER_NEWS: "border-[color:var(--accent)] ui-accent"
  };
  return tones[source];
}

export function hostFromUrl(url: string): string {
  try {
    return new URL(url).hostname.replace(/^www\./, "");
  } catch {
    return "external";
  }
}

export function compactDate(value: string): string {
  return new Intl.DateTimeFormat("en", {
    month: "short",
    day: "2-digit"
  }).format(new Date(value)).toUpperCase();
}

export function fullDate(value: string): string {
  return new Intl.DateTimeFormat("en", {
    year: "numeric",
    month: "short",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit"
  }).format(new Date(value));
}
