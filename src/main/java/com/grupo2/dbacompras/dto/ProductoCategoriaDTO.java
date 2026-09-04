package com.grupo2.dbacompras.dto;

import java.math.BigDecimal;

public record ProductoCategoriaDTO(
        Long idCategoria,
        String nombreCategoria,
        Long cantidadVendida,
        BigDecimal totalVendido
) {
}
