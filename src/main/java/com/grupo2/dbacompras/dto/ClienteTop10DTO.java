package com.grupo2.dbacompras.dto;

/**
 * DTO de salida para /api/clientes/top10.
 * Se construye a partir de las filas Object[] que devuelve la consulta nativa.
 */
public record ClienteTop10DTO(
        Long idCliente,
        String nombre,
        String apellido,
        Double totalComprado
) {
}
