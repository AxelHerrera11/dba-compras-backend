package com.grupo2.dbacompras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Entidad mapeada a TBL_TARJETAS.
 */
@Entity
@Table(name = "TBL_TARJETAS")
public class Tarjeta {

    @Id
    @Column(name = "ID_TARJETA")
    private Long id;

    @Column(name = "ID_CLIENTE")
    private Long idCliente;

    @Column(name = "ID_MARCA")
    private Long idMarca;

    @Column(name = "NUMERO_TARJETA")
    private String numeroTarjeta;

    @Column(name = "TIPO_TARJETA")
    private String tipoTarjeta;

    @Column(name = "FECHA_EXP")
    private LocalDate fechaExp;

    public Tarjeta() {
    }

    public Tarjeta(Long id, Long idCliente, Long idMarca, String numeroTarjeta, String tipoTarjeta, LocalDate fechaExp) {
        this.id = id;
        this.idCliente = idCliente;
        this.idMarca = idMarca;
        this.numeroTarjeta = numeroTarjeta;
        this.tipoTarjeta = tipoTarjeta;
        this.fechaExp = fechaExp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Long idMarca) {
        this.idMarca = idMarca;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public LocalDate getFechaExp() {
        return fechaExp;
    }

    public void setFechaExp(LocalDate fechaExp) {
        this.fechaExp = fechaExp;
    }
}
