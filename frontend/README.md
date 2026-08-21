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