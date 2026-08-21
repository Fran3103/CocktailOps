# CocktailOps

![Java CI](https://github.com/Fran3103/CocktailOps/actions/workflows/ci.yml/badge.svg)

CocktailOps es una aplicación **full stack** para planificar órdenes de cócteles para eventos. Permite consultar catálogos de cócteles e insumos, usar listas rápidas predefinidas, calcular productos necesarios, generar listas de compra, crear órdenes guardadas para usuarios registrados y descargar PDFs listos para imprimir.

El proyecto está desarrollado como solución de portfolio profesional, con foco en buenas prácticas de backend, frontend conectado a una API real, seguridad, documentación, testing, migraciones, CI y una experiencia funcional de punta a punta.

---

## Índice

- [Objetivo del proyecto](#objetivo-del-proyecto)
- [Estado actual](#estado-actual)
- [Estructura del repositorio](#estructura-del-repositorio)
- [Módulos](#módulos)
- [Tecnologías principales](#tecnologías-principales)
- [Reglas principales del producto](#reglas-principales-del-producto)
- [Seguridad y permisos](#seguridad-y-permisos)
- [Cálculo de órdenes](#cálculo-de-órdenes)
- [PDF y lista de compra](#pdf-y-lista-de-compra)
- [Catálogo demo y listas rápidas](#catálogo-demo-y-listas-rápidas)
- [Flujos principales](#flujos-principales)
- [Ejecución rápida](#ejecución-rápida)
- [Variables de entorno](#variables-de-entorno)
- [CI/CD](#cicd)
- [Roadmap](#roadmap)
- [Autor](#autor)

---

## Objetivo del proyecto

El objetivo principal de CocktailOps es resolver un problema real del rubro eventos y barras:

> Calcular de forma rápida y ordenada qué productos se necesitan para preparar determinados cócteles en un evento, según cantidad de invitados, duración o cantidad total de bebidas.

El sistema permite cubrir tres tipos de uso:

```txt
Visitante anónimo
→ Ver dashboard público
→ Consultar catálogo
→ Usar listas rápidas o elegir cócteles manualmente
→ Generar una orden temporal
→ Ver resultado inmediato
→ Descargar PDF preview
```

```txt
Usuario autenticado
→ Iniciar sesión
→ Crear orden guardada asociada a su cuenta
→ Consultar dashboard personal
→ Ver historial propio
→ Consultar detalle
→ Descargar PDF protegido
```

```txt
Administrador
→ Iniciar sesión con rol ADMIN
→ Ver dashboard administrativo
→ Consultar métricas generales
→ Ver órdenes del sistema
→ Acceder a detalle de órdenes guardadas
→ Descargar PDFs según permisos del backend
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
- Generación de PDFs
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
| Catálogo ampliado de cócteles | Implementado |
| Productos con categoría básica | Implementado |
| Listas rápidas predefinidas | Implementadas |
| Dashboard invitado | Implementado |
| Dashboard USER | Implementado |
| Dashboard ADMIN | Implementado |
| Orden temporal de invitado | Implementada |
| Preview público JSON | Implementado |
| PDF preview público para invitados | Implementado |
| Creación de órdenes guardadas para usuarios | Implementada |
| Historial de órdenes | Implementado |
| Detalle de orden protegida | Implementado |
| PDF protegido por ownership | Implementado |
| Docker Compose PostgreSQL | Implementado |
| Flyway + seed demo | Implementado |
| Normalización de unidades de catálogo | Implementada |
| GitHub Actions CI | Implementado |
| Testing backend | En progreso |
| Testing frontend automatizado | Pendiente |
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
- Creación de órdenes modo `TIME`
- Creación de órdenes modo `DRINKS`
- Preview público de órdenes temporales
- Cálculo de ingredientes y packs
- Generación de PDF protegido por ID
- Generación pública de PDF preview desde el body
- Autenticación con JWT
- Roles `USER` / `ADMIN`
- Historial de órdenes por usuario
- Protección de detalle y PDF por ownership
- PostgreSQL con Flyway
- Seed demo de catálogo
- Catálogo ampliado de cócteles
- Normalización de unidades
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
- Página de acceso no autorizado
- Dashboard público para invitados
- Dashboard personal para usuarios registrados
- Dashboard administrativo para rol `ADMIN`
- Métricas de órdenes propias
- Métricas generales de órdenes del sistema para admin
- Tabla de últimas órdenes
- Catálogo de cócteles conectado al backend
- Buscador de cócteles
- Listado de productos conectado al backend
- Filtro/buscador de productos
- Creación de órdenes por evento
- Creación de órdenes por cantidad total de tragos
- Listas rápidas predefinidas de cócteles
- Carga automática de cócteles desde presets
- Distribución de cantidades por peso en modo tragos
- Edición manual posterior al preset
- Diferenciación entre usuario invitado y usuario registrado
- Historial de órdenes del usuario autenticado
- Detalle de orden guardada
- Descarga de PDF por orden guardada
- Descarga de PDF preview para órdenes invitadas
- Nota visual sobre cálculo conservador de compra
- Estados de carga, error y vacío
- Feedback visual de orden creada
- Toast de éxito
- Navegación desde orden creada hacia detalle cuando la orden tiene ID
- Prevención de navegación al detalle para órdenes temporales
- Manejo específico de errores HTTP en descarga de PDF

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

- ver el dashboard público
- ver productos
- ver cócteles
- usar listas rápidas predefinidas
- elegir cócteles manualmente
- generar una orden temporal
- ver el resultado inmediato
- descargar un PDF preview desde el resumen inmediato

Un visitante no puede:

- acceder a historial
- acceder a perfil
- conservar órdenes asociadas a una cuenta
- recuperar una orden temporal después de salir del flujo
- consultar una orden temporal por ID
- descargar PDFs protegidos por ID

Regla actual:

```txt
Una orden invitada es temporal.
No se guarda en base de datos.
No tiene ID.
No tiene historial.
El PDF debe descargarse desde el resumen inmediato.
```

---

### Usuario registrado

Un usuario registrado puede:

- iniciar sesión
- crear órdenes asociadas a su cuenta
- usar listas rápidas predefinidas
- guardar órdenes reales en backend
- ver su dashboard personal
- ver su historial de órdenes
- entrar al detalle de órdenes propias
- descargar PDFs desde el detalle o desde historial
- acceder a su perfil

---

### Administrador

Un usuario con rol `ADMIN` puede:

- ver el dashboard administrativo
- consultar métricas generales de órdenes guardadas
- ver últimas órdenes del sistema
- ver columna de usuario en órdenes recientes
- acceder al detalle de órdenes guardadas según reglas del backend
- descargar PDFs de órdenes guardadas según permisos del backend

Las páginas administrativas CRUD para productos, cócteles y categorías todavía no están implementadas en frontend.

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
| `/shop/**` | ADMIN |
| `POST /orders/preview` | Público |
| `POST /orders/by-drinks/preview` | Público |
| `POST /orders/preview/pdf` | Público |
| `POST /orders/by-drinks/preview/pdf` | Público |
| `POST /orders` | Usuario autenticado |
| `POST /orders/by-drinks` | Usuario autenticado |
| `GET /orders/my-orders` | Usuario autenticado |
| `GET /orders` | ADMIN |
| `GET /orders/{id}` | Dueño de la orden o ADMIN |
| `GET /orders/{id}/pdf` | Dueño de la orden o ADMIN |

---

## Cálculo de órdenes

### Modo TIME

El modo `TIME` calcula la cantidad total de tragos a partir de:

```txt
invitados × duración × tragos por persona por hora
```

Ejemplo base:

```txt
60 invitados × 5 horas × 1 trago/persona/hora = 300 tragos
```

Además, el backend aplica una estimación reforzada para eventos grandes con muchas opciones de cócteles:

```txt
Si invitados >= 60
y cócteles seleccionados >= 8
→ usa 2 tragos por persona por hora
```

Ejemplo:

```txt
60 invitados × 5 horas × 2 tragos/persona/hora = 600 tragos
```

Para eventos más chicos, aunque haya muchos cócteles, se mantiene la estimación conservadora:

```txt
40 invitados × 5 horas × 1 trago/persona/hora = 200 tragos
```

---

### Pesos por cóctel

En modo `TIME`, cada cóctel puede recibir un `weight`.

El peso indica preferencia relativa:

```txt
Más peso → más tragos asignados a ese cóctel.
Menos peso → menos tragos asignados.
```

---

### Modo DRINKS

El modo `DRINKS` no estima por invitados ni duración.

El usuario indica:

- total exacto de tragos
- cantidad de tragos por cóctel

Regla:

```txt
La suma de cantidades por cóctel debe ser igual a totalDrinks.
```

En este modo, el frontend puede dividir cantidades equitativamente o distribuirlas por peso cuando se aplica una lista rápida.

---

### Acumulación de ingredientes

El backend no calcula botellas por cóctel de forma separada.

Primero acumula los ingredientes por producto y luego calcula la compra sugerida.

Ejemplo:

```txt
Mojito usa Ron
Daiquiri usa Ron

El sistema suma todo el ron requerido.
Después calcula cuántas botellas comprar.
```

---

## PDF y lista de compra

CocktailOps genera PDFs con **Thymeleaf + OpenHTMLToPDF**.

Tipos de PDF:

| Caso | Endpoint |
|---|---|
| Orden guardada | `GET /orders/{id}/pdf` |
| Preview TIME | `POST /orders/preview/pdf` |
| Preview DRINKS | `POST /orders/by-drinks/preview/pdf` |

La lista de compra representa una **compra sugerida**.

No significa que no vaya a sobrar producto. El sistema calcula la cantidad necesaria para poder preparar hasta el total de tragos estimado y redondea hacia arriba según el formato de compra:

```txt
Botellas
Packs
Unidades
```

Ejemplo:

```txt
Ron requerido: 1600 ML
Botella: 750 ML

1600 / 750 = 2.13
Resultado: comprar 3 botellas
```

Por eso el PDF y el frontend incluyen aclaraciones para explicar la estimación y el redondeo.

---

## Catálogo demo y listas rápidas

El catálogo demo se carga mediante Flyway e incluye aproximadamente 30 cócteles.

Ejemplos:

- Mojito
- Daiquiri
- Gin Tonic
- Margarita
- Fernet Cola
- Aperol Spritz
- Cuba Libre
- Negroni
- Old Fashioned
- Whisky Sour
- Tom Collins
- Dry Martini
- Cosmopolitan
- Moscow Mule
- Paloma
- Caipirinha
- Caipiroska
- Piña Colada
- Sex on the Beach
- Espresso Martini
- Tequila Sunrise
- Americano
- Garibaldi
- French 75
- Bellini
- Vodka Tonic
- Campari Tonic
- Gin Fizz
- Vodka Collins
- Caipirissima

El frontend usa ese catálogo para ofrecer listas rápidas predefinidas:

- Clásicos simples
- Clásicos completos
- Boda / evento elegante
- Modernos y fiesta
- Verano / tropical
- Aperitivos
- Premium clásico
- Popular y rápido

Los presets no guardan IDs fijos. Buscan cócteles por nombre dentro del catálogo real que viene desde backend.

El usuario puede aplicar una lista rápida y después ajustar manualmente:

```txt
Modo TIME   → pesos/preferencias
Modo DRINKS → cantidades por cóctel
```

---

## Flujos principales

### Flujo invitado

```txt
Entrar a la app
→ Ver dashboard público
→ Ver cócteles/productos
→ Crear orden
→ Elegir lista rápida o seleccionar cócteles manualmente
→ Ver resumen de orden temporal
→ Descargar PDF preview
```

El preview JSON se genera mediante:

```http
POST /orders/preview
```

o:

```http
POST /orders/by-drinks/preview
```

El PDF preview se genera mediante:

```http
POST /orders/preview/pdf
```

o:

```http
POST /orders/by-drinks/preview/pdf
```

Estos endpoints son públicos y reciben el body usado para calcular la orden.

Limitación actual:

```txt
La orden invitada no se guarda.
Si el usuario abandona el flujo o refresca fuera del resumen inmediato,
puede perder el payload necesario para regenerar el PDF preview.
```

---

### Flujo usuario registrado

```txt
Login
→ Dashboard personal
→ Crear orden
→ Elegir lista rápida o seleccionar cócteles manualmente
→ Orden asociada al usuario
→ Ver detalle
→ Descargar PDF
→ Ir a Historial
→ Ver órdenes anteriores
→ Entrar al detalle de una orden anterior
```

El historial se obtiene mediante:

```http
GET /orders/my-orders
Authorization: Bearer <token>
```

---

### Flujo administrador

```txt
Login ADMIN
→ Dashboard admin
→ Ver métricas generales
→ Ver últimas órdenes del sistema
→ Acceder a detalle de órdenes guardadas
→ Descargar PDF según permisos backend
```

El listado administrativo se obtiene mediante:

```http
GET /orders
Authorization: Bearer <token-admin>
```

---

## Ejecución rápida

### Levantar PostgreSQL

Desde la raíz del proyecto:

```bash
docker compose up -d
```

El contenedor expone PostgreSQL en:

```txt
localhost:5434
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

Ejemplo base local con Docker:

```properties
server.port=8081

spring.datasource.url=jdbc:postgresql://127.0.0.1:5434/cocktailOps_db?sslmode=disable
spring.datasource.username=postgres
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

spring.jackson.time-zone=UTC

order.drinksPerPersonPerHour=1

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

El proyecto utiliza GitHub Actions para ejecutar build y tests del backend en cada push o pull request sobre `master`.

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
- Catálogo ampliado de cócteles
- Normalización de unidades
- Cálculo de órdenes `TIME` / `DRINKS`
- Orden temporal para invitados
- Preview JSON público
- Generación de PDF preview público
- Creación de órdenes guardadas para usuarios
- Seguridad con Spring Security + JWT
- Roles `USER` / `ADMIN`
- Ownership de órdenes y PDFs
- Historial de órdenes por usuario
- Dashboard invitado
- Dashboard USER
- Dashboard ADMIN
- Listas rápidas predefinidas
- Catálogo de productos y cócteles conectado al frontend
- Docker Compose para PostgreSQL
- GitHub Actions CI
- README y documentación técnica

### Próximos pasos

- Preparar configuración productiva
- Ajustar CORS para URL real del frontend
- Evaluar deploy frontend/backend
- Agregar tests específicos para endpoints protegidos
- Agregar tests para endpoints públicos de PDF preview
- Aumentar cobertura de tests backend
- Agregar tests frontend básicos
- Mejorar estados de error específicos según status HTTP
- Persistir temporalmente payload preview en `sessionStorage`
- Agregar páginas administrativas CRUD para productos/cócteles/categorías
- Refactorizar `CreateOrderPage` en hooks y subcomponentes más pequeños
- Documentar plan de QA manual
- Preparar capturas para portfolio/LinkedIn
- Dockerizar también la aplicación Spring Boot
- Confirmación de correo en registro
- Recuperación de contraseña
- Límites de uso / rate limiting para previews, PDFs, login y registro

---

## Autor

Proyecto desarrollado por **Franco Aguirre** como parte de su portfolio profesional en desarrollo backend/full stack.

Stack principal aplicado:

- Java
- Spring Boot
- PostgreSQL
- React
- TypeScript
- Testing
- QA Manual
