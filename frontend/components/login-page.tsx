"use client";

import { useEffect, useState } from "react";
import { API_BASE_URL, me } from "@/lib/api";
import { getToken } from "@/lib/auth";
import { ErrorState, LoadingState } from "@/components/states";

export function LoginPage() {
  const [state, setState] = useState<"loading" | "anonymous" | "authenticated" | "error">("loading");

  useEffect(() => {
    const token = getToken();
    if (token === null) {
      setState("anonymous");
      return;
    }
    me(token)
      .then(() => setState("authenticated"))
      .catch(() => setState("error"));
  }, []);

  if (state === "loading") {
    return <LoadingState label="Checking session" />;
  }
  if (state === "error") {
    return <ErrorState message="Your saved session is no longer valid. Sign in again with Google." />;
  }

  return (
    <section className="terminal-panel max-w-xl rounded-xl p-6 sm:p-7">
      <p className="metric-font text-[12px] font-black uppercase ui-accent">Identity gateway</p>
      <h1 className="mt-2 text-3xl font-black tracking-tight ui-text">{state === "authenticated" ? "Session active" : "Operator login"}</h1>
      <div className="metric-font mt-5 grid grid-cols-1 gap-2 text-[11px] font-black uppercase sm:grid-cols-2">
        <div className="surface-strong rounded-lg p-3">
          <div className="ui-faint">Provider</div>
          <div className="mt-1 ui-text">Google</div>
        </div>
        <div className="surface-strong rounded-lg p-3">
          <div className="ui-faint">Access</div>
          <div className="mt-1 ui-accent-2">{state === "authenticated" ? "Granted" : "Pending"}</div>
        </div>
      </div>
      <a className="soft-focus metric-font mt-6 inline-flex rounded-full border border-[color:var(--accent-2)] bg-[color:var(--accent-2)] px-5 py-2.5 text-xs font-black uppercase text-[color:var(--bg)]" href={`${API_BASE_URL}/oauth2/authorization/google`}>
        Continue with Google
      </a>
    </section>
  );
}
