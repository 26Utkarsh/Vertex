"use client";

import { ErrorState } from "@/components/states";

export default function Error({ reset }: Readonly<{ error: Error; reset: () => void }>) {
  return <ErrorState message="This page failed to load." onRetry={reset} />;
}

