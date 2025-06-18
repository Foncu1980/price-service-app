# 🧾 Price Service

Este proyecto implementa un servicio REST en Spring Boot para consultar el precio aplicable de un producto, para una marca específica, en una fecha y hora dadas.

La aplicación sigue los principios de la Arquitectura Hexagonal, el diseño SOLID, y utiliza una base de datos en memoria H2 para la persistencia.

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
Ubicado en `src/main/resources/logback-spring.xml`, este archivo define la configuración de logeo de la aplicación, incluyendo:

 - Nivel de logs (INFO, DEBUG, ERROR, etc.)
 - Formato de los mensajes
 - Salida por consola o archivo (según configuración)
 - Filtros o patrones por paquete o clase

Spring Boot lo carga automáticamente al arrancar la aplicación.

### Notas sobre la configuracion

Es una configuración simple, por lo que está hecha en un solo archivo. Podríamos tener configuraciones
diferentes por perfil.

Hay dos perfiles posibles (dev y prod), que solo afectan a como se generan los logs (ver **logback-spring.xml**). Hecho a modo de ejemplo.

---

## ▶️ Ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Foncu1980/price-service.git
   cd price-service
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

### Alternativa para arrancar la aplicacion

1. Compila el proyecto generando un jar ejecutable

  ```bash
  mvn clean package -DskipTests
  ```

2. Ejecuta el .jar con java:

  ```bash
  java -jar bootstrap/target/bootstrap-1.1.0.jar
  ```

### Si no se tiene maven instalado, se puede usar el script Maven Wrapper incluido en el proyecto: **mvnw** (mvnw.cmd en windows)

  ```bash
  ./mvnw clean install
  ./mvnw spring-boot:run -pl bootstrap
  ```

---

## 📡 Cómo Probar la Aplicación

La documentación interactiva de la API estará disponible en:

- [📘 Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [📄 OpenAPI JSON (v3)](http://localhost:8080/v3/api-docs)

El servicio expone un único **endpoint REST** para consultar precios:

### `GET /prices/calculate`

Consulta el precio aplicable a un producto en una marca concreta, en una fecha y hora determinadas.

### 🔸 Parámetros de query (obligatorios)

| Parámetro        | Tipo   | Descripción                                                     | Ejemplo               |
|------------------|--------|-----------------------------------------------------------------|-----------------------|
| `applicationDate`| String | Fecha y hora de aplicación (formato `yyyy-MM-dd'T'HH:mm:ss`)    | `2020-06-14T10:00:00` |
| `productId`      | Long   | Identificador del producto                                      | `35455`               |
| `brandId`        | Long   | Identificador de la marca                                       | `1`                   |

### ✅ Ejemplo de petición exitosa con `curl`

```bash
curl "http://localhost:8080/prices/calculate?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
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
curl -X GET "http://localhost:8080/prices/calculate?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=2"
```

📥 Respuesta No encontrada (`404`)

```json
{
   "timestamp":"2025-06-14T22:55:54.394312385",
   "message":"No se encontró un precio para el producto 35455, marca 2 en la fecha 2020-06-14T10:00",
   "error":"Not Found",
   "status":404}
```

### ✅ Ejemplo de las peticiones para las pruebas pedidas

Puedes usar curl o cualquier cliente REST (como Postman) para probar el endpoint.

1. Test 1: Petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)

```bash
curl "http://localhost:8080/prices/calculate?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

Resultado esperado: Tarifa 1 (precio 35.50)

2. Test 2: Petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)

```bash
curl "http://localhost:8080/prices/calculate?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

Resultado esperado: Tarifa 2 (precio 25.45) - Mayor prioridad

3. Test 3: Petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)

```bash
curl "http://localhost:8080/prices/calculate?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1"
```

Resultado esperado: Tarifa 1 (precio 35.50)

4. Test 4: Petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)

```bash
curl "http://localhost:8080/prices/calculate?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1"
```

Resultado esperado: Tarifa 3 (precio 30.50)

5. Test 5: Petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)

```bash
curl "http://localhost:8080/prices/calculate?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1"
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

contiene los tests de integración que validan el endpoint `/prices/calculate`. Se incluyen:

- Los 5 casos funcionales definidos en el enunciado
- Casos adicionales de validación y control de errores
- Ejecución con contexto real de Spring Boot y base de datos H2

Los resultados esperados están documentados en el apartado anterior.


## ✅ Criterios de evaluación

- ✔️ **Arquitectura hexagonal** – Separación clara entre dominio, aplicación e infraestructura.
- ✔️ **Diseño basado en DDD** – Uso correcto de entidades, puertos y casos de uso.
- ✔️ **Principios SOLID** – Aplicación de SRP, DIP y otros principios clave.
- ✔️ **Servicio REST bien estructurado** – Uso de `@RestController`, DTOs, `ResponseEntity`, etc.
- ✔️ **Validación y formato de fechas** – Control robusto con `@DateTimeFormat` y validaciones de entrada.
- ✔️ **Manejo de errores** – Respuestas 404 cuando no se encuentra un precio aplicable.
- ✔️ **Tests de integración completos** – Verificación de todos los escenarios funcionales requeridos.
- ✔️ **Inicialización automática de datos** – Script `data.sql` cargado en arranque para entorno H2.
- ✔️ **JavaDoc completo** – Documentación presente en las clases públicas clave.
- ✔️ **Código limpio y modularizado** – Nomenclatura clara, estructura de paquetes coherente y uso correcto de Maven multi-módulo.

---

## 🔐 Consideraciones de Seguridad (no implementadas en esta versión)

Este proyecto ha sido desarrollado como un ejercicio técnico centrado en arquitectura, diseño y pruebas funcionales. Por tanto, no se han incorporado mecanismos de seguridad, pero se tendrían en cuenta para un entorno real.
Entre las medidas que se plantearían podrían estar:

- **Autenticación y autorización**: Integración con mecanismos como OAuth2, utilizando tokens JWT como formato habitual de autenticación.
- **Rate limiting y control de acceso**: Para proteger el endpoint público de abusos o llamadas no autorizadas.
- **Uso obligatorio de HTTPS**: Aunque esta aplicación está pensada para ejecutarse en local, en un entorno real se desplegaría detrás de un proxy o balanceador de carga que obligue al uso de HTTPS para proteger la confidencialidad e integridad del tráfico.
- **Análisis de dependencias**: En un entorno real, se integraría un análisis automático de vulnerabilidades para garantizar que las librerías utilizadas estén actualizadas y libres de riesgos conocidos.

---

## Otras mejoras:

- **Añadir traceId o requestId en logs**
- **Tener en cuenta métricas como el número de peticiones, peticiones correctas, erróneas, etc.**
- **Considerar la integración de SonarQube como herramienta de análisis estático para asegurar la calidad del código.**

---

## 🧑‍💻 Autor y Fecha

- **Autor**: Francisco Javier Dávila Foncuverta
- **Fecha**: 16 de junio de 2025
