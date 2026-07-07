import { Suspense } from "react";
import { AuthCallbackPage } from "@/components/auth-callback-page";
import { LoadingState } from "@/components/states";

export default function AuthCallback() {
  return (
    <Suspense fallback={<LoadingState label="Finishing sign in" />}>
      <AuthCallbackPage />
    </Suspense>
  );
}

