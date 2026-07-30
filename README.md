# spring-evaluacion-final

Aplicación Spring Boot para gestión de preguntas tipo test con autenticación JWT y formularios Thymeleaf.

## Requisitos

- **Java 21** (JDK)
- **Maven** (o usar el wrapper `./mvnw` incluido)
- **MySQL 8** en local (puerto 3306, usuario `root`, contraseña `root`), o usar Docker

## Arrancar en local

```bash
./mvnw spring-boot:run
```

Requiere MySQL en `localhost:3306` con base de datos `evaluacion_final` (usuario `root`/`root`). Los datos de semilla (`data.sql`) se cargan automáticamente al iniciar:

| Usuario | Contraseña | Roles                |
|---------|------------|----------------------|
| `admin` | `admin123` | ROLE_ADMIN, ROLE_USER |
| `user`  | `user123`  | ROLE_USER            |

## Docker (alternativa)

```bash
docker compose up --build
```

Levanta MySQL + aplicación sin necesidad de tener MySQL instalado en local.

## Endpoints principales

| URL                              | Descripción                              |
|----------------------------------|------------------------------------------|
| `http://localhost:8080/`         | Página principal                        |
| `http://localhost:8080/login`    | Formulario de login                     |
| `http://localhost:8080/preguntas` | CRUD de preguntas (web)               |
| `http://localhost:8080/juego`    | Juego de trivia                         |
| `http://localhost:8080/usuarios` | Administración de usuarios (solo admin) |
| `http://localhost:8080/swagger`  | Redirección a Swagger UI               |
| `http://localhost:8080/swagger-ui.html` | Documentación Swagger           |
| `http://localhost:8080/v3/api-docs` | API docs (JSON)                     |
| `POST /api/auth/login`           | JWT token (público)                    |
| `GET /api/preguntas`             | Listar preguntas (autenticado)         |

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
./mvnw test                      # Todos los tests (4 clases)
./mvnw test -Dtest=ClassName     # Clase específica
```

## Build

```bash
./mvnw clean package
java -jar target/evaluacion-final-0.0.1-SNAPSHOT.jar
```

## Stack técnico

- Spring Boot 4.1.0 / Java 21
- Thymeleaf + Bootstrap 5
- Spring Data JPA + MySQL
- Spring Security (form login + JWT)
- Lombok, Jakarta Validation, Swagger/OpenAPI