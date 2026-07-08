<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=F7941E&height=200&section=header&text=VERTEX&fontSize=80&fontColor=ffffff&fontAlignY=35&desc=Developer+Intelligence+Engine&descAlignY=55&descSize=22&descColor=ffffff&animation=fadeIn" />

<br/>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=700&size=22&pause=1000&color=F7941E&center=true&vCenter=true&width=700&lines=Stop+checking+15+tabs+every+morning.;Vertex+reads+the+internet+for+you.;GitHub+%C2%B7+arXiv+%C2%B7+Hacker+News+%E2%86%92+One+Feed.;AI-ranked.+Summarized.+Signal+only." alt="Typing SVG" />

<br/><br/>

<a href="https://vertex-frontend-946u.onrender.com">
  <img src="https://img.shields.io/badge/🚀%20LIVE%20DEMO-F7941E?style=for-the-badge&logoColor=white" />
</a>
&nbsp;
<img src="https://img.shields.io/badge/Release-v1.0-blue?style=for-the-badge&logo=github&logoColor=white" />
&nbsp;
<img src="https://img.shields.io/badge/Java-21%20LTS-E34C26?style=for-the-badge&logo=openjdk&logoColor=white" />
&nbsp;
<img src="https://img.shields.io/badge/TypeScript-Strict-3178C6?style=for-the-badge&logo=typescript&logoColor=white" />
&nbsp;
<img src="https://img.shields.io/badge/License-MIT-22C55E?style=for-the-badge" />
&nbsp;
<img src="https://img.shields.io/badge/Infra%20Cost-$0-black?style=for-the-badge&logo=render&logoColor=white" />

<br/><br/>

<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="700" />

</div>

<br/>

---

<div align="center">

## 💀 The Problem

</div>

```
Every morning, developers open:

  → GitHub Trending          (5 min)
  → arXiv new submissions    (10 min)
  → Hacker News              (10 min)
  → AI news blogs            (10 min)
  → Twitter/X tech threads   (10 min)
  → Random newsletters       (10 min)
                             ────────
                             45 min wasted · 3 things worth reading
```

<div align="center">

### ⚡ Vertex fixes this in 2 minutes.

</div>

<br/>

---

<div align="center">

## 🧠 How Vertex Works

<img src="https://user-images.githubusercontent.com/74038190/229223263-cf2e4b07-2615-4f87-9c38-e37600f8381a.gif" width="400" />

</div>

```mermaid
flowchart LR
    A[🐙 GitHub API] --> D
    B[📄 arXiv API] --> D
    C[🔥 Hacker News API] --> D

    D[📥 Collection Layer\nSpring Boot Collectors] --> E

    E[🔄 Normalization\nCommon Item Schema] --> F

    F[🤖 Gemini AI Layer\nSummary · Tags · Score] --> G

    G[📊 Ranking Engine\nGrowth · Activity · Freshness] --> H

    H[(🗄️ PostgreSQL\nNeon Serverless)] --> I

    I[⚙️ Spring Boot REST API] --> J

    J[⚡ Next.js 14 Frontend]

    style A fill:#238636,color:#fff
    style B fill:#B31B1B,color:#fff
    style C fill:#FF6600,color:#fff
    style D fill:#1f6feb,color:#fff
    style E fill:#1f6feb,color:#fff
    style F fill:#7c3aed,color:#fff
    style G fill:#7c3aed,color:#fff
    style H fill:#0d1117,color:#fff
    style I fill:#F7941E,color:#fff
    style J fill:#F7941E,color:#fff
```

<br/>

---

<div align="center">

## ✨ Features

</div>

<table>
<tr>
<td align="center" width="33%">
<img src="https://user-images.githubusercontent.com/74038190/212257454-16e3712e-945a-4ca2-b238-408ad0bf87e6.gif" width="80" /><br/>
<b>📡 Unified Feed</b><br/>
GitHub · arXiv · Hacker News in one ranked interface. Stop tab switching forever.
</td>
<td align="center" width="33%">
<img src="https://user-images.githubusercontent.com/74038190/212257472-08e52665-c503-4bd9-aa20-f5a4dae769b5.gif" width="80" /><br/>
<b>🤖 AI Summarization</b><br/>
Gemini reads every item. You get the insight in 10 seconds, not 10 minutes.
</td>
<td align="center" width="33%">
<img src="https://user-images.githubusercontent.com/74038190/212257468-1e9a91f1-b626-4baa-b15d-5c385dfa7ed2.gif" width="80" /><br/>
<b>📊 Relevance Ranking</b><br/>
Custom score = growth velocity + activity + freshness. Not just "what's new."
</td>
</tr>
<tr>
<td align="center" width="33%">
<img src="https://user-images.githubusercontent.com/74038190/212257465-7ce8d493-cac5-494e-982a-5a9deb852c4b.gif" width="80" /><br/>
<b>🔗 Story Merging</b><br/>
Same breakthrough across 3 sources? Vertex merges it into one story automatically.
</td>
<td align="center" width="33%">
<img src="https://user-images.githubusercontent.com/74038190/212257460-738ff738-247f-4445-a718-cdd0ca76e2db.gif" width="80" /><br/>
<b>📝 Daily Brief</b><br/>
One AI paragraph. Everything important from the past 24 hours. Done.
</td>
<td align="center" width="33%">
<img src="https://user-images.githubusercontent.com/74038190/212257463-4d082cb9-7808-4fce-93d6-f9a9d671d06c.gif" width="80" /><br/>
<b>🔖 Search & Bookmarks</b><br/>
Full-text search across everything. Save any item. Find it in seconds.
</td>
</tr>
</table>

