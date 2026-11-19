# AGENTS.md

## 1. Overview

This module publishes dependency-management coordinates for the Spring Boot Support libraries. It contains no production source code.

## 2. Folder Structure

- `pom.xml`: declares managed versions for the sibling published libraries.

## 3. Core Behaviors & Patterns

- The BOM inherits the repository version and manages sibling artifacts using `${project.groupId}` and `${project.version}`.
- Keep the BOM aligned with the root module list and every library intended for consumer import.

## 4. Conventions

- Preserve Maven XML indentation and element ordering used by the parent POM.
- Add managed dependencies only for published sibling artifacts; use project coordinates rather than hard-coded versions.

## 5. Working Agreements

See root `/AGENTS.md` for common working agreements.
