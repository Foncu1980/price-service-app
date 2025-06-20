# 🧾 Price Service

Este proyecto implementa un servicio REST en Spring Boot para consultar el precio aplicable de un producto, para una cadena específica, en una fecha y hora dadas.

La aplicación sigue los principios de la Arquitectura Hexagonal, el diseño SOLID, y utiliza una base de datos en memoria H2 para la persistencia.

📦 [Repositorio en GitHub](https://github.com/Foncu1980/price-service-app)

---

## 📌 Requisitos
Para construir y ejecutar este proyecto, necesitas lo siguiente:

- Java 17+
- Maven 3.8+

## 🧱 Arquitectura del Proyecto

Este proyecto sigue los principios de la **Arquitectura Hexagonal** (también conocida como *Ports and Adapters*), 
lo que garantiza una separación clara entre las reglas de negocio, la lógica de aplicación y los mecanismos externos.

```
price-service/
├── domain/         # Entidades de dominio y puertos de salida
├── application/    # Casos de uso y puertos de entrada
├── infrastructure/ # Adaptadores secundarios (persistencia con JPA/H2)
├── config/         # Configuración de la documentación OpenAPI
├── web/            # Adaptadores primarios (exposición vía REST API)
├── bootstrap/      # Módulo de arranque de Spring Boot (main class)
├── mvnw            # Maven Wrapper script, por si no se tiene maven instalado
└── README.md       # Este archivo
```

### 🔍 Detalle por módulo

- **Domain**: Contiene el núcleo del modelo de negocio, incluyendo entidades y puertos. No depende de ningún framework ni librería externa.
- **Application**: Define y orquesta los casos de uso de la aplicación, actuando como intermediario entre el dominio y los adaptadores.
- **Infrastructure**: Implementa los detalles técnicos de los puertos de salida, como la persistencia de datos mediante JPA/H2.
- **Web**: Actúa como adaptador primario, exponiendo los casos de uso a través de endpoints REST.
- **Bootstrap**: Módulo de arranque que contiene la clase `main` y las configuraciones de Spring Boot necesarias para iniciar la aplicación.

---

## ⚙️ Tecnología
- Spring Boot: Framework principal para el desarrollo de microservicios.
- Spring Data JPA / Hibernate: Para la persistencia de datos y el mapeo objeto-relacional.
- H2 Database: Base de datos en memoria para el desarrollo y las pruebas.
- Maven: Herramienta de gestión de proyectos y dependencias.

---

## ⚙️ Configuración de la aplicación

La configuración del comportamiento de la aplicación está definida en los siguientes archivos:

### 📄 `application.properties`
Ubicado en `bootstrap/src/main/resources/application.properties`, este archivo define la configuración general de la aplicación, incluyendo:
 - Nombre de la aplicación
 - Conexión a base de datos H2 en memoria
 - Inicialización automática de la base de datos mediante los scripts schema.sql y data.sql
 - Activación de la consola web H2
 - Configuración JPA (Hibernate)
 - Perfil activo (por defecto: dev)

Este archivo permite ejecutar la aplicación sin necesidad de configurar una base de datos externa.

### 📄 `logback-spring.xml`
Ubicado en `src/main/resources/logback-spring.xml`, este archivo define la configuración del sistema de logs de la aplicación, incluyendo:

 - Nivel de logs (INFO, DEBUG, ERROR, etc.)
 - Formato de los mensajes
 - Salida por consola o archivo (según configuración)
 - Filtros o patrones por paquete o clase

Spring Boot lo carga automáticamente al arrancar la aplicación.

### Notas sobre la configuracion

Es una configuración simple, por lo que está hecha en un solo archivo. Podríamos tener configuraciones
diferentes por perfil.

Hay dos perfiles posibles (dev y prod), que solo afectan a cómo se generan los logs (ver **logback-spring.xml**).
Hecho a modo de ejemplo.

---

## ▶️ Ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Foncu1980/price-service-app.git
   cd price-service-app
   ```

2. Construye el proyecto (esto ejecutará todos los tests; añade -DskipTests para omitirlos):
   ```bash
   mvn clean install
   ```

3.  Ejecuta la aplicación desde el módulo de arranque (`bootstrap`):
   ```bash
   mvn spring-boot:run -pl bootstrap
   ```
  
4. La aplicación se iniciará en http://localhost:8080 (puerto por defecto).

### Alternativa para arrancar la aplicación

1. Compila el proyecto generando un jar ejecutable

  ```bash
  mvn clean package -DskipTests
  ```

2. Ejecuta el .jar con java:

  ```bash
  java -jar bootstrap/target/bootstrap-1.3.0.jar
  ```

### Si no se tiene maven instalado, se puede usar el script Maven Wrapper incluido en el proyecto: **mvnw** (mvnw.cmd en windows)

  ```bash
  ./mvnw clean install
  ./mvnw spring-boot:run -pl bootstrap
  ```

---

### 📊 Observabilidad y métricas
El proyecto incluye un interceptor HTTP personalizado que recopila métricas básicas de las peticiones entrantes, utilizando Micrometer y compatible con Prometheus.

Las métricas estarán disponibles en:

- [📊 Prometheus Metrics](http://localhost:8080/actuator/prometheus)

Se registran automáticamente los siguientes contadores:

- **`http_requests_total{method, uri, status}`**
  Registra cada petición, etiquetada por método (`GET`, `POST`, etc.), URI y código de estado HTTP (`200`, `404`, `400`, `500`.).
  📌 *Ejemplo:*
  ```text
  http_requests_total{method="GET",status="200",uri="/prices/applicable"} 11.0
  http_requests_total{method="GET",status="404",uri="/prices/applicable"} 1.0
  ```

- **`http_requests_global_total`**
  Contador agregado del total de peticiones gestionadas (exitosas y con error).
  📌 *Ejemplo:*
  ```text
  http_requests_global_total 12.0
  ```

#### Exclusiones

Para mantener la métrica limpia y centrada en el uso real de la API, se han excluido las siguientes rutas:

 - /favicon.ico
 - /swagger-ui
 - /swagger-ui.html
 - /v3/api-docs

Estas métricas permiten una integración sencilla con herramientas como Prometheus y Grafana, facilitando
la monitorización del comportamiento y la disponibilidad de la API en entornos de desarrollo y producción.

#### 🔎 Nota
Además de las métricas personalizadas implementadas en este proyecto, el endpoint de Prometheus expone automáticamente muchas otras métricas proporcionadas por Spring Boot Actuator y Micrometer (como uso de conexiones JDBC, tiempo de respuesta HTTP, etc.). Estas métricas estándar pueden ser útiles para el monitoreo general del sistema, pero no forman parte explícita de la lógica de este servicio.

---

### 🔐 Seguridad de la API

La aplicación incorpora un filtro de autenticación basado en tokens Bearer para proteger el endpoint /prices/applicable.

#### 🔑 ¿Cómo funciona?

Cualquier solicitud a la API requiere el envío de un header Authorization con un token Bearer válido.

Si no se incluye o el formato es incorrecto, se devuelve un error 401 Unauthorized en formato JSON, gestionado de forma centralizada.

Las rutas relacionadas con Swagger UI, OpenAPI y Actuator están abiertas para facilitar el desarrollo y la monitorización.

📘 Ejemplo de llamada autenticada

```http
GET /prices/applicable?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1
Authorization: Bearer dummy-token
```

🐚 Ejemplo usando curl:

```bash
curl -H "Authorization: Bearer dummy-token" \
     "http://localhost:8080/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

🧪 Token dummy para desarrollo
Durante el desarrollo, se usa token dummy fijo configurado en application.properties:

```ini
security.token=12345678
```

Esto permite probar la funcionalidad con una autenticación muy básica, ya que no hay ningun
sistema de autenticación externo.

El filtro de seguridad verifica que el token recibido coincida con el valor configurado.

---

## 📡 Cómo Probar la Aplicación

La documentación interactiva de la API estará disponible en:

- [📘 Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [📄 OpenAPI JSON (v3)](http://localhost:8080/v3/api-docs)

El servicio expone un único **endpoint REST** para consultar precios:

### `GET /prices/applicable`

Consulta el precio aplicable a un producto en una cadena específica, en una fecha y hora determinadas.

### 🔸 Parámetros de entrada (obligatorios)

| Parámetro        | Tipo          | Descripción                                                              | Ejemplo               |
|------------------|---------------|--------------------------------------------------------------------------|-----------------------|
| `applicationDate`| LocalDateTime | Fecha y hora de aplicación (formato ISO-8601: `yyyy-MM-dd'T'HH:mm:ss`)   | `2020-06-14T10:00:00` |
| `productId`      | Long          | Identificador del producto                                               | `35455`               |
| `brandId`        | Long          | Identificador de la cadena                                               | `1`                   |

> ℹ️ **Nota sobre el formato de fecha**
> Aunque en el enunciado se muestran las fechas con el formato `yyyy-MM-dd-HH.mm.ss`, en esta implementación se utiliza el formato **ISO-8601 estándar**:
> `yyyy-MM-dd'T'HH:mm:ss`
> Este formato es el que espera Spring Boot por defecto con `@DateTimeFormat(iso = ISO.DATE_TIME)`, y garantiza una mejor compatibilidad con herramientas como Swagger, Postman y curl.


### ✅ Ejemplo de petición exitosa con `curl`

```bash
curl "http://localhost:8080/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

📥 Respuesta Exitosa (`200 OK`)

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50,
  "curr": "EUR"
}
```

### ✅ Ejemplo de petición fallida con `curl`

```bash
curl -X GET "http://localhost:8080/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=2"
```

📥 Respuesta No encontrada (`404`)

```json
{
   "timestamp":"2025-06-14T22:55:54.394312385",
   "message":"No se encontró un precio para el producto 35455, cadena 2 en la fecha 2020-06-14T10:00",
   "error":"Not Found",
   "status":404}
```

### ✅ Ejemplo de las peticiones para las pruebas pedidas

Puedes usar curl o cualquier cliente REST (como Postman) para probar el endpoint.

1. Test 1: Petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)

```bash
curl "http://localhost:8080/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

Resultado esperado: Tarifa 1 (precio 35.50)

2. Test 2: Petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)

```bash
curl "http://localhost:8080/prices/applicable?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

Resultado esperado: Tarifa 2 (precio 25.45) - Mayor prioridad

3. Test 3: Petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)

```bash
curl "http://localhost:8080/prices/applicable?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1"
```

Resultado esperado: Tarifa 1 (precio 35.50)

4. Test 4: Petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)

