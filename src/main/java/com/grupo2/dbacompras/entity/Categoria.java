package com.grupo2.dbacompras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TBL_CATEGORIAS")
public class Categoria {

    @Id
    @Column(name = "ID_CATEGORIA")
    private Long idCategoria;

    @Column(name = "NOMBRE_CATEGORIA", nullable = false, length = 100)
    private String nombreCategoria;

    public Categoria() {
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
