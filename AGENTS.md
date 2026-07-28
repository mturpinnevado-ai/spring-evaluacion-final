# AGENTS.md — spring-evaluacion-final

## Stack

- **Spring Boot 4.1.0 / Java 21** — uses `spring-boot-starter-webmvc` (not `spring-boot-starter-web`) and `spring-boot-h2console` (separate dep)
- **Maven wrapper** (`./mvnw`) — no Gradle
- **Thymeleaf** templates in `src/main/resources/templates/` with `th:replace="~{fragments/navbar :: navbar}"` for layout
- **Spring Data JPA** with H2 in-memory (`create-drop` DDL)
- **Bootstrap 5** static assets at `static/css/bootstrap.min.css` and `static/js/bootstrap.bundle.min.js`
- **H2 console** enabled at `/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`)
- **Lombok** — used in `PreguntaDTO`, `Usuario`, `Rol` (`@Data`, `@Builder`, `@Getter`/`@Setter`, etc.)
- **Jakarta Validation** — messages in `ValidationMessages.properties` (Spanish)
- **Swagger/OpenAPI** via `springdoc-openapi-starter-webmvc-ui:2.8.6` at `/swagger-ui.html` and `/v3/api-docs`
- **Spring Security** — dual auth: form login (Thymeleaf sessions) + JWT Bearer token (`/api/**`);
  BCrypt password encoder; `@EnableMethodSecurity` for `@PreAuthorize`; CSRF ignored for `/h2-console/**` and `/api/**`; frame options `sameOrigin` for H2 console
- **JWT** via `jjwt` 0.12.6 (`JwtService` in `config/`, `JwtAuthenticationFilter` as `OncePerRequestFilter`)

## Package

`com.miempresa.evaluacion_final` — underscore (Java package restriction), not hyphen.

## Testing

- Tests use **Mockito** (`@ExtendWith(MockitoExtension.class)`, no `@SpringBootTest`) — service and controller tests are pure unit tests
- `PreguntaControllerTest` (313 lines) covers all CRUD operations + validation for all 4 question types
- `PreguntaServiceImplTest` (238 lines) covers filtering by temática, type, and both, plus CRUD
- `PreguntaRestControllerTest` (191 lines) — REST controller tested with `MockMvcBuilders.standaloneSetup` (not `@WebMvcTest`)
- **Spring Boot 4.1 removed `@MockBean`/`@MockitoBean`** — `@WebMvcTest` and `@SpringBootTest` + `@AutoConfigureMockMvc` cannot inject mocks into the context. REST controller tests must use `MockMvcBuilders.standaloneSetup` with `@ExtendWith(MockitoExtension.class)`, `PageableHandlerMethodArgumentResolver`, a manual `@ControllerAdvice` for exception→HTTP mapping, and `JsonMapper` with `SpringDataJacksonConfiguration.pageModule()` for `Page` serialization.

## Build & run

```bash
./mvnw spring-boot:run        # dev server on port 8080
./mvnw test                   # all tests (3 test classes, ~550 lines)
./mvnw test -Dtest=ClassName  # single test class
./mvnw clean package          # build JAR in target/
```

## application.properties

- `server.error.whitelabel.enabled=false` — custom error controller handles 400/403/500
- `spring.jpa.show-sql=true` — Hibernate SQL logged to console
- `spring.jpa.hibernate.ddl-auto=create-drop` — data reset on restart
- `jwt.secret` — Base64-encoded key, defined in properties (not env var)

## Seed data (`import.sql`)

Inserts 2 roles (`ROLE_ADMIN`, `ROLE_USER`), 2 users, 5 temáticas, and 74 preguntas.

**Seed users** (BCrypt passwords):
- `admin` / `admin` — has `ROLE_ADMIN` + `ROLE_USER`
- `user` / `user` — has `ROLE_USER` only

Preguntas: 50 abiertas, 3 V/F, 3 selección única, 2 selección múltiple.

## Security

