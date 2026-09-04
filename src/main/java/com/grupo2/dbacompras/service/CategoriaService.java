package com.grupo2.dbacompras.service;

import com.grupo2.dbacompras.entity.Categoria;
import com.grupo2.dbacompras.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAllOrdenadoPorNombre();
    }
}
