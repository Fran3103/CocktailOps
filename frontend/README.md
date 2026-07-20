# CocktailOps Frontend

Web App para CocktailOps, desarrollada con React, Vite, TypeScript y Tailwind CSS.

Este módulo representa la interfaz visual del sistema y conecta el flujo principal del usuario con la API REST del backend: autenticación, catálogo de cócteles, catálogo de productos, creación de órdenes, historial propio, detalle de orden y descarga de PDFs.

---

## Índice

- [Descripción](#descripción)
- [Objetivo del frontend](#objetivo-del-frontend)
- [Reglas actuales del producto](#reglas-actuales-del-producto)
- [Decisiones técnicas](#decisiones-técnicas)
- [Características principales](#características-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Autenticación y acceso](#autenticación-y-acceso)
- [Instalación y uso](#instalación-y-uso)
- [Variables de entorno](#variables-de-entorno)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Rutas principales](#rutas-principales)
- [Integración con backend](#integración-con-backend)
- [Flujos principales](#flujos-principales)
- [Estado actual del frontend](#estado-actual-del-frontend)
- [Próximos pasos frontend](#próximos-pasos-frontend)
- [Autor](#autor)

---

## Descripción

CocktailOps Frontend permite interactuar visualmente con el sistema de planificación de pedidos de cócteles para eventos.

La aplicación está pensada para que un usuario pueda:

- consultar cócteles disponibles
- consultar productos disponibles
- crear una orden para un evento
- crear una orden por cantidad total de tragos
- obtener una lista calculada de productos e insumos necesarios
- ver el detalle de una orden generada
- descargar el PDF de una orden
- iniciar sesión o registrarse
- guardar órdenes asociadas a su usuario
- consultar su historial de órdenes

El frontend forma parte de una solución full stack orientada a portfolio, con foco en una experiencia clara, profesional y funcional.

---

## Objetivo del frontend

El objetivo principal de este módulo es convertir la API de CocktailOps en una aplicación web usable.

El frontend permite mostrar el flujo completo del producto:

```txt
Ver cócteles disponibles
→ Crear una orden
→ Calcular productos necesarios
→ Ver resumen de orden
→ Ver detalle de orden
→ Descargar PDF
```

Para usuarios autenticados, también permite:

```txt
Login
→ Crear y guardar orden
→ Consultar historial propio
→ Entrar al detalle de una orden anterior
→ Descargar PDF
```

La autenticación no busca bloquear todo el uso de la aplicación. Su objetivo principal es habilitar funcionalidades persistentes como historial, perfil y asociación de órdenes a una cuenta.

---

## Reglas actuales del producto

### Usuario invitado

Un usuario invitado puede:

- ver el catálogo de cócteles
- ver el catálogo de productos
- generar una orden
- ver el detalle inmediato de la orden generada
- descargar el PDF de la orden generada

Un usuario invitado no puede:

- acceder al historial
- conservar órdenes asociadas a una cuenta
- acceder al perfil

### Usuario registrado

Un usuario registrado puede:

- iniciar sesión
- crear órdenes asociadas a su cuenta
- ver su historial de órdenes
- entrar al detalle de órdenes propias
- descargar PDFs desde el detalle
- acceder a su perfil

### Administrador

El rol `ADMIN` queda previsto para funcionalidades de gestión futuras, como administración de productos, cócteles, categorías y recursos internos.

---

## Decisiones técnicas

### React + Vite

El frontend se construye con React y Vite para tener una base moderna, rápida y simple de mantener.

Vite permite:

- iniciar el proyecto con poca configuración
- tener un entorno de desarrollo rápido
- trabajar cómodamente con TypeScript
- preparar la aplicación para un futuro deploy

---

### TypeScript desde el inicio

El proyecto utiliza TypeScript para mejorar la seguridad y claridad del código.

Motivos:

- tipar respuestas del backend
- representar DTOs del sistema
- evitar errores comunes en props y estados
- mejorar el autocompletado del editor
- facilitar el mantenimiento a medida que crece el frontend

Archivos esperados:

```txt
.tsx → componentes y páginas React
.ts  → services, types, helpers y configuración
```

---

### Tailwind CSS

Tailwind CSS se utiliza para construir una interfaz moderna, responsive y consistente.

El diseño visual sigue una estética tipo dashboard SaaS:

- fondo oscuro
- tarjetas limpias
- bordes sutiles
- acentos dorados
- tablas legibles
- formularios claros
- sidebar responsive
- diseño profesional para portfolio

---

### Arquitectura por features

El frontend se organiza por dominio funcional.

Ejemplo:

```txt
features/orders
features/products
features/cocktails
features/auth
```

Esto evita mezclar toda la aplicación en una sola carpeta de componentes y facilita la evolución del proyecto.

---

### Servicios separados para API

La comunicación con el backend se centraliza en services.

Ejemplo:

```txt
authService.ts
productService.ts
cocktailService.ts
orderService.ts
```

Motivos:

- separar lógica de API de los componentes visuales
- reutilizar llamadas HTTP
- mantener componentes más limpios
- facilitar cambios futuros en endpoints

---

### Rutas públicas y privadas

La aplicación distingue entre rutas públicas y rutas protegidas.

Ejemplo:

- `/cocktails` es pública
- `/products` es pública
- `/orders/new` es pública
- `/orders/:id` es pública para permitir ver el detalle inmediato
- `/orders` es privada porque representa el historial personal
- `/profile` es privada

---

## Características principales

### Funcionalidades implementadas

- Layout principal tipo dashboard
- Sidebar responsive
- Login y registro de usuarios
- Manejo de JWT en frontend
- Persistencia de sesión en `localStorage`
- Rutas protegidas
- Guardas por autenticación
- Guardas por rol administrativo
- Catálogo de cócteles conectado al backend
- Listado de productos conectado al backend
- Creación de órdenes por evento
- Creación de órdenes por cantidad total de tragos
- Diferenciación entre usuario invitado y usuario registrado
- Historial de órdenes del usuario autenticado
- Detalle de orden
- Descarga de PDF por orden guardada
- Descarga de PDF preview para órdenes invitadas
- Estados de carga
- Estados de error
- Estados vacíos
- Feedback visual de orden creada
- Toast de éxito
- Navegación desde orden creada hacia detalle
- Navegación desde historial hacia detalle

---

## Tecnologías utilizadas

- React
- Vite
- TypeScript
- Tailwind CSS
- React Router
- Axios
- Lucide React
- ESLint
- npm

---

## Autenticación y acceso

El backend de CocktailOps utiliza autenticación con JWT.

El frontend consume los endpoints de autenticación:

```http
POST /auth/register
POST /auth/login
```

Luego, para requests protegidas, envía el token en el header:

```http
Authorization: Bearer <token>
```

El token y los datos básicos del usuario se guardan en `localStorage`.

---

### Tipos de usuario previstos

| Tipo de usuario | Acceso esperado |
|---|---|
| Invitado | Puede usar la funcionalidad principal, generar órdenes, ver detalle inmediato y descargar PDF preview |
| USER | Puede guardar órdenes, ver historial propio, entrar al detalle y descargar PDFs |
| ADMIN | Puede acceder a futuras funcionalidades administrativas |

---

### Funcionalidades según acceso

| Funcionalidad | Acceso |
|---|---|
| Login / Registro | Público |
| Dashboard | Público |
| Ver cócteles | Público |
| Ver productos | Público |
| Crear orden | Público |
| Ver detalle de orden | Público |
| Descargar PDF de orden invitada | Público mediante endpoint preview |
| Ver historial propio | Usuario autenticado |
| Ver perfil | Usuario autenticado |
| Descargar PDF de orden guardada | Usuario autenticado dueño / regla backend |
| Funcionalidades administrativas | ADMIN |

---

## Instalación y uso

### Requisitos previos

- Node.js
- npm
- Backend de CocktailOps corriendo localmente

---

### Clonar repositorio

Desde la carpeta donde quieras guardar el proyecto:

```bash
git clone https://github.com/Fran3103/CocktailOps.git
cd CocktailOps
```

---

### Instalar dependencias del frontend

Desde la carpeta del frontend:

```bash
cd frontend
npm install
```

---

### Ejecutar frontend en desarrollo

```bash
npm run dev
```

La aplicación debería quedar disponible en:

```txt
http://localhost:5173
```

---

### Ejecutar build

```bash
npm run build
```

---

### Previsualizar build

```bash
npm run preview
```

---

## Variables de entorno

Crear un archivo `.env.local` dentro de la carpeta `frontend/`.

Ejemplo:

```env
VITE_API_BASE_URL=http://localhost:8081
```

También se puede mantener un archivo `.env.example` con la misma clave para documentar la configuración necesaria:

```env
VITE_API_BASE_URL=http://localhost:8081
```

> Los archivos `.env` y `.env.local` no deben versionarse si contienen valores sensibles o configuraciones específicas del entorno local.

---

## Estructura del proyecto

Estructura actual/objetivo del frontend:

```txt
frontend/
├── src/
│   ├── app/
│   │   ├── App.tsx
│   │   └── router.tsx
│   │
│   ├── api/
│   │   └── apiClient.ts
│   │
│   ├── layouts/
│   │   ├── AppLayout.tsx
│   │   └── AuthLayout.tsx
│   │
│   ├── shared/
│   │   ├── components/
│   │   │   ├── feedback/
│   │   │   ├── navigation/
│   │   │   ├── placeholders/
│   │   │   └── ui/
│   │   ├── constants/
│   │   ├── hooks/
│   │   ├── types/
│   │   └── utils/
│   │
│   ├── features/
│   │   ├── auth/
│   │   ├── cocktails/
│   │   ├── dashboard/
│   │   ├── orders/
│   │   ├── products/
│   │   └── profile/
│   │
│   ├── assets/
│   ├── styles/
│   ├── main.tsx
│   └── index.css
│
├── public/
├── package.json
├── vite.config.ts
├── tsconfig.json
└── README.md
```

---

## Rutas principales

### Rutas públicas

```txt
/login
/register
/dashboard
/cocktails
/products
/orders/new
/orders/:id
/unauthorized
```

### Rutas privadas USER

```txt
/orders
/profile
```

### Rutas privadas ADMIN

```txt
Rutas administrativas futuras
```

> Las rutas administrativas se incorporarán progresivamente según avance el frontend.

---

## Integración con backend

El frontend se conecta con el backend mediante API REST.

### Autenticación

```txt
POST /auth/register
POST /auth/login
```

Flujo:

```txt
Login/Register
→ Backend devuelve JWT
→ Frontend guarda token y usuario
→ Requests protegidas envían Authorization Bearer
```

---

### Catálogo de cócteles

```http
GET /cocktails
```

Uso en frontend:

```txt
Frontend solicita cócteles
→ Backend responde catálogo
→ Frontend renderiza cards y buscador
```

---

### Catálogo de productos

```http
GET /products
```

Uso en frontend:

```txt
Frontend solicita productos
→ Backend responde catálogo
→ Frontend renderiza tabla y filtros
```

---

### Crear orden por evento

```http
POST /orders
```

Payload esperado:

```json
{
  "guests": 50,
  "durationHours": 5,
  "cocktails": [
    {
      "cocktailId": 1,
      "weight": 1
    }
  ]
}
```

Uso en frontend:

```txt
Usuario completa invitados, duración y cócteles
→ Frontend envía payload
→ Backend calcula cantidad de tragos, ingredientes y packs
→ Frontend muestra resumen de orden creada
```

---

### Crear orden por cantidad de tragos

```http
POST /orders/by-drinks
```

Payload esperado:

```json
{
  "totalDrinks": 100,
  "cocktails": [
    {
      "cocktailId": 1,
      "quantity": 50
    },
    {
      "cocktailId": 2,
      "quantity": 50
    }
  ]
}
```

Uso en frontend:

```txt
Usuario define total de tragos
→ Selecciona cócteles
→ Asigna cantidades manualmente o divide equitativamente
→ Frontend envía payload
→ Backend calcula productos y packs
→ Frontend muestra resumen de orden creada
```

---

### Historial

```http
GET /orders/my-orders
```

Uso en frontend:

```txt
Usuario autenticado ingresa a Historial
→ Frontend solicita /orders/my-orders
→ Backend devuelve solo órdenes asociadas al usuario autenticado
→ Frontend muestra tabla/listado con acceso al detalle
```

---

### Detalle de orden

```http
GET /orders/{id}
```

Uso en frontend:

```txt
Frontend obtiene o recibe una orden
→ Renderiza modo, estado, fecha, tragos, cócteles calculados y productos
```

---

### PDF de orden guardada

```http
GET /orders/{id}/pdf
```

Uso en frontend:

```txt
Usuario autenticado solicita descarga desde el detalle
→ Frontend llama /orders/{id}/pdf
→ Backend genera PDF de la orden guardada
→ Frontend descarga el archivo
```

---

### PDF preview para orden invitada por evento

```http
POST /orders/preview/pdf
```

Payload esperado:

```json
{
  "guests": 50,
  "durationHours": 5,
  "cocktails": [
    {
      "cocktailId": 1,
      "weight": 1
    }
  ]
}
```

Uso en frontend:

```txt
Invitado genera orden por evento
→ Frontend conserva temporalmente el payload original
→ Invitado entra al detalle inmediato
→ Frontend envía el payload a /orders/preview/pdf
→ Backend genera PDF preview
→ Frontend descarga order-preview.pdf
```

---

### PDF preview para orden invitada por cantidad de tragos

```http
POST /orders/by-drinks/preview/pdf
```

Payload esperado:

```json
{
  "totalDrinks": 100,
  "cocktails": [
    {
      "cocktailId": 1,
      "quantity": 50
    },
    {
      "cocktailId": 2,
      "quantity": 50
    }
  ]
}
```

Uso en frontend:

```txt
Invitado genera orden por cantidad de tragos
→ Frontend conserva temporalmente el payload original
→ Invitado entra al detalle inmediato
→ Frontend envía el payload a /orders/by-drinks/preview/pdf
→ Backend genera PDF preview
→ Frontend descarga order-preview.pdf
```

---

## Flujos principales

### Flujo invitado

```txt
Entrar a la app
→ Ver cócteles/productos
→ Crear orden
→ Ver resumen de orden creada
→ Ver detalle
→ Descargar PDF preview
```

Limitación actual:

```txt
Si el invitado refresca el detalle o entra directo a /orders/:id,
el frontend puede perder el payload original necesario para generar el PDF preview.
```

Posible mejora futura:

```txt
Guardar temporalmente el payload preview en sessionStorage.
```

---

### Flujo usuario registrado

```txt
Login
→ Crear orden
→ Orden asociada al usuario
→ Ver detalle
→ Descargar PDF
→ Ir a Historial
→ Ver órdenes anteriores
→ Entrar al detalle de una orden anterior
```

---

### Flujo historial

```txt
Usuario autenticado
→ /orders
→ GET /orders/my-orders
→ Tabla de órdenes
→ Ver detalle
```

---

## Estado actual del frontend

| Módulo | Estado |
|---|---|
| Proyecto React + Vite | Implementado |
| TypeScript | Implementado |
| Tailwind CSS | Implementado |
| React Router | Implementado |
| Axios | Implementado |
| Lucide React | Implementado |
| Layout público / privado | Implementado |
| Sidebar responsive | Implementado |
| Rutas base | Implementado |
| Login / Register | Implementado |
| Manejo de JWT | Implementado |
| AuthContext | Implementado |
| ProtectedRoute | Implementado |
| AdminRoute | Implementado |
| Catálogo de cócteles | Implementado |
| Productos | Implementado |
| Crear orden modo evento | Implementado |
| Crear orden modo cantidad de tragos | Implementado |
| Feedback de orden creada | Implementado |
| Toast de éxito | Implementado |
| Historial de órdenes | Implementado |
| Detalle de orden | Implementado |
| Descarga de PDF por ID | Implementado |
| Descarga de PDF preview invitado | Implementado |
| Deploy frontend | Pendiente |
| Testing frontend | Pendiente |
| Funcionalidades admin | Pendiente |
| Persistencia temporal de payload preview | Pendiente |

---

## Próximos pasos frontend

### Próximas mejoras razonables

- Mejorar persistencia temporal del payload preview en `sessionStorage`
- Agregar tests frontend básicos
- Mejorar estados de error específicos según status HTTP
- Agregar páginas administrativas para productos/cócteles/categorías
- Mejorar dashboard inicial con métricas reales
- Agregar deploy frontend
- Documentar casos QA manuales del flujo principal
- Preparar capturas para portfolio/LinkedIn

### Roadmap futuro de producto

- Links de compra por producto
- Lista de compra compartible
- Opciones de compra desde una orden
- Registro de tiendas/proveedores
- Catálogo de productos por tienda
- Integración futura con tiendas externas o marketplaces

---

## Autor

Proyecto desarrollado por Franco Aguirre como parte de su formación y portfolio profesional en desarrollo backend/full stack.

Stack principal de estudio:

- Java
- Spring Boot
- PostgreSQL
- React
- TypeScript
- Testing
- QA Manual