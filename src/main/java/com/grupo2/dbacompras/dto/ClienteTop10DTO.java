package com.grupo2.dbacompras.dto;

/**
 * DTO de salida para /api/clientes/top10.
 */
public record ClienteTop10DTO(
        Long idCliente,
        String primerNombre,
        String primerApellido,
        Double totalComprado
) {
}
