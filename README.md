# CocktailOps

![Java CI](https://github.com/Fran3103/CocktailOps/actions/workflows/ci.yml/badge.svg)

CocktailOps es una aplicación para planificar pedidos de cócteles para eventos. Permite seleccionar cócteles, calcular ingredientes necesarios, generar listas de compra y descargar PDFs listos para imprimir.

El proyecto está pensado como una solución full stack para portfolio, con foco en buenas prácticas de backend, seguridad, documentación, testing, automatización y una futura demo visual en frontend.

---

## Objetivo del proyecto

El objetivo principal de CocktailOps es resolver un problema real del rubro eventos/barras:

> Calcular de forma rápida y ordenada qué productos se necesitan para preparar determinados cócteles en un evento, según cantidad de invitados, duración o cantidad total de bebidas.

Además, el proyecto busca demostrar conocimientos técnicos aplicados en:

- Diseño de API REST
- Arquitectura por capas
- Persistencia con PostgreSQL
- Migraciones con Flyway
- Seguridad con JWT
- Roles y autorización
- Ownership de recursos
- Testing unitario
- CI/CD básico
- Documentación técnica
- Preparación para frontend y QA

---

## Estado actual

| Módulo | Estado |
|---|---|
| Backend API | Funcional |
| Seguridad JWT | Implementada |
| Roles USER / ADMIN | Implementados |
| Órdenes asociadas a usuario | Implementado |
| Historial de órdenes | Implementado |
| PDF protegido por ownership | Implementado |
| Docker Compose PostgreSQL | Implementado |
| GitHub Actions CI | Implementado |
| Frontend | Planificado / próximo módulo |
| QA Manual | Planificado |

---

## Estructura del repositorio

```txt
CocktailOps/
├── backend/          # API REST con Java + Spring Boot
├── frontend/         # Web App React, planificada
├── docs/             # Documentación técnica, diagramas, capturas y Postman
├── qa/               # Documentación futura de pruebas manuales
├── .github/          # Workflows de GitHub Actions
├── docker-compose.yml
├── README.md
└── .gitignore
```

---

## Módulos

### Backend

El backend está desarrollado con Java y Spring Boot.

Incluye:

- API REST
- Gestión de productos
- Gestión de cócteles
- Creación de órdenes
- Cálculo de ingredientes y packs
- Generación de PDF
- Autenticación con JWT
- Roles USER / ADMIN
- Historial de órdenes por usuario
- Protección de PDFs por ownership
- Tests unitarios
- CI con GitHub Actions

Más detalle en:

```txt
backend/README.md
```

---

### Frontend

El frontend está planificado como una aplicación React para mostrar el flujo principal de CocktailOps de punta a punta.

Flujo esperado:

```txt
Registro / Login
→ Ver cócteles disponibles
→ Crear orden
→ Ver resultado calculado
→ Descargar PDF
→ Ver historial propio
```

Stack planificado:

- React
- Vite
- TypeScript
- Tailwind CSS
- React Router

---

### Docs

La carpeta `docs/` contiene o contendrá documentación complementaria:

- Capturas de Postman
- Diagramas
- Imágenes para README
- Documentación técnica extendida

---

### QA

La carpeta `qa/` está pensada para documentar pruebas manuales y criterios de calidad.

Contenido futuro:

- Plan de pruebas
- Casos de prueba
- Reportes de bugs
- Pruebas exploratorias
- Validaciones de seguridad y permisos

---

## Tecnologías principales

### Backend

- Java 17
- Spring Boot
- Maven
- PostgreSQL
- Spring Data JPA / Hibernate
- Flyway
- Spring Security + JWT
- Swagger / OpenAPI
- Thymeleaf + OpenHTMLToPDF
- JUnit 5
- Mockito
- H2
- Docker Compose
- GitHub Actions CI

### Frontend

- React
- Vite
- TypeScript
- Tailwind CSS

---

## Seguridad y permisos

CocktailOps usa autenticación stateless con JWT.

Reglas actuales principales:

| Endpoint | Acceso |
|---|---|
| `/auth/**` | Público |
| Swagger / OpenAPI | Público |
| GET catálogo | Público |
| POST / PUT / PATCH / DELETE catálogo | ADMIN |
| `/user/**` | ADMIN |
| `POST /orders` | Usuario autenticado |
| `GET /orders/my-orders` | Usuario autenticado |
| `GET /orders` | ADMIN |
| `GET /orders/{id}/pdf` | Dueño de la orden o ADMIN |

---

## Funcionalidades principales

- Registro e inicio de sesión de usuarios
- Autenticación con JWT
- Roles `USER` y `ADMIN`
- Gestión de productos
- Gestión de cócteles
- Creación de órdenes por tiempo del evento
- Creación de órdenes por cantidad total de bebidas
- Cálculo automático de ingredientes
- Cálculo de packs a comprar
- Generación de PDF
- Historial de órdenes por usuario
- Protección de PDFs por dueño de la orden
- Endpoints administrativos protegidos

---

## Ejecución rápida del backend

Levantar PostgreSQL desde la raíz del proyecto:

```bash
docker compose up -d
```

Ejecutar backend:

```bash
cd backend
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

Swagger UI:

```txt
http://localhost:8081/swagger-ui/index.html
```

Más información en:

```txt
backend/README.md
```

---

## CI/CD

El proyecto utiliza GitHub Actions para ejecutar el build y los tests del backend en cada push o pull request sobre `master`.

Workflow:

```txt
.github/workflows/ci.yml
```

---

## Roadmap

### Completado

- Backend API funcional
- Migraciones con Flyway
- Cálculo de órdenes TIME / DRINKS
- Generación de PDFs
- Seguridad con Spring Security + JWT
- Roles USER / ADMIN
- Ownership de órdenes y PDFs
- Docker Compose para PostgreSQL
- GitHub Actions CI
- README y documentación inicial

### Próximos pasos

- Crear frontend con React + Vite + TypeScript
- Conectar login/register con backend
- Consumir catálogo de cócteles
- Crear órdenes desde frontend
- Mostrar historial de órdenes
- Descargar PDFs desde la web
- Agregar tests específicos para endpoints protegidos
- Documentar plan de QA manual
- Preparar demo visual para portfolio

---

## Autor

Proyecto desarrollado por Franco Aguirre como parte de su formación y portfolio profesional en desarrollo backend/full stack.

Stack principal de estudio:

- Java
- Spring Boot
- PostgreSQL
- React
- Testing
- QA Manual