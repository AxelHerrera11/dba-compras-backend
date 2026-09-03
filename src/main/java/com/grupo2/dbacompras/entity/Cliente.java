package com.grupo2.dbacompras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de EJEMPLO mapeada a TBL_CLIENTES.
 *
 * IMPORTANTE: los nombres de columnas de abajo son un supuesto razonable.
 * Antes de usarla, revisen la estructura real de la tabla en Oracle
 * (DESCRIBE TBL_CLIENTES;) y ajusten los @Column a los nombres reales.
 *
 * Esta clase es solo la base/plantilla que Gerson puede extender para su
 * modulo de Clientes; los demas (Javier, Albino, Axel) deben crear su propia
 * entidad siguiendo este mismo patron para Producto, Tarjeta, EncCompra, etc.
 */
@Entity
@Table(name = "TBL_CLIENTES")
public class Cliente {

    @Id
    @Column(name = "ID_CLIENTE")
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "GENERO")
    private String genero;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FECHA_REGISTRO")
    private java.time.LocalDate fechaRegistro;

    public Cliente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public java.time.LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(java.time.LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
