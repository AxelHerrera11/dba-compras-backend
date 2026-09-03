# API RESTful de Análisis y Reportería de Compras — Grupo 2

Proyecto del curso, sobre la base de datos Oracle `DBA_COMPRAS`. Backend en **Java Spring Boot**, Frontend en **JavaScript** (framework libre).

## Integrantes y roles

| Carné | Integrante | Rol |
|---|---|---|
| 1890-23-3193 | **Axel Eliú Herrera Sánchez** | Backend lead: esqueleto del proyecto, conexión a Oracle, arquitectura en capas, endpoints de **Compras** |
| 1890-21-11156 | **Lis Ivette Rosales Colindrez** | Frontend lead: estructura del dashboard, KPIs, consumo de la API |
| 1890-23-7082 | **Gerson Giovanni Orellana Véliz** | Backend: endpoints de **Clientes** |
| 1890-23-2681 | **Javier Alexander Fajardo López** | Backend: endpoints de **Productos** |
| 1890-23-12105 | **Albino Sebastian Rosales Ruano** | Backend: endpoints de **Tarjetas** |
| 1890-23-8474 | **Didhyer Alexander Ortíz Guevara** | Frontend: gráficas de Ventas por mes, Top 10 clientes, Top 10 productos |
| 1890-23-4982 | **Keily Fabiola Orellana Marroquín** | Frontend: gráficas de Categoría/Tarjetas/Crédito vs Débito, y filtros (fecha, cliente, categoría, producto) |
| 1890-23-22889 | **Hugo David Moscoso Castro** | QA y documentación: pruebas de cada endpoint con Postman, checklist de validación, esqueleto del PPT |
| 1890-23-10832 | **María de los Ángeles López Fajardo** | Base de datos: diagrama ER, consultas SQL base, validación de estructura de las 7 tablas |

## Distribución detallada de endpoints

### Clientes (Gerson)
- `GET /api/clientes/top10`
- `GET /api/clientes/sin-compras`
- `GET /api/clientes/mayor-consumo`
- `GET /api/clientes/por-genero`

### Productos (Javier)
- `GET /api/productos/top10`
- `GET /api/productos/sin-ventas`
- `GET /api/productos/por-categoria`

### Tarjetas (Albino)
- `GET /api/tarjetas/mas-utilizadas`
- `GET /api/tarjetas/credito-vs-debito`
- `GET /api/tarjetas/por-marca`

### Compras (Axel)
- `GET /api/compras/por-mes`
- `GET /api/compras/por-anio`
- `GET /api/compras/promedio`

> Cada endpoint debe: conectarse a Oracle, ejecutar consultas parametrizadas, retornar JSON válido (usando el wrapper `ApiResponse`), y manejar errores (ya cubierto por `GlobalExceptionHandler`, no hace falta repetir try/catch en cada controller).

## Cómo está organizado el código

```
src/main/java/com/grupo2/dbacompras/
├── DbaComprasApplication.java   # punto de entrada
├── config/CorsConfig.java       # habilita que el frontend consuma la API
├── controller/                  # un @RestController por dominio (Clientes, Productos, Tarjetas, Compras)
├── service/                     # logica de negocio y validaciones
├── repository/                  # consultas a Oracle (JPQL o nativas con @Query)
├── entity/                      # clases mapeadas a las tablas (@Entity)
├── dto/                         # formas de salida hacia el frontend (records)
└── exception/                   # ApiException + manejador global de errores
```

**Patrón a seguir para cada endpoint nuevo:** `Repository` (consulta) → `Service` (validación + mapeo a DTO) → `Controller` (expone la ruta). Ya hay un ejemplo completo funcionando en el módulo de Clientes (`top10` y `sin-compras`) — úsenlo como plantilla, no reinventen la estructura.

## Cómo levantar el proyecto

1. Requiere **Java 21** y **Maven** instalados.
2. La conexión a Oracle ya está configurada en `src/main/resources/application.properties` (servidor, usuario y clave del curso).
3. Para correr el proyecto:
   ```bash
   ./mvnw spring-boot:run
   ```
4. La API queda disponible en `http://localhost:8080/api/...`
5. Antes de agregar un endpoint nuevo, revisen la estructura real de su tabla en Oracle con `DESCRIBE NOMBRE_TABLA;` — las entidades de ejemplo usan nombres de columna supuestos que hay que confirmar.

## Checklist de entrega (referencia para Hugo)

- [ ] Los 12 endpoints responden JSON válido
- [ ] Cada endpoint maneja parámetros inválidos sin tumbar el servidor
- [ ] CORS habilitado y probado desde el frontend real (no solo Postman)
- [ ] Dashboard muestra los 4 KPIs (total compras, clientes con compras, monto total, ticket promedio)
- [ ] Las 6 gráficas mínimas están conectadas a datos reales (no dummy)
- [ ] Filtros de fecha, cliente, categoría y producto funcionan
- [ ] PPT listo para la presentación presencial
