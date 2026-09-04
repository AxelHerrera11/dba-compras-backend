package com.grupo2.dbacompras.repository;

import com.grupo2.dbacompras.entity.EncCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompraRepository extends JpaRepository<EncCompra, Long> {

    /** /api/compras/por-mes */
    @Query(value = """
            SELECT EXTRACT(YEAR FROM e.FECHA_COMPRA)  AS ANIO,
                   EXTRACT(MONTH FROM e.FECHA_COMPRA) AS MES,
                   COUNT(*)                            AS CANTIDAD,
                   SUM(e.TOTAL_COMPRA)                 AS TOTAL
            FROM TBL_ENC_COMPRAS e
            GROUP BY EXTRACT(YEAR FROM e.FECHA_COMPRA), EXTRACT(MONTH FROM e.FECHA_COMPRA)
            ORDER BY ANIO, MES
            """, nativeQuery = true)
    List<Object[]> ventasPorMes();

    /** /api/compras/por-anio */
    @Query(value = """
            SELECT EXTRACT(YEAR FROM e.FECHA_COMPRA) AS ANIO,
                   COUNT(*)                            AS CANTIDAD,
                   SUM(e.TOTAL_COMPRA)                 AS TOTAL
            FROM TBL_ENC_COMPRAS e
            GROUP BY EXTRACT(YEAR FROM e.FECHA_COMPRA)
            ORDER BY ANIO
            """, nativeQuery = true)
    List<Object[]> ventasPorAnio();

    /**
     * /api/compras/promedio
     * "anio" es opcional: si viene null, calcula el ticket promedio general;
     * si viene con valor, lo calcula solo para ese año (soporta el filtro de fecha del dashboard).
     */
    @Query(value = """
            SELECT AVG(e.TOTAL_COMPRA) AS PROMEDIO,
                   COUNT(*)             AS CANTIDAD
            FROM TBL_ENC_COMPRAS e
            WHERE (:anio IS NULL OR EXTRACT(YEAR FROM e.FECHA_COMPRA) = :anio)
            """, nativeQuery = true)
    List<Object[]> ticketPromedio(@Param("anio") Integer anio);

    /** /api/compras/total — KPI de total de compras y monto total vendido */
    @Query(value = """
            SELECT COUNT(*)             AS TOTAL_COMPRAS,
                   SUM(e.TOTAL_COMPRA)  AS MONTO_TOTAL
            FROM TBL_ENC_COMPRAS e
            """, nativeQuery = true)
    List<Object[]> totalCompras();
}
