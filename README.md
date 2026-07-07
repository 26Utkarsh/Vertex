# Vertex

**AI-ranked developer intelligence feed.** Aggregates GitHub, arXiv, and Hacker News into one ranked, summarized signal — so you stop checking 15 tabs every morning.

![Status](https://img.shields.io/badge/status-V1-orange) ![License](https://img.shields.io/badge/license-MIT-blue)

---

## What it does

- Pulls trending GitHub repos, new arXiv papers, and top Hacker News stories
- AI-summarizes and ranks everything by relevance, not just recency
- Daily brief, cross-source story merging, bookmarks, search

## Stack

| Layer | Tech |
|---|---|
| Backend | Java 21, Spring Boot, Spring Security, JPA, PostgreSQL |
| Frontend | Next.js (App Router), TypeScript, Tailwind |
| Auth | Google OAuth + JWT |
| AI | Gemini API |
| Hosting | Render (backend + frontend) |

## Setup

```bash
# Backend
cd backend
cp .env.example .env.local   # fill in DB + API keys
mvn test
mvn package

# Frontend
cd ../frontend
npm install
npm run test
npm run build
```

### Local backend (Windows)
```powershell
cd backend
.\scripts\run-local.ps1
```

Secrets are never committed — use `.env.local` (gitignored).

## Roadmap

- [x] V1 — GitHub, arXiv, HN feed + ranking + auth
- [ ] V2 — Security intel, AI model tracking, dev tool hub, assistant chat
