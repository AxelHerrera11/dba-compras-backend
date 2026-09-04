package com.grupo2.dbacompras.dto;

/**
 * DTO de salida para /api/tarjetas/por-marca.
 */
public record TarjetasPorMarcaDTO(
        String marca,
        Long cantidadTarjetas,
        Long totalCompras,
        Double montoTotal
) {
}
