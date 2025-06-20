# 📓 CHANGELOG

Historial de versiones del proyecto `price-service`.

---

## [1.3.0-SNAPSHOT] - En desarrollo
### 🔧 Preparación
- Preparada la siguiente iteración de desarrollo tras estabilizar la versión `1.2.1`.

---

## [1.2.1] - 2025-06-19
### 🐞 Corregido
- Fix en el `pom.xml` del módulo `config` que impedía la compilación de la versión estable `1.2.0`.

✅ Esta versión es considerada **estable**.

---

## [1.2.0] - 2025-06-19
### ✨ Añadido
- Métricas Prometheus para monitorización de peticiones (200, 400, 404, 500).
- Módulo `config` para centralizar propiedades y configuración técnica.
- Ejemplos detallados en el `README`, incluyendo uso de curl.

### 🛠️ Mejorado
- Refactorizaciones menores para mayor claridad.
- Mejora de la documentación.

---

## [1.1.0] - 2025-06-17
### ✨ Añadido
- Documentación Swagger UI (`/swagger-ui.html`) y especificación OpenAPI (`/v3/api-docs`).
- Ejemplos ilustrativos de uso en el `README.md`.

---

## [1.0.0] - 2025-06-15
### 🚀 Inicial
- Primera versión funcional de la API `price-service`.
- Carga de datos en memoria con H2.
- Endpoint `/prices/applicable` implementado con lógica de prioridad.
- Tests de integración completos.
- Arquitectura hexagonal aplicada.
