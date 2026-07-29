# spring-evaluacion-final

Aplicación Spring Boot para gestión de preguntas tipo test con autenticación JWT y formularios Thymeleaf.

## Requisitos

- **Java 21** (JDK)
- **Maven** (o usar el wrapper `./mvnw` incluido)

## Arrancar en local

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd spring-evaluacion-final

# Compilar y arrancar (puerto 8080)
./mvnw spring-boot:run
```

La aplicación arranca con base de datos H2 en memoria. Los datos de semilla (`import.sql`) se cargan automáticamente:

| Usuario | Contraseña | Roles                |
|---------|------------|----------------------|
| `admin` | `admin`    | ROLE_ADMIN, ROLE_USER |
| `user`  | `user`     | ROLE_USER            |

## Endpoints principales

| URL                     | Descripción                              |
|-------------------------|------------------------------------------|
| `http://localhost:8080/` | Página principal                        |
| `http://localhost:8080/login` | Formulario de login               |
| `http://localhost:8080/preguntas` | CRUD de preguntas (web)        |
| `http://localhost:8080/juego` | Juego de trivia                     |
| `http://localhost:8080/usuarios` | Administración de usuarios (solo admin) |
| `http://localhost:8080/h2-console` | Consola H2 (JDBC URL: `jdbc:h2:mem:testdb`) |
| `http://localhost:8080/swagger-ui.html` | Documentación Swagger     |
| `http://localhost:8080/v3/api-docs` | API docs (JSON)              |

## API REST (JWT)

```bash
# Obtener token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

# Usar token en peticiones a /api/**
curl http://localhost:8080/api/preguntas \
  -H "Authorization: Bearer <token>"
```

## Tests

```bash
./mvnw test                    # Todos los tests
./mvnw test -Dtest=PreguntaControllerTest  # Clase específica
```

## Build

```bash
./mvnw clean package
java -jar target/evaluacion-final-0.0.1-SNAPSHOT.jar
```

## Stack técnico

- Spring Boot 3.4.1 / Java 21
- Thymeleaf + Bootstrap 5
- Spring Data JPA + H2 (in-memory)
- Spring Security (form login + JWT)
- Lombok, Jakarta Validation, Swagger/OpenAPI
