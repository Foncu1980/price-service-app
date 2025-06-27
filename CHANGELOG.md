# 📓 CHANGELOG

Historial de versiones del proyecto `price-service`.

---

## [1.4.0] - 2025-06-27
### ✨ Añadido
- Servicio de dominio PriceSelectionService que encapsula la lógica de negocio de selección de precios.
- Índice en la base de datos para mejorar la eficiencia de búsquedas por producto, marca, fechas y prioridad.
- Creación de nuevos test unitarios para clases que aún no tenían cobertura.
- Corrección de vulnerabilidades.

### 🛠️ Mejorado
- Refactorización de la lógica de negocio (PriceNotFoundException) trasladándola de application a domain.
- Revisión de código y estilo para cumplir estándares: uso de final, mejoras de claridad y simplificación.
- Ampliación de cobertura en pruebas unitarias existentes.

### 📘 Documentación
- Javadoc añadido o completado en entidades, servicios, configuraciones e interceptores.
- Comentarios mejorados para facilitar la comprensión del código y las pruebas.

---

## [1.3.2] - 2025-06-22
### 🐞 Corregido
- Se corrigió la versión en los archivos pom.xml para reflejar correctamente la numeración del proyecto.

✅ Esta versión reemplaza a `v1.3.1` como la última versión estable.

---

## [1.3.1] - 2025-06-22
### 🐞 Corregido
- Corrección menor en `README.md`: el enlace `http://localhost:8080` se mostraba como clicable, lo que podía llevar a errores al abrirlo en navegadores.

---

## [1.3.0] - 2025-06-22
### ✨ Añadido
- **Soporte de seguridad básica**: se añade un filtro personalizado que valida un token Bearer fijo (`12345678`) para simular autenticación.
- **Integración con Swagger/OpenAPI**: configuración de seguridad en Swagger UI para incluir el token en las peticiones.

### 📘 Documentación
- Actualización del `README.md` con nuevas secciones:
    - Consideraciones de seguridad.
    - Ejemplos de autenticación con peticiones válidas y erróneas (`curl`).
    - Historial de versiones.
    - Autoría y fecha del proyecto.
    - Mejoras futuras propuestas.


---

## [1.2.1] - 2025-06-19
### 🐞 Corregido
- Fix en el `pom.xml` del módulo `config` que impedía la compilación de la versión estable `1.2.0`.

---

## [1.2.0] - 2025-06-19
### ✨ Añadido
- Métricas Prometheus para monitorización de peticiones (200, 400, 404, 500).
- Módulo `config` para centralizar propiedades y configuración técnica.
- Ejemplos detallados en el `README.md`, incluyendo uso de curl.

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
