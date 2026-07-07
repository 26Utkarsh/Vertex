type ErrorStateProps = {
  message: string;
  onRetry?: () => void;
};

export function LoadingState({ label }: Readonly<{ label: string }>) {
  return (
    <section className="terminal-panel flex min-h-72 items-center justify-center rounded-xl p-8">
      <div className="h-5 w-5 animate-spin rounded-full border-2 border-[color:var(--accent-2)] border-t-transparent" aria-hidden="true" />
      <span className="metric-font ml-3 text-xs font-black uppercase ui-muted">{label}</span>
    </section>
  );
}

export function ErrorState({ message, onRetry }: Readonly<ErrorStateProps>) {
  return (
    <section className="terminal-panel rounded-xl border-[color:var(--danger)] p-5">
      <p className="metric-font text-xs font-black uppercase ui-danger">Feed interruption</p>
      <p className="mt-2 text-sm font-semibold ui-text">{message}</p>
      {onRetry ? (
        <button className="soft-focus metric-font mt-4 rounded-full border border-[color:var(--accent)] bg-[color:var(--accent)] px-4 py-2 text-xs font-black uppercase text-[color:var(--bg)]" type="button" onClick={onRetry}>
          Retry pull
        </button>
      ) : null}
    </section>
  );
}

export function EmptyState({ title, detail }: Readonly<{ title: string; detail: string }>) {
  return (
    <section className="terminal-panel rounded-xl p-8 text-center">
      <h2 className="metric-font text-sm font-black uppercase ui-accent">{title}</h2>
      <p className="mx-auto mt-2 max-w-xl text-sm leading-6 ui-muted">{detail}</p>
    </section>
  );
}
