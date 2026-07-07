# Vertex

Vertex V1 aggregates GitHub repositories, arXiv papers, and Hacker News stories into a ranked, AI-summarized developer intelligence feed.

## Apps

- `backend`: Java 21 Spring Boot REST API, collectors, Google OAuth, JWT, PostgreSQL persistence.
- `frontend`: Next.js App Router, TypeScript, Tailwind UI.

## Local Environment

Copy `.env.example` into environment variables for your shell or hosting provider. Secrets are never committed.

## Build

```powershell
cd backend
mvn test
mvn package

cd ..\frontend
npm install
npm run test
npm run build
```

## Local Backend

The ignored `backend/.env.local` file is used for local database credentials.

```powershell
cd backend
.\scripts\run-local.ps1
```
