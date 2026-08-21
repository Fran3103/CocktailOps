# CocktailOps Frontend

Web App para CocktailOps, desarrollada con React, Vite, TypeScript y Tailwind CSS.

Este módulo representa la interfaz visual del sistema y conecta el flujo principal del usuario con la API REST del backend: autenticación, dashboard por rol, catálogo de cócteles, catálogo de productos, creación de órdenes, listas rápidas predefinidas, historial propio, detalle de orden y descarga de PDFs.

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
- [Listas rápidas predefinidas](#listas-rápidas-predefinidas)
- [Notas sobre el cálculo](#notas-sobre-el-cálculo)
- [Estado actual del frontend](#estado-actual-del-frontend)
- [Próximos pasos frontend](#próximos-pasos-frontend)
- [Autor](#autor)

---

## Descripción

CocktailOps Frontend permite interactuar visualmente con un sistema de planificación de órdenes de cócteles para eventos.

La aplicación está pensada para que un usuario pueda:

- consultar cócteles disponibles
- consultar productos e insumos disponibles
- crear una orden para un evento según invitados y duración
- crear una orden por cantidad total de tragos
- usar listas rápidas predefinidas según el tipo de evento
- ajustar manualmente cócteles, pesos o cantidades
- obtener una lista calculada de productos e insumos necesarios
- ver el resumen de una orden generada
- descargar el PDF de una orden temporal o guardada
- iniciar sesión o registrarse
- guardar órdenes asociadas a su usuario
- consultar su historial de órdenes
- entrar al detalle de una orden guardada
- ver un dashboard distinto según el tipo de acceso

El frontend forma parte de una solución full stack orientada a portfolio, con foco en una experiencia clara, profesional y funcional.

---

## Objetivo del frontend

El objetivo principal de este módulo es convertir la API de CocktailOps en una aplicación web usable.

El frontend permite mostrar el flujo completo del producto:

```txt
Ver dashboard
→ Consultar catálogo de cócteles/productos
→ Crear una orden
→ Elegir cócteles manualmente o usar una lista rápida
→ Calcular productos necesarios
→ Ver resumen de orden
→ Descargar PDF
```

Para usuarios autenticados, también permite:

```txt
Login
→ Crear y guardar orden
→ Consultar dashboard personalizado
→ Consultar historial propio
→ Entrar al detalle de una orden anterior
→ Descargar PDF de una orden guardada
```

Para usuarios administradores, permite:

```txt
Login ADMIN
→ Ver dashboard administrativo
→ Consultar métricas generales de órdenes
→ Ver últimas órdenes del sistema
→ Acceder al detalle de órdenes guardadas
```

La autenticación no bloquea el uso principal de la aplicación. Su objetivo es habilitar funcionalidades persistentes como historial, perfil y asociación de órdenes a una cuenta.

---

## Reglas actuales del producto

### Usuario invitado

Un usuario invitado puede:

- ver el dashboard público
- ver el catálogo de cócteles
- ver el catálogo de productos
- generar una orden temporal
- usar listas rápidas predefinidas
- ver el resumen inmediato de la orden generada
- descargar el PDF de la orden generada

Un usuario invitado no puede:

- acceder al historial
- acceder al perfil
- conservar órdenes asociadas a una cuenta
- recuperar una orden temporal después de salir del flujo

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
- feedback visual de acciones importantes
- diseño profesional para portfolio

---

### Arquitectura por features

El frontend se organiza por dominio funcional.

Ejemplo:

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

Ejemplo:

```txt
authService.ts
cocktailService.ts
productService.ts
orderService.ts
```

Motivos:

- separar lógica HTTP de componentes visuales
- reutilizar llamadas a API
- mantener componentes más limpios
- facilitar cambios futuros en endpoints

---

### Helpers para lógica de negocio del frontend

La lógica auxiliar que no pertenece directamente al render se mueve a archivos externos.

Ejemplo:

```txt
orderPresetUtils.ts
```

Este helper se encarga de:

- buscar cócteles del preset por nombre dentro del catálogo real
- detectar cócteles faltantes
- transformar presets en cócteles seleccionados
- distribuir cantidades por peso en modo cantidad total de tragos

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

Rutas privadas:

- `/orders`
- `/orders/:id`
- `/profile`

El detalle de orden está protegido porque representa una orden guardada. Las órdenes temporales de invitados no navegan a `/orders/:id`; se muestran en el resumen inmediato y permiten descargar PDF preview desde ahí.

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
- Estados de carga
- Estados de error
- Estados vacíos
- Feedback visual de orden creada
- Toast de éxito
- Navegación desde orden creada hacia detalle cuando la orden tiene ID
- Prevención de navegación al detalle para órdenes temporales
- Manejo específico de errores HTTP en descarga de PDF

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

---

## Instalación y uso

### Requisitos previos

- Node.js
- npm
- Backend de CocktailOps corriendo localmente
- Base de datos configurada en backend

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
│   │   │   ├── order.types.ts
│   │   │   ├── orderPresets.ts
│   │   │   ├── orderPresetUtils.ts
│   │   │   ├── orderService.ts
│   │   │   └── components/
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
│   │   │   ├── feedback/
│   │   │   ├── navigation/
│   │   │   ├── placeholders/
│   │   │   └── ui/
│   │   ├── constants/
│   │   ├── hooks/
│   │   ├── types/
│   │   └── utils/
│   │
│   ├── styles/
│   ├── index.css
│   ├── main.tsx
│   └── vite-env.d.ts
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
/create-order
/unauthorized
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
```

El dashboard administrativo se resuelve por rol dentro de `/dashboard`. Si el usuario autenticado tiene rol `ADMIN`, se muestra el dashboard administrativo.

Actualmente no hay rutas CRUD administrativas separadas para productos, cócteles o categorías.

---

## Integración con backend

El frontend se conecta con el backend mediante API REST.

La URL base se configura con:

```env
VITE_API_BASE_URL=http://localhost:8081
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
→ Frontend renderiza cards, buscador, preview y selector de órdenes
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
→ Frontend envía payload
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
→ Frontend envía payload preview
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
→ Frontend muestra tabla/listado con acceso al detalle
```

---

### Órdenes del sistema para admin

```http
GET /orders
```

Uso en frontend:

```txt
Usuario ADMIN entra al dashboard
→ Frontend solicita /orders
→ Backend devuelve órdenes del sistema según permisos
→ Frontend muestra métricas y últimas órdenes
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
→ Elegir lista rápida o seleccionar cócteles manualmente
→ Ver resumen de orden temporal
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
→ Acceder a detalle de órdenes guardadas
→ Descargar PDF según permisos backend
```

---

### Flujo historial

```txt
Usuario autenticado
→ /orders
→ GET /orders/my-orders
→ Tabla de órdenes
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
El preset carga cócteles y pesos.
El backend usa los pesos para distribuir los tragos calculados.
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
Editar peso
Editar cantidad
Eliminar cóctel
Dividir equitativamente
```

el preset deja de marcarse como activo.

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

- puede quedar sobrante de algunos insumos
- no descuenta stock previo
- no optimiza compras mínimas
- prioriza que alcance para preparar la cantidad calculada

Componente:

```txt
src/features/orders/components/CalculationNotice.tsx
```

Esta aclaración también debería mantenerse alineada con la nota incluida en el PDF generado por backend.

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
| UnauthorizedPage | Implementado |
| Dashboard invitado | Implementado |
| Dashboard USER | Implementado |
| Dashboard ADMIN | Implementado |
| Métricas por rol | Implementado |
| Tabla de últimas órdenes | Implementado |
| Catálogo de cócteles | Implementado |
| Buscador de cócteles | Implementado |
| Productos | Implementado |
| Filtro de productos | Implementado |
| Crear orden modo evento | Implementado |
| Crear orden modo cantidad de tragos | Implementado |
| Listas rápidas predefinidas | Implementado |
| Distribución por peso en presets | Implementado |
| Feedback de orden creada | Implementado |
| Toast de éxito | Implementado |
| Historial de órdenes | Implementado |
| Detalle de orden guardada | Implementado |
| Descarga de PDF por ID | Implementado |
| Descarga de PDF preview invitado | Implementado |
| Nota sobre cálculo conservador | Implementado |
| Deploy frontend | Pendiente |
| Testing frontend automatizado | Pendiente |
| Funcionalidades CRUD admin | Pendiente |
| Persistencia temporal de payload preview | Pendiente |
| Confirmación de correo | Pendiente backend/frontend |
| Recuperación de contraseña | Pendiente backend/frontend |
| Límites de uso / rate limiting | Pendiente principalmente backend |
| Refactor SOLID de CreateOrderPage | Pendiente |

---

## Próximos pasos frontend

### Próximas mejoras razonables

- Preparar variables de entorno para deploy
- Deploy frontend en Vercel u otra plataforma
- Conectar frontend deployado con backend productivo
- Mejorar persistencia temporal del payload preview en `sessionStorage`
- Agregar tests frontend básicos
- Mejorar manejo de errores específicos en creación de órdenes
- Refactorizar `CreateOrderPage` en hooks y subcomponentes más pequeños
- Agregar páginas administrativas para productos/cócteles/categorías
- Documentar casos QA manuales del flujo principal
- Preparar capturas para portfolio/LinkedIn

---

### Roadmap futuro de producto

- Confirmación de correo en registro
- Recuperación de contraseña
- Límites de uso para previews, PDFs, login y registro
- Links de compra por producto
- Lista de compra compartible
- Opciones de compra desde una orden
- Registro de tiendas/proveedores
- Catálogo de productos por tienda
- Integración futura con tiendas externas o marketplaces
- Modo compra optimizada considerando stock disponible

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
