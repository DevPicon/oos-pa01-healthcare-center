# API de Centros de Salud — Controladores y Operaciones

Este documento lista todas las operaciones HTTP expuestas por los controladores del paquete `controller` de este proyecto.

URL base (Spring Boot por defecto): `http://localhost:8080`

Nota: Todos los endpoints devuelven códigos de estado HTTP estándar. `404 Not Found` se usa cuando una entidad no existe; `400 Bad Request` para entradas inválidas; `201 Created` cuando la creación es exitosa; `204 No Content` cuando la eliminación es exitosa.

## Instrucciones iniciales: levantar Docker (docker-compose)

Antes de iniciar la aplicación, levanta la base de datos PostgreSQL con Docker Compose.

Requisitos previos:
- Docker (20+ recomendado) y Docker Compose v2 instalados.

Pasos:
1) En la raíz del proyecto, inicia los servicios definidos en `docker-compose.yml`:
   ```
   docker compose up -d
   ```
2) Verifica que el contenedor esté saludable y corriendo:
   ```
   docker compose ps
   docker compose logs -f postgres
   ```
   Espera a ver el mensaje de que PostgreSQL está listo para aceptar conexiones.
3) Credenciales y conexión (usadas por la app Spring):
   - Host: `localhost`
   - Puerto: `5432`
   - Base de datos: `healthcare_center`
   - Usuario: `admin`
   - Password: `admin123`
   Estos valores ya están configurados en `src/main/resources/application.properties`.

4) Cuando termines, detén y elimina los contenedores:
   ```
   docker compose down
   ```
   Si además quieres borrar los volúmenes (datos persistidos):
   ```
   docker compose down -v
   ```

Luego, ejecuta la aplicación Spring Boot (ejemplos):
```
./mvnw spring-boot:run
```

La API quedará disponible en `http://localhost:8080`.


## Uso de la API con Postman

Para facilitar la prueba y la interacción con la API, se proporciona una colección de Postman que incluye todos los endpoints disponibles.

1.  **Importar la Colección**:
    - El archivo de la colección se encuentra en el directorio `_docs`:
      ```
      _docs/postman_collection.json
      ```
    - Abre Postman, ve a `File > Import...` y selecciona este archivo.

2.  **Configurar la URL Base**:
    - La colección utiliza una variable `{{baseUrl}}` para la dirección del servidor.
    - Por defecto, esta variable está configurada como `http://localhost:8080`, que es la URL donde se ejecuta la aplicación Spring Boot.
    - Si ejecutas la aplicación en un puerto diferente, puedes cambiar el valor de esta variable en la configuración de la colección en Postman.


## Ejecutar Tests y Generar Reporte de Cobertura

El proyecto incluye una suite completa de tests con **86 tests** que cubren el **83%** del código.

### Ejecutar todos los tests

Para ejecutar todos los tests del proyecto:

```bash
./mvnw test
```

O si necesitas limpiar antes de ejecutar:

```bash
./mvnw clean test
```

### Ejecutar tests de una clase específica

Para ejecutar solo los tests de una clase:

```bash
# Tests de servicios
./mvnw test -Dtest="CentroDeSaludServiceTest"

# Tests de repositorios
./mvnw test -Dtest="CalificacionRepositoryTest"

# Tests de controladores
./mvnw test -Dtest="CentroDeSaludControllerTest"
```

### Ejecutar tests por patrón

Para ejecutar todos los tests de un tipo:

```bash
# Todos los tests de servicios
./mvnw test -Dtest="*ServiceTest"

# Todos los tests de repositorios
./mvnw test -Dtest="*RepositoryTest"

# Todos los tests de controladores
./mvnw test -Dtest="*ControllerTest"
```

### Generar reporte de cobertura con JaCoCo

JaCoCo se ejecuta automáticamente cuando corres los tests. Para generar y visualizar el reporte de cobertura:

```bash
# Ejecutar tests y generar reporte
./mvnw clean test jacoco:report
```

El reporte HTML se genera en:
```
target/site/jacoco/index.html
```

Para abrir el reporte en tu navegador (macOS):
```bash
open target/site/jacoco/index.html
```

En Linux:
```bash
xdg-open target/site/jacoco/index.html
```

