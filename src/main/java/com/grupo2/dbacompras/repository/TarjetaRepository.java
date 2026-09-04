package com.grupo2.dbacompras.repository;

import com.grupo2.dbacompras.entity.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {

    /**
     * /api/tarjetas/mas-utilizadas — tarjetas con mayor numero de compras registradas.
     */
    @Query(value = """
            SELECT t.ID_TARJETA,
                   t.NUMERO_TARJETA,
                   m.NOMBRE_MARCA,
                   t.TIPO_TARJETA,
                   TRIM(c.PRIMER_NOMBRE || ' ' || c.PRIMER_APELLIDO) AS TITULAR,
                   COUNT(e.ID_COMPRA) AS TOTAL_COMPRAS,
                   NVL(SUM(e.TOTAL_COMPRA), 0) AS MONTO_TOTAL
            FROM TBL_TARJETAS t
            JOIN TBL_MARCAS m ON m.ID_MARCA = t.ID_MARCA
            JOIN TBL_CLIENTES c ON c.ID_CLIENTE = t.ID_CLIENTE
            JOIN TBL_ENC_COMPRAS e ON e.ID_TARJETA = t.ID_TARJETA
            GROUP BY t.ID_TARJETA, t.NUMERO_TARJETA, m.NOMBRE_MARCA, t.TIPO_TARJETA, c.PRIMER_NOMBRE, c.PRIMER_APELLIDO
            ORDER BY TOTAL_COMPRAS DESC, MONTO_TOTAL DESC
            FETCH FIRST :limite ROWS ONLY
            """, nativeQuery = true)
    List<Object[]> findTarjetasMasUtilizadas(@Param("limite") int limite);

    /**
     * /api/tarjetas/credito-vs-debito — comparativa entre compras con tarjeta de credito y debito.
     * Los filtros van en el ON del LEFT JOIN (no en el WHERE) para no perder tipos de tarjeta
     * sin compras en el rango filtrado (un WHERE convertiria el LEFT JOIN en INNER JOIN).
     */
    @Query(value = """
            SELECT t.TIPO_TARJETA,
                   COUNT(DISTINCT t.ID_TARJETA) AS CANTIDAD_TARJETAS,
                   COUNT(e.ID_COMPRA) AS TOTAL_COMPRAS,
                   NVL(SUM(e.TOTAL_COMPRA), 0) AS MONTO_TOTAL
            FROM TBL_TARJETAS t
            LEFT JOIN TBL_ENC_COMPRAS e
                ON e.ID_TARJETA = t.ID_TARJETA
               AND (:fechaDesde IS NULL OR e.FECHA_COMPRA >= :fechaDesde)
               AND (:fechaHasta IS NULL OR e.FECHA_COMPRA <= :fechaHasta)
               AND (:idCliente IS NULL OR e.ID_CLIENTE = :idCliente)
            GROUP BY t.TIPO_TARJETA
            ORDER BY t.TIPO_TARJETA
            """, nativeQuery = true)
    List<Object[]> findCreditoVsDebito(@Param("fechaDesde") LocalDate fechaDesde,
                                        @Param("fechaHasta") LocalDate fechaHasta,
                                        @Param("idCliente") Long idCliente);

    /**
     * /api/tarjetas/por-marca — distribucion de compras y tarjetas agrupadas por marca.
     * Mismo criterio de filtros en el ON del LEFT JOIN que credito-vs-debito.
     */
    @Query(value = """
            SELECT m.NOMBRE_MARCA,
                   COUNT(DISTINCT t.ID_TARJETA) AS CANTIDAD_TARJETAS,
                   COUNT(e.ID_COMPRA) AS TOTAL_COMPRAS,
                   NVL(SUM(e.TOTAL_COMPRA), 0) AS MONTO_TOTAL
            FROM TBL_MARCAS m
            JOIN TBL_TARJETAS t ON t.ID_MARCA = m.ID_MARCA
            LEFT JOIN TBL_ENC_COMPRAS e
                ON e.ID_TARJETA = t.ID_TARJETA
               AND (:fechaDesde IS NULL OR e.FECHA_COMPRA >= :fechaDesde)
               AND (:fechaHasta IS NULL OR e.FECHA_COMPRA <= :fechaHasta)
               AND (:idCliente IS NULL OR e.ID_CLIENTE = :idCliente)
            GROUP BY m.ID_MARCA, m.NOMBRE_MARCA
            ORDER BY TOTAL_COMPRAS DESC, MONTO_TOTAL DESC
            """, nativeQuery = true)
    List<Object[]> findTarjetasPorMarca(@Param("fechaDesde") LocalDate fechaDesde,
                                         @Param("fechaHasta") LocalDate fechaHasta,
                                         @Param("idCliente") Long idCliente);
}
