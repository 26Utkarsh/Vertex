"use client";

import { useEffect, useState } from "react";
import { skipItem } from "@/lib/api";
import { getToken } from "@/lib/auth";

export function SkipButton({ itemId }: Readonly<{ itemId: string }>) {
  const [token, setToken] = useState<string | null>(null);
  const [status, setStatus] = useState<"idle" | "saving" | "saved" | "error">("idle");

  useEffect(() => {
    setToken(getToken());
  }, []);

  if (token === null) {
    return null;
  }

  return (
    <button
      className="soft-focus metric-font rounded-full border border-[color:var(--border)] px-4 py-2 text-xs font-black uppercase ui-muted transition hover:border-[color:var(--danger)] hover:text-[color:var(--danger)] disabled:opacity-55"
      type="button"
      disabled={status === "saving" || status === "saved"}
      onClick={() => {
        setStatus("saving");
        skipItem(token, itemId)
          .then(() => setStatus("saved"))
          .catch(() => setStatus("error"));
      }}
    >
      {status === "saved" ? "Hidden signal" : status === "saving" ? "Saving" : status === "error" ? "Retry hide" : "Not interested"}
    </button>
  );
}

