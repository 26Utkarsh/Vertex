"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import { ErrorState, LoadingState } from "@/components/states";
import { setToken } from "@/lib/auth";

export function AuthCallbackPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const token = searchParams.get("token");
    if (token === null || token.length < 20) {
      setError("OAuth callback did not include a valid token.");
      return;
    }
    setToken(token);
    router.replace("/bookmarks");
  }, [router, searchParams]);

  if (error) {
    return <ErrorState message={error} />;
  }
  return <LoadingState label="Finishing sign in" />;
}

