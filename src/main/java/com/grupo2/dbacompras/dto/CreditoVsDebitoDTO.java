package com.grupo2.dbacompras.dto;

/**
 * DTO de salida para /api/tarjetas/credito-vs-debito.
 */
public record CreditoVsDebitoDTO(
        String tipoTarjeta,
        Long cantidadTarjetas,
        Long totalCompras,
        Double montoTotal
) {
}
