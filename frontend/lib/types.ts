export type SourceType = "GITHUB" | "ARXIV" | "HACKER_NEWS";

export type Item = {
  id: string;
  source: SourceType;
  title: string;
  url: string;
  summary: string;
  tags: string[];
  score: number;
  createdAt: string;
  updatedAt: string;
  metadata: Record<string, unknown>;
};

export type PageResponse<T> = {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
};

export type User = {
  id: string;
  email: string;
};

export type DailyBrief = {
  date: string;
  summary: string;
  createdAt: string;
};

export type Story = {
  id: string;
  title: string;
  itemIds: string[];
  sources: SourceType[];
  score: number;
  createdAt: string;
  updatedAt: string;
};

export type WeeklyDiff = {
  week: string;
  newItems: Item[];
  droppedItems: Item[];
};

export type ActivityAction = "LOGIN" | "VIEW_ITEM" | "BOOKMARK_ADD" | "BOOKMARK_REMOVE" | "SKIP_ITEM" | "REFRESH_SIGNALS";

export type UserActivity = {
  id: string;
  action: ActivityAction;
  itemId: string | null;
  detail: string;
  metadata: Record<string, unknown>;
  createdAt: string;
};

export type CollectorRunResult = {
  writes: number;
};
