package com.grupo2.dbacompras.dto;

/** /api/compras/total — KPI: total de compras y monto total vendido */
public record TotalComprasDTO(
        Long totalCompras,
        Double montoTotalVendido
) {
}
