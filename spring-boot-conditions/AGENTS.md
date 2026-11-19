# AGENTS.md

## 1. Overview

This module supplies Spring Boot conditions for detecting beans by type and qualifier. Its public API is intended for use on Spring configuration and auto-configuration classes.

## 2. Folder Structure

- `src/main/kotlin/`: condition annotation, condition implementations, and qualifier matching support in `io.toolisticon.spring.condition`.
- `src/test/kotlin/`: `ApplicationContextRunner` tests and Spring configuration fixtures; add new fixtures under `fixture/`.
- `pom.xml`: module dependencies; Spring and SLF4J APIs are provided by consumers.

## 3. Core Behaviors & Patterns

- `ConditionalOnMissingQualifiedBean` delegates to `OnMissingQualifiedBeanCondition`, which inverts the presence match computed by `AbstractQualifiedBeanCondition`.
- Qualifier matching checks bean-definition metadata, factory methods, bean types, aliases, and bean names; a qualifier prefixed with `!` expresses a non-match check.
- Conditions execute in `REGISTER_BEAN` phase and return `ConditionOutcome` messages; failure to resolve a configured class is logged at warning level and produces a no-match outcome.
- Context behavior is verified with `ApplicationContextRunner` and concise `@Configuration` fixture classes.

## 4. Conventions

- Use Kotlin classes in the `io.toolisticon.spring.condition` package; expose annotations and condition types with descriptive PascalCase names.
- Use expression-bodied functions for simple bean factory methods; retain block bodies where control flow or outcomes require it.
- Name JUnit tests with backticked behavior statements and assert with AssertJ.
- Use KDoc for public APIs and non-obvious Spring condition semantics; cite external inspiration where retained.

## 5. Working Agreements

See root `/AGENTS.md` for common working agreements.
