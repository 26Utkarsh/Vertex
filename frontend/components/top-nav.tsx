"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { clearToken, getToken } from "@/lib/auth";
import { ThemeToggle } from "@/components/theme-toggle";
import { VertexLogo } from "@/components/vertex-logo";

const NAV_ITEMS = [
  { href: "/", label: "FEED" },
  { href: "/search", label: "SEARCH" },
  { href: "/weekly", label: "WEEKLY" },
  { href: "/bookmarks", label: "BOOKMARKS" }
];

export function TopNav() {
  const [authenticated, setAuthenticated] = useState(false);

  useEffect(() => {
    setAuthenticated(getToken() !== null);
  }, []);

  return (
    <header className="sticky top-0 z-30 border-b border-[color:var(--border)] bg-[color:var(--bg)]/92 backdrop-blur-xl">
      <nav className="mx-auto flex min-h-16 w-full max-w-[1540px] flex-col gap-3 px-3 py-3 sm:px-4 lg:flex-row lg:items-center lg:justify-between lg:px-5">
        <div className="flex items-center justify-between gap-3">
          <Link className="soft-focus" href="/">
            <VertexLogo />
          </Link>
          <div className="metric-font hidden text-[11px] font-semibold uppercase leading-tight ui-faint md:block">
            <div>DEV INTEL TERMINAL</div>
            <div className="ui-accent-2">LIVE SIGNAL DESK</div>
          </div>
          <div className="lg:hidden">
            <ThemeToggle />
          </div>
        </div>
        <div className="flex flex-wrap items-center gap-2 text-xs font-bold">
          {NAV_ITEMS.map((item) => (
            <Link className="soft-focus rounded-full border border-[color:var(--border)] px-4 py-2 ui-muted transition hover:border-[color:var(--accent)] hover:text-[color:var(--accent)]" href={item.href} key={item.href}>
              {item.label}
            </Link>
          ))}
          {authenticated ? (
            <Link className="soft-focus rounded-full border border-[color:var(--border)] px-4 py-2 ui-muted transition hover:border-[color:var(--accent-2)] hover:text-[color:var(--accent-2)]" href="/activity">
              ACTIVITY
            </Link>
          ) : null}
          <div className="hidden lg:block">
            <ThemeToggle />
          </div>
          {authenticated ? (
            <button
              className="soft-focus rounded-full border border-[color:var(--border)] px-4 py-2 ui-muted transition hover:border-[color:var(--danger)] hover:text-[color:var(--danger)]"
              type="button"
              onClick={() => {
                clearToken();
                setAuthenticated(false);
              }}
            >
              SIGN OUT
            </button>
          ) : (
            <Link className="soft-focus rounded-full border border-[color:var(--accent-2)] bg-[color:var(--accent-2)] px-4 py-2 font-black text-[color:var(--bg)] shadow-[0_10px_28px_rgba(37,208,166,0.22)]" href="/login">
              LOGIN
            </Link>
          )}
        </div>
      </nav>
      <div className="ticker-shell border-t border-[color:var(--border-soft)] bg-[color:var(--panel)]">
        <div className="mx-auto max-w-[1540px] overflow-hidden px-3 py-2 sm:px-4 lg:px-5">
          <div className="ticker-track metric-font flex w-max gap-7 text-[11px] font-black uppercase ui-muted">
            {Array.from({ length: 2 }).map((_, index) => (
              <div className="flex gap-7" key={index}>
                <span className="ui-accent">VTX:DEV</span>
                <span>GH MOMENTUM +12.4</span>
                <span className="ui-accent-2">ARXIV AI +7.8</span>
                <span className="ui-success">HN HEAT +5.1</span>
                <span className="ui-danger">NOISE FILTER 0.23</span>
                <span>UTC SYNC READY</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </header>
  );
}
