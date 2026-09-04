/*
  Consultas SQL base - DBA_COMPRAS
  Alcance: Base de datos, consultas de lectura para analisis y validacion.
  Motor: Oracle Database
  Importante: este archivo contiene unicamente consultas SELECT.
*/

/* ==================================================
   1. VALIDACION DE ESTRUCTURA
   ================================================== */

/* Validar existencia de las 7 tablas principales */
SELECT table_name
FROM user_tables
WHERE table_name IN (
    'TBL_CLIENTES',
    'TBL_MARCAS',
    'TBL_TARJETAS',
    'TBL_CATEGORIAS',
    'TBL_PRODUCTOS',
    'TBL_ENC_COMPRAS',
    'TBL_DET_COMPRAS'
)
ORDER BY table_name;

/* Validar columnas de las 7 tablas principales */
SELECT table_name,
       column_id,
       column_name,
       data_type,
       nullable
FROM user_tab_columns
WHERE table_name IN (
    'TBL_CLIENTES',
    'TBL_MARCAS',
    'TBL_TARJETAS',
    'TBL_CATEGORIAS',
    'TBL_PRODUCTOS',
    'TBL_ENC_COMPRAS',
    'TBL_DET_COMPRAS'
)
ORDER BY table_name, column_id;

/* Validar PK y FK registradas en Oracle */
SELECT c.table_name,
       c.constraint_name,
       c.constraint_type,
       cc.column_name,
       r.table_name AS referenced_table
FROM user_constraints c
JOIN user_cons_columns cc
  ON c.constraint_name = cc.constraint_name
LEFT JOIN user_constraints r
  ON c.r_constraint_name = r.constraint_name
WHERE c.table_name IN (
    'TBL_CLIENTES',
    'TBL_MARCAS',
    'TBL_TARJETAS',
    'TBL_CATEGORIAS',
    'TBL_PRODUCTOS',
    'TBL_ENC_COMPRAS',
    'TBL_DET_COMPRAS'
)
  AND c.constraint_type IN ('P', 'R')
ORDER BY c.table_name, c.constraint_type, c.constraint_name, cc.position;

/* ==================================================
   2. CLIENTES
   ================================================== */

/* Top 10 clientes por monto comprado */
SELECT c.id_cliente,
       c.primer_nombre,
       c.primer_apellido,
       SUM(d.subtotal) AS total_comprado
FROM tbl_clientes c
JOIN tbl_enc_compras e
  ON e.id_cliente = c.id_cliente
JOIN tbl_det_compras d
  ON d.id_compra = e.id_compra
GROUP BY c.id_cliente, c.primer_nombre, c.primer_apellido
ORDER BY total_comprado DESC
FETCH FIRST 10 ROWS ONLY;

/* Top 10 clientes por cantidad de compras */
SELECT c.id_cliente,
       c.primer_nombre,
       c.primer_apellido,
       COUNT(e.id_compra) AS cantidad_compras
FROM tbl_clientes c
JOIN tbl_enc_compras e
  ON e.id_cliente = c.id_cliente
GROUP BY c.id_cliente, c.primer_nombre, c.primer_apellido
ORDER BY cantidad_compras DESC
FETCH FIRST 10 ROWS ONLY;

/* Clientes sin compras */
SELECT c.id_cliente,
       c.primer_nombre,
       c.primer_apellido,
       c.correo
FROM tbl_clientes c
WHERE NOT EXISTS (
    SELECT 1
    FROM tbl_enc_compras e
    WHERE e.id_cliente = c.id_cliente
)
ORDER BY c.id_cliente;

/* Clientes por genero */
SELECT c.genero,
       COUNT(*) AS total_clientes
FROM tbl_clientes c
GROUP BY c.genero
ORDER BY total_clientes DESC;

/* ==================================================
   3. PRODUCTOS
   ================================================== */

/* Top 10 productos mas vendidos */
SELECT p.id_producto,
       p.nombre_producto,
       SUM(d.cantidad) AS unidades_vendidas
FROM tbl_productos p
JOIN tbl_det_compras d
  ON d.id_producto = p.id_producto
GROUP BY p.id_producto, p.nombre_producto
ORDER BY unidades_vendidas DESC
FETCH FIRST 10 ROWS ONLY;

/* Productos nunca comprados */
SELECT p.id_producto,
       p.nombre_producto
FROM tbl_productos p
WHERE NOT EXISTS (
    SELECT 1
    FROM tbl_det_compras d
    WHERE d.id_producto = p.id_producto
)
ORDER BY p.id_producto;

/* Productos con mayores ingresos */
SELECT p.id_producto,
       p.nombre_producto,
       SUM(d.subtotal) AS ingresos
FROM tbl_productos p
JOIN tbl_det_compras d
  ON d.id_producto = p.id_producto
GROUP BY p.id_producto, p.nombre_producto
ORDER BY ingresos DESC;

/* ==================================================
   4. CATEGORIAS
   ================================================== */

/* Categoria mas consumida */
SELECT ca.id_categoria,
       ca.nombre_categoria,
       SUM(d.cantidad) AS unidades_vendidas
FROM tbl_categorias ca
JOIN tbl_productos p
  ON p.id_categoria = ca.id_categoria
JOIN tbl_det_compras d
  ON d.id_producto = p.id_producto
GROUP BY ca.id_categoria, ca.nombre_categoria
ORDER BY unidades_vendidas DESC
FETCH FIRST 1 ROW ONLY;

