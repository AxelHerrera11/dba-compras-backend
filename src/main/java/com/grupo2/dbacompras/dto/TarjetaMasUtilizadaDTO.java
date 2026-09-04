package com.grupo2.dbacompras.dto;

/**
 * DTO de salida para /api/tarjetas/mas-utilizadas.
 */
public record TarjetaMasUtilizadaDTO(
        Long idTarjeta,
        String numeroTarjeta,
        String marca,
        String tipoTarjeta,
        String titular,
        Long totalCompras,
        Double montoTotal
) {
}
