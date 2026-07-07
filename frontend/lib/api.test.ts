import { describe, expect, it } from "vitest";
import { sourceLabel } from "@/lib/api";

describe("sourceLabel", () => {
  it("formats source names for the interface", () => {
    expect(sourceLabel("GITHUB")).toBe("GitHub");
    expect(sourceLabel("ARXIV")).toBe("arXiv");
    expect(sourceLabel("HACKER_NEWS")).toBe("Hacker News");
  });
});

