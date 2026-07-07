<div align="center">

# Vertex

### AI-Ranked Developer Intelligence Feed

Consolidate GitHub, arXiv, and Hacker News into a single, intelligently ranked feed. Cut through the noise with relevance-based aggregation and AI-powered summarization.

[![GitHub Release](https://img.shields.io/badge/release-v1.0-blue?style=flat-square)](https://github.com/26Utkarsh/Vertex/releases)
[![License: MIT](https://img.shields.io/badge/license-MIT-green?style=flat-square)](LICENSE)
[![Java 21](https://img.shields.io/badge/java-21+-E34C26?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![TypeScript](https://img.shields.io/badge/typescript-5.0+-3178C6?style=flat-square&logo=typescript)](https://www.typescriptlang.org/)

[📡 Live Demo](https://vertex-frontend-946u.onrender.com) • [📚 Documentation](#documentation) • [🚀 Quick Start](#quick-start) • [🗺️ Roadmap](#roadmap)

</div>

---

## Overview

**Vertex** solves a fundamental problem: information overload. Modern developers spend hours curating content across multiple platforms—GitHub, arXiv, Hacker News—each with its own signal-to-noise ratio.

This application applies machine learning and intelligent curation to surface the content that matters most to your interests and expertise. The core value proposition:

- **Single unified interface** for multiple knowledge sources
- **Relevance-based ranking** that learns from your engagement patterns
- **AI summarization** to extract key insights without context-switching
- **Cross-source deduplication** to prevent redundant stories
- **Search & bookmarking** for future reference and knowledge management

---

## Technical Architecture

### System Design

```
┌─────────────────────────────────────────────────────┐
│                    Frontend Layer                    │
│         Next.js 14 + TypeScript + Tailwind          │
│  (Component-driven, SSR, Client-side Optimization)  │
└────────────────────┬────────────────────────────────┘
                     │ gRPC / REST
┌────────────────────▼────────────────────────────────┐
│                    API Gateway                       │
│          Spring Boot + Spring Security              │
│    (Request routing, Auth validation, Caching)      │
└────────────────────┬────────────────────────────────┘
                     │
      ┌──────────────┼──────────────┐
      │              │              │
┌─────▼────┐  ┌──────▼───┐  ┌──────▼────┐
│  GitHub  │  │  arXiv   │  │ HN API    │
│  GraphQL │  │  RSS/XML │  │  REST     │
└─────┬────┘  └──────┬───┘  └──────┬────┘
      │              │              │
┌─────▼─────────────▼──────────────▼─────────┐
│        Data Processing & Aggregation       │
│  Spring Batch, Async Processing, Caching   │
└────────────────────┬──────────────────────┘
                     │
┌────────────────────▼──────────────────────┐
│    AI Ranking & Summarization Engine      │
│    Gemini API, LLM Chain Processing       │
└────────────────────┬──────────────────────┘
                     │
┌────────────────────▼──────────────────────┐
│         PostgreSQL Database                │
│  (User profiles, Feed state, Bookmarks)    │
└────────────────────────────────────────────┘
```

### Technology Stack

| Layer | Component | Version | Notes |
|-------|-----------|---------|-------|
| **Language Composition** | — | — | Java 62.8% • TypeScript 33.5% • CSS 2.3% • Other 1.4% |
| **Backend** | Java | 21 LTS | Type-safe, performant JVM runtime |
| **Framework** | Spring Boot | 3.x | Proven enterprise framework |
| **Security** | Spring Security + OAuth 2.0 | — | Google OAuth + JWT tokens |
| **Database** | PostgreSQL | 14+ | ACID compliance, JSON support |
| **ORM** | Spring Data JPA | — | Type-safe data access |
| **Frontend** | Next.js | 14 | App Router, SSR, API routes |
| **Language** | TypeScript | 5.0+ | End-to-end type safety |
| **Styling** | Tailwind CSS | 3.x | Utility-first, optimized |
| **AI/ML** | Gemini API | Latest | Multi-modal LLM integration |
| **Deployment** | Render | — | PostgreSQL + App hosting |

---

## Quick Start

### Prerequisites

Ensure you have the following installed:

- **Java 21** or higher ([openjdk](https://openjdk.org/))
- **Node.js** 18+ and npm 9+
- **PostgreSQL** 14+
- **Git**

### Environment Setup

Create a `.env.local` file in the backend directory with:

```env
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/vertex_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password

# OAuth (Google)
GOOGLE_CLIENT_ID=your_client_id
GOOGLE_CLIENT_SECRET=your_client_secret

# AI
GEMINI_API_KEY=your_gemini_key

# Application
JWT_SECRET=your_jwt_secret_key
```

### Building & Running

#### Backend

```bash
cd backend

# Build and test
mvn clean test
mvn clean package

# Run locally (Unix/Linux/macOS)
mvn spring-boot:run

# Run locally (Windows PowerShell)
.\scripts\run-local.ps1
```

The backend API will be available at `http://localhost:8080`

#### Frontend

```bash
cd frontend

# Install dependencies
npm install

# Development mode (with hot reload)
npm run dev

# Build for production
npm run build
npm run start

# Run tests
npm run test
```

The frontend will be available at `http://localhost:3000`

---

## API Documentation

### Core Endpoints

```
GET  /api/v1/feeds             - Retrieve aggregated feed
POST /api/v1/bookmarks         - Create bookmark
GET  /api/v1/bookmarks         - List user bookmarks
POST /api/v1/preferences       - Update ranking preferences
GET  /api/v1/search?q=term     - Full-text search
```

Full API documentation available at `/api-docs` (Swagger UI)

---

## Project Structure

```
Vertex/
├── backend/
│   ├── src/main/java/com/vertex/
│   │   ├── controller/         # REST endpoints
│   │   ├── service/            # Business logic
│   │   ├── model/              # JPA entities
│   │   ├── repository/         # Data access
│   │   ├── config/             # Spring configs
│   │   └── security/           # Auth & authorization
│   ├── src/test/               # Unit & integration tests
│   ├── pom.xml                 # Maven dependencies
│   └── scripts/
│
├── frontend/
│   ├── app/                    # Next.js App Router
│   ├── components/             # React components
│   ├── lib/                    # Utilities & helpers
│   ├── types/                  # TypeScript types
│   ├── styles/                 # Global styles
│   ├── package.json
│   └── next.config.js
│
└── README.md
```

---

## Development Workflow

### Code Quality Standards

- **Backend**: Checkstyle, SpotBugs, Maven Shade Plugin
- **Frontend**: ESLint, Prettier, TypeScript strict mode
- **Testing**: JUnit 5, Mockito (backend); Jest, React Testing Library (frontend)

### Git Workflow

```bash
# Create feature branch
git checkout -b feature/your-feature

# Commit with conventional commits
git commit -m "feat: add new feature"

# Push and create PR
git push origin feature/your-feature
```

### Running Tests Locally

```bash
# Backend
cd backend && mvn test

# Frontend  
cd frontend && npm test
```

---

## Performance & Optimization

- **Caching Strategy**: Redis for feed data (future enhancement)
- **Database**: Connection pooling via HikariCP, query optimization
- **Frontend**: Code splitting, lazy loading, image optimization
- **API**: Request batching, pagination for large datasets
- **Rate Limiting**: Per-user API quotas to prevent abuse

---

## Security Considerations

- **Authentication**: JWT tokens with 24-hour expiration
- **Authorization**: Role-based access control (RBAC)
- **Data Protection**: HTTPS only, secrets in environment variables
- **Input Validation**: Server-side validation on all endpoints
- **CORS**: Configured for frontend domain only
- **SQL Injection**: Parameterized queries via JPA

---

## Deployment

### Production Deployment (Render)

1. **Backend Service**
   ```bash
   git push origin main
   # Render auto-deploys on push
   ```

2. **Frontend Service**
   ```bash
   # Environment variables configured in Render dashboard
   npm run build
   ```

3. **Database Migration**
   ```bash
   mvn flyway:migrate
   ```

Live endpoint: [https://vertex-frontend-946u.onrender.com](https://vertex-frontend-946u.onrender.com)

---

## Roadmap

### Phase 1: Core MVP ✅
- [x] Multi-source feed aggregation (GitHub, arXiv, HN)
- [x] AI-powered ranking and summarization
- [x] User authentication (Google OAuth)
- [x] Bookmark and search functionality
- [x] Initial deployment

### Phase 2: Enhanced Intelligence 🚧
- [ ] Security vulnerability tracking
- [ ] AI model release tracking
- [ ] Developer tools directory
- [ ] Context-aware AI assistant
- [ ] Personalized digest generation

### Phase 3: Community & Advanced Features 📋
- [ ] Team collaboration features
- [ ] Custom source integration
- [ ] Mobile app (React Native)
- [ ] API for third-party integrations
- [ ] Machine learning model fine-tuning

---

## Troubleshooting

### Backend Won't Start

- Verify PostgreSQL is running: `pg_isready -h localhost`
- Check `.env.local` has correct database credentials
- Run migrations: `mvn flyway:migrate`

### Frontend Build Issues

- Clear node_modules: `rm -rf node_modules && npm install`
- Clear Next.js cache: `rm -rf .next && npm run build`
- Verify Node version: `node --version` (should be 18+)

### API Connection Errors

- Ensure backend is running on port 8080
- Check CORS configuration in Spring Boot
- Verify frontend `.env.local` has correct `NEXT_PUBLIC_API_URL`

---

## Contributing

We welcome contributions from developers of all levels. See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

### How to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**[View Live Demo](https://vertex-frontend-946u.onrender.com)** • **[Report Bug](https://github.com/26Utkarsh/Vertex/issues)** • **[Request Feature](https://github.com/26Utkarsh/Vertex/issues)**

Made with ❤️ by [26Utkarsh](https://github.com/26Utkarsh)

</div>