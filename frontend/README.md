# CocktailOps Frontend

Web App para CocktailOps, desarrollada con **React**, **Vite**, **TypeScript** y **Tailwind CSS**.

Este módulo representa la interfaz visual del sistema CocktailOps y conecta el flujo principal del usuario con la API REST del backend: autenticación, dashboard por rol, catálogo de cócteles, catálogo de productos, creación de órdenes, listas rápidas predefinidas, historial, detalle de orden, descarga de PDFs y estados visuales de error.

---

## Índice

- [Descripción](#descripción)
- [Objetivo del frontend](#objetivo-del-frontend)
- [Estado actual](#estado-actual)
- [Reglas actuales del producto](#reglas-actuales-del-producto)
- [Decisiones técnicas](#decisiones-técnicas)
- [Características principales](#características-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Autenticación y acceso](#autenticación-y-acceso)
- [Instalación y uso](#instalación-y-uso)
- [Variables de entorno](#variables-de-entorno)
- [Deploy](#deploy)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Rutas principales](#rutas-principales)
- [Integración con backend](#integración-con-backend)
- [Flujos principales](#flujos-principales)
- [Listas rápidas predefinidas](#listas-rápidas-predefinidas)
- [Prioridades de cócteles](#prioridades-de-cócteles)
- [Notas sobre el cálculo](#notas-sobre-el-cálculo)
- [Manejo de errores](#manejo-de-errores)
- [Estado de módulos](#estado-de-módulos)
- [Próximos pasos frontend](#próximos-pasos-frontend)
- [Autor](#autor)

---

## Descripción

CocktailOps Frontend permite interactuar visualmente con un sistema de planificación de órdenes de cócteles para eventos.

La aplicación está pensada para que un usuario pueda:

- consultar cócteles disponibles;
- consultar productos e insumos disponibles;
- crear una orden para un evento según invitados y duración;
- crear una orden por cantidad total de tragos;
- usar listas rápidas predefinidas según el tipo de evento;
- ajustar manualmente cócteles, prioridades o cantidades;
- obtener una lista calculada de productos e insumos necesarios;
- ver el resumen de una orden generada;
- descargar el PDF de una orden temporal o guardada;
- iniciar sesión o registrarse;
- guardar órdenes asociadas a su usuario;
- consultar su historial de órdenes;
- entrar al detalle de una orden guardada;
- ver un dashboard distinto según el tipo de acceso;
- visualizar estados claros de carga, vacío, error, 403 y 404.

El frontend forma parte de una solución full stack orientada a portfolio, con foco en una experiencia clara, profesional y funcional.

---

## Objetivo del frontend

El objetivo principal de este módulo es convertir la API de CocktailOps en una aplicación web usable.

Flujo general del producto:

```txt
Ver dashboard
→ Consultar catálogo de cócteles/productos
→ Crear una orden
→ Elegir cócteles manualmente o usar una lista rápida
→ Ajustar prioridades o cantidades
→ Ver resumen previo
→ Calcular productos necesarios
→ Ver resumen de orden generada
→ Descargar PDF
```

Para usuarios autenticados:

```txt
Login
→ Crear y guardar orden
→ Consultar dashboard personalizado
→ Consultar historial propio
→ Entrar al detalle de una orden anterior
→ Descargar PDF de una orden guardada
```

Para usuarios administradores:

```txt
Login ADMIN
→ Ver dashboard administrativo
→ Consultar métricas generales de órdenes
→ Ver últimas órdenes del sistema
→ Acceder al detalle de órdenes guardadas
→ Descargar PDFs según permisos del backend
```

La autenticación no bloquea el uso principal de la aplicación. Su objetivo es habilitar funcionalidades persistentes como historial, perfil y asociación de órdenes a una cuenta.

---

## Estado actual

El frontend se encuentra en estado funcional para portfolio.

Actualmente incluye:

- deploy en Vercel;
- conexión con backend productivo mediante proxy `/api`;
- login y registro;
- dashboards por rol;
- catálogo de cócteles;
- catálogo de productos;
- creación de órdenes por evento;
- creación de órdenes por cantidad total de tragos;
- uso de listas rápidas predefinidas;
- prioridades visuales para cócteles en modo evento;
- órdenes temporales para invitados;
- órdenes guardadas para usuarios autenticados;
- historial de órdenes;
- detalle de orden;
- descarga de PDF guardado y PDF preview;
- sidebar responsive;
- tablas adaptadas para mobile/desktop;
- estados visuales de error reutilizables;
- pantalla 403;
- pantalla 404;
- manejo específico de errores HTTP;
- build y lint funcionales.

---

## Reglas actuales del producto

### Usuario invitado

Un usuario invitado puede:

- ver el dashboard público;
- ver el catálogo de cócteles;
- ver el catálogo de productos;
- generar una orden temporal;
- usar listas rápidas predefinidas;
- ver el resumen inmediato de la orden generada;
- descargar el PDF de la orden generada.

Un usuario invitado no puede:

- acceder al historial;
- acceder al perfil;
- conservar órdenes asociadas a una cuenta;
- recuperar una orden temporal después de salir del flujo.

Regla actual:

```txt
Una orden invitada es temporal.
No se guarda en base de datos.
No tiene ID.
No tiene historial.
El PDF debe descargarse desde el resumen inmediato.
```

En frontend, una orden se considera temporal cuando:

```ts
order.id == null
```

---

### Usuario registrado

Un usuario registrado puede:

- iniciar sesión;
- crear órdenes asociadas a su cuenta;
- usar listas rápidas predefinidas;
- guardar órdenes reales en backend;
- ver su dashboard personal;
- ver su historial de órdenes;
- entrar al detalle de órdenes propias;
- descargar PDFs desde el detalle o desde historial;
- acceder a su perfil.

---

### Administrador

Un usuario con rol `ADMIN` puede:

- ver el dashboard administrativo;
- consultar métricas generales de órdenes guardadas;
- ver últimas órdenes del sistema;
- ver columna de usuario en órdenes recientes;
- acceder al detalle de órdenes guardadas según reglas del backend;
- descargar PDFs de órdenes guardadas según permisos del backend.

Las páginas administrativas CRUD para productos, cócteles y categorías todavía no están implementadas en frontend.

---

### Órdenes por evento

En modo evento, el usuario completa:

- cantidad de invitados;
- duración del evento;
- cócteles seleccionados;
- prioridad de cada cóctel.

El frontend muestra prioridades en palabras y envía pesos numéricos al backend.

```txt
Prioridad baja   → weight 1
Prioridad normal → weight 2
Prioridad media  → weight 3
Prioridad alta   → weight 5
```

La regla anterior de aumentar automáticamente tragos por persona/hora según la cantidad de cócteles seleccionados fue removida de la UI. Las listas predefinidas ya no muestran etiquetas como `1 pax/hora` o `2 pax/hora`.

---

### Órdenes por cantidad total de tragos

En modo cantidad de tragos, el usuario completa:

- total de tragos;
- cócteles seleccionados;
- cantidad asignada a cada cóctel.

También puede usar una lista rápida predefinida. Si existe un total válido, el frontend puede distribuir cantidades según los pesos internos del preset.

---

## Decisiones técnicas

### React + Vite

El frontend se construye con React y Vite para tener una base moderna, rápida y simple de mantener.

Vite permite:

- iniciar el proyecto con poca configuración;
- tener un entorno de desarrollo rápido;
- trabajar cómodamente con TypeScript;
- preparar la aplicación para deploy.

---

### TypeScript desde el inicio

El proyecto utiliza TypeScript para mejorar la seguridad y claridad del código.

Motivos:

- tipar respuestas del backend;
- representar DTOs del sistema;
- evitar errores comunes en props y estados;
- mejorar el autocompletado del editor;
- facilitar el mantenimiento a medida que crece el frontend.

Archivos esperados:

```txt
.tsx → componentes y páginas React
.ts  → services, types, helpers y configuración
```

---

### Tailwind CSS

Tailwind CSS se utiliza para construir una interfaz moderna, responsive y consistente.

El diseño visual sigue una estética tipo dashboard SaaS:

- fondo oscuro;
- tarjetas limpias;
- bordes sutiles;
- acentos dorados;
- tablas legibles;
- formularios claros;
- sidebar responsive;
- feedback visual de acciones importantes;
- estados visuales de error;
- diseño profesional para portfolio.

---

### Arquitectura por features

El frontend se organiza por dominio funcional.

```txt
features/auth
features/cocktails
features/dashboard
features/orders
features/products
features/profiles
```

Esto evita mezclar toda la aplicación en una sola carpeta de componentes y facilita la evolución del proyecto.

---

### Servicios separados para API

La comunicación con el backend se centraliza en services.

```txt
authService.ts
cocktailService.ts
productService.ts
orderService.ts
```

Motivos:

- separar lógica HTTP de componentes visuales;
- reutilizar llamadas a API;
- mantener componentes más limpios;
- facilitar cambios futuros en endpoints.

---

### Hooks y helpers para creación de órdenes

El flujo de creación de órdenes fue dividido en componentes, hooks y helpers para evitar que `CreateOrderPage` concentre toda la lógica.

Ejemplos:

```txt
hooks/useOrderCocktailsCatalog.ts
hooks/useOrderCocktailSelection.ts
hooks/useOrderPayloads.ts
hooks/useOrderCreationState.ts
hooks/useCreateOrder.ts
orderPresetUtils.ts
orderPriority.ts
```

Estos archivos separan responsabilidades como:

- carga del catálogo de cócteles;
- selección manual o por presets;
- distribución por pesos internos;
- construcción de payloads;
- creación de orden temporal o guardada;
- limpieza de estados;
- conversión entre prioridad visual y `weight`.

---

### Rutas públicas y privadas

La aplicación distingue entre rutas públicas y rutas protegidas.

Rutas públicas:

- `/dashboard`
- `/cocktails`
- `/products`
- `/create-order`
- `/login`
- `/register`
- `/unauthorized`
- `/404`

Rutas privadas:

- `/orders`
- `/orders/:id`
- `/profile`

El detalle de orden está protegido porque representa una orden guardada. Las órdenes temporales de invitados no navegan a `/orders/:id`; se muestran en el resumen inmediato y permiten descargar PDF preview desde ahí.

---

### Responsive design

El frontend fue adaptado para uso desktop y mobile.

Se trabajó especialmente en:

- sidebar responsive;
- prevención de scroll de fondo cuando el menú mobile está abierto;
- cards adaptadas a mobile;
- tablas que se convierten en cards en pantallas chicas;
- layouts con ancho máximo más estable;
- flujo de creación de orden en pasos.

---

## Características principales

### Funcionalidades implementadas

- Layout principal tipo dashboard.
- Sidebar responsive.
- Login y registro de usuarios.
- Mostrar/ocultar contraseña en formularios de autenticación.
- Manejo de JWT en frontend.
- Persistencia de sesión en `localStorage`.
- Rutas protegidas.
- Guardas por autenticación.
- Guardas por rol administrativo.
- Página de acceso no autorizado `403`.
- Página de no encontrado `404`.
- Dashboard público para invitados.
- Dashboard personal para usuarios registrados.
- Dashboard administrativo para rol `ADMIN`.
- Métricas de órdenes propias.
- Métricas generales de órdenes del sistema para admin.
- Tabla de últimas órdenes.
- Catálogo de cócteles conectado al backend.
- Buscador de cócteles.
- Filtros por tipo de preparación.
- Paginación del catálogo de cócteles.
- Listado de productos conectado al backend.
- Filtro/buscador de productos.
- Creación de órdenes por evento.
- Creación de órdenes por cantidad total de tragos.
- Flujo de creación de orden en pasos.
- Listas rápidas predefinidas de cócteles.
- Carga automática de cócteles desde presets.
- Prioridades visuales para cócteles en modo evento.
- Distribución de cantidades por peso en modo tragos.
- Edición manual posterior al preset.
- Diferenciación entre usuario invitado y usuario registrado.
- Historial de órdenes del usuario autenticado.
- Vista de historial propia y general para admin.
- Detalle de orden guardada.
- Descarga de PDF por orden guardada.
- Descarga de PDF preview para órdenes invitadas.
- Nota visual sobre cálculo conservador de compra.
- Estados de carga.
- Estados de error reutilizables.
- Estados vacíos.
- Feedback visual de orden creada.
- Toast de éxito.
- Navegación desde orden creada hacia detalle cuando la orden tiene ID.
- Prevención de navegación al detalle para órdenes temporales.
- Manejo específico de errores HTTP en descarga de PDF.
- Tablas responsive para historial, dashboard, productos y detalle de orden.

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

### Tipos de usuario

| Tipo de usuario | Acceso esperado |
|---|---|
| Invitado | Puede usar la funcionalidad principal, generar órdenes temporales y descargar PDF preview |
| USER | Puede guardar órdenes, ver dashboard personal, historial propio, detalle y descargar PDFs |
| ADMIN | Puede ver dashboard administrativo y consultar órdenes del sistema según reglas del backend |

---

### Funcionalidades según acceso

| Funcionalidad | Acceso |
|---|---|
| Login / Registro | Público |
| Dashboard invitado | Público |
| Ver cócteles | Público |
| Ver productos | Público |
| Crear orden temporal | Público |
| Descargar PDF preview | Público desde resumen inmediato |
| Crear orden guardada | Usuario autenticado |
| Ver historial propio | Usuario autenticado |
| Ver detalle de orden guardada | Usuario autenticado según permisos backend |
| Ver perfil | Usuario autenticado |
| Descargar PDF de orden guardada | Usuario autenticado dueño / ADMIN según regla backend |
| Dashboard administrativo | ADMIN |
| Métricas generales de órdenes | ADMIN |
| Historial general de órdenes | ADMIN |
| Pantalla 403 | Público dentro del layout |
| Pantalla 404 | Público dentro del layout |

---

## Instalación y uso

### Requisitos previos

- Node.js 22 o superior recomendado.
- npm.
- Backend de CocktailOps corriendo localmente o backend productivo accesible.
- Base de datos configurada en backend.

---

### Clonar repositorio

```bash
git clone https://github.com/Fran3103/CocktailOps.git
cd CocktailOps
```

---

### Instalar dependencias del frontend

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

### Ejecutar lint

```bash
npm run lint
```

---

### Previsualizar build

```bash
npm run preview
```

---

## Variables de entorno

Crear un archivo `.env.local` dentro de la carpeta `frontend/`.

Ejemplo para desarrollo local:

```env
VITE_API_BASE_URL=http://localhost:8080
```

Cuando se usa el frontend desplegado en Vercel con rewrite interno hacia el backend productivo, la variable puede quedar así:

```env
VITE_API_BASE_URL=/api
```

> Los archivos `.env` y `.env.local` no deben versionarse si contienen valores sensibles o configuraciones específicas del entorno local.

---

## Deploy

El frontend se encuentra desplegado en Vercel.

```txt
https://cocktailops.vercel.app
```

### Estrategia de conexión con backend productivo

En producción, el frontend usa `/api` como base URL y Vercel reescribe esas requests hacia el backend productivo.

Ejemplo conceptual:

```json
{
  "rewrites": [
    {
      "source": "/api/:path*",
      "destination": "http://<BACKEND_HOST>:8080/:path*"
    },
    {
      "source": "/:path*",
      "destination": "/index.html"
    }
  ]
}
```

Esto permite que el frontend llame a la API mediante rutas relativas:

```txt
/api/cocktails
/api/products
/api/orders
/api/auth/login
```

También permite mantener funcionando las rutas internas de React Router al refrescar el navegador.

---

## Estructura del proyecto

Estructura actual del frontend:

```txt
frontend/
├── src/
│   ├── api/
│   │   └── apiClient.ts
│   │
│   ├── app/
│   │   ├── App.tsx
│   │   └── router.tsx
│   │
│   ├── assets/
│   │
│   ├── features/
│   │   ├── auth/
│   │   │   ├── AdminRoute.tsx
│   │   │   ├── AuthContext.ts
│   │   │   ├── AuthProvider.tsx
│   │   │   ├── LoginPage.tsx
│   │   │   ├── ProtectedRoute.tsx
│   │   │   ├── RegisterPage.tsx
│   │   │   ├── UnauthorizedPage.tsx
│   │   │   ├── auth.types.ts
│   │   │   ├── authService.ts
│   │   │   ├── authStorage.ts
│   │   │   └── useAuth.ts
│   │   │
│   │   ├── cocktails/
│   │   │   ├── CocktailsPage.tsx
│   │   │   ├── cocktail.types.ts
│   │   │   ├── cocktailService.ts
│   │   │   └── components/
│   │   │
│   │   ├── dashboard/
│   │   │   ├── DashboardPage.tsx
│   │   │   └── components/
│   │   │       ├── AdminDashboard.tsx
│   │   │       ├── CocktailsPreview.tsx
│   │   │       ├── DashboardMetricCard.tsx
│   │   │       ├── GuestDashboard.tsx
│   │   │       ├── RecentOrdersTable.tsx
│   │   │       └── UserDashboard.tsx
│   │   │
│   │   ├── orders/
│   │   │   ├── CreateOrderPage.tsx
│   │   │   ├── OrderDetailPage.tsx
│   │   │   ├── OrderHistoryPage.tsx
│   │   │   ├── hooks/
│   │   │   ├── components/
│   │   │   ├── order.types.ts
│   │   │   ├── orderPresets.ts
│   │   │   ├── orderPresetUtils.ts
│   │   │   ├── orderPriority.ts
│   │   │   └── orderService.ts
│   │   │
│   │   ├── products/
│   │   │   ├── ProductsPage.tsx
│   │   │   ├── product.types.ts
│   │   │   ├── productService.ts
│   │   │   └── components/
│   │   │
│   │   └── profiles/
│   │       └── ProfilePage.tsx
│   │
│   ├── layouts/
│   │   ├── AppLayout.tsx
│   │   └── AuthLayout.tsx
│   │
│   ├── shared/
│   │   ├── components/
│   │   │   ├── navigation/
│   │   │   ├── placeholders/
│   │   │   └── ui/
│   │   │       ├── Button.tsx
│   │   │       ├── Card.tsx
│   │   │       ├── ErrorState.tsx
│   │   │       ├── Input.tsx
│   │   │       ├── PageHeader.tsx
│   │   │       └── PasswordInput.tsx
│   │   ├── constants/
│   │   ├── hooks/
│   │   ├── pages/
│   │   │   └── NotFoundPage.tsx
│   │   ├── types/
│   │   └── utils/
│   │       └── getApiErrorMessage.ts
│   │
│   ├── styles/
│   ├── index.css
│   ├── main.tsx
│   └── vite-env.d.ts
│
├── public/
├── package.json
├── vite.config.ts
├── vercel.json
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
/create-order
/unauthorized
/404
```

### Rutas privadas USER

```txt
/orders
/orders/:id
/profile
```

### Rutas privadas ADMIN

```txt
/dashboard
/orders
/orders/:id
/profile
```

El dashboard administrativo se resuelve por rol dentro de `/dashboard`. Si el usuario autenticado tiene rol `ADMIN`, se muestra el dashboard administrativo.

Actualmente no hay rutas CRUD administrativas separadas para productos, cócteles o categorías.

Las rutas inexistentes muestran la página 404.

---

## Integración con backend

El frontend se conecta con el backend mediante API REST.

La URL base se configura con:

```env
VITE_API_BASE_URL=http://localhost:8080
```

o, en deploy:

```env
VITE_API_BASE_URL=/api
```

El cliente HTTP se centraliza en:

```txt
src/api/apiClient.ts
```

Este cliente agrega automáticamente el token JWT en requests protegidas cuando existe sesión activa.

---

### Autenticación

```http
POST /auth/register
POST /auth/login
```

Flujo:

```txt
Login/Register
→ Backend devuelve JWT + datos de usuario
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
→ Frontend ordena/filtra/renderiza cards, buscador, preview y selector de órdenes
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
→ Frontend renderiza tabla/cards responsive y filtros
```

---

### Crear orden por evento guardada

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
      "weight": 2
    }
  ]
}
```

Uso en frontend:

```txt
Usuario autenticado completa invitados, duración y cócteles
→ Frontend muestra prioridad en palabras
→ Frontend envía weight numérico al backend
→ Backend calcula cantidad de tragos, ingredientes y packs
→ Backend guarda la orden
→ Frontend muestra resumen
→ Usuario puede ver detalle o descargar PDF
```

---

### Crear orden temporal por evento

```http
POST /orders/preview
```

Uso en frontend:

```txt
Invitado completa invitados, duración y cócteles
→ Frontend muestra prioridad en palabras
→ Frontend envía weight numérico al backend
→ Backend calcula sin guardar en base de datos
→ Frontend muestra resumen temporal
→ Invitado puede descargar PDF preview
```

---

### Crear orden por cantidad de tragos guardada

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
Usuario autenticado define total de tragos
→ Selecciona cócteles
→ Asigna cantidades manualmente, divide equitativamente o usa preset
→ Frontend envía payload
→ Backend calcula productos y packs
→ Backend guarda la orden
→ Frontend muestra resumen
```

---

### Crear orden temporal por cantidad de tragos

```http
POST /orders/by-drinks/preview
```

Uso en frontend:

```txt
Invitado define total de tragos
→ Selecciona cócteles
→ Asigna cantidades manualmente, divide equitativamente o usa preset
→ Frontend envía payload preview
→ Backend calcula sin guardar
→ Frontend muestra resumen temporal
→ Invitado puede descargar PDF preview
```

---

### Historial propio

```http
GET /orders/my-orders
```

Uso en frontend:

```txt
Usuario autenticado ingresa a Historial
→ Frontend solicita /orders/my-orders
→ Backend devuelve solo órdenes asociadas al usuario autenticado
→ Frontend muestra tabla/card responsive con acceso al detalle y PDF
```

---

### Órdenes del sistema para admin

```http
GET /orders
```

Uso en frontend:

```txt
Usuario ADMIN entra al dashboard o historial
→ Frontend solicita /orders
→ Backend devuelve órdenes del sistema según permisos
→ Frontend muestra métricas, últimas órdenes o historial general
```

---

### Detalle de orden guardada

```http
GET /orders/{id}
```

Uso en frontend:

```txt
Usuario autenticado entra al detalle
→ Frontend solicita /orders/{id}
→ Backend valida permisos
→ Frontend renderiza modo, estado, fecha, tragos, cócteles calculados y productos
```

---

### PDF de orden guardada

```http
GET /orders/{id}/pdf
```

Uso en frontend:

```txt
Usuario autenticado solicita descarga
→ Frontend llama /orders/{id}/pdf
→ Backend genera PDF de la orden guardada
→ Frontend descarga order-{id}.pdf
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
      "weight": 2
    }
  ]
}
```

Uso en frontend:

```txt
Invitado genera orden por evento
→ Frontend conserva temporalmente el payload original
→ Invitado descarga PDF desde el resumen inmediato
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
→ Invitado descarga PDF desde el resumen inmediato
→ Frontend envía el payload a /orders/by-drinks/preview/pdf
→ Backend genera PDF preview
→ Frontend descarga order-preview.pdf
```

---

## Flujos principales

### Flujo invitado

```txt
Entrar a la app
→ Ver dashboard público
→ Ver cócteles/productos
→ Crear orden
→ Completar datos
→ Elegir lista rápida o seleccionar cócteles manualmente
→ Ajustar prioridades o cantidades
→ Ver resumen previo
→ Generar orden temporal
→ Descargar PDF preview
```

Limitación actual:

```txt
La orden invitada no se guarda.
Si el usuario abandona el flujo o refresca fuera del resumen inmediato,
puede perder el payload necesario para regenerar el PDF preview.
```

Posible mejora futura:

```txt
Guardar temporalmente el payload preview en sessionStorage.
```

---

### Flujo usuario registrado

```txt
Login
→ Dashboard personal
→ Crear orden
→ Elegir lista rápida o seleccionar cócteles manualmente
→ Ajustar prioridades o cantidades
→ Orden asociada al usuario
→ Ver detalle
→ Descargar PDF
→ Ir a Historial
→ Ver órdenes anteriores
→ Entrar al detalle de una orden anterior
```

---

### Flujo administrador

```txt
Login ADMIN
→ Dashboard admin
→ Ver métricas generales
→ Ver últimas órdenes del sistema
→ Ir a historial
→ Alternar entre todas las órdenes y mis órdenes
→ Acceder a detalle de órdenes guardadas
→ Descargar PDF según permisos backend
```

---

### Flujo historial

```txt
Usuario autenticado
→ /orders
→ GET /orders/my-orders o GET /orders según rol/vista
→ Tabla/cards de órdenes
→ Ver detalle
→ Descargar PDF
```

---

## Listas rápidas predefinidas

El frontend incluye presets de cócteles para acelerar la creación de órdenes.

Archivo principal:

```txt
src/features/orders/orderPresets.ts
```

Helper asociado:

```txt
src/features/orders/orderPresetUtils.ts
```

Componente visual:

```txt
src/features/orders/components/OrderPresetSelector.tsx
```

Los presets actuales incluyen:

- Clásicos simples
- Clásicos completos
- Boda / evento elegante
- Modernos y fiesta
- Verano / tropical
- Aperitivos
- Premium clásico
- Popular y rápido

Cada preset contiene:

```ts
{
  cocktailName: string;
  weight: number;
}
```

El frontend no guarda IDs fijos de cócteles en los presets. En su lugar, busca el cóctel por nombre dentro del catálogo real que viene desde backend.

Esto evita depender de IDs de base de datos y permite que los presets funcionen mientras los nombres coincidan con el catálogo real.

---

### Reglas de presets

En modo por evento:

```txt
El preset carga cócteles y pesos internos.
El usuario ve prioridades en palabras.
El backend recibe weight numérico para calcular la distribución.
```

En modo por cantidad total de tragos:

```txt
El preset carga cócteles.
Si ya existe un total de tragos válido, el frontend distribuye cantidades según peso.
El usuario puede ajustar cantidades manualmente después.
```

Si el usuario modifica manualmente la selección:

```txt
Agregar cóctel
Editar prioridad
Editar cantidad
Eliminar cóctel
Dividir equitativamente
```

el preset deja de marcarse como activo.

Las listas predefinidas no muestran reglas de `1 pax/hora` ni `2 pax/hora`.

---

## Prioridades de cócteles

Para que el usuario no tenga que entender el concepto técnico de `weight`, el frontend muestra prioridades en palabras.

Archivo principal:

```txt
src/features/orders/orderPriority.ts
```

Mapeo actual:

| Prioridad visible | Valor enviado al backend |
|---|---:|
| Baja | 1 |
| Normal | 2 |
| Media | 3 |
| Alta | 5 |

Motivo:

- `weight` sigue existiendo como dato interno.
- El usuario ve una opción clara y entendible.
- No se usa `0` porque eso equivaldría a excluir el cóctel del cálculo.
- Para excluir un cóctel, el usuario debe quitarlo de la selección.
- `Normal` no usa `1` para diferenciarse de `Baja`.

Ejemplo de payload real enviado al backend:

```json
{
  "guests": 80,
  "durationHours": 5,
  "cocktails": [
    {
      "cocktailId": 1,
      "weight": 5
    },
    {
      "cocktailId": 2,
      "weight": 2
    },
    {
      "cocktailId": 3,
      "weight": 1
    }
  ]
}
```

En el resumen final, el frontend muestra:

```txt
Prioridad alta
Prioridad normal
Prioridad baja
```

y no muestra:

```txt
Peso 5
Peso 2
Peso 1
```

---

## Notas sobre el cálculo

El frontend muestra una aclaración visual sobre el cálculo de productos a comprar.

La idea del cálculo actual es ser conservador:

```txt
El sistema calcula insumos suficientes para preparar la cantidad estimada de tragos.
Cuando un producto se compra en unidades comerciales completas, como botellas o packs,
puede redondear hacia arriba para asegurar disponibilidad.
```

Esto significa que:

- puede quedar sobrante de algunos insumos;
- no descuenta stock previo;
- no optimiza compras mínimas;
- prioriza que alcance para preparar la cantidad calculada.

Componente:

```txt
src/features/orders/components/CalculationNotice.tsx
```

Esta aclaración debe mantenerse alineada con la nota incluida en el PDF generado por backend.

---

## Manejo de errores

El frontend incluye manejo visual de errores mediante componentes reutilizables.

Archivos principales:

```txt
src/shared/utils/getApiErrorMessage.ts
src/shared/components/ui/ErrorState.tsx
src/shared/pages/NotFoundPage.tsx
src/features/auth/UnauthorizedPage.tsx
```

Errores contemplados:

| Caso | Resultado visual |
|---|---|
| 400 | Orden inválida, ID inválido o datos incorrectos |
| 401 | Sesión no activa o vencida |
| 403 | Acceso denegado |
| 404 | Página o recurso no encontrado |
| 500 | Error del servidor |
| Sin respuesta | Error de conexión o backend no disponible |

Páginas y módulos que usan estados visuales de error:

- Dashboard USER
- Dashboard ADMIN
- Catálogo de cócteles
- Catálogo de productos
- Historial de órdenes
- Detalle de orden
- Descarga de PDF
- 403 Unauthorized
- 404 Not Found

---

## Estado de módulos

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
| Ruta 404 | Implementado |
| Login / Register | Implementado |
| Mostrar/ocultar contraseña | Implementado |
| Manejo de JWT | Implementado |
| AuthContext | Implementado |
| ProtectedRoute | Implementado |
| AdminRoute | Implementado |
| UnauthorizedPage | Implementado |
| Dashboard invitado | Implementado |
| Dashboard USER | Implementado |
| Dashboard ADMIN | Implementado |
| Métricas por rol | Implementado |
| Tabla de últimas órdenes | Implementado |
| Tablas/cards responsive | Implementado |
| Catálogo de cócteles | Implementado |
| Buscador de cócteles | Implementado |
| Filtros por preparación | Implementado |
| Paginación de cócteles | Implementado |
| Productos | Implementado |
| Filtro de productos | Implementado |
| Crear orden modo evento | Implementado |
| Crear orden modo cantidad de tragos | Implementado |
| Wizard de creación de orden | Implementado |
| Refactor de CreateOrderPage en hooks/componentes | Implementado |
| Listas rápidas predefinidas | Implementado |
| Prioridades visuales | Implementado |
| Distribución por peso en presets | Implementado |
| Feedback de orden creada | Implementado |
| Toast de éxito | Implementado |
| Historial de órdenes | Implementado |
| Vista historial propia/general por rol | Implementado |
| Detalle de orden guardada | Implementado |
| Descarga de PDF por ID | Implementado |
| Descarga de PDF preview invitado | Implementado |
| Nota sobre cálculo conservador | Implementado |
| Estados visuales de error | Implementado |
| Deploy frontend | Implementado |
| Testing frontend automatizado | Pendiente |
| Funcionalidades CRUD admin | Pendiente |
| Persistencia temporal de payload preview | Pendiente |
| Confirmación de correo | Pendiente backend/frontend |
| Recuperación de contraseña | Pendiente backend/frontend |
| UI específica de límites de uso | Pendiente / dependiente de backend |
| Links de compra por producto | Futuro |
| Integración con tiendas/proveedores | Futuro |

---

## Próximos pasos frontend

### Próximas mejoras razonables

- Ejecutar QA manual completo del flujo principal.
- Retestear deploy en Vercel con usuario invitado, USER y ADMIN.
- Documentar casos QA manuales del flujo principal.
- Agregar tests frontend básicos.
- Mejorar persistencia temporal del payload preview en `sessionStorage`.
- Agregar páginas administrativas para productos/cócteles/categorías.
- Preparar capturas para portfolio/LinkedIn.
- Revisar accesibilidad básica de formularios, selects y navegación mobile.

---

### Roadmap futuro de producto

- Confirmación de correo en registro.
- Recuperación de contraseña.
- Límites de uso visibles para previews, PDFs, login y registro.
- Links de compra por producto.
- Lista de compra compartible.
- Opciones de compra desde una orden.
- Registro de tiendas/proveedores.
- Catálogo de productos por tienda.
- Integración futura con tiendas externas o marketplaces.
- Modo compra optimizada considerando stock disponible.

---

## Autor

Proyecto desarrollado por **Franco Aguirre** como parte de su formación y portfolio profesional en desarrollo backend/full stack.

Stack principal de estudio:

- Java
- Spring Boot
- PostgreSQL
- React
- TypeScript
- Testing
- QA Manual