<br/>

---

<div align="center">

## 🛠️ Tech Stack

<img src="https://skillicons.dev/icons?i=java,spring,postgres,nextjs,ts,tailwind,docker,github&theme=dark" />

</div>

<br/>

<div align="center">

| Layer | Technology | Details |
|:---:|:---:|:---|
| ☕ **Backend** | Java 21 + Spring Boot 3 | Layered architecture: controller → service → repository → entity |
| 🔐 **Auth** | Spring Security + Google OAuth + JWT | Stateless, no passwords stored |
| 🗄️ **Database** | PostgreSQL (Neon serverless) | Flyway migrations, JPA/Hibernate ORM |
| ⚡ **Frontend** | Next.js 14 App Router | TypeScript strict, Tailwind, SSR |
| 🤖 **AI** | Google Gemini API | Summarization, tagging, rank signals |
| ⏰ **Scheduler** | GitHub Actions (cron) | Auto-collects every 6 hours, free tier |
| 🚀 **Hosting** | Render | Backend + frontend, **$0/month** |
| 🔍 **Search** | PostgreSQL Full-Text Search | No external search service needed |

</div>

<br/>

---

<div align="center">

## 🚀 Quick Start

</div>

### 1. Clone
```bash
git clone https://github.com/26Utkarsh/Vertex.git && cd Vertex
```

### 2. Backend `.env.local`
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
COLLECTOR_ENABLED=true
```

### 3. Run
```bash
# Backend
cd backend
mvn clean package && mvn spring-boot:run     # macOS/Linux
.\scripts\run-local.ps1                      # Windows

# Frontend
cd frontend && npm install && npm run dev
```

> Backend → `http://localhost:8080` · Frontend → `http://localhost:3000`

<br/>

---

<div align="center">

## 📁 Structure

</div>

```
Vertex/
├── 📁 .github/workflows/
│   └── collectors.yml          # ⏰ Auto-refresh every 6 hours
│
├── 📁 backend/
│   └── src/main/java/com/vertex/
│       ├── 🎮 controller/      # REST endpoints
│       ├── ⚙️  service/         # Business logic
│       ├── 🗃️  repository/      # JPA data access
│       ├── 📦 entity/           # DB models
│       ├── 📋 dto/              # Request/response shapes
│       ├── 🕷️  collector/       # GitHub · arXiv · HN collectors
│       ├── 🤖 ai/               # Gemini summarization
│       └── 🔐 auth/             # OAuth + JWT
│
└── 📁 frontend/
    ├── 📱 app/                  # Next.js App Router
    ├── 🧩 components/           # Reusable UI
    └── 📚 lib/                  # API client, utilities
```

<br/>

---

<div align="center">

## 🗺️ Roadmap

</div>

```
████████████████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░  V1 COMPLETE ✅

  ✅ GitHub · arXiv · Hacker News feed
  ✅ Gemini AI summarization + ranking
  ✅ Google OAuth + JWT authentication
  ✅ Bookmarks + full-text search
  ✅ Daily brief + cross-source story merging
  ✅ GitHub Actions auto-collector cron
  ✅ Live on Render — $0 infrastructure

░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░  V2 BUILDING 🚧

  ○ Security vulnerability hub (NVD API)
  ○ AI model release tracking
  ○ Developer tools directory
  ○ AI assistant (context-aware, DB-grounded)
  ○ Personalized skip list + weekly digest

░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░  V3 PLANNED 📋

  ○ Team workspaces
  ○ Public API
  ○ Mobile app
  ○ Custom source integration
```

<br/>

---

<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=F7941E&height=120&section=footer&text=Built+Solo+·+Zero+Paid+Infra+·+Production+Grade&fontSize=16&fontColor=ffffff&fontAlignY=65&animation=fadeIn" />

<br/>

**[📡 Live Demo](https://vertex-frontend-946u.onrender.com)** &nbsp;·&nbsp; **[🐛 Report Bug](https://github.com/26Utkarsh/Vertex/issues)** &nbsp;·&nbsp; **[💡 Request Feature](https://github.com/26Utkarsh/Vertex/issues)**

<br/>

Built by [**26Utkarsh**](https://github.com/26Utkarsh)

<img src="https://komarev.com/ghpvc/?username=26Utkarsh&label=Profile+Views&color=F7941E&style=flat-square" />

</div>