```bash
curl "http://localhost:8080/prices/applicable?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1"
```

Resultado esperado: Tarifa 3 (precio 30.50)

5. Test 5: Petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)

```bash
curl "http://localhost:8080/prices/applicable?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1"
```

Resultado esperado: Tarifa 4 (precio 38.95)


## 🧭 Casos de prueba cubiertos

| Test | applicationDate      | productId | brandId | Resultado esperado     |
|------|----------------------|-----------|---------|-------------------------|
| 1    | 2020-06-14T10:00:00  | 35455     | 1       | Tarifa 1 – 35.50 €      |
| 2    | 2020-06-14T16:00:00  | 35455     | 1       | Tarifa 2 – 25.45 €      |
| 3    | 2020-06-14T21:00:00  | 35455     | 1       | Tarifa 1 – 35.50 €      |
| 4    | 2020-06-15T10:00:00  | 35455     | 1       | Tarifa 3 – 30.50 €      |
| 5    | 2020-06-16T21:00:00  | 35455     | 1       | Tarifa 4 – 38.95 €      |

### Casos extra añadidos

| Test | applicationDate     | productId | brandId | Resultado esperado | Comentario                         |
|------|---------------------|-----------|---------|--------------------|------------------------------------|
| 6    | 2020-06-14T15:00:00 | 35455     | 1       | Tarifa 2 – 25.45 € | Prueba justo al inicio de vigencia |
| 7    | 2020-06-14T18:30:00 | 35455     | 1       | Tarifa 2 – 25.45 € | Prueba justo al final de vigencia  |
| 8    | 2020-06-13T10:00:00 | 35455     | 1       | 404 Not Found      | Fecha fuera de rango               |
| 9    | 2020-06-14T10:00:00 | 9999      | 1       | 404 Not Found      | ProductId no existe                |
| 10   | 14-06-2020T10:00:00 | 35455     | 1       | 400 Bad Request    | Formato de fecha erroneo           |
| 11   | 2020-06-14T10:00:00 |           | 1       | 400 Bad Request    | Falta parametro productId          |
| 12   | 2020-06-14T10:00:00 | 35455     | -1      | 400 Bad Request    | BrandId negativo                   |

