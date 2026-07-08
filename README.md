<div align="center">

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=700&size=40&pause=1000&color=F7941E&center=true&vCenter=true&width=700&lines=⚡+VERTEX;Developer+Intelligence+Engine;Signal+Over+Noise." alt="Vertex" />

<br/>

> **Stop checking 15 tabs every morning.**
> Vertex reads GitHub, arXiv, and Hacker News — ranks what actually matters — and delivers it in one feed.

<br/>

<a href="https://vertex-frontend-946u.onrender.com"><img src="https://img.shields.io/badge/🚀%20Live%20Demo-F7941E?style=for-the-badge" /></a>
<a href="#"><img src="https://img.shields.io/badge/Release-v1.0-blue?style=for-the-badge&logo=github" /></a>
<a href="#"><img src="https://img.shields.io/badge/Java-21%20LTS-E34C26?style=for-the-badge&logo=openjdk&logoColor=white" /></a>
<a href="#"><img src="https://img.shields.io/badge/TypeScript-5.0+-3178C6?style=for-the-badge&logo=typescript&logoColor=white" /></a>
<a href="#"><img src="https://img.shields.io/badge/License-MIT-22C55E?style=for-the-badge" /></a>

<br/><br/>

<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="600" />

</div>

<br/>

---

## ⚡ What is Vertex?

Every developer knows the morning ritual — open GitHub Trending, check arXiv new submissions, scroll Hacker News, repeat until you've wasted 45 minutes finding 3 things worth reading.

**Vertex kills that ritual.**

It's not a feed aggregator. It's a **ranking engine**. Vertex pulls raw data from across the developer internet, runs every item through an AI enrichment pipeline, scores it by actual relevance — growth, activity, cross-source significance — and surfaces only what's worth your attention.

<br/>

<div align="center">

| | Before Vertex | With Vertex |
|---|---|---|
| 🕐 **Time to update** | 45 min across 15 tabs | 2 min, one feed |
| 📊 **Sorted by** | Recency | Relevance score |
| 🤖 **AI summaries** | None | Every item |
| 🔗 **Cross-source** | Fragmented | Merged stories |

</div>

<br/>

---

## 🧠 Core Features

<table>
<tr>
<td width="50%">

**📡 Unified Intelligence Feed**
GitHub repos, arXiv papers, and Hacker News — normalized, ranked, and displayed in one place. No more tab switching.

**🤖 AI Summarization**
Every item gets a Gemini-powered summary. Understand what matters without reading the full article or README.

**📊 Relevance Ranking**
A custom score based on growth velocity, community activity, and freshness — not just publication time.

</td>
<td width="50%">

**🔗 Cross-Source Story Merging**
When a GitHub repo, its arXiv paper, and its HN discussion all surface together, Vertex merges them into one unified story.

**📝 Daily Brief**
One AI-generated paragraph summarizing the day's strongest developer signals. Read it in 30 seconds.

**🔖 Bookmarks & Search**
Full-text search across all indexed content. Save anything, find it instantly.

</td>
</tr>
</table>

<br/>

---

## 🏗️ Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                      Data Sources                            │
│          GitHub API · arXiv API · Hacker News API           │
└────────────────────────┬─────────────────────────────────────┘
                         │  (GitHub Actions cron, every 6h)
                         ▼
