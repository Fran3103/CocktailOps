# CocktailOps Frontend

Interfaz web de **CocktailOps**, una aplicación full stack para calcular bebidas, cócteles, insumos y packs necesarios para eventos.

El frontend está desarrollado con **React**, **Vite**, **TypeScript** y **Tailwind CSS**, y consume una API REST construida con Java y Spring Boot.

> CocktailOps es un proyecto portfolio no comercial, creado para demostrar desarrollo full stack aplicado a un caso real de cálculo de bebidas e insumos para eventos.

---

## Índice

- [Descripción](#descripción)
- [Objetivo del frontend](#objetivo-del-frontend)
- [Demo](#demo)
- [Stack técnico](#stack-técnico)
- [Arquitectura del frontend](#arquitectura-del-frontend)
- [Reglas de acceso](#reglas-de-acceso)
- [Funcionalidades principales](#funcionalidades-principales)
- [Flujos principales](#flujos-principales)
- [Rutas de la aplicación](#rutas-de-la-aplicación)
- [Integración con backend](#integración-con-backend)
- [Variables de entorno](#variables-de-entorno)
- [Instalación y uso local](#instalación-y-uso-local)
- [Deploy](#deploy)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Decisiones de diseño y UX](#decisiones-de-diseño-y-ux)
- [Manejo de errores](#manejo-de-errores)
- [Estado actual](#estado-actual)
- [Próximas mejoras](#próximas-mejoras)
- [Autor](#autor)

---

## Descripción

CocktailOps Frontend permite utilizar visualmente el sistema CocktailOps desde el navegador.

La aplicación permite:

- consultar un catálogo de cócteles;
- calcular una orden para un evento según invitados y duración;
- calcular una orden por cantidad total de tragos;
- seleccionar cócteles manualmente;
- usar listas rápidas predefinidas según el tipo de evento;
- ajustar prioridades de cócteles en modo evento;
- asignar cantidades por cóctel en modo cantidad de tragos;
- distribuir tragos equitativamente;
- visualizar el resumen calculado;
- ver productos e insumos necesarios;
- descargar PDFs;
- iniciar sesión y registrarse;
- guardar historial para usuarios autenticados;
- acceder a vistas diferenciadas según rol;
- navegar correctamente desde desktop y mobile.

El objetivo no es simular una tienda online ni un sistema comercial completo, sino presentar una aplicación funcional, clara y mantenible, con reglas de negocio reales y una integración full stack.

---

## Objetivo del frontend

El frontend transforma la API REST de CocktailOps en una experiencia usable para el usuario final.

La app resuelve principalmente esta necesidad:

```txt
Tengo un evento
→ necesito preparar cócteles
→ quiero estimar tragos, ingredientes, productos y packs
→ quiero obtener un resumen claro
→ quiero descargar una orden en PDF
```

La autenticación no bloquea el uso principal de la aplicación. Un invitado puede generar una orden temporal y descargar el PDF. La cuenta de usuario agrega persistencia: historial, detalle de órdenes guardadas y acceso posterior.

---

## Demo

Frontend desplegado:

```txt
https://cocktailops.vercel.app
```

Portfolio del desarrollador:

```txt
https://franaguirredev.com
```

Repositorio del proyecto:

```txt
https://github.com/Fran3103/CocktailOps
```

---

## Stack técnico

| Área | Tecnología |
|---|---|
| Framework UI | React |
| Build tool | Vite |
| Lenguaje | TypeScript |
| Estilos | Tailwind CSS |
| Routing | React Router |
| HTTP client | Axios |
| Iconos | Lucide React |
| Estado de sesión | Context API + localStorage |
| Deploy frontend | Vercel |
| Backend consumido | Java + Spring Boot |
| Base de datos backend | PostgreSQL |

---

## Arquitectura del frontend

El frontend está organizado por **features**, separando cada dominio funcional de la aplicación.

```txt
src/
├── api/
│   └── apiClient.ts
│
├── app/
│   ├── App.tsx
│   └── router.tsx
│
├── features/
│   ├── auth/
│   ├── cocktails/
│   ├── dashboard/
│   ├── orders/
│   ├── products/
│   └── profiles/
│
├── layouts/
│   ├── AppLayout.tsx
│   └── AuthLayout.tsx
│
├── shared/
│   ├── components/
│   ├── constants/
│   ├── hooks/
│   ├── pages/
│   ├── types/
│   └── utils/
│
├── styles/
├── index.css
└── main.tsx
```

### Criterio de organización

```txt
features/auth       → login, registro, sesión, rutas protegidas
features/cocktails  → catálogo y filtros de cócteles
features/dashboard  → dashboards según tipo de usuario
features/orders     → creación, historial, detalle y PDF
features/products   → catálogo interno de productos
features/profiles   → perfil del usuario autenticado
shared/             → componentes, helpers y constantes reutilizables
```

Esta separación evita que la aplicación dependa de componentes gigantes y facilita el mantenimiento del proyecto.

---

## Reglas de acceso

CocktailOps tiene tres tipos principales de uso:

| Tipo de usuario | Descripción |
|---|---|
| Invitado | Puede usar el cálculo principal sin crear cuenta |
| USER | Puede guardar órdenes y consultar su historial |
| ADMIN | Puede consultar información general del sistema y acceder a productos |

---

### Invitado

Un usuario invitado puede:

- ver el dashboard público;
- consultar el catálogo de cócteles;
- crear una orden temporal;
- usar listas rápidas predefinidas;
- descargar el PDF desde el resumen generado.

Un usuario invitado no puede:

- acceder al historial;
- acceder al perfil;
- acceder al catálogo interno de productos;
- guardar órdenes en la base de datos;
- recuperar una orden temporal luego de abandonar el flujo.

Regla principal:

```txt
Una orden invitada es temporal.
No se guarda en base de datos.
No tiene ID.
No aparece en historial.
El PDF se descarga desde el resumen inmediato.
```

En frontend, una orden se considera temporal cuando:

```ts
order.id == null
```

---

### Usuario autenticado

Un usuario autenticado con rol `USER` puede:

- iniciar sesión;
- crear órdenes guardadas;
- ver dashboard personal;
- consultar su historial;
- entrar al detalle de una orden propia;
- descargar PDF de órdenes guardadas;
- acceder a su perfil.

---

### Administrador

Un usuario con rol `ADMIN` puede:

- ver dashboard administrativo;
- consultar métricas generales;
- alternar entre historial propio e historial general;
- acceder al detalle de órdenes según permisos del backend;
- descargar PDFs según permisos del backend;
- acceder al catálogo interno de productos.

El dashboard administrativo se resuelve por rol dentro de `/dashboard`.

---

## Funcionalidades principales

### Autenticación

- Login de usuario.
- Registro de usuario.
- Mostrar/ocultar contraseña.
- Persistencia de sesión en `localStorage`.
- Envío automático de JWT en requests protegidas.
- Redirección a login cuando una ruta requiere sesión.
- Página de acceso no autorizado para rutas restringidas.

---

### Dashboard por tipo de usuario

La pantalla `/dashboard` muestra contenido distinto según el estado de sesión.

```txt
Invitado → Dashboard público con explicación del flujo
USER     → Dashboard personal con resumen propio
ADMIN    → Dashboard administrativo con métricas generales
```

---

### Catálogo de cócteles

El usuario puede consultar cócteles disponibles con:

- cards visuales;
- buscador;
- filtros por tipo de preparación;
- paginación;
- diseño responsive.

Los cócteles también se usan dentro del flujo de creación de orden.

---

### Catálogo de productos

La sección de productos está pensada como vista interna para administrador.

Permite consultar productos e insumos disponibles para el cálculo de bebidas.

Acceso:

```txt
ADMIN → puede acceder
USER → no puede acceder
Invitado → no puede acceder
```

---

### Creación de orden

La creación de orden funciona como un wizard dividido en pasos:

```txt
1. Datos del cálculo
2. Selección de cócteles
3. Resumen y generación
```

El usuario puede calcular de dos maneras:

```txt
Modo evento
→ invitados + duración + cócteles + prioridades

Modo cantidad de tragos
→ total de tragos + cócteles + cantidades
```

---

### Listas rápidas predefinidas

El frontend incluye listas rápidas para acelerar la selección de cócteles.

Ejemplos:

- Clásicos simples
- Clásicos completos
- Boda / evento elegante
- Modernos y fiesta
- Verano / tropical
- Aperitivos
- Premium clásico
- Popular y rápido

Los presets se definen por nombre de cóctel, no por ID fijo. El frontend busca cada cóctel dentro del catálogo real recibido desde el backend.

Esto evita depender de IDs de base de datos.

---

### Prioridades de cócteles

En modo evento, el usuario no ve valores técnicos como `weight`. Ve prioridades en palabras.

| Prioridad visible | Valor enviado al backend |
|---|---:|
| Baja | 1 |
| Normal | 2 |
| Media | 3 |
| Alta | 5 |

Ejemplo conceptual:

```txt
Fernet Cola      → Prioridad alta
Gin Tonic        → Prioridad normal
Aperol Spritz    → Prioridad baja
```

Payload enviado al backend:

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

---

### Cantidad total de tragos

En modo cantidad de tragos, el usuario define un total y asigna cantidades por cóctel.

Ejemplo:

```txt
Total de tragos: 100

Fernet Cola   → 40
Gin Tonic     → 30
Aperol Spritz → 30
```

El frontend muestra un contador de asignación:

```txt
Asignados: 100 / 100
```

También permite repartir equitativamente entre los cócteles seleccionados.

---

### PDF

CocktailOps permite descargar PDFs en dos escenarios:

| Caso | Endpoint | Resultado |
|---|---|---|
| Orden guardada | `GET /orders/{id}/pdf` | Descarga `order-{id}.pdf` |
| Orden temporal por evento | `POST /orders/preview/pdf` | Descarga `order-preview.pdf` |
| Orden temporal por tragos | `POST /orders/by-drinks/preview/pdf` | Descarga `order-preview.pdf` |

El usuario invitado descarga el PDF desde el resumen inmediato.

El usuario autenticado puede descargarlo desde el detalle o historial.

---

## Flujos principales

### Flujo invitado

```txt
Entrar a la app
→ Ver dashboard público
→ Consultar cócteles
→ Crear orden
→ Elegir modo de cálculo
→ Completar datos
→ Seleccionar cócteles manualmente o usar lista rápida
→ Ajustar prioridades o cantidades
→ Ver resumen
→ Generar orden temporal
→ Descargar PDF preview
```

---

### Flujo usuario registrado

```txt
Login
→ Dashboard personal
→ Crear orden
→ Elegir modo de cálculo
→ Completar datos
→ Seleccionar cócteles
→ Generar orden guardada
→ Ver resumen
→ Entrar al detalle
→ Descargar PDF
→ Consultar historial
```

---

### Flujo administrador

```txt
Login ADMIN
→ Dashboard administrativo
→ Consultar métricas generales
→ Ver últimos registros
→ Ir a historial
→ Alternar entre historial propio e historial general
→ Acceder al detalle
→ Descargar PDF según permisos
→ Consultar catálogo interno de productos
```

---

## Rutas de la aplicación

### Rutas públicas

| Ruta | Descripción |
|---|---|
| `/login` | Inicio de sesión |
| `/register` | Registro de usuario |
| `/dashboard` | Dashboard según estado de sesión |
| `/cocktails` | Catálogo público de cócteles |
| `/create-order` | Creación de orden temporal o guardada |
| `/unauthorized` | Acceso no autorizado |
| `/404` | Página no encontrada |

---

### Rutas protegidas

| Ruta | Acceso | Descripción |
|---|---|---|
| `/orders` | USER / ADMIN | Historial |
| `/orders/:id` | USER / ADMIN | Detalle de orden guardada |
| `/profile` | USER / ADMIN | Perfil de usuario |
| `/products` | ADMIN | Catálogo interno de productos |

---

## Integración con backend

El frontend consume una API REST de CocktailOps.

La configuración base se centraliza en:

```txt
src/api/apiClient.ts
```

Este cliente HTTP:

- toma la URL base desde variables de entorno;
- adjunta el JWT en requests protegidas;
- permite centralizar la configuración de Axios;
- separa la comunicación HTTP de los componentes visuales.

---

### Endpoints principales

#### Autenticación

```http
POST /auth/register
POST /auth/login
```

---

#### Cócteles

```http
GET /cocktails
```

---

#### Productos

```http
GET /products
```

---

#### Órdenes guardadas

```http
POST /orders
POST /orders/by-drinks
GET /orders/my-orders
GET /orders
GET /orders/{id}
GET /orders/{id}/pdf
```

---

#### Órdenes temporales

```http
POST /orders/preview
POST /orders/by-drinks/preview
POST /orders/preview/pdf
POST /orders/by-drinks/preview/pdf
```

---

## Variables de entorno

Crear un archivo `.env.local` dentro de la carpeta `frontend`.

### Desarrollo local

```env
VITE_API_BASE_URL=http://localhost:8080
```

### Producción con proxy de Vercel

```env
VITE_API_BASE_URL=/api
```

Los archivos `.env` y `.env.local` no deben versionarse si contienen valores sensibles o configuraciones específicas del entorno local.

---

## Instalación y uso local

### 1. Clonar el repositorio

```bash
git clone https://github.com/Fran3103/CocktailOps.git
cd CocktailOps
```

### 2. Entrar al frontend

```bash
cd frontend
```

### 3. Instalar dependencias

```bash
npm install
```

### 4. Configurar variables de entorno

Crear `.env.local`:

```env
VITE_API_BASE_URL=http://localhost:8080
```

### 5. Ejecutar en desarrollo

```bash
npm run dev
```

La aplicación queda disponible en:

```txt
http://localhost:5173
```

### 6. Ejecutar lint

```bash
npm run lint
```

### 7. Generar build

```bash
npm run build
```

### 8. Previsualizar build

```bash
npm run preview
```

---

## Deploy

El frontend está desplegado en Vercel.

En producción, el frontend utiliza `/api` como base URL y Vercel reescribe esas requests hacia el backend productivo.

Ejemplo conceptual de `vercel.json`:

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

Esto permite:

- consumir el backend mediante rutas relativas;
- evitar exponer la URL base dentro del código del frontend;
- mantener funcionando React Router al refrescar rutas internas.

---

## Decisiones de diseño y UX

### Estética visual

La interfaz usa una estética tipo dashboard SaaS:

- fondo oscuro;
- tarjetas limpias;
- bordes sutiles;
- acentos dorados;
- tipografía clara;
- layout centrado;
- sidebar responsive;
- tablas adaptadas a mobile.

---

### Responsive design

La aplicación está pensada para desktop y mobile.

Se trabajó especialmente en:

- menú lateral desktop;
- menú mobile con bloqueo de scroll de fondo;
- cards en pantallas chicas;
- tablas convertidas a cards en mobile;
- formularios legibles;
- wizard de creación de orden en pasos.

---

### Claridad del copy

El frontend usa lenguaje orientado al usuario.

Ejemplos:

```txt
Prioridad alta
Prioridad normal
Asignados 100 / 100
Orden temporal
Historial
Cálculo de bebidas e insumos
```

Se evita mostrar al usuario conceptos técnicos internos cuando no aportan claridad.

---

### Footer profesional

La aplicación incluye un footer con:

- nombre del desarrollador;
- rol profesional;
- aclaración de proyecto portfolio no comercial;
- links profesionales;
- repositorio;
- portfolio;
- email de contacto.

---

## Manejo de errores

El frontend incluye estados visuales reutilizables para mostrar errores de forma clara.

Casos contemplados:

| Caso | Resultado esperado |
|---|---|
| 400 | Datos inválidos |
| 401 | Sesión no activa o vencida |
| 403 | Acceso denegado |
| 404 | Recurso o página no encontrada |
| 500 | Error del servidor |
| Sin respuesta | Error de conexión o backend no disponible |

Componentes y páginas relacionados:

```txt
src/shared/utils/getApiErrorMessage.ts
src/shared/components/ui/ErrorState.tsx
src/shared/pages/NotFoundPage.tsx
src/features/auth/UnauthorizedPage.tsx
```

---

## Estado actual

| Módulo | Estado |
|---|---|
| Proyecto React + Vite | Implementado |
| TypeScript | Implementado |
| Tailwind CSS | Implementado |
| React Router | Implementado |
| Axios | Implementado |
| Layout principal | Implementado |
| Sidebar responsive | Implementado |
| Footer profesional | Implementado |
| Login / Register | Implementado |
| Mostrar/ocultar contraseña | Implementado |
| JWT en frontend | Implementado |
| AuthContext | Implementado |
| ProtectedRoute | Implementado |
| AdminRoute | Implementado |
| Dashboard invitado | Implementado |
| Dashboard USER | Implementado |
| Dashboard ADMIN | Implementado |
| Catálogo de cócteles | Implementado |
| Buscador de cócteles | Implementado |
| Filtros por preparación | Implementado |
| Paginación | Implementado |
| Catálogo de productos ADMIN | Implementado |
| Crear orden por evento | Implementado |
| Crear orden por cantidad de tragos | Implementado |
| Wizard de creación de orden | Implementado |
| Listas rápidas predefinidas | Implementado |
| Prioridades visuales | Implementado |
| Contador de tragos asignados | Implementado |
| Reparto equitativo de tragos | Implementado |
| Historial | Implementado |
| Historial propio/general por rol | Implementado |
| Detalle de orden | Implementado |
| PDF de orden guardada | Implementado |
| PDF preview invitado | Implementado |
| Tablas/cards responsive | Implementado |
| Estados visuales de error | Implementado |
| 403 Unauthorized | Implementado |
| 404 Not Found | Implementado |
| Deploy Vercel | Implementado |
| Testing frontend automatizado | Pendiente |
| CRUD admin completo | Pendiente |
| Persistencia temporal de preview | Pendiente |
| Recuperación de contraseña | Pendiente |
| Confirmación de correo | Pendiente |
| Links de compra | Futuro |
| Integración con tiendas/proveedores | Futuro |

---

## Próximas mejoras

Mejoras razonables para una evolución futura:

- ejecutar QA manual completo del flujo principal;
- documentar casos de prueba funcionales;
- agregar tests frontend básicos;
- persistir temporalmente el payload preview en `sessionStorage`;
- mejorar accesibilidad de formularios, selects y navegación;
- agregar CRUD administrativo para productos, cócteles y categorías;
- mostrar límites de uso desde la UI;
- preparar capturas para portfolio y LinkedIn;
- evaluar links de compra por producto;
- evaluar integración futura con tiendas o proveedores.

---

## Autor

Proyecto desarrollado por **Franco Aguirre** como parte de su portfolio profesional en desarrollo de software.

**Rol:** Desarrollador de software orientado a Java, Spring Boot y desarrollo full stack.

**Stack principal:**

- Java
- Spring Boot
- PostgreSQL
- React
- TypeScript
- Testing
- QA Manual

**Links profesionales:**

- Portfolio: https://franaguirredev.com
- GitHub: https://github.com/Fran3103
- Proyecto: https://github.com/Fran3103/CocktailOps
- Email: francoaguirre.ar@gmail.com
