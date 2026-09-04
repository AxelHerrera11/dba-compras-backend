# Base de Datos Oracle

Este proyecto usa Oracle Database para la base existente `DBA_COMPRAS`.

## Conexion

- Motor: Oracle Database
- Driver Java: Oracle JDBC `ojdbc11`
- Host recomendado: `svr-sql-ctezo.southcentralus.cloudapp.azure.com`
- IP de referencia: `52.171.58.51`
- Puerto: `1521`
- Service Name: `FREEPDB1`
- Usuario: `DBA_COMPRAS`

Las pruebas de consola de Base de Datos toman la configuracion desde variables de entorno o desde el archivo local `.env`:

```properties
ORACLE_HOST=svr-sql-ctezo.southcentralus.cloudapp.azure.com
ORACLE_PORT=1521
ORACLE_SERVICE=FREEPDB1
ORACLE_USER=DBA_COMPRAS
ORACLE_PASSWORD=
```

El archivo `.env` local debe contener la contrasena real y no debe subirse a Git. El archivo versionable es `.env.example`.

La configuracion runtime del Backend no forma parte de este apartado. Las APIs, controladores, servicios, repositorios y configuracion de ejecucion quedan a cargo de los integrantes responsables de Backend.

## Tablas Principales

- `TBL_CLIENTES`
- `TBL_MARCAS`
- `TBL_TARJETAS`
- `TBL_CATEGORIAS`
- `TBL_PRODUCTOS`
- `TBL_ENC_COMPRAS`
- `TBL_DET_COMPRAS`

Las tablas ya existen en Oracle. No se deben crear migraciones ni ejecutar scripts que recreen, alteren o eliminen la estructura.

## Relaciones Validadas

- `TBL_TARJETAS.ID_CLIENTE` -> `TBL_CLIENTES.ID_CLIENTE`
- `TBL_TARJETAS.ID_MARCA` -> `TBL_MARCAS.ID_MARCA`
- `TBL_PRODUCTOS.ID_CATEGORIA` -> `TBL_CATEGORIAS.ID_CATEGORIA`
- `TBL_ENC_COMPRAS.ID_CLIENTE` -> `TBL_CLIENTES.ID_CLIENTE`
- `TBL_ENC_COMPRAS.ID_TARJETA` -> `TBL_TARJETAS.ID_TARJETA`
- `TBL_DET_COMPRAS.ID_COMPRA` -> `TBL_ENC_COMPRAS.ID_COMPRA`
- `TBL_DET_COMPRAS.ID_PRODUCTO` -> `TBL_PRODUCTOS.ID_PRODUCTO`

## Pruebas Desde Consola

Todas las pruebas son de lectura y ejecutan unicamente consultas `SELECT`.

Compilar clases de prueba:

```bash
mvn -q -DskipTests test-compile
```

Probar conexion:

```bash
mvn -q -DskipTests test-compile exec:java "-Dexec.classpathScope=test" "-Dexec.mainClass=com.grupo2.dbacompras.database.OracleDatabaseConsoleTest" "-Dexec.args=connection"
```

Validar las 7 tablas:

```bash
mvn -q -DskipTests test-compile exec:java "-Dexec.classpathScope=test" "-Dexec.mainClass=com.grupo2.dbacompras.database.OracleDatabaseConsoleTest" "-Dexec.args=tables"
```

Consultar conteos:

```bash
mvn -q -DskipTests test-compile exec:java "-Dexec.classpathScope=test" "-Dexec.mainClass=com.grupo2.dbacompras.database.OracleDatabaseConsoleTest" "-Dexec.args=counts"
```

Validar relaciones FK:

```bash
mvn -q -DskipTests test-compile exec:java "-Dexec.classpathScope=test" "-Dexec.mainClass=com.grupo2.dbacompras.database.OracleDatabaseConsoleTest" "-Dexec.args=relations"
```

Validacion estructural completa:

```bash
mvn -q -DskipTests test-compile exec:java "-Dexec.classpathScope=test" "-Dexec.mainClass=com.grupo2.dbacompras.database.OracleDatabaseConsoleTest" "-Dexec.args=all"
```

## Resultados Obtenidos

Conexion:

```text
[OK] Conexion a Oracle establecida correctamente.
SELECT 1 FROM DUAL -> 1
```

Tablas:

```text
[OK] TBL_CLIENTES
[OK] TBL_MARCAS
[OK] TBL_TARJETAS
[OK] TBL_CATEGORIAS
[OK] TBL_PRODUCTOS
[OK] TBL_ENC_COMPRAS
[OK] TBL_DET_COMPRAS

[OK] 7 de 7 tablas disponibles.
```

Conteos actuales de referencia:

```text
TBL_CLIENTES       -> 38 registros
TBL_MARCAS         -> 8 registros
TBL_TARJETAS       -> 162 registros
TBL_CATEGORIAS     -> 20 registros
TBL_PRODUCTOS      -> 120 registros
TBL_ENC_COMPRAS    -> 419 registros
TBL_DET_COMPRAS    -> 1028 registros
```

Relaciones FK:

```text
[OK] TBL_TARJETAS.ID_CLIENTE -> TBL_CLIENTES.ID_CLIENTE
[OK] TBL_TARJETAS.ID_MARCA -> TBL_MARCAS.ID_MARCA
[OK] TBL_PRODUCTOS.ID_CATEGORIA -> TBL_CATEGORIAS.ID_CATEGORIA
[OK] TBL_ENC_COMPRAS.ID_CLIENTE -> TBL_CLIENTES.ID_CLIENTE
[OK] TBL_ENC_COMPRAS.ID_TARJETA -> TBL_TARJETAS.ID_TARJETA
[OK] TBL_DET_COMPRAS.ID_COMPRA -> TBL_ENC_COMPRAS.ID_COMPRA
[OK] TBL_DET_COMPRAS.ID_PRODUCTO -> TBL_PRODUCTOS.ID_PRODUCTO

[OK] 7 de 7 relaciones FK disponibles.
```

## Alcance

Este apartado contiene documentacion, diagrama ER y pruebas de validacion de Base de Datos desde consola. No crea endpoints ni infraestructura runtime para el Backend.
