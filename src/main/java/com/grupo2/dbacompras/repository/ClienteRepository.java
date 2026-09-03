package com.grupo2.dbacompras.repository;

import com.grupo2.dbacompras.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Ejemplo de consulta nativa parametrizada para /api/clientes/top10.
     * AJUSTAR nombres de tabla/columnas a la estructura real de
     * TBL_ENC_COMPRAS / TBL_DET_COMPRAS una vez confirmada por Maria.
     *
     * nativeQuery = true porque es mas facil expresar el JOIN + GROUP BY +
     * ORDER BY + LIMIT (Oracle: FETCH FIRST n ROWS ONLY) que con JPQL puro.
     */
    @Query(value = """
            SELECT c.ID_CLIENTE, c.NOMBRE, c.APELLIDO, SUM(d.SUBTOTAL) AS TOTAL_COMPRADO
            FROM TBL_CLIENTES c
            JOIN TBL_ENC_COMPRAS e ON e.ID_CLIENTE = c.ID_CLIENTE
            JOIN TBL_DET_COMPRAS d ON d.ID_COMPRA = e.ID_COMPRA
            GROUP BY c.ID_CLIENTE, c.NOMBRE, c.APELLIDO
            ORDER BY TOTAL_COMPRADO DESC
            FETCH FIRST :limite ROWS ONLY
            """, nativeQuery = true)
    List<Object[]> findTop10PorMontoComprado(@Param("limite") int limite);

    // Ejemplo adicional que Gerson puede usar como plantilla para "sin-compras":
    @Query(value = """
            SELECT c.*
            FROM TBL_CLIENTES c
            WHERE NOT EXISTS (
                SELECT 1 FROM TBL_ENC_COMPRAS e WHERE e.ID_CLIENTE = c.ID_CLIENTE
            )
            """, nativeQuery = true)
    List<Cliente> findClientesSinCompras();
}
