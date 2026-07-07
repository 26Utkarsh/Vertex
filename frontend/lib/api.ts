import type { CollectorRunResult, DailyBrief, Item, PageResponse, SourceType, Story, User, UserActivity, WeeklyDiff } from "@/lib/types";

export const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

type ApiOptions = {
  method?: "GET" | "POST" | "DELETE";
  token?: string | null;
};

export class ApiError extends Error {
  readonly status: number;

  constructor(status: number, message: string) {
    super(message);
    this.status = status;
  }
}

export function sourceLabel(source: SourceType): string {
  const labels: Record<SourceType, string> = {
    GITHUB: "GitHub",
    ARXIV: "arXiv",
    HACKER_NEWS: "Hacker News"
  };
  return labels[source];
}

export async function apiFetch<T>(path: string, options: ApiOptions = {}): Promise<T> {
  const headers = new Headers({ Accept: "application/json" });
  if (options.token) {
    headers.set("Authorization", `Bearer ${options.token}`);
  }
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: options.method ?? "GET",
    headers,
    cache: "no-store"
  });
  if (!response.ok) {
    const message = await readError(response);
    throw new ApiError(response.status, message);
  }
  if (response.status === 204) {
    return undefined as T;
  }
  return (await response.json()) as T;
}

export async function listItems(source: SourceType | "ALL", page = 0): Promise<PageResponse<Item>> {
  const params = new URLSearchParams({ page: String(page), size: "20" });
  if (source !== "ALL") {
    params.set("source", source);
  }
  return apiFetch<PageResponse<Item>>(`/api/items?${params.toString()}`);
}

export async function listPersonalizedItems(token: string, source: SourceType | "ALL", page = 0): Promise<PageResponse<Item>> {
  const params = new URLSearchParams({ page: String(page), size: "20" });
  if (source !== "ALL") {
    params.set("source", source);
  }
  return apiFetch<PageResponse<Item>>(`/api/items/personalized?${params.toString()}`, { token });
}

export async function getItem(id: string): Promise<Item> {
  return apiFetch<Item>(`/api/items/${id}`);
}

export async function searchItems(query: string, source: SourceType | "ALL"): Promise<PageResponse<Item>> {
  const params = new URLSearchParams({ q: query, page: "0", size: "20" });
  if (source !== "ALL") {
    params.set("source", source);
  }
  return apiFetch<PageResponse<Item>>(`/api/search?${params.toString()}`);
}

export async function todayBrief(): Promise<DailyBrief> {
  return apiFetch<DailyBrief>("/api/brief/today");
}

export async function listStories(): Promise<PageResponse<Story>> {
  return apiFetch<PageResponse<Story>>("/api/stories");
}

export async function me(token: string): Promise<User> {
  return apiFetch<User>("/api/me", { token });
}

export async function listBookmarks(token: string): Promise<PageResponse<Item>> {
  return apiFetch<PageResponse<Item>>("/api/bookmarks", { token });
}

export async function saveBookmark(token: string, itemId: string): Promise<void> {
  await apiFetch<void>(`/api/bookmarks/${itemId}`, { method: "POST", token });
}

export async function removeBookmark(token: string, itemId: string): Promise<void> {
  await apiFetch<void>(`/api/bookmarks/${itemId}`, { method: "DELETE", token });
}

export async function skipItem(token: string, itemId: string): Promise<void> {
  await apiFetch<void>(`/api/skips/${itemId}`, { method: "POST", token });
}

export async function refreshSignals(token: string): Promise<CollectorRunResult> {
  return apiFetch<CollectorRunResult>("/api/collectors/refresh", { method: "POST", token });
}

export async function listActivity(token: string): Promise<PageResponse<UserActivity>> {
  return apiFetch<PageResponse<UserActivity>>("/api/activity", { token });
}

export async function recordItemView(token: string, itemId: string): Promise<void> {
  await apiFetch<void>(`/api/activity/items/${itemId}/view`, { method: "POST", token });
}

export async function weeklyDiff(): Promise<WeeklyDiff> {
  return apiFetch<WeeklyDiff>("/api/weekly");
}

async function readError(response: Response): Promise<string> {
  try {
    const body = (await response.json()) as { message?: string };
    return body.message ?? response.statusText;
  } catch {
    return response.statusText;
  }
}
