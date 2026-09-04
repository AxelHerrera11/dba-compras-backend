package com.grupo2.dbacompras.dto;

/**
 * DTO de salida para /api/clientes/por-genero.
 */
public record ClientePorGeneroDTO(
        String genero,
        Long total
) {
}