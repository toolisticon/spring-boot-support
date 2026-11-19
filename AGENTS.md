# AGENTS.md

## 1. Overview

Spring Boot Support is a collection of small, publishable libraries that extend Spring Boot configuration behavior. The repository also provides a BOM for consuming the libraries at aligned versions.

## 2. Folder Structure

- `spring-boot-conditions/`: condition annotations and implementations for qualified Spring beans.
- `spring-boot-properties/`: property-source utilities, including YAML-backed defaults.
- `bom/`: dependency-management module for the published libraries.
- `.mvn/`: Maven Wrapper configuration; use `./mvnw` for builds.
- `README.md`: consumer-facing usage and dependency documentation.

## 3. Working Agreements

- Respond in English; keep technical terms in English and never translate code blocks.
- Create tests or run lint/format tasks only when explicitly requested.
- Before editing code, review related usages and recurring patterns.
- Prefer small, focused changes; do not add abstractions or features outside the request.
- Ask for clarification when requirements are materially ambiguous.
- Keep new code single-purpose and colocated with its related module.
- Add comments only for non-obvious logic or invariants.
- Prefer clean, explicit failures over speculative guards, fallbacks, or compatibility shims unless requested.