### Pruebas de integración

La clase `PriceControllerIntegrationTest`, ubicada en

```
bootstrap/src/test/java/com/bcnc/ecommerce/priceservice/PriceControllerIntegrationTest.java
```

contiene los tests de integración que validan el endpoint `/prices/applicable`. Se incluyen:

- Los 5 casos funcionales definidos en el enunciado
- Casos adicionales de validación y control de errores
- Ejecución con contexto real de Spring Boot y base de datos H2

Los resultados esperados están documentados en el apartado anterior.

#### 🔎 Nota
Esta sección se ha centrado en los tests de integración requeridos por el enunciado del ejercicio.
Adicionalmente, se han implementado pruebas unitarias para las clases clave de la lógica de negocio,
asegurando su correcto funcionamiento de forma aislada.

---

## ✅ Criterios de evaluación

- ✔️ **Entregar la prueba en un repositorio** – Proyecto publicado en GitHub.
- ✔️ **Arquitectura Hexagonal** – Separación de responsabilidades entre dominio, aplicación, infraestructura y adaptadores, siguiendo el patrón Ports and Adapters.
- ✔️ **Eficiencia de la extracción de datos** – Consulta optimizada por fecha, producto y cadena, usando `ORDER BY priority DESC` y limitando el resultado a la mejor coincidencia.
- ✔️ **Pruebas de integración solicitadas en el enunciado** – Incluye los 5 tests requeridos y casos adicionales para errores, bordes y validaciones.
- ✔️ **Claridad de código** – Código legible, organizado, con nomenclatura coherente y uso apropiado de JavaDoc.
- ✔️ **Endpoint GET con buenas prácticas** – Uso de `@GetMapping`, `@RequestParam`, validaciones con `@DateTimeFormat`, y DTOs bien definidos.
- ✔️ **Inicializar con los datos del ejemplo al arrancar la aplicación - H2** – Se cargan `schema.sql` y `data.sql` automáticamente en una base de datos en memoria H2.
- ✔️ **Readme** – Documento completo con instrucciones de ejecución, pruebas, estructura del proyecto y criterios de evaluación cumplidos.
- ✔️ **SOLID** – Aplicación de principios SOLID en servicios, entidades, puertos y adaptadores.
- ✔️ **API REST** – Endpoint REST documentado, correctamente estructurado y documentado con OpenAPI/Swagger.
- ✔️ **Eficiencia** – Lógica de negocio centrada y optimizada en la capa de dominio con retorno único y consultas eficientes.
- ✔️ **Testing** – Tests de integración robustos y cubrimiento de múltiples escenarios de éxito y error.
- ✔️ **Claridad de código** – Código autoexplicativo, modular, siguiendo buenas prácticas de diseño.
- ✔️ **Control de versiones** – Proyecto versionado correctamente en Git (tags: `v1.0.0`, `v1.1.0`...), lista para futuras extensiones.
- ✔️ **Configuración** – Uso de `application.properties` y `logback-spring.xml`, con perfiles separados (`dev`, `prod`) y logeo ajustable.
- ✔️ **Devolver único resultado** – Retorna exclusivamente la tarifa aplicable con mayor prioridad.
- ✔️ **Parámetros de entrada: fecha, id de cadena e id de producto** – Validados correctamente con mensajes de error claros.
- ✔️ **Retorno: id producto, id cadena, tarifa a aplicar, fechas y precio** – Respuesta estructurada mediante DTO `PriceResponse`, cumpliendo con los requisitos.

