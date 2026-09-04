package com.grupo2.dbacompras.repository;

import com.grupo2.dbacompras.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /** /api/categorias — catalogo para poblar selects del frontend */
    @Query(value = "SELECT c.* FROM TBL_CATEGORIAS c ORDER BY c.NOMBRE_CATEGORIA", nativeQuery = true)
    List<Categoria> findAllOrdenadoPorNombre();
}
