package com.grupo2.dbacompras.dto;

/** /api/compras/promedio */
public record PromedioDTO(
        Double ticketPromedio,
        Long cantidadCompras
) {
}
