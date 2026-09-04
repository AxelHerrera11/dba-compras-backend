package com.grupo2.dbacompras.repository;

import com.grupo2.dbacompras.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * /api/clientes/top10 — concatena PRIMER_NOMBRE + PRIMER_APELLIDO
     * para mostrar un nombre legible sin traer las 5 columnas de nombre por separado.
     */
    @Query(value = """
            SELECT c.ID_CLIENTE,
                   c.PRIMER_NOMBRE,
                   c.PRIMER_APELLIDO,
                   SUM(d.SUBTOTAL) AS TOTAL_COMPRADO
            FROM TBL_CLIENTES c
            JOIN TBL_ENC_COMPRAS e ON e.ID_CLIENTE = c.ID_CLIENTE
            JOIN TBL_DET_COMPRAS d ON d.ID_COMPRA = e.ID_COMPRA
            GROUP BY c.ID_CLIENTE, c.PRIMER_NOMBRE, c.PRIMER_APELLIDO
            ORDER BY TOTAL_COMPRADO DESC
            FETCH FIRST :limite ROWS ONLY
            """, nativeQuery = true)
    List<Object[]> findTop10PorMontoComprado(@Param("limite") int limite);

    @Query(value = """
            SELECT c.*
            FROM TBL_CLIENTES c
            WHERE NOT EXISTS (
                SELECT 1 FROM TBL_ENC_COMPRAS e WHERE e.ID_CLIENTE = c.ID_CLIENTE
            )
            """, nativeQuery = true)
    List<Cliente> findClientesSinCompras();

    /**
     * /api/clientes/mayor-consumo — cliente con el mayor monto total comprado.
     */
    @Query(value = """
            SELECT c.ID_CLIENTE,
                   c.PRIMER_NOMBRE,
                   c.PRIMER_APELLIDO,
                   SUM(d.SUBTOTAL) AS TOTAL_COMPRADO
            FROM TBL_CLIENTES c
            JOIN TBL_ENC_COMPRAS e ON e.ID_CLIENTE = c.ID_CLIENTE
            JOIN TBL_DET_COMPRAS d ON d.ID_COMPRA = e.ID_COMPRA
            GROUP BY c.ID_CLIENTE, c.PRIMER_NOMBRE, c.PRIMER_APELLIDO
            ORDER BY TOTAL_COMPRADO DESC
            FETCH FIRST 1 ROWS ONLY
            """, nativeQuery = true)
    List<Object[]> findClienteMayorConsumo();

    /**
     * /api/clientes/por-genero — cantidad de clientes agrupados por GENERO.
     */
    @Query(value = """
            SELECT GENERO, COUNT(*) AS TOTAL
            FROM TBL_CLIENTES
            GROUP BY GENERO
            ORDER BY GENERO
            """, nativeQuery = true)
    List<Object[]> findClientesPorGenero();
}
