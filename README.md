# CocktailOps

![Java CI](https://github.com/Fran3103/CocktailOps/actions/workflows/ci.yml/badge.svg)

CocktailOps es una aplicación full stack para planificar pedidos de cócteles para eventos. Permite seleccionar cócteles, calcular ingredientes necesarios, generar listas de compra, crear órdenes, consultar historial de usuario y descargar PDFs listos para imprimir.

El proyecto está desarrollado como solución de portfolio profesional, con foco en buenas prácticas de backend, frontend conectado a API real, seguridad, documentación, testing, automatización y una experiencia funcional de punta a punta.

---

## Objetivo del proyecto

El objetivo principal de CocktailOps es resolver un problema real del rubro eventos y barras:

> Calcular de forma rápida y ordenada qué productos se necesitan para preparar determinados cócteles en un evento, según cantidad de invitados, duración o cantidad total de bebidas.

El sistema permite cubrir dos tipos de uso:

```txt
Visitante anónimo
→ Ver catálogo
→ Crear/calcular orden
→ Ver resultado
→ Descargar PDF preview
```

```txt
Usuario autenticado
→ Iniciar sesión
→ Crear orden asociada a su cuenta
→ Ver historial propio
→ Consultar detalle
→ Descargar PDF protegido
```

Además, el proyecto busca demostrar conocimientos técnicos aplicados en:

- Diseño de API REST
- Arquitectura por capas
- Persistencia con PostgreSQL
- Migraciones con Flyway
- Seguridad con JWT
- Roles y autorización
- Ownership de recursos
- Integración frontend-backend
- Manejo de estados en frontend
- Testing unitario
- CI/CD básico
- Documentación técnica
- Preparación para QA manual

---

## Estado actual

| Módulo / Funcionalidad | Estado |
|---|---|
| Backend API | Implementado |
| Frontend React | Implementado |
| Seguridad JWT | Implementada |
| Roles USER / ADMIN | Implementados |
| Catálogo de productos | Implementado |
| Catálogo de cócteles | Implementado |
| Productos con categoría básica | Implementado |
| Creación pública de órdenes | Implementada |
| Asociación opcional de órdenes a usuario | Implementada |
| Historial de órdenes | Implementado |
| Detalle de orden | Implementado |
| PDF protegido por ownership | Implementado |
| PDF preview público para invitados | Implementado |
| Docker Compose PostgreSQL | Implementado |
| Flyway + seed demo | Implementado |
| GitHub Actions CI | Implementado |
| Testing backend | En progreso |
| Testing frontend | Pendiente |
| QA Manual | Planificado |
| Deploy frontend/backend | Pendiente |

---

## Estructura del repositorio

```txt
CocktailOps/
├── backend/          # API REST con Java + Spring Boot
├── frontend/         # Web App React + TypeScript + Vite
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
- Gestión de categorías
- Gestión de cócteles
- Creación de órdenes modo TIME
- Creación de órdenes modo DRINKS
- Cálculo de ingredientes y packs
- Creación pública de órdenes para visitantes
- Asociación opcional de órdenes al usuario autenticado
- Generación de PDF protegido por ID
- Generación pública de PDF preview desde el body
- Autenticación con JWT
- Roles USER / ADMIN
- Historial de órdenes por usuario
- Protección de PDFs por ownership
- PostgreSQL con Flyway
- Seed demo de catálogo
- Tests unitarios
- CI con GitHub Actions

Más detalle en:

```txt
backend/README.md
```

---

### Frontend

El frontend está desarrollado con React, Vite, TypeScript y Tailwind CSS.

Incluye:

- Layout principal tipo dashboard
- Sidebar responsive
- Login y registro conectados al backend
- Manejo de JWT en frontend
- Persistencia de sesión en `localStorage`
- Rutas públicas y protegidas
- Guardas por autenticación
- Guardas por rol administrativo
- Catálogo de cócteles conectado al backend
- Catálogo de productos conectado al backend
- Creación de órdenes por evento
- Creación de órdenes por cantidad total de tragos
- Diferenciación entre usuario invitado y usuario registrado
- Historial de órdenes del usuario autenticado
- Detalle de orden
- Descarga de PDF por ID para órdenes guardadas
- Descarga de PDF preview para órdenes invitadas
- Estados de carga, error y vacío
- Feedback visual de orden creada
- Toast de éxito
- Navegación desde orden creada hacia detalle
- Navegación desde historial hacia detalle

Más detalle en:

```txt
frontend/README.md
```

---

### Docs

La carpeta `docs/` contiene o contendrá documentación complementaria:

- Capturas de Postman
- Diagramas
- Imágenes para README
- Documentación técnica extendida
- Referencias visuales para portfolio

---

### QA

La carpeta `qa/` está pensada para documentar pruebas manuales y criterios de calidad.

Contenido futuro:

- Plan de pruebas
- Casos de prueba
- Reportes de bugs
- Pruebas exploratorias
- Validaciones de seguridad y permisos
- Evidencia de pruebas del flujo principal

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
- Lombok
- MapStruct

### Frontend

- React
- Vite
- TypeScript
- Tailwind CSS
- React Router
- Axios
- Lucide React
- ESLint
- npm

### Herramientas y prácticas

- Git / GitHub
- Postman
- Docker Desktop
- Arquitectura por capas
- DTOs para requests y responses
- Validaciones
- Manejo global de errores
- Documentación técnica
- Testing unitario
- QA Manual en planificación

---

## Reglas principales del producto

### Visitante anónimo

Un visitante puede:

- ver productos
- ver cócteles
- crear/calcular una orden
- ver el resultado inmediato
- descargar un PDF preview desde el body de la orden

Un visitante no puede:

- acceder a historial
- conservar órdenes asociadas a una cuenta
- acceder a perfil
- descargar PDFs protegidos por ID

---

### Usuario registrado

Un usuario registrado puede:

- iniciar sesión
- crear órdenes asociadas a su cuenta
- ver su historial de órdenes
- entrar al detalle de órdenes propias
- descargar PDFs protegidos de sus órdenes
- acceder a su perfil

---

### Administrador

El rol `ADMIN` queda previsto para funcionalidades de gestión y administración.

Puede acceder a recursos administrativos protegidos y descargar PDFs según reglas del backend.

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
| `POST /orders` | Público. Si hay JWT válido, asocia usuario |
| `POST /orders/by-drinks` | Público. Si hay JWT válido, asocia usuario |
| `POST /orders/preview/pdf` | Público |
| `POST /orders/by-drinks/preview/pdf` | Público |
| `GET /orders/my-orders` | Usuario autenticado |
| `GET /orders` | ADMIN |
| `GET /orders/{id}` | ADMIN |
| `GET /orders/{id}/pdf` | Dueño de la orden o ADMIN |

---

## Funcionalidades principales

- Registro e inicio de sesión de usuarios
- Autenticación con JWT
- Roles `USER` y `ADMIN`
- Gestión de productos
- Productos con información básica de categoría (`categoryId` y `categoryName`)
- Gestión de categorías
- Gestión de cócteles
- Creación de órdenes por tiempo del evento
- Creación de órdenes por cantidad total de bebidas
- Creación pública de órdenes para visitantes
- Asociación opcional de órdenes a usuario autenticado
- Cálculo automático de ingredientes
- Cálculo de packs a comprar
- Generación de PDF protegido por ID
- Generación de PDF preview público
- Historial de órdenes por usuario
- Protección de PDFs por dueño de la orden
- Endpoints administrativos protegidos
- Frontend conectado al backend real
- Documentación Swagger
- Docker Compose para PostgreSQL local
- CI con GitHub Actions

---

## Flujos principales

### Flujo invitado

```txt
Entrar a la app
→ Ver cócteles/productos
→ Crear orden
→ Ver resumen calculado
→ Ver detalle inmediato
→ Descargar PDF preview
```

El PDF preview se genera mediante:

```http
POST /orders/preview/pdf
```

o:

```http
POST /orders/by-drinks/preview/pdf
```

Estos endpoints son públicos y reciben el mismo body usado para calcular la orden.

---

### Flujo usuario registrado

```txt
Login
→ Crear orden
→ Orden asociada al usuario
→ Ver detalle
→ Descargar PDF protegido por ID
→ Ir a historial
→ Ver órdenes anteriores
```

El historial se obtiene mediante:

```http
GET /orders/my-orders
Authorization: Bearer <token>
```

---

### Flujo administrador

```txt
Login como ADMIN
→ Acceso a recursos protegidos
→ Gestión futura de catálogo
→ Acceso a PDFs según reglas administrativas
```

---

## Ejecución rápida

### Levantar PostgreSQL

Desde la raíz del proyecto:

```bash
docker compose up -d
```

---

### Ejecutar backend

Desde la carpeta `backend`:

```bash
cd backend
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

O desde la raíz:

```bash
mvn -f backend/pom.xml spring-boot:run "-Dspring-boot.run.profiles=local"
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

### Ejecutar frontend

Desde la carpeta `frontend`:

```bash
cd frontend
npm install
npm run dev
```

La aplicación queda disponible en:

```txt
http://localhost:5173
```

Más información en:

```txt
frontend/README.md
```

---

## Variables de entorno

### Backend

Crear:

```txt
backend/src/main/resources/application-local.properties
```

Ejemplo base:

```properties
server.port=8081

spring.datasource.url=jdbc:postgresql://localhost:5433/cocktailOps_db?sslmode=disable
spring.datasource.username=postgres
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

spring.jackson.time-zone=UTC

order.drinksPerPersonPerHour=2

security.jwt.secret=local-dev-secret-key-32-characters-minimum-change-me-123456
```

---

### Frontend

Crear:

```txt
frontend/.env.local
```

Ejemplo:

```env
VITE_API_BASE_URL=http://localhost:8081
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
- Frontend React implementado
- Migraciones con Flyway
- Seed demo de catálogo
- Cálculo de órdenes TIME / DRINKS
- Creación pública de órdenes
- Asociación opcional de órdenes al usuario autenticado
- Generación de PDF protegido por ID
- Generación de PDF preview público
- Seguridad con Spring Security + JWT
- Roles USER / ADMIN
- Ownership de órdenes y PDFs
- Historial de órdenes por usuario
- Catálogo de productos y cócteles conectado al frontend
- Docker Compose para PostgreSQL
- GitHub Actions CI
- README y documentación técnica

### Próximos pasos

- Agregar tests específicos para endpoints protegidos
- Agregar tests para endpoints públicos de PDF preview
- Aumentar cobertura de tests backend
- Agregar tests frontend básicos
- Mejorar estados de error específicos según status HTTP
- Persistir temporalmente el payload preview en `sessionStorage`
- Agregar páginas administrativas para productos/cócteles/categorías
- Documentar plan de QA manual
- Preparar capturas para portfolio/LinkedIn
- Evaluar deploy frontend/backend
- Dockerizar también la aplicación Spring Boot

---

## Autor

Proyecto desarrollado por Franco Aguirre como parte de su portfolio profesional en desarrollo backend/full stack.

Stack principal aplicado:

- Java
- Spring Boot
- PostgreSQL
- React
- TypeScript
- Testing
- QA Manual