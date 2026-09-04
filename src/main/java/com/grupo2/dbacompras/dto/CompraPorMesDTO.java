package com.grupo2.dbacompras.dto;

/** /api/compras/por-mes */
public record CompraPorMesDTO(
        Integer anio,
        Integer mes,
        Long cantidadCompras,
        Double totalVentas
) {
}
