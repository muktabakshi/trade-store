# Trade Store (Gradle)

This repository contains a Java Spring Boot application for a Trade Store (Gradle-based).

## What I added
- GitHub Actions CI workflow at `.github/workflows/ci.yml`:
  - Runs on push / pull_request to `main`/`master`
  - Uses JDK 21 
  - Runs `./gradlew clean build` which runs compilation and tests
  - Uploads build/test artifacts

- `README.md` (this file) with quick instructions.

## How to run locally

### Prerequisites
- Java 21 (or compatible JDK)
- Gradle wrapper (`./gradlew`) is included in the repo
- Docker (optional)

### Build & test
From the project root:
```bash
chmod +x ./gradlew || true
./gradlew clean build
```

Test results and reports will be generated under `build/`.

### Run the application
If the project is a Spring Boot application with `bootRun` configured:
```bash
./gradlew bootRun
```
Or run the generated JAR:
```bash
java -jar build/libs/your-app-name.jar
```
Replace `your-app-name.jar` with the actual jar name created under `build/libs/`.

## Notes & next steps
- If the project uses a different Java version, update `.github/workflows/ci.yml`.


## CI: Vulnerability scanning and deployment

Added workflow: `.github/workflows/deploy.yml`

What it does:
- Runs build and unit tests.
- Runs OWASP Dependency-Check CLI (dockerized) and fails the build if any HIGH or CRITICAL vulnerabilities are found.

### Notes & tuning
- Connected kafka , PostgreSQL and MongoDB on local system

