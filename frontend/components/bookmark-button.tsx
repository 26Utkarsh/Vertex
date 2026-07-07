"use client";

import { useEffect, useState } from "react";
import { getToken } from "@/lib/auth";
import { saveBookmark } from "@/lib/api";

export function BookmarkButton({ itemId }: Readonly<{ itemId: string }>) {
  const [token, setToken] = useState<string | null>(null);
  const [status, setStatus] = useState<"idle" | "saving" | "saved" | "error">("idle");

  useEffect(() => {
    setToken(getToken());
  }, []);

  if (token === null) {
    return (
      <a className="soft-focus metric-font inline-flex rounded-full border border-[color:var(--accent-2)] bg-[color:var(--accent-2)] px-4 py-2 text-xs font-black uppercase text-[color:var(--bg)]" href="/login">
        Login to save
      </a>
    );
  }

  return (
    <button
      className="soft-focus metric-font rounded-full border border-[color:var(--accent)] bg-[color:var(--accent)] px-4 py-2 text-xs font-black uppercase text-[color:var(--bg)] disabled:cursor-not-allowed disabled:border-[color:var(--border)] disabled:bg-[color:var(--panel-strong)] disabled:text-[color:var(--faint)]"
      type="button"
      disabled={status === "saving" || status === "saved"}
      onClick={() => {
        setStatus("saving");
        saveBookmark(token, itemId)
          .then(() => setStatus("saved"))
          .catch(() => setStatus("error"));
      }}
    >
      {status === "saved" ? "Saved" : status === "saving" ? "Saving" : "Save"}
      {status === "error" ? <span className="ml-2">Retry</span> : null}
    </button>
  );
}
