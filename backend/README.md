# CocktailOps Backend

Backend REST API para CocktailOps, desarrollado con Java y Spring Boot.

Este módulo contiene la lógica principal del sistema: gestión de productos, cócteles, órdenes, cálculo de ingredientes, generación de PDFs, autenticación JWT, autorización por roles y ownership de recursos.

---

## Índice

- [Descripción](#descripción)
- [Decisiones técnicas](#decisiones-técnicas)
- [Características principales](#características-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Autenticación y seguridad](#autenticación-y-seguridad)
- [Instalación y uso](#instalación-y-uso)
- [API - Ejemplos principales](#api---ejemplos-principales)
- [Testing unitario](#testing-unitario)
- [Diagramas](#diagramas)
- [Estado actual del backend](#estado-actual-del-backend)
- [Próximos pasos backend](#próximos-pasos-backend)

---

## Descripción

CocktailOps Backend permite calcular pedidos de cócteles para eventos.

A partir de una selección de cócteles, cantidad de invitados, duración del evento o cantidad total de bebidas, la API calcula:

- cantidad necesaria de cada ingrediente
- productos involucrados
- packs sugeridos a comprar
- detalle de cócteles incluidos
- PDF con lista de compra

También incorpora autenticación y autorización para que cada usuario pueda crear y consultar sus propias órdenes.

---

## Decisiones técnicas

### Arquitectura por capas

El backend está organizado con una arquitectura por capas:

```txt
Controller → Service → Repository
```

Responsabilidades:

- **Controller**: expone endpoints HTTP, maneja requests/responses y documentación Swagger.
- **Service**: contiene reglas de negocio, validaciones, cálculos y orquestación.
- **Repository**: accede a la base de datos mediante Spring Data JPA.

Esta separación mejora la mantenibilidad, el testeo y la evolución del proyecto.

---

### DTOs en lugar de exponer entidades

El proyecto utiliza DTOs para requests y responses.

Motivos:

- evitar exponer entidades JPA directamente
- mantener estable el contrato de la API
- validar inputs con Bean Validation
- controlar qué información se devuelve al cliente
- mejorar la documentación Swagger

---

### Flyway para migraciones

Flyway se utiliza para versionar la base de datos mediante scripts SQL.

Ventajas:

- base reproducible entre ambientes
- cambios controlados
- historial de migraciones
- integración simple con Spring Boot

---

### JPA / Hibernate

La persistencia se implementa con Spring Data JPA y Hibernate.

Se usa para:

- mapear entidades a tablas relacionales
- definir relaciones
- trabajar con repositorios
- simplificar consultas frecuentes

---

### Manejo global de errores

El backend centraliza errores con `@RestControllerAdvice`.

Esto permite devolver respuestas consistentes para casos como:

- recursos no encontrados
- credenciales inválidas
- reglas de negocio incumplidas
- errores de validación
- accesos prohibidos
- errores inesperados

---

### Logging

Se agregaron logs en servicios principales para registrar flujos importantes:

- creación de órdenes
- búsqueda de recursos
- generación de PDFs
- errores esperados e inesperados

---

## Características principales

- Gestión de usuarios
- Registro e inicio de sesión
- Autenticación con JWT
- Roles `USER` y `ADMIN`
- Gestión de productos
- Gestión de categorías
- Gestión de cócteles
- Gestión de tiendas
- Creación de órdenes modo TIME
- Creación de órdenes modo DRINKS
- Cálculo automático de ingredientes
- Cálculo de packs a comprar
- Generación de PDF
- Historial de órdenes por usuario
- Protección de PDFs por ownership
- Endpoints administrativos protegidos
- Documentación Swagger
- Tests unitarios
- Perfil de testing con H2
- CI con GitHub Actions

---

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Maven
- PostgreSQL
- Spring Data JPA
- Hibernate
- Flyway
- Spring Security
- JWT
- Swagger / OpenAPI
- Thymeleaf
- OpenHTMLToPDF
- JUnit 5
- Mockito
- H2
- Docker Compose
- GitHub Actions CI
- Lombok
- MapStruct

---

## Autenticación y seguridad

El backend utiliza Spring Security + JWT.

El flujo actual permite:

- registrar usuarios desde `/auth/register`
- iniciar sesión desde `/auth/login`
- recibir un token JWT
- enviar el token en requests protegidas
- validar usuarios mediante filtro JWT
- proteger endpoints por rol
- asociar órdenes al usuario autenticado
- proteger PDFs por dueño de la orden o rol ADMIN

---

### Registro de usuario

```http
POST /auth/register
```

Body:

```json
{
  "email": "usuario@ejemplo.com",
  "password": "123456",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

Respuesta:

```json
{
  "id": 1,
  "email": "usuario@ejemplo.com",
  "firstName": "Juan",
  "lastName": "Pérez",
  "role": "USER",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### Inicio de sesión

```http
POST /auth/login
```

Body:

```json
{
  "email": "usuario@ejemplo.com",
  "password": "123456"
}
```

Respuesta:

```json
{
  "id": 1,
  "email": "usuario@ejemplo.com",
  "firstName": "Juan",
  "lastName": "Pérez",
  "role": "USER",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### Uso del token

Para acceder a endpoints protegidos:

```http
Authorization: Bearer <token>
```

Ejemplo:

```http
GET /orders/my-orders
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### Reglas actuales de acceso

| Endpoint | Acceso |
|---|---|
| `/auth/**` | Público |
| Swagger / OpenAPI | Público |
| GET catálogo | Público |
| POST / PUT / PATCH / DELETE catálogo | ADMIN |
| `/user/**` | ADMIN |
| `POST /orders` | Usuario autenticado |
| `POST /orders/by-drinks` | Usuario autenticado |
| `GET /orders/my-orders` | Usuario autenticado |
| `GET /orders` | ADMIN |
| `GET /orders/{id}` | ADMIN |
| `GET /orders/{id}/pdf` | Dueño de la orden o ADMIN |

---

### Historial de órdenes

```http
GET /orders/my-orders
Authorization: Bearer <token>
```

Este endpoint devuelve únicamente las órdenes asociadas al usuario autenticado.

---

### Descarga de PDF

```http
GET /orders/{id}/pdf
Authorization: Bearer <token>
```

Reglas:

- `USER` puede descargar PDFs de sus propias órdenes.
- `USER` no puede descargar PDFs de órdenes ajenas.
- `ADMIN` puede descargar PDFs de cualquier orden.

---

## Instalación y uso

### Requisitos previos

- Java 17 o superior
- Maven 3.6.3 o superior
- Docker Desktop
- Docker Compose
- PostgreSQL local, solo si no se usa Docker

---

### Clonar repositorio

Desde la carpeta donde quieras guardar el proyecto:

```bash
git clone https://github.com/Fran3103/CocktailOps.git
cd CocktailOps
```

---

### Levantar PostgreSQL con Docker Compose

El archivo `docker-compose.yml` está en la raíz del proyecto.

Desde la raíz:

```bash
docker compose up -d
```

Verificar contenedor:

```bash
docker compose ps
```

---

### Configuración local

Crear el archivo:

```txt
backend/src/main/resources/application-local.properties
```

Tomar como base:

```txt
backend/src/main/resources/application-local.example.properties
```

Ejemplo:

```properties
server.port=8081

spring.datasource.url=jdbc:postgresql://localhost:5433/cocktailOps_db?sslmode=disable
spring.datasource.username=postgres
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

spring.jpa.hibernate.ddl-auto=validate

order.drinksPerPersonPerHour=2
```

> `application-local.properties` no debe versionarse porque puede contener credenciales locales.

---

### Ejecutar backend

Desde la raíz del repo:

```bash
cd backend
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

O también desde la raíz:

```bash
mvn -f backend/pom.xml spring-boot:run "-Dspring-boot.run.profiles=local"
```

---

### Ejecutar tests y build

Desde `backend/`:

```bash
mvn clean install
```

Desde la raíz:

```bash
mvn -f backend/pom.xml clean install
```

---

### Swagger UI

Con la aplicación corriendo:

```txt
http://localhost:8081/swagger-ui/index.html
```

---

### Apagar PostgreSQL

Desde la raíz:

```bash
docker compose down
```

Para borrar también el volumen de datos:

```bash
docker compose down -v
```

> Usar `docker compose down -v` solo si se quiere reiniciar completamente la base local.

---

## API - Ejemplos principales

### Crear orden modo TIME

```http
POST /orders
Authorization: Bearer <token>
```

Body:

```json
{
  "guests": 100,
  "durationHours": 5,
  "cocktails": [
    { "cocktailId": 1, "weight": 5 },
    { "cocktailId": 2, "weight": 4 }
  ]
}
```

---

### Crear orden modo DRINKS

```http
POST /orders/by-drinks
Authorization: Bearer <token>
```

Body:

```json
{
  "totalDrinks": 100,
  "cocktails": [
    { "cocktailId": 1, "quantity": 25 },
    { "cocktailId": 12, "quantity": 25 },
    { "cocktailId": 13, "quantity": 25 },
    { "cocktailId": 5, "quantity": 25 }
  ]
}
```

---

### Consultar historial propio

```http
GET /orders/my-orders
Authorization: Bearer <token>
```

---

### Descargar PDF

```http
GET /orders/{id}/pdf
Authorization: Bearer <token>
```

---

## Testing unitario

El backend incorpora tests unitarios en la capa service usando JUnit 5 y Mockito.

Estado actual:

### ProductServiceImpl

Cobertura básica completada:

- búsqueda por ID
- creación
- actualización
- eliminación
- búsqueda por nombre
- búsqueda por categoría
- listado general
- validaciones y excepciones principales

### OrderServiceImpl

Cobertura parcial en progreso:

- búsqueda por ID
- listado general
- validaciones iniciales de creación de órdenes

Objetivos de testing:

- validar caminos felices
- validar caminos alternativos
- probar excepciones controladas
- evitar dependencia de base de datos real
- asegurar reglas de negocio principales

---

## Diagramas

### Diagrama de Entidad-Relación

```mermaid
erDiagram
  SHOP {
    bigint id PK
    string name
    string slug
  }

  USER {
    bigint id PK
    string email
    string password_hash
    string first_name
    string last_name
    string role
    bigint shop_id FK
  }

  CATEGORY {
    bigint id PK
    bigint shop_id FK
    string name
    string slug
    boolean active
  }

  PRODUCT {
    bigint id PK
    bigint category_id FK
    string name
    string unit
    numeric unit_size
    boolean active
    string image_url
    string image_alt
  }

  SHOP_PRODUCT {
    bigint id PK
    bigint shop_id FK
    bigint product_id FK
    string purchase_url
    numeric price
    string sku
  }

  COCKTAIL {
    bigint id PK
    string name
    string description
    string image_url
    string image_alt
  }

  COCKTAIL_INGREDIENT {
    bigint id PK
    bigint cocktail_id FK
    bigint product_id FK
    numeric amount
    string unit
  }

  ORDER {
    bigint id PK
    bigint user_id FK
    timestamp created_at
    int guests
    int drinks_per_person
    int duration_hours
    string status
  }

  ORDER_COCKTAIL {
    bigint order_id PK
    bigint cocktail_id PK
    int drinks
    int weight
  }

  ORDER_ITEM {
    bigint id PK
    bigint order_id FK
    bigint product_id FK
    int packs_to_buy
    string unit
  }

  SHOP ||--o{ USER : has
  USER ||--o{ ORDER : creates

  SHOP ||--o{ CATEGORY : defines
  CATEGORY ||--o{ PRODUCT : contains

  SHOP ||--o{ SHOP_PRODUCT : lists
  PRODUCT ||--o{ SHOP_PRODUCT : sold_as

  COCKTAIL ||--o{ COCKTAIL_INGREDIENT : has
  PRODUCT  ||--o{ COCKTAIL_INGREDIENT : used_in

  ORDER   ||--o{ ORDER_COCKTAIL : includes
  COCKTAIL||--o{ ORDER_COCKTAIL : selected

  ORDER   ||--o{ ORDER_ITEM : generates
  PRODUCT ||--o{ ORDER_ITEM : included_in
```

---

### Diagrama de arquitectura

```mermaid
flowchart LR

U[Usuario final]
A[Admin]
S[Dueño de tienda]

subgraph SYS[App: CocktailOps]
  FE[Web App React]
  BE[Backend API Spring Boot]
  DB[(PostgreSQL)]
  PDF[PDF Generator Thymeleaf + OpenHTMLtoPDF]
end

subgraph EXT[Integraciones opcional]
  SHOPAPI[API Tienda Shopify / WooCommerce / MercadoLibre]
end

U -->|Crea orden TIME o DRINKS| FE
A -->|Gestiona catálogo, cócteles, órdenes| FE
S -->|Carga links/precios por tienda| FE

FE -->|REST/JSON| BE
BE --> DB
BE --> PDF
BE -->|Sync opcional| SHOPAPI

class U,A,S actor
class FE,BE,PDF box
class DB db
class SHOPAPI ext

classDef actor fill:#ffffff,stroke:#444,stroke-width:1px
classDef box fill:#ffffff,stroke:#5a5a8a,stroke-width:1px
classDef db fill:#ffffff,stroke:#a06a00,stroke-width:1px
classDef ext fill:#ffffff,stroke:#1f7a3a,stroke-width:1px
```

---

### Diagrama de componentes

```mermaid
flowchart TB
subgraph API["API Layer"]
    C1["AuthController"]:::box
    C2["CatalogController<br/>(products + categories)"]:::box
    C3["CocktailController<br/>(cocktails + ingredients)"]:::box
    C4["OrderController<br/>(orders + pdf)"]:::box
    C5["ShopController<br/>(shops, shop_products)"]:::box
end

subgraph APP["Application/Service Layer"]
    S1["AuthService<br/>JWT + roles"]:::box
    S2["ProductService"]:::box
    S7["CategoryService"]:::box
    S3["CocktailService"]:::box
    S4["OrderService<br/>calcula packs + distribuye tragos"]:::box
    S5["PdfService<br/>genera PDF"]:::box
    S6["ShopService"]:::box
end

subgraph DOMAIN["Domain Model"]
    D7["Shop"]:::entity
    D6["User"]:::entity
    D9["Category"]:::entity
    D1["Product"]:::entity
    D8["ShopProduct"]:::entity

    D2["Cocktail"]:::entity
    D3["CocktailIngredient<br/>(amount + unit)"]:::entity

    D4["Order"]:::entity
    D10["OrderCocktail"]:::entity
    D5["OrderItem"]:::entity
end

subgraph INFRA["Infrastructure"]
    R1["Repositories (JPA)"]:::box
    FLY["Flyway Migrations"]:::box
    DB[("PostgreSQL")]:::db
end

C1 --> S1
C2 --> S2
C2 --> S7
C3 --> S3
C4 --> S4
C4 --> S5
C5 --> S6

S1 --> R1
S2 --> R1
S7 --> R1
S3 --> R1
S4 --> R1
S6 --> R1

R1 --> DB
FLY --> DB

classDef box fill:#000000,stroke:#5a5a8a,stroke-width:1px;
classDef db fill:#000000,stroke:#a06a00,stroke-width:1px;
classDef entity fill:#000000,stroke:#1f7a3a,stroke-width:1px;
```

---

## Estado actual del backend

- **Backend API**: listo
- **Cálculo de órdenes TIME / DRINKS**: listo
- **Generación de PDF**: listo
- **Swagger / documentación API**: listo, en mejora continua
- **Tests unitarios**: en progreso
- **Perfil de testing con H2**: listo
- **Docker Compose para PostgreSQL local**: listo
- **GitHub Actions CI**: listo
- **Spring Security + JWT**: listo en versión inicial
- **Autenticación de usuarios**: lista en versión inicial
- **Roles USER / ADMIN**: definidos
- **Autorización por roles**: lista en versión inicial
- **Órdenes asociadas al usuario autenticado**: listo
- **Historial de órdenes por usuario**: listo
- **PDF protegido por ownership**: listo

---

## Próximos pasos backend

- Agregar tests específicos para endpoints protegidos
- Aumentar cobertura de tests en services y controllers
- Mejorar manejo de respuestas 401/403
- Evaluar endpoint público de preview para usuarios anónimos sin guardar historial
- Revisar permisos finos si se agregan nuevos roles
- Dockerizar también la aplicación Spring Boot
- Mejorar documentación Swagger/Postman del flujo completo