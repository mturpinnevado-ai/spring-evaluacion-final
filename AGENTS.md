# AGENTS.md — spring-evaluacion-final

## Stack

- **Spring Boot 4.1.0 / Java 21** — uses `spring-boot-starter-webmvc` (not `spring-boot-starter-web`) and `spring-boot-h2console` (separate dep)
- **Maven wrapper** (`./mvnw`) — no Gradle
- **Thymeleaf** templates in `src/main/resources/templates/` with `th:replace="~{fragments/navbar :: navbar}"` for layout
- **Spring Data JPA** with H2 in-memory (`create-drop` DDL)
- **Bootstrap 5** static assets at `static/css/bootstrap.min.css` and `static/js/bootstrap.bundle.min.js`
- **H2 console** enabled at `/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`)
- **Lombok** — used in `PreguntaDTO` (`@Data`, `@Builder`, etc.)
- **Jakarta Validation** — messages in `ValidationMessages.properties` (Spanish)
- **Swagger/OpenAPI** via `springdoc-openapi-starter-webmvc-ui:2.8.6` at `/swagger-ui.html` and `/v3/api-docs`

## Package

`com.miempresa.evaluacion_final` — underscore (Java package restriction), not hyphen.

## Build & run

```bash
./mvnw spring-boot:run        # dev server on port 8080
./mvnw test                   # all tests (only contextLoads smoke test exists)
./mvnw test -Dtest=ClassName  # single test class
./mvnw clean package          # build JAR in target/
```

## application.properties notes

- `server.error.whitelabel.enabled=false` — custom error controller handles 400/500
- `spring.jpa.show-sql=true` — Hibernate SQL logged to console
- `spring.jpa.hibernate.ddl-auto=create-drop` — data reset on restart

## Seed data

`src/main/resources/import.sql` inserts 5 temáticas and 74 preguntas (50 abiertas, 3 V/F, 3 selección única, 2 selección múltiple).

## Architecture

- **Entity inheritance**: `Pregunta` (base, discriminator value `ABIERTA`) → `PreguntaVerdaderoFalso` (`V_F`), `PreguntaSeleccionUnica` (`UNICA`), `PreguntaSeleccionMultiple` (`MULTIPLE`). Uses `SINGLE_TABLE` with `tipo_pregunta` discriminator column. Subclasses pass `null` for `respuesta`.
- **DTO**: `PreguntaDTO` (Lombok) maps from entity via `PreguntaDTO::fromEntity`. Used only in REST controller.
- **Repositories**: `PreguntaRepository` has `findFiltered(@Param("tematicaId") Long, @Param("clase") Class, Pageable)` using `TYPE(p)` JPQL for subclass filtering. `TematicaRepository` has `findAllByOrderByNombreAsc()`.
- **Services**: `IPreguntaService`/`PreguntaServiceImpl` (constructor injection). Key method: `listarFiltradas(Long tematicaId, String tipo, Pageable)` maps string type → Class via switch (`V_F`→`PreguntaVerdaderoFalso.class`, etc.).
- **Controllers**: `HomeController`, `PreguntaController` (Thymeleaf CRUD), `PreguntaRestController` (REST at `/api/preguntas`, injects `TematicaRepository` directly bypassing service layer), `CustomErrorController` (error pages for 400, 500).

## Routes

| Method | Path | Action |
|--------|------|--------|
| GET | `/` | Home |
| GET | `/preguntas` | List (paginated 10/page, filterable by `?tematicaId=` & `?tipo=`) |
| GET | `/preguntas/nueva` | Show create form |
| POST | `/preguntas/guardar` | Create (requires `tipo`, type-specific extra params) |
| GET | `/preguntas/editar/{id}` | Show edit form |
| POST | `/preguntas/actualizar/{id}` | Update |
| GET | `/preguntas/eliminar/{id}` | Delete |
| GET | `/api/preguntas` | REST list (paginated, same filters) |
| GET | `/api/preguntas/{id}` | REST get one |
| POST | `/api/preguntas` | REST create (JSON body, returns 201) |
| PUT | `/api/preguntas/{id}` | REST update (JSON body) |
| DELETE | `/api/preguntas/{id}` | REST delete (returns 204) |

## Conventions

- Redirect with flash attributes (`RedirectAttributes`, `success` key)
- Controller methods return template paths (e.g. `"pregunta/listar"`)
- `Tematica` list sorted by `nombre` ASC; `Pregunta` list sorted by `tematicaId` then `id` ASC
- Templates use `th:object`/`th:field` for form binding and `th:each` for iteration
- Form: type selector JavaScript toggles visible section (seccion-ABIERTA, seccion-V_F, seccion-UNICA, seccion-MULTIPLE) and disables hidden inputs
- `PreguntaController.crearSegunTipo()` instantiates the correct subclass from raw form params
- Server-side custom validation for open questions in `validarRespuestaAbierta()`
- `PreguntaNoEncontradaException` (RuntimeException) thrown from controller, no explicit handler
- `.gitignore` excludes `target/`, IDE files, `HELP.md`
