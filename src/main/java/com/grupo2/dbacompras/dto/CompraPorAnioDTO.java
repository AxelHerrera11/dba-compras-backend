package com.grupo2.dbacompras.dto;

/** /api/compras/por-anio */
public record CompraPorAnioDTO(
        Integer anio,
        Long cantidadCompras,
        Double totalVentas
) {
}
