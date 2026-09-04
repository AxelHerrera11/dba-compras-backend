package com.grupo2.dbacompras.repository;

import com.grupo2.dbacompras.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /** /api/productos — catalogo/busqueda para poblar selects del frontend */
    @Query(value = """
            SELECT p.*
            FROM TBL_PRODUCTOS p
            WHERE (:q IS NULL OR UPPER(p.NOMBRE_PRODUCTO) LIKE UPPER('%' || :q || '%'))
              AND (:idCategoria IS NULL OR p.ID_CATEGORIA = :idCategoria)
            ORDER BY p.NOMBRE_PRODUCTO
            """, nativeQuery = true)
    List<Producto> buscarProductos(@Param("q") String q, @Param("idCategoria") Long idCategoria);

    @Query(value = """
            SELECT p.ID_PRODUCTO,
                   p.NOMBRE_PRODUCTO,
                   SUM(d.CANTIDAD) AS CANTIDAD_VENDIDA,
                   SUM(d.SUBTOTAL) AS TOTAL_VENDIDO
            FROM TBL_PRODUCTOS p
            JOIN TBL_DET_COMPRAS d
                ON d.ID_PRODUCTO = p.ID_PRODUCTO
            JOIN TBL_ENC_COMPRAS e
                ON e.ID_COMPRA = d.ID_COMPRA
            WHERE (:fechaDesde IS NULL OR e.FECHA_COMPRA >= :fechaDesde)
              AND (:fechaHasta IS NULL OR e.FECHA_COMPRA <= :fechaHasta)
              AND (:idCliente IS NULL OR e.ID_CLIENTE = :idCliente)
              AND (:idCategoria IS NULL OR p.ID_CATEGORIA = :idCategoria)
            GROUP BY p.ID_PRODUCTO, p.NOMBRE_PRODUCTO
            ORDER BY CANTIDAD_VENDIDA DESC
            FETCH FIRST 10 ROWS ONLY
            """, nativeQuery = true)
    List<Object[]> findTop10Productos(@Param("fechaDesde") LocalDate fechaDesde,
                                       @Param("fechaHasta") LocalDate fechaHasta,
                                       @Param("idCliente") Long idCliente,
                                       @Param("idCategoria") Long idCategoria);

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
            JOIN TBL_ENC_COMPRAS e
                ON e.ID_COMPRA = d.ID_COMPRA
            WHERE (:fechaDesde IS NULL OR e.FECHA_COMPRA >= :fechaDesde)
              AND (:fechaHasta IS NULL OR e.FECHA_COMPRA <= :fechaHasta)
              AND (:idCliente IS NULL OR e.ID_CLIENTE = :idCliente)
            GROUP BY c.ID_CATEGORIA, c.NOMBRE_CATEGORIA
            ORDER BY TOTAL_VENDIDO DESC
            """, nativeQuery = true)
    List<Object[]> findVentasPorCategoria(@Param("fechaDesde") LocalDate fechaDesde,
                                           @Param("fechaHasta") LocalDate fechaHasta,
                                           @Param("idCliente") Long idCliente);
}
