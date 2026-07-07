export function VertexLogo() {
  return (
    <div className="flex items-center gap-2" aria-label="Vertex">
      <span className="relative grid h-10 w-10 place-items-center overflow-hidden border border-[color:var(--accent)] bg-[color:var(--panel-strong)] shadow-[0_0_26px_rgba(255,177,61,0.18)]">
        <svg className="h-7 w-7" viewBox="0 0 32 32" role="img" aria-hidden="true">
          <path d="M6 7h20l-3.9 18H9.9L6 7Z" fill="none" stroke="var(--accent)" strokeWidth="2" strokeLinejoin="round" />
          <path d="M10 11l6 12 6-12" fill="none" stroke="var(--accent-2)" strokeWidth="2.4" strokeLinecap="round" strokeLinejoin="round" />
          <circle cx="10" cy="11" r="2" fill="var(--accent)" />
          <circle cx="22" cy="11" r="2" fill="var(--accent)" />
          <circle cx="16" cy="23" r="2" fill="var(--accent-2)" />
          <path d="M10 11h12" stroke="var(--accent-3)" strokeWidth="1.5" strokeLinecap="round" />
        </svg>
        <span className="absolute inset-x-0 bottom-0 h-[2px] bg-[color:var(--accent)]" />
      </span>
      <span className="leading-none">
        <span className="block text-xl font-black tracking-tight ui-text">Vertex</span>
        <span className="metric-font block text-[10px] font-black uppercase ui-faint">Signal Terminal</span>
      </span>
    </div>
  );
}