┌──────────────────────────────────────────────────────────────┐
│                  Collection & Normalization                   │
│        Spring Boot Collectors → Common Item Schema           │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│                  AI Intelligence Layer                        │
│     Gemini API → Summary · Tags · Score · Rank Reason        │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│               PostgreSQL (Neon serverless)                    │
│         items · users · bookmarks · weekly_snapshots         │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│           Spring Boot REST API (Render)                       │
│      Auth · Feed · Search · Bookmarks · Brief endpoint       │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│           Next.js 14 Frontend (Render)                        │
│    App Router · TypeScript · Tailwind · Strict types         │
└──────────────────────────────────────────────────────────────┘
```

<br/>

---

## 🛠️ Tech Stack

<div align="center">

| Layer | Technology | Why |
|---|---|---|
| **Backend Language** | Java 21 LTS | Type-safe, performant, enterprise-grade |
| **Backend Framework** | Spring Boot 3.x | Layered architecture, production-ready |
| **Security** | Spring Security + JWT + Google OAuth | Stateless auth, zero friction login |
| **Database** | PostgreSQL via Neon | Serverless, free tier, ACID compliant |
| **Migrations** | Flyway | Schema versioning, reproducible deploys |
| **ORM** | Spring Data JPA | Type-safe queries, no raw SQL |
| **Frontend** | Next.js 14 (App Router) | SSR, clean routing, performance |
| **Language** | TypeScript 5 (strict) | End-to-end type safety, zero `any` |
| **Styling** | Tailwind CSS 3 | Utility-first, consistent design system |
| **AI** | Google Gemini API | Summarization, ranking signal generation |
| **Scheduling** | GitHub Actions (cron) | Free, reliable, zero-config collector jobs |
| **Hosting** | Render | Full-stack deploy, zero paid infra |

</div>

<br/>

---

## 🚀 Quick Start

### Prerequisites
- Java 21+ · Node.js 18+ · PostgreSQL 14+ · Git

### 1. Clone
```bash
git clone https://github.com/26Utkarsh/Vertex.git
cd Vertex
```

### 2. Backend Environment
Create `backend/.env.local`:
```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/vertex_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
GOOGLE_CLIENT_ID=your_client_id
GOOGLE_CLIENT_SECRET=your_client_secret
GEMINI_API_KEY=your_gemini_key
GITHUB_TOKEN=your_github_pat
JWT_SECRET=your_32_char_secret
INTERNAL_API_KEY=your_32_char_secret
```

### 3. Run Backend
```bash
cd backend
mvn clean test && mvn clean package
mvn spring-boot:run             # macOS/Linux
.\scripts\run-local.ps1         # Windows
```

### 4. Run Frontend
```bash
cd frontend
npm install && npm run dev
```

**Backend** → `http://localhost:8080` · **Frontend** → `http://localhost:3000`

<br/>

---

## 📁 Project Structure

```
Vertex/
├── .github/workflows/
│   └── collectors.yml          # Cron job — collects data every 6 hours
├── backend/
│   └── src/main/java/com/vertex/
│       ├── controller/         # REST endpoints
│       ├── service/            # Business logic
│       ├── repository/         # Data access (JPA)
│       ├── entity/             # DB models
│       ├── dto/                # Request/response shapes
│       ├── collector/          # GitHub · arXiv · HN collectors
│       ├── ai/                 # Gemini summarization
│       ├── auth/               # OAuth + JWT
│       └── config/             # Spring config
└── frontend/
    ├── app/                    # Next.js App Router pages
    ├── components/             # Reusable UI components
    └── lib/                    # API client, utilities
```

<br/>

---

## 🔐 Security

- JWT tokens, 24h expiration
- Google OAuth — no passwords stored
- All secrets in environment variables, never committed
- CORS locked to frontend domain
- Server-side input validation on every endpoint
- Parameterized queries via JPA — no SQL injection surface

<br/>

---

## 🗺️ Roadmap

```
v1.0 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ ✅ LIVE
  ✔ GitHub · arXiv · HN feed aggregation
  ✔ AI summarization + relevance ranking
  ✔ Google OAuth + JWT auth
  ✔ Bookmarks + full-text search
  ✔ Daily brief, cross-source story merging
  ✔ Deployed on Render, zero paid infra

v2.0 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ 🚧 NEXT
  ○ Security vulnerability tracking (NVD API)
  ○ AI model release tracking (Hugging Face)
  ○ Developer tools directory
  ○ AI assistant chat (context-aware, DB-grounded)
  ○ Personalized skip list + digest

v3.0 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ 📋 PLANNED
  ○ Team workspaces
  ○ Public API
  ○ Mobile app
  ○ Custom source integration
```

<br/>

---

<div align="center">

<img src="https://user-images.githubusercontent.com/74038190/213866269-5d00981c-7c98-46d7-8a8e-16f462f15227.gif" width="80" />

**Built solo. Zero paid infrastructure. Production-grade.**

[**📡 Live Demo**](https://vertex-frontend-946u.onrender.com) &nbsp;•&nbsp; [**🐛 Report Bug**](https://github.com/26Utkarsh/Vertex/issues) &nbsp;•&nbsp; [**💡 Request Feature**](https://github.com/26Utkarsh/Vertex/issues)

<br/>

Made by [**26Utkarsh**](https://github.com/26Utkarsh)

</div>