En Windows:
```bash
start target/site/jacoco/index.html
```

### Estructura de los tests

El proyecto incluye los siguientes tipos de tests:

- **Tests de Entidades** (14 tests): Prueban la lógica de negocio en las entidades
  - `CalificacionTest.java` - Cálculo de calificaciones
  - `CentroDeSaludTest.java` - Métodos de negocio

- **Tests de Repositorios** (10 tests): Pruebas de integración con `@DataJpaTest`
  - `CalificacionRepositoryTest.java` - Queries personalizadas
  - `CentroDeSaludRepositoryTest.java` - Filtros y búsquedas

- **Tests de Servicios** (36 tests): Tests unitarios con Mockito
  - `CalificacionServiceTest.java`
  - `CentroDeSaludServiceTest.java`
  - `RegionServiceTest.java`
  - `TipoCentroServiceTest.java`
  - `AmbulanciaServiceTest.java`

- **Tests de Controladores** (24 tests): Tests de endpoints con `@WebMvcTest`
  - `CentroDeSaludControllerTest.java`
  - `CalificacionControllerTest.java`

### Cobertura de código

Cobertura actual por paquete:
- **Config (DataLoader)**: 100%
- **Entity**: 96%
- **Service**: 75%
- **Controller**: 64%
- **Total**: 83% ✓

### Requisitos para ejecutar los tests

- Java 21
- Maven 3.6+
- No es necesario que PostgreSQL esté corriendo para los tests (se usa H2 en memoria)

### Configurar Java 21

Si necesitas configurar Java 21:

```bash
# macOS con Homebrew
export JAVA_HOME=/opt/homebrew/opt/openjdk@21
export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"

# Verificar versión
java -version
```


## AmbulanciaController
Ruta base: `/ambulancias`

- GET `/ambulancias`
  - Descripción: Listar todas las ambulancias.
  - Respuesta: `200 OK` con `List<Ambulancia>`

- GET `/ambulancias/{id}`
  - Descripción: Obtener una ambulancia por su ID.
  - Parámetros de ruta: `id` (Long)
  - Respuesta:
    - `200 OK` con `Ambulancia`
    - `404 Not Found` si no existe

- POST `/ambulancias`
  - Descripción: Crear una nueva ambulancia.
  - Cuerpo: `Ambulancia`
  - Respuesta:
    - `201 Created` con la `Ambulancia` creada
    - `400 Bad Request` si los datos son inválidos

- DELETE `/ambulancias/{id}`
  - Descripción: Eliminar una ambulancia por ID.
  - Parámetros de ruta: `id` (Long)
  - Respuesta: `204 No Content`


## CalificacionController
Ruta base: `/calificaciones`

- GET `/calificaciones`
  - Descripción: Listar todas las calificaciones.
  - Respuesta: `200 OK` con `List<Calificacion>`

- GET `/calificaciones/{id}`
  - Descripción: Obtener una calificación por ID.
  - Parámetros de ruta: `id` (Long)
  - Respuesta:
    - `200 OK` con `Calificacion`
    - `404 Not Found` si no existe

- GET `/calificaciones/centro/{centroId}`
  - Descripción: Listar calificaciones de un centro específico.
  - Parámetros de ruta: `centroId` (Long)
  - Respuesta:
    - `200 OK` con `List<Calificacion>` si el centro existe
    - `404 Not Found` si el centro no existe

- POST `/calificaciones`
  - Descripción: Crear una nueva calificación.
  - Cuerpo: `Calificacion`
  - Respuesta:
    - `201 Created` con la `Calificacion` creada
    - `400 Bad Request` si los datos son inválidos

- DELETE `/calificaciones/{id}`
  - Descripción: Eliminar una calificación por ID.
  - Parámetros de ruta: `id` (Long)
  - Respuesta: `204 No Content`


## CentroDeSaludController
Ruta base: `/centros`

- GET `/centros`
  - Descripción: Listar todos los centros de salud.
  - Respuesta: `200 OK` con `List<CentroDeSalud>`

- GET `/centros/con-calificaciones`
  - Descripción: Listar centros de salud incluyendo sus calificaciones relacionadas.
  - Respuesta: `200 OK` con `List<CentroDeSalud>`

