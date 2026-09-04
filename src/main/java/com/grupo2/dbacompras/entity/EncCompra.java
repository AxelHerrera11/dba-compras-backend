package com.grupo2.dbacompras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "TBL_ENC_COMPRAS")
public class EncCompra {

    @Id
    @Column(name = "ID_COMPRA")
    private Long id;

    @Column(name = "ID_CLIENTE")
    private Long idCliente;

    @Column(name = "ID_TARJETA")
    private Long idTarjeta;

    @Column(name = "FECHA_COMPRA")
    private LocalDate fechaCompra;

    @Column(name = "TOTAL_COMPRA")
    private Double totalCompra;

    public EncCompra() {
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

    public Long getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(Long idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(Double totalCompra) {
        this.totalCompra = totalCompra;
    }
}
