# AGENTS.md

## 1. Overview

This module provides Spring property-source support for loading default configuration from YAML resources. It is designed for use in Spring configuration via `@PropertySource`.

## 2. Folder Structure

- `src/main/kotlin/`: public property-source factory in `io.toolisticon.spring.properties`.
- `src/test/kotlin/`: `ApplicationContextRunner` integration tests and configuration-property fixtures under `fixture/`.
- `src/test/resources/`: YAML resources consumed by integration tests.
- `pom.xml`: module dependencies; Spring and SLF4J APIs are provided by consumers.

## 3. Core Behaviors & Patterns

- `YamlPropertySourceFactory` implements Spring's `PropertySourceFactory` and transforms an `EncodedResource` through `YamlPropertiesFactoryBean` into a `PropertiesPropertySource`.
- The source name uses the explicit `@PropertySource` name when supplied, otherwise the resource filename; required values are enforced with `Objects.requireNonNull`.
- Tests load a YAML default through `@PropertySource`, bind it to `@ConfigurationProperties`, and verify that supplied context properties override defaults.

## 4. Conventions

- Keep public utilities in `io.toolisticon.spring.properties` with descriptive PascalCase class names.
- Use Kotlin nullability and Spring annotations to describe framework contracts explicitly.
- Name JUnit tests with backticked behavior statements and assert with AssertJ.
- Keep test YAML small and purpose-specific; place it in `src/test/resources` beside the behavior it supports.

## 5. Working Agreements

See root `/AGENTS.md` for common working agreements.
