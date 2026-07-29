# AGENTS.md — spring-evaluacion-final

## Stack

- **Spring Boot 4.1.0 / Java 21** — uses `spring-boot-starter-webmvc` (not `web`) and `spring-boot-h2console`
- **Maven wrapper** (`./mvnw`)
- **Thymeleaf** templates with `th:replace="~{fragments/navbar :: navbar}"` for layout
- **Spring Data JPA** with H2 in-memory (`create-drop` DDL)
- **Bootstrap 5** at `static/css/bootstrap.min.css` and `static/js/bootstrap.bundle.min.js`
- **Lombok** — used in `PreguntaDTO`, `Usuario`, `Rol`
- **Jakarta Validation** — messages in `ValidationMessages.properties` (Spanish)
- **Swagger/OpenAPI** at `/swagger-ui.html` and `/v3/api-docs`
- **Spring Security** — dual auth: form login (Thymeleaf sessions) + JWT Bearer token (`/api/**`); `@EnableMethodSecurity` for `@PreAuthorize`; CSRF ignored for `/h2-console/**` and `/api/**`; frame options `sameOrigin`
- **JWT** via `jjwt` 0.12.6 (`JwtService` + `JwtAuthenticationFilter`)

## Package

`com.miempresa.evaluacion_final` — underscore, not hyphen.

## Build & run

```bash
./mvnw spring-boot:run        # dev server on port 8080
./mvnw test                   # all tests (4 test classes)
./mvnw test -Dtest=ClassName  # single test class
./mvnw clean package          # build JAR in target/
```

## application.properties

- `jwt.secret` — Base64-encoded key, defined inline (not env var)
- `server.error.whitelabel.enabled=false` — custom error controller
- `spring.jpa.hibernate.ddl-auto=create-drop` — data reset on restart

## Seed data (`import.sql`)

- 2 roles: `ROLE_ADMIN`, `ROLE_USER`
- 2 users: `admin`/`admin` (both roles), `user`/`user` (ROLE_USER only)
- 5 temáticas, 58 preguntas (50 abiertas, 3 V/F, 3 selección única, 2 selección múltiple)

## Testing

- **All tests use `@ExtendWith(MockitoExtension.class)` — no `@SpringBootTest`** (except the trivial `contextLoads`)
- `PreguntaControllerTest` (309 lines) — controller CRUD + validation
- `PreguntaServiceImplTest` (238 lines) — filtering + CRUD
- `PreguntaRestControllerTest` (198 lines) — REST CRUD
- **Spring Boot 4.1 removed `@MockBean`/`@MockitoBean`** — REST controller tests must use `MockMvcBuilders.standaloneSetup` with `PageableHandlerMethodArgumentResolver`, a manual `@ControllerAdvice` for exception→HTTP mapping, and `JsonMapper` with `SpringDataJacksonConfiguration.pageModule()` for `Page` serialization.

## Security

- `SecurityConfig` at `config/SecurityConfig.java` — form login at `/login`, JWT filter for `/api/**`
- Public: `/login`, `/css/**`, `/js/**`, `/error`, `/h2-console/**`, `/swagger-ui/**`, `/v3/api-docs/**`
- All write operations (`POST`/`PUT`/`DELETE` on `/preguntas/**` and `/api/preguntas/**`) require `ROLE_ADMIN`
- `/usuarios/**` requires `ROLE_ADMIN` (user administration)
- `GET /preguntas` and `GET /api/preguntas` require any authenticated user
- REST controller adds `@PreAuthorize("hasRole('ADMIN')")` on POST/PUT/DELETE (defense in depth)
- JWT: `POST /api/auth/login` (public) → `{"token":"..."}`; `JwtAuthenticationFilter` extracts Bearer from `Authorization` header for `/api/**`

## Architecture

- **Entity inheritance**: `Pregunta` (base, discriminator `ABIERTA`) → `PreguntaVerdaderoFalso` (`V_F`), `PreguntaSeleccionUnica` (`UNICA`), `PreguntaSeleccionMultiple` (`MULTIPLE`). `SINGLE_TABLE` with `tipo_pregunta` discriminator column. Subclasses pass `null` for `respuesta`.
- **DTO**: `PreguntaDTO` (Lombok) used only in REST controller via `PreguntaDTO::fromEntity`
- **Repositories**: `PreguntaRepository.findFiltered(@Param("tematicaId") Long, @Param("clase") Class, Pageable)` uses `TYPE(p)` JPQL for subclass filtering.
- **Services**: `PreguntaServiceImpl.listarFiltradas(Long tematicaId, String tipo, Pageable)` maps string→Class via switch. `TematicaService` delegates to `TematicaRepository.findAllByOrderByNombreAsc()`. `UsuarioService` handles CRUD + password encoding (skips re-encoding if password starts with `$2a$`).
- **Controllers**: `HomeController` (home + `/swagger`), `PreguntaController` (Thymeleaf CRUD), `PreguntaRestController` (REST at `/api/preguntas`), `CustomErrorController` (400/403/500), `AuthController` (JWT login at `/api/auth/login`), `MvcConfig` (view controllers for `/login` and `/error/403`), `JuegoController` (trivia game at `/juego`), `UsuarioController` (user admin at `/usuarios`).
- **REST API** uses `Map<String, Object>` for request bodies (not typed DTOs) and string switch for type dispatch. `PreguntaRestController` injects `TematicaRepository` directly (bypasses `ITematicaService`).
- Web form: type selector JS toggles sections (seccion-ABIERTA, seccion-V_F, seccion-UNICA, seccion-MULTIPLE); `PreguntaController.crearSegunTipo()` instantiates subclass from raw form params; server-side validation for open questions in `validarRespuestaAbierta()`.
- `PreguntaNoEncontradaException` (RuntimeException) — no explicit handler in production; caught by default error handling.