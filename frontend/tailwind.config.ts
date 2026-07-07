import type { Config } from "tailwindcss";

const config: Config = {
  content: ["./app/**/*.{ts,tsx}", "./components/**/*.{ts,tsx}", "./lib/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        ink: "#07090d",
        paper: "#0d1117",
        mint: "#25d0a6",
        signal: "#ff9d2e",
        violet: "#8b7cf6",
        terminal: "#10151d",
        grid: "#253041",
        gain: "#4ade80",
        loss: "#fb5c5c",
        cyan: "#4dd4ff"
      }
    }
  },
  plugins: []
};

export default config;