- GET `/centros/tipo/{tipo}`
  - Descripción: Listar centros por tipo.
  - Parámetros de ruta: `tipo` (String)
  - Respuesta: `200 OK` con `List<CentroDeSalud>`

- GET `/centros/{id}`
  - Descripción: Obtener un centro de salud por ID.
  - Parámetros de ruta: `id` (Long)
  - Respuesta:
    - `200 OK` con `CentroDeSalud`
    - `404 Not Found` si no existe

- GET `/centros/{id}/estado`
  - Descripción: Obtener estado de aprobación del centro.
  - Parámetros de ruta: `id` (Long)
  - Respuesta:
    - `200 OK` con JSON `{ "aprobado": boolean }`
    - `404 Not Found` si no existe

- GET `/centros/{id}/ambulancias`
  - Descripción: Listar ambulancias asociadas a un centro.
  - Parámetros de ruta: `id` (Long)
  - Respuesta:
    - `200 OK` con `List<Ambulancia>`
    - `404 Not Found` si el centro no existe

- GET `/centros/{id}/ultima-calificacion`
  - Descripción: Obtener la última calificación del centro.
  - Parámetros de ruta: `id` (Long)
  - Respuesta:
    - `200 OK` con JSON `{ "calificacion": number }`
    - `404 Not Found` si no existe

- POST `/centros`
  - Descripción: Crear un nuevo centro de salud.
  - Cuerpo: `CentroDeSalud`
  - Respuesta:
    - `201 Created` con el `CentroDeSalud` creado
    - `400 Bad Request` si los datos son inválidos

- PUT `/centros/{id}/nombre`
  - Descripción: Actualizar el nombre de un centro de salud.
  - Parámetros de ruta: `id` (Long)
  - Cuerpo: JSON `{ "nombre": string }`
  - Respuesta:
    - `200 OK` con el `CentroDeSalud` actualizado
    - `400 Bad Request` si los datos son inválidos

- DELETE `/centros/{id}`
  - Descripción: Eliminar un centro por ID.
  - Parámetros de ruta: `id` (Long)
  - Respuesta: `204 No Content`


## RegionController
Ruta base: `/regiones`

- GET `/regiones`
  - Descripción: Listar todas las regiones.
  - Respuesta: `200 OK` con `List<Region>`

- GET `/regiones/{id}`
  - Descripción: Obtener una región por ID.
  - Parámetros de ruta: `id` (Long)
  - Respuesta:
    - `200 OK` con `Region`
    - `404 Not Found` si no existe

- POST `/regiones`
  - Descripción: Crear una nueva región.
  - Cuerpo: `Region`
  - Respuesta:
    - `201 Created` con la `Region` creada
    - `400 Bad Request` si los datos son inválidos

- DELETE `/regiones/{id}`
  - Descripción: Eliminar una región por ID.
  - Parámetros de ruta: `id` (Long)
  - Respuesta: `204 No Content`


## TipoCentroController
Ruta base: `/tipos-centro`

- GET `/tipos-centro`
  - Descripción: Listar todos los tipos de centro.
  - Respuesta: `200 OK` con `List<TipoCentro>`

- GET `/tipos-centro/{id}`
  - Descripción: Obtener un tipo de centro por ID.
  - Parámetros de ruta: `id` (Long)
  - Respuesta:
    - `200 OK` con `TipoCentro`
    - `404 Not Found` si no existe

- POST `/tipos-centro`
  - Descripción: Crear un nuevo tipo de centro.
  - Cuerpo: `TipoCentro`
  - Respuesta:
    - `201 Created` con el `TipoCentro` creado
    - `400 Bad Request` si los datos son inválidos

- DELETE `/tipos-centro/{id}`
  - Descripción: Eliminar un tipo de centro por ID.
  - Parámetros de ruta: `id` (Long)
  - Respuesta: `204 No Content`


---

## Notas adicionales
- Asegúrate de revisar los modelos de entidad (`entity` package) para conocer la estructura de los objetos enviados y recibidos.
- Para datos de ejemplo cargados automáticamente, ver `config/DataLoader.java`.
- El comportamiento exacto de validación y negocio se define en los servicios correspondientes (`service` package).
