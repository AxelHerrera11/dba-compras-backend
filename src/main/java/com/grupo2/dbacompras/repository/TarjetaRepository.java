package com.grupo2.dbacompras.repository;

import com.grupo2.dbacompras.entity.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
     */
    @Query(value = """
            SELECT t.TIPO_TARJETA,
                   COUNT(DISTINCT t.ID_TARJETA) AS CANTIDAD_TARJETAS,
                   COUNT(e.ID_COMPRA) AS TOTAL_COMPRAS,
                   NVL(SUM(e.TOTAL_COMPRA), 0) AS MONTO_TOTAL
            FROM TBL_TARJETAS t
            LEFT JOIN TBL_ENC_COMPRAS e ON e.ID_TARJETA = t.ID_TARJETA
            GROUP BY t.TIPO_TARJETA
            ORDER BY t.TIPO_TARJETA
            """, nativeQuery = true)
    List<Object[]> findCreditoVsDebito();

    /**
     * /api/tarjetas/por-marca — distribucion de compras y tarjetas agrupadas por marca.
     */
    @Query(value = """
            SELECT m.NOMBRE_MARCA,
                   COUNT(DISTINCT t.ID_TARJETA) AS CANTIDAD_TARJETAS,
                   COUNT(e.ID_COMPRA) AS TOTAL_COMPRAS,
                   NVL(SUM(e.TOTAL_COMPRA), 0) AS MONTO_TOTAL
            FROM TBL_MARCAS m
            JOIN TBL_TARJETAS t ON t.ID_MARCA = m.ID_MARCA
            LEFT JOIN TBL_ENC_COMPRAS e ON e.ID_TARJETA = t.ID_TARJETA
            GROUP BY m.ID_MARCA, m.NOMBRE_MARCA
            ORDER BY TOTAL_COMPRAS DESC, MONTO_TOTAL DESC
            """, nativeQuery = true)
    List<Object[]> findTarjetasPorMarca();
}