---

## 📌 Control de versiones

El proyecto sigue una estrategia de versionado basada en [SemVer](https://semver.org/lang/es/), combinada con el uso del sufijo `-SNAPSHOT` para indicar versiones en desarrollo.

### Funcionamiento del versionado

- Durante el desarrollo de una funcionalidad o iteración, se utiliza una versión con el sufijo `-SNAPSHOT`, por ejemplo: `1.1.0-SNAPSHOT`. Esto indica que el código está en evolución y aún no es una versión final.
- Una vez que se completa y valida una versión, se **elimina el sufijo `-SNAPSHOT`** y se libera como versión **estable**, por ejemplo: `1.1.0`.
- En ese momento, se crea una etiqueta (`tag`) en el repositorio Git con el número de versión correspondiente:
  ```bash
  git tag -a v1.1.0 -m "Versión estable 1.1.0"
  git push origin v1.1.0

- Después de publicar una versión estable (`1.1.0`), se actualiza el `pom.xml` para iniciar una nueva iteración de desarrollo con la siguiente versión: `1.2.0-SNAPSHOT`.
- El número de versión se define y gestiona en los archivos `pom.xml` del proyecto.
- En proyectos más grandes, este proceso puede automatizarse usando herramientas como `maven-release-plugin` junto con Jenkins.

### 🕒 Historial de evolución

| Versión            | Commit     | Descripción                                                                                      | Fecha       |
|--------------------|------------|--------------------------------------------------------------------------------------------------|-------------|
| `1.2.0-SNAPSHOT`   | `f90de7a`  | Mejora de la documentación y pequeña refactorización                                             | 18-jun-2025 |
| `1.2.0-SNAPSHOT`   | `d1da892`  | Preparación de la siguiente iteración de desarrollo                                              | 18-jun-2025 |
| `v1.1.0`           | `55e3bdb`  | Versión estable con documentación Swagger UI y API doc                                           | 18-jun-2025 |
| `1.1.0-SNAPSHOT`   | `4121d2b`  | Info sobre Swagger UI y api-doc añadida                                                          | 17-jun-2025 |
| `1.1.0-SNAPSHOT`   | `58d0bf9`  | Añadiendo documentación Swagger UI y API doc                                                     | 17-jun-2025 |
| `1.1.0-SNAPSHOT`   | `0aff188`  | Cambio en README para mayor claridad                                                             | 17-jun-2025 |
| `1.1.0-SNAPSHOT`   | `72352cb`  | Preparación de la siguiente iteración de desarrollo                                              | 17-jun-2025 |
| `v1.0.0`           | `00742d2`  | Versión inicial estable: servicio REST funcional, pruebas completas, H2, arquitectura hexagonal  | 17-jun-2025 |

---

## 🔐 Consideraciones de Seguridad

Aunque este proyecto se ha desarrollado como un ejercicio técnico centrado en arquitectura, diseño y pruebas
funcionales, se ha incorporado una simulación básica de autenticación mediante un filtro personalizado que valida
un token Bearer fijo.

Esta solución permite ilustrar de forma sencilla el control de acceso al endpoint principal sin depender de
sistemas externos.

En un entorno real, se aplicarían medidas de seguridad más robustas, tales como:

- **Autenticación y autorización**: Uso de estándares como OAuth2, con tokens JWT firmados y validados por un
servidor de autorización (Authorization Server). Esto permitiría una gestión segura y escalable de los accesos.
- **Rate limiting y control de acceso**: Implementación de límites de peticiones (por IP, cliente o token) para evitar 
abusos o ataques de denegación de servicio (DoS)
- **Uso obligatorio de HTTPS**: Todas las comunicaciones deberían estar cifradas mediante HTTPS, protegidas por
certificados válidos. En entornos reales, esto se logra mediante despliegue detrás de proxies inversos
o balanceadores de carga que fuerzan HTTPS.
- **Análisis de vulnerabilidades**: En un entorno real, se integraría un análisis automático de vulnerabilidades
para garantizar que las librerías utilizadas estén actualizadas y libres de riesgos conocidos.
- **Auditoría y trazabilidad**: Registro de intentos de acceso, tokens utilizados, errores y patrones sospechosos
para facilitar el análisis forense y la monitorización.

---

## 🛠️ Otras mejoras

- **Añadir `traceId` o `requestId` en logs**: para facilitar el seguimiento de peticiones y la trazabilidad en
entornos distribuidos.
- **Integrar SonarQube**: como herramienta de análisis estático para asegurar la calidad del código
y detectar errores potenciales.
- **Implementar caché en la lógica de precios**: mejorar el rendimiento en escenarios de alta concurrencia mediante
caching de precios ya calculados (por ejemplo con Caffeine o Redis).
- **Pipeline CI/CD**: automatizar el proceso de build, test y despliegue mediante herramientas como GitHub Actions,
Jenkins o GitLab CI.
- **Internacionalización**: preparar la API para devolver mensajes en distintos idiomas, especialmente útil si el
producto tiene alcance internacional.
- **Contenerización y despliegue en entornos cloud-native** Crear un `Dockerfile` y configuraciones para
Kubernetes (Helm, Kustomize) con el objetivo de facilitar despliegues en plataformas cloud como AWS, Azure o GCP.

---

## 🧑‍💻 Autor y Fecha

- **Autor**: Francisco Javier Dávila Foncuverta
- **Fecha de creación**: 16 de junio de 2025
- **Última actualización**: 18 de junio de 2025