/* Categoria menos consumida */
SELECT ca.id_categoria,
       ca.nombre_categoria,
       COALESCE(SUM(d.cantidad), 0) AS unidades_vendidas
FROM tbl_categorias ca
LEFT JOIN tbl_productos p
  ON p.id_categoria = ca.id_categoria
LEFT JOIN tbl_det_compras d
  ON d.id_producto = p.id_producto
GROUP BY ca.id_categoria, ca.nombre_categoria
ORDER BY unidades_vendidas ASC
FETCH FIRST 1 ROW ONLY;

/* Participacion porcentual por categoria */
SELECT ca.id_categoria,
       ca.nombre_categoria,
       SUM(d.subtotal) AS monto_categoria,
       ROUND(SUM(d.subtotal) * 100 / SUM(SUM(d.subtotal)) OVER (), 2) AS participacion_porcentaje
FROM tbl_categorias ca
JOIN tbl_productos p
  ON p.id_categoria = ca.id_categoria
JOIN tbl_det_compras d
  ON d.id_producto = p.id_producto
GROUP BY ca.id_categoria, ca.nombre_categoria
ORDER BY participacion_porcentaje DESC;

/* ==================================================
   5. TARJETAS
   ================================================== */

/* Marca de tarjeta mas utilizada */
SELECT m.id_marca,
       m.nombre_marca,
       COUNT(e.id_compra) AS total_usos
FROM tbl_marcas m
JOIN tbl_tarjetas t
  ON t.id_marca = m.id_marca
JOIN tbl_enc_compras e
  ON e.id_tarjeta = t.id_tarjeta
GROUP BY m.id_marca, m.nombre_marca
ORDER BY total_usos DESC
FETCH FIRST 1 ROW ONLY;

/* Credito vs debito */
SELECT t.tipo_tarjeta,
       COUNT(e.id_compra) AS total_compras,
       SUM(d.subtotal) AS monto_total
FROM tbl_tarjetas t
JOIN tbl_enc_compras e
  ON e.id_tarjeta = t.id_tarjeta
JOIN tbl_det_compras d
  ON d.id_compra = e.id_compra
GROUP BY t.tipo_tarjeta
ORDER BY total_compras DESC;

/* Top clientes por tipo de tarjeta */
SELECT t.tipo_tarjeta,
       c.id_cliente,
       c.primer_nombre,
       c.primer_apellido,
       COUNT(e.id_compra) AS total_compras
FROM tbl_clientes c
JOIN tbl_tarjetas t
  ON t.id_cliente = c.id_cliente
JOIN tbl_enc_compras e
  ON e.id_tarjeta = t.id_tarjeta
GROUP BY t.tipo_tarjeta, c.id_cliente, c.primer_nombre, c.primer_apellido
ORDER BY t.tipo_tarjeta, total_compras DESC;

/* ==================================================
   6. COMPRAS
   ================================================== */

/* Ventas por mes */
SELECT TO_CHAR(e.fecha_compra, 'YYYY-MM') AS mes,
       COUNT(DISTINCT e.id_compra) AS total_compras,
       SUM(d.subtotal) AS total_vendido
FROM tbl_enc_compras e
JOIN tbl_det_compras d
  ON d.id_compra = e.id_compra
GROUP BY TO_CHAR(e.fecha_compra, 'YYYY-MM')
ORDER BY mes;

/* Ventas por anio */
SELECT EXTRACT(YEAR FROM e.fecha_compra) AS anio,
       COUNT(DISTINCT e.id_compra) AS total_compras,
       SUM(d.subtotal) AS total_vendido
FROM tbl_enc_compras e
JOIN tbl_det_compras d
  ON d.id_compra = e.id_compra
GROUP BY EXTRACT(YEAR FROM e.fecha_compra)
ORDER BY anio;

/* Mes con mayor facturacion */
SELECT TO_CHAR(e.fecha_compra, 'YYYY-MM') AS mes,
       SUM(d.subtotal) AS total_vendido
FROM tbl_enc_compras e
JOIN tbl_det_compras d
  ON d.id_compra = e.id_compra
GROUP BY TO_CHAR(e.fecha_compra, 'YYYY-MM')
ORDER BY total_vendido DESC
FETCH FIRST 1 ROW ONLY;

/* Ticket promedio */
SELECT AVG(total_compra) AS ticket_promedio
FROM (
    SELECT e.id_compra,
           SUM(d.subtotal) AS total_compra
    FROM tbl_enc_compras e
    JOIN tbl_det_compras d
      ON d.id_compra = e.id_compra
    GROUP BY e.id_compra
);

/* ==================================================
   7. KPI
   ================================================== */

/* Total de compras */
SELECT COUNT(*) AS total_compras
FROM tbl_enc_compras;

/* Clientes con compras */
SELECT COUNT(DISTINCT e.id_cliente) AS clientes_con_compras
FROM tbl_enc_compras e;

/* Monto total vendido */
SELECT SUM(d.subtotal) AS monto_total_vendido
FROM tbl_det_compras d;

/* Ticket promedio general */
SELECT AVG(total_compra) AS ticket_promedio
FROM (
    SELECT e.id_compra,
           SUM(d.subtotal) AS total_compra
    FROM tbl_enc_compras e
    JOIN tbl_det_compras d
      ON d.id_compra = e.id_compra
    GROUP BY e.id_compra
);
