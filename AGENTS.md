# AI Agent Guide for plain-utils

## What this repository is

- Java utility library with reusable helpers for JSON, JWT, image/captcha, reflection, and converters.
- Maven-based project targeting Java 8.
- Source packages live under `src/main/java/org/plain/utils` and subpackages:
  - `captcha`
  - `converter`
  - `reflection`

## Build and test commands

- Build the project: `mvn clean install`
- Run tests: `mvn test`
- Existing workspace uses Maven executable at `/usr/share/maven/bin/mvn`.

## Important conventions

- Keep code Java 8 compatible.
- Tests use TestNG and plain `assert` statements rather than JUnit assertions.
- Lombok is declared as a provided dependency; do not rely on Lombok at runtime.
- Existing utilities favor static helper methods and `RuntimeException` wrappers for checked exceptions.
- `JsonUtil` uses Jackson `ObjectMapper`; preserve serialization/deserialization behavior and module registration patterns.

## Notes for AI agents

- There is no existing `README.md` or project-specific documentation in the repository.
- Use the `pom.xml` and package source files as the authoritative reference for dependencies and project structure.
- Do not introduce Java language features newer than Java 8.

## When to update this file

- Add new top-level modules or major package areas.
- Document repository-wide conventions that are not obvious from code.
- Add any repo-specific build or test tooling requirements.