- `SecurityConfig` at `config/SecurityConfig.java`
- `UsuarioDetailsService` implements `UserDetailsService` using `IUsuarioRepository`
- `Usuario` entity (`usuarios` table) with `ManyToMany` → `Rol` (`roles` table) via join table `usuario_roles`
- `MvcConfig` registers `/login` → `login` view and `/error/403` → `error/403` view
- Unauthenticated users can access `/login`, `/css/**`, `/js/**`, `/error`, `/h2-console/**`, `/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`
- All write operations (`POST`, `PUT`, `DELETE` on `/preguntas/**` and `/api/preguntas/**`) require `ROLE_ADMIN`
- `GET /preguntas` (Thymeleaf list) and `GET /api/preguntas` require any authenticated user
- **REST controller** also uses `@PreAuthorize("hasRole('ADMIN')")` on `POST`, `PUT`, `DELETE` (defense in depth)
- **JWT**: `POST /api/auth/login` (public) accepts `{"username","password"}` → returns `{"token":"eyJ..."}`.
  `JwtAuthenticationFilter` extracts Bearer token from `Authorization` header for all `/api/**` requests.
  Web UI continues using form-login sessions; both auth methods work concurrently.

## Architecture

- **Entity inheritance**: `Pregunta` (base, discriminator value `ABIERTA`) → `PreguntaVerdaderoFalso` (`V_F`), `PreguntaSeleccionUnica` (`UNICA`), `PreguntaSeleccionMultiple` (`MULTIPLE`). Uses `SINGLE_TABLE` with `tipo_pregunta` discriminator column. Subclasses pass `null` for `respuesta`.
- **DTO**: `PreguntaDTO` (Lombok) maps from entity via `PreguntaDTO::fromEntity`. Used only in REST controller.
- **Repositories**: `PreguntaRepository` has `findFiltered(@Param("tematicaId") Long, @Param("clase") Class, Pageable)` using `TYPE(p)` JPQL for subclass filtering. `TematicaRepository` has `findAllByOrderByNombreAsc()`. `IUsuarioRepository` has `findByUsername(String)`.
- **Services**: `IPreguntaService`/`PreguntaServiceImpl`, `ITematicaService`/`TematicaService`, `UsuarioDetailsService` — all constructor injection. Key method: `listarFiltradas(Long tematicaId, String tipo, Pageable)` maps string type → Class via switch.
- **Controllers**: `HomeController` (home + `/swagger`), `PreguntaController` (Thymeleaf CRUD), `PreguntaRestController` (REST at `/api/preguntas`), `CustomErrorController` (error pages for 400, 403, 500), `MvcConfig` (view controllers for `/login` and `/error/403`).

## Routes

| Method | Path | Auth | Action |
|--------|------|------|--------|
| GET | `/` | authenticated | Home |
| GET | `/swagger` | authenticated | Swagger iframe page |
| GET | `/login` | anonymous | Login form |
| GET | `/preguntas` | authenticated | List (paginated 10/page, filterable by `?tematicaId=` & `?tipo=`) |
| GET | `/preguntas/nueva` | ADMIN | Show create form |
| POST | `/preguntas/guardar` | ADMIN | Create (requires `tipo`, type-specific extra params) |
| GET | `/preguntas/editar/{id}` | ADMIN | Show edit form |
| POST | `/preguntas/actualizar/{id}` | ADMIN | Update |
| GET | `/preguntas/eliminar/{id}` | ADMIN | Delete |
| GET | `/api/preguntas` | authenticated | REST list (paginated, same filters, returns `PreguntaDTO`) |
| GET | `/api/preguntas/{id}` | authenticated | REST get one |
| POST | `/api/preguntas` | ADMIN | REST create (JSON `Map<String,Object>`, returns 201) |
| PUT | `/api/preguntas/{id}` | ADMIN | REST update (JSON `Map<String,Object>`) |
| DELETE | `/api/preguntas/{id}` | ADMIN | REST delete (returns 204) |
| POST | `/api/auth/login` | anonymous | JWT login: `{"username","password"}` → `{"token"}` |

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
- REST API uses `Map<String, Object>` for request bodies (not typed DTOs) and string switch for type dispatch
- `PreguntaRestController` injects `TematicaRepository` directly (bypasses `ITematicaService`)