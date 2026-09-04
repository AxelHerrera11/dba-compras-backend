package com.grupo2.dbacompras.repository;

import com.grupo2.dbacompras.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query(value = """
            SELECT p.ID_PRODUCTO,
                   p.NOMBRE_PRODUCTO,
                   SUM(d.CANTIDAD) AS CANTIDAD_VENDIDA,
                   SUM(d.SUBTOTAL) AS TOTAL_VENDIDO
            FROM TBL_PRODUCTOS p
            JOIN TBL_DET_COMPRAS d
                ON d.ID_PRODUCTO = p.ID_PRODUCTO
            GROUP BY p.ID_PRODUCTO, p.NOMBRE_PRODUCTO
            ORDER BY CANTIDAD_VENDIDA DESC
            FETCH FIRST 10 ROWS ONLY
            """, nativeQuery = true)
    List<Object[]> findTop10Productos();

    @Query(value = """
            SELECT p.*
            FROM TBL_PRODUCTOS p
            WHERE NOT EXISTS (
                SELECT 1
                FROM TBL_DET_COMPRAS d
                WHERE d.ID_PRODUCTO = p.ID_PRODUCTO
            )
            ORDER BY p.NOMBRE_PRODUCTO
            """, nativeQuery = true)
    List<Producto> findProductosSinVentas();

    @Query(value = """
            SELECT c.ID_CATEGORIA,
                   c.NOMBRE_CATEGORIA,
                   SUM(d.CANTIDAD) AS CANTIDAD_VENDIDA,
                   SUM(d.SUBTOTAL) AS TOTAL_VENDIDO
            FROM TBL_CATEGORIAS c
            JOIN TBL_PRODUCTOS p
                ON p.ID_CATEGORIA = c.ID_CATEGORIA
            JOIN TBL_DET_COMPRAS d
                ON d.ID_PRODUCTO = p.ID_PRODUCTO
            GROUP BY c.ID_CATEGORIA, c.NOMBRE_CATEGORIA
            ORDER BY TOTAL_VENDIDO DESC
            """, nativeQuery = true)
    List<Object[]> findVentasPorCategoria();
}
