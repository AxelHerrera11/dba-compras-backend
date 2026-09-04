package com.grupo2.dbacompras.dto;

import java.math.BigDecimal;

public record ProductoTop10DTO(
        Long idProducto,
        String nombreProducto,
        Long cantidadVendida,
        BigDecimal totalVendido
) {
}

