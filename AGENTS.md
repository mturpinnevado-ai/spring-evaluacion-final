# AGENTS.md — spring-evaluacion-final

## Stack

- **Spring Boot 4.1.0 / Java 25** — Maven-compiler-plugin 3.15.0 does not support `--release` for Java 25; pom.xml sets `maven.compiler.release` empty, uses `source`/`target` instead
- **Maven wrapper** (`./mvnw`) — no Gradle
- **Thymeleaf** templates in `src/main/resources/templates/` with `th:replace="~{fragments/navbar :: navbar}"` for layout
- **Spring Data JPA** with H2 in-memory (`create-drop` DDL), MySQL connector available but commented out
- **Bootstrap 5** static assets checked in at `static/css/bootstrap.min.css` and `static/js/bootstrap.bundle.min.js`
- **H2 console** enabled at `/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`)

## Package

`com.miempresa.evaluacion_final` — underscore (Java package restriction), not hyphen.

## Build & run

```bash
./mvnw spring-boot:run        # dev server on port 8080
./mvnw test                   # all tests
./mvnw test -Dtest=ClassName  # single test class
./mvnw clean package          # build JAR in target/
```

## Seed data

`src/main/resources/import.sql` inserts 5 temáticas and 50 preguntas on startup. No other data source.

## Architecture

- **Entities**: `Pregunta` (id, enunciado, respuesta, @ManyToOne → Tematica) and `Tematica` (id, nombre, @OneToMany → Pregunta)
- **Repositories**: `PreguntaRepository` (JpaRepository, custom `findAllByOrderByTematicaIdAscIdAsc(Pageable)`), `TematicaRepository` (JpaRepository, `findAllByOrderByNombreAsc()`)
- **Services**: `IPreguntaService`/`PreguntaServiceImpl`, `ITematicaService`/`TematicaService` — constructor injection
- **Controllers**: `HomeController` (GET / → home), `PreguntaController` (CRUD at `/preguntas`)

## Routes

| Method | Path | Action |
|--------|------|--------|
| GET | `/` | Home |
| GET | `/preguntas` | List (paginated, 10/page, defaults to sort by id ASC; custom method sort applied) |
| GET | `/preguntas/nueva` | Show create form |
| POST | `/preguntas/guardar` | Create |
| GET | `/preguntas/editar/{id}` | Show edit form |
| POST | `/preguntas/actualizar/{id}` | Update |
| GET | `/preguntas/eliminar/{id}` | Delete |

## Conventions

- Redirect with flash attributes (`RedirectAttributes`, `success` key)
- Controller methods return template paths (e.g. `"pregunta/listar"`)
- `Tematica` list sorted by `nombre` ASC; `Pregunta` list sorted by `tematicaId` then `id` ASC (via repository method name)
- Templates use `th:object`/`th:field` for form binding and `th:each` for iteration
- `.gitignore` excludes `target/`, IDE files, `HELP.md`