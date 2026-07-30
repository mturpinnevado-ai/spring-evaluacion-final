# AGENTS.md — spring-evaluacion-final

## Stack

- **Spring Boot 4.1.0 / Java 21** — uses `spring-boot-starter-webmvc` (not `web`); `springdoc-openapi` pinned to `2.8.6`
- **Maven wrapper** (`./mvnw`); MySQL at runtime, H2 only in test scope
- **Thymeleaf** templates with `th:replace="~{fragments/navbar :: navbar}"` for layout
- **Spring Data JPA** with `ddl-auto=update` (main) / `create-drop` (test)
- **Bootstrap 5** at `static/css/bootstrap.min.css` / `static/js/bootstrap.bundle.min.js`
- **Lombok** — used in `PreguntaDTO`, `Usuario`, `Rol`
- **Jakarta Validation** — messages in `ValidationMessages.properties` (Spanish)
- **Spring Security** — dual auth: form login (Thymeleaf sessions) + JWT Bearer token (`/api/**`); `@EnableMethodSecurity`; CSRF ignored for `/api/**`; frame options `sameOrigin`
- **JWT** via `jjwt` 0.12.6 (`JwtService` + `JwtAuthenticationFilter`)
- **DevTools** as runtime dependency

## Package

`com.miempresa.evaluacion_final` — underscore, not hyphen.

## Build & run

```bash
./mvnw spring-boot:run        # requires MySQL at localhost:3306 (root/root)
./mvnw test                   # all tests (4 classes) — uses H2 in-memory
./mvnw test -Dtest=ClassName  # single test class
./mvnw clean package          # build JAR in target/
java -jar target/evaluacion-final-0.0.1-SNAPSHOT.jar
docker compose up --build     # spins up MySQL + app (alternative to local MySQL)
```

## application.properties (main)

- **MySQL** — `jdbc:mysql://localhost:3306/evaluacion_final` (root/root)
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.jpa.defer-datasource-initialization=true` — `data.sql` runs **after** Hibernate DDL
- `spring.sql.init.mode=always` — loads `data.sql` on startup
- `server.error.whitelabel.enabled=false` + `spring.web.resources.add-mappings=false` + `spring.mvc.throw-exception-if-no-handler-found=true` — custom error handling
- `jwt.secret` — Base64-encoded key, defined inline (not env var)

## Seed data (`data.sql`)

- 2 roles: `ROLE_ADMIN`, `ROLE_USER`
- 2 users: `admin`/`admin` (both roles), `user`/`user` (ROLE_USER only)
- 5 temáticas, 58 preguntas (50 abiertas, 3 V/F, 3 selección única, 2 selección múltiple)
- Uses `WHERE NOT EXISTS` guards for idempotent re-runs

## Testing

- **All tests use `@ExtendWith(MockitoExtension.class)` — no `@SpringBootTest`** (except trivial `contextLoads`)
- **Spring Boot 4.1 removed `@MockBean`/`@MockitoBean`** — REST controller tests use `MockMvcBuilders.standaloneSetup` with `PageableHandlerMethodArgumentResolver`, a manual `@ControllerAdvice` for exception→HTTP mapping, and `JsonMapper` with `SpringDataJacksonConfiguration.pageModule()` for `Page` serialization.
- `PreguntaControllerTest` — controller CRUD + validation
- `PreguntaServiceImplTest` — filtering + CRUD
- `PreguntaRestControllerTest` — REST CRUD

## Security

- Form login at `/login`, JWT filter for `/api/**`
- Public: `/login`, `/css/**`, `/js/**`, `/error`, `/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`, `/api/auth/**`
- Write ops (`POST`/`PUT`/`DELETE` on `/preguntas/**` and `/api/preguntas/**`) require `ROLE_ADMIN`
- `/usuarios/**` requires `ROLE_ADMIN`
- `GET /preguntas` and `GET /api/preguntas` require any authenticated user
- REST controller adds `@PreAuthorize("hasRole('ADMIN')")` on POST/PUT/DELETE (defense in depth)
- JWT: `POST /api/auth/login` (public) → `{"token":"..."}`; filter extracts Bearer from `Authorization` header

## Architecture

- **Entity inheritance**: `Pregunta` (base, `SINGLE_TABLE`, discriminator `tipo_pregunta`) → `PreguntaVerdaderoFalso` (`V_F`), `PreguntaSeleccionUnica` (`UNICA`), `PreguntaSeleccionMultiple` (`MULTIPLE`). Subclasses pass `null` for `respuesta`.
- **DTO**: `PreguntaDTO` (Lombok) used only in REST controller via `PreguntaDTO::fromEntity`
- **Repositories**: `PreguntaRepository.findFiltered(tematicaId, Class, Pageable)` uses `TYPE(p)` JPQL. `TematicaRepository.findAllByOrderByNombreAsc()`.
- **Services**: `PreguntaServiceImpl.listarFiltradas(Long, String, Pageable)` maps string→Class via switch. `listarParaJuego(Long tematicaId)` retrieves unfiltered for the trivia game. `UsuarioService` skips password re-encoding if it starts with `$2a$`.
- **REST API** uses `Map<String, Object>` for request bodies (not typed DTOs) and string switch for type dispatch. `PreguntaRestController` injects `TematicaRepository` directly.
- Web form: type selector JS toggles sections; `PreguntaController.crearSegunTipo()` instantiates subclass from raw form params; `validarRespuestaAbierta()` for server-side validation.
- `PreguntaNoEncontradaException` (RuntimeException) — no explicit handler in production; caught by default error handling. Tests register a `@ControllerAdvice` to map it to HTTP 404.

## Controllers (routes)

| URL | Controller | Notes |
|---|---|---|
| `/` | `HomeController` | home page |
| `/login` | `MvcConfig` view controller | form login page |
| `/preguntas` | `PreguntaController` | Thymeleaf CRUD |
| `/api/preguntas` | `PreguntaRestController` | REST CRUD |
| `/api/auth/login` | `AuthController` | JWT token endpoint |
| `/juego` | `JuegoController` | trivia game (session) |
| `/usuarios` | `UsuarioController` | user admin |
| `/error`, `/error/403` | `CustomErrorController`, `MvcConfig` | error pages |
| `/swagger` | `HomeController` | Swagger redirect |

## Session-based game (`SesionJuego`)

- Stored in `HttpSession`; not persisted; no Lombok (manual getters/setters)
- 10 questions per round (or fewer if pool < 10), shuffled randomly
- Question types dispatched by string switch in `JuegoController.responder()`
- Answers stored as `RespuestaJuego` records in `sesion.getRespuestas()`