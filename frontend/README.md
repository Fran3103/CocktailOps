# CocktailOps Frontend

Web App para CocktailOps, desarrollada con React, Vite, TypeScript y Tailwind CSS.

Este módulo representa la interfaz visual del sistema y tiene como objetivo conectar el flujo principal del usuario con la API REST del backend: autenticación, catálogo de cócteles, creación de órdenes, historial propio y descarga de PDFs.

---

## Índice

- [Descripción](#descripción)
- [Objetivo del frontend](#objetivo-del-frontend)
- [Decisiones técnicas](#decisiones-técnicas)
- [Características principales](#características-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Autenticación y acceso](#autenticación-y-acceso)
- [Instalación y uso](#instalación-y-uso)
- [Variables de entorno](#variables-de-entorno)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Rutas principales](#rutas-principales)
- [Integración con backend](#integración-con-backend)
- [Estado actual del frontend](#estado-actual-del-frontend)
- [Próximos pasos frontend](#próximos-pasos-frontend)

---

## Descripción

CocktailOps Frontend permite interactuar visualmente con el sistema de planificación de pedidos de cócteles para eventos.

La aplicación está pensada para que un usuario pueda:

- consultar cócteles disponibles
- crear una orden para un evento
- obtener una lista calculada de ingredientes
- iniciar sesión o registrarse
- guardar órdenes asociadas a su usuario
- consultar su historial
- acceder al detalle de una orden
- descargar el PDF generado por el backend

El frontend forma parte de una solución full stack orientada a portfolio, con foco en una experiencia clara, profesional y funcional.

---

## Objetivo del frontend

El objetivo principal de este módulo es convertir la API de CocktailOps en una aplicación web usable.

El frontend debe permitir mostrar el flujo completo del producto:

```txt
Ver cócteles disponibles
→ Crear una orden de evento
→ Calcular ingredientes necesarios
→ Ver resumen de compra
→ Guardar orden si el usuario está autenticado
→ Ver historial propio
→ Descargar PDF protegido
```

También se contempla un flujo de uso como invitado:

```txt
Usuario invitado
→ Puede usar la herramienta principal
→ Puede calcular una orden
→ No conserva historial
→ No accede a órdenes guardadas
```

La autenticación no busca bloquear todo el uso de la aplicación, sino habilitar funcionalidades persistentes como historial, ownership de órdenes y descarga protegida de PDFs.

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

El diseño visual busca seguir una estética tipo dashboard SaaS:

- fondo oscuro
- tarjetas limpias
- bordes sutiles
- acentos dorados
- tablas legibles
- formularios claros
- diseño profesional para portfolio

---

### Arquitectura por features

El frontend se organizará por dominio funcional.

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

La comunicación con el backend se centralizará en services.

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

### Rutas públicas, privadas y administrativas

La aplicación distingue entre distintos niveles de acceso:

- rutas públicas
- rutas para usuarios autenticados
- rutas para administradores

Esto permite reflejar las reglas del backend y mejorar la experiencia del usuario.

---

## Características principales

### Funcionalidades objetivo

- Layout principal tipo dashboard
- Login y registro de usuarios
- Manejo de JWT en frontend
- Rutas protegidas
- Rutas públicas para uso básico
- Catálogo de cócteles
- Listado de productos
- Creación de órdenes
- Diferenciación entre usuario invitado y usuario registrado
- Historial de órdenes del usuario autenticado
- Detalle de orden
- Descarga de PDF
- Control visual según rol `USER` o `ADMIN`
- Estados de carga, error y datos vacíos

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

El frontend deberá consumir los endpoints de autenticación:

```http
POST /auth/register
POST /auth/login
```

Luego, para requests protegidas, deberá enviar el token en el header:

```http
Authorization: Bearer <token>
```

---

### Tipos de usuario previstos

| Tipo de usuario | Acceso esperado |
|---|---|
| Invitado | Puede usar funcionalidades básicas sin historial |
| USER | Puede guardar órdenes, ver historial propio y descargar sus PDFs |
| ADMIN | Puede gestionar catálogo y acceder a recursos administrativos |

---

### Funcionalidades protegidas

| Funcionalidad | Acceso |
|---|---|
| Login / Registro | Público |
| Ver cócteles | Público |
| Crear orden como invitado | Público / futuro flujo de preview |
| Ver historial propio | Usuario autenticado |
| Ver detalle de orden guardada | Usuario autenticado dueño de la orden |
| Descargar PDF | Dueño de la orden o ADMIN |
| Crear / editar productos | ADMIN |
| Crear / editar cócteles | ADMIN |

> El flujo de invitado forma parte de la evolución funcional del producto. Si el backend requiere autenticación para crear órdenes guardadas, el frontend deberá diferenciar entre cálculo temporal y orden persistida.

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

Crear un archivo `.env` dentro de la carpeta `frontend/`.

Ejemplo:

```env
VITE_API_BASE_URL=http://localhost:8081
```

Esta variable será utilizada por el cliente HTTP del frontend para conectarse con el backend.

> Los archivos `.env` no deben versionarse si contienen valores sensibles o configuraciones específicas del entorno local.

---

## Estructura del proyecto

Estructura objetivo del frontend:

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
│   │   └── utils/
│   │
│   ├── features/
│   │   ├── auth/
│   │   ├── dashboard/
│   │   ├── cocktails/
│   │   ├── products/
│   │   ├── orders/
│   │   └── profile/
│   │
│   ├── assets/
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
/cocktails
/orders/new
```

### Rutas privadas USER

```txt
/dashboard
/orders
/orders/:id
/profile
```

### Rutas privadas ADMIN

```txt
/products
/products/new
/products/:id/edit
/cocktails/new
/cocktails/:id/edit
```

> Las rutas administrativas pueden incorporarse progresivamente según avance el frontend.

---

## Integración con backend

El frontend se conectará con el backend mediante API REST.

Flujos principales:

### Autenticación

```txt
Login/Register
→ Backend devuelve JWT
→ Frontend guarda token
→ Requests protegidas envían Authorization Bearer
```

### Catálogo

```txt
Frontend solicita cócteles/productos
→ Backend responde datos del catálogo
→ Frontend renderiza cards o tablas
```

### Órdenes

```txt
Usuario completa formulario
→ Frontend envía datos al backend
→ Backend calcula ingredientes y packs
→ Frontend muestra resumen calculado
```

### Historial

```txt
Usuario autenticado ingresa a historial
→ Frontend solicita /orders/my-orders
→ Backend devuelve solo órdenes del usuario autenticado
```

### PDF

```txt
Usuario solicita descarga
→ Frontend llama /orders/{id}/pdf con JWT
→ Backend valida ownership
→ Frontend descarga el archivo
```

---

## Estado actual del frontend

| Módulo | Estado |
|---|---|
| Proyecto React + Vite | Inicializado |
| TypeScript | Configurado |
| Tailwind CSS | Configurado |
| React Router | Instalado |
| Axios | Instalado |
| Lucide React | Instalado |
| Layout público / privado | Pendiente |
| Rutas base | Pendiente |
| Login / Register | Pendiente |
| Manejo de JWT | Pendiente |
| Catálogo de cócteles | Pendiente |
| Productos | Pendiente |
| Crear orden | Pendiente |
| Historial de órdenes | Pendiente |
| Detalle de orden | Pendiente |
| Descarga de PDF | Pendiente |
| Deploy frontend | Pendiente |

---

## Próximos pasos frontend

### Roadmap futuro

- Links de compra por producto
- Lista de compra compartible
- Opciones de compra desde una orden
- Registro de tiendas/proveedores
- Catálogo de productos por tienda
- Integración futura con tiendas externas o marketplaces
- Deploy frontend
- Testing frontend
- Documentación QA manual

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
