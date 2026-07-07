"use client";

import { useEffect, useState } from "react";

type Theme = "dark" | "light";

const STORAGE_KEY = "vertex_theme";

export function ThemeToggle() {
  const [theme, setTheme] = useState<Theme>("dark");

  useEffect(() => {
    const stored = window.localStorage.getItem(STORAGE_KEY);
    const preferred: Theme = stored === "light" || stored === "dark" ? stored : "dark";
    setTheme(preferred);
    document.documentElement.dataset.theme = preferred;
  }, []);

  function toggleTheme() {
    const next: Theme = theme === "dark" ? "light" : "dark";
    setTheme(next);
    document.documentElement.dataset.theme = next;
    window.localStorage.setItem(STORAGE_KEY, next);
  }

  return (
    <button
      className="soft-focus grid h-9 w-9 place-items-center rounded-full border border-[color:var(--border)] bg-[color:var(--panel-strong)] text-sm shadow-sm transition hover:scale-105"
      type="button"
      onClick={toggleTheme}
      aria-label={`Switch to ${theme === "dark" ? "light" : "dark"} theme`}
      title={`Switch to ${theme === "dark" ? "light" : "dark"} theme`}
    >
      <span aria-hidden="true">{theme === "dark" ? "☀" : "☾"}</span>
    </button>
  );
}

