import type { Metadata } from "next";
import { TopNav } from "@/components/top-nav";
import "./globals.css";

export const metadata: Metadata = {
  title: "Vertex",
  description: "Ranked AI-summarized developer intelligence feed",
  icons: {
    icon: "/icon.svg"
  },
  openGraph: {
    title: "Vertex",
    description: "Ranked AI-summarized developer intelligence feed",
    type: "website"
  }
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="en">
      <body>
        <TopNav />
        <main className="mx-auto flex w-full max-w-[1540px] flex-col px-3 pb-10 pt-3 sm:px-4 lg:px-5">
          {children}
        </main>
      </body>
    </html>
  );
}
