package com.grupo2.dbacompras.dto;

/** /api/clientes/con-compras — KPI: total de clientes que han realizado al menos una compra */
public record ClientesConComprasDTO(
        Long cantidad
) {
}
