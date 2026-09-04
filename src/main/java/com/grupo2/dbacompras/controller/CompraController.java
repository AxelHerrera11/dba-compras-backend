package com.grupo2.dbacompras.controller;

import com.grupo2.dbacompras.dto.ApiResponse;
import com.grupo2.dbacompras.dto.CompraPorAnioDTO;
import com.grupo2.dbacompras.dto.CompraPorMesDTO;
import com.grupo2.dbacompras.dto.PromedioDTO;
import com.grupo2.dbacompras.service.CompraService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping("/por-mes")
    public ApiResponse<List<CompraPorMesDTO>> porMes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Long idCliente,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Long idProducto) {
        return ApiResponse.ok(compraService.obtenerVentasPorMes(fechaDesde, fechaHasta, idCliente, idCategoria, idProducto));
    }

    @GetMapping("/por-anio")
    public ApiResponse<List<CompraPorAnioDTO>> porAnio() {
        return ApiResponse.ok(compraService.obtenerVentasPorAnio());
    }

    @GetMapping("/total")
    public ApiResponse<com.grupo2.dbacompras.dto.TotalComprasDTO> total() {
        return ApiResponse.ok(compraService.obtenerTotalCompras());
    }

    /**
     * ?anio=2025 es opcional: sin parametro devuelve el ticket promedio general;
     * con parametro, lo filtra a ese anio (util para el filtro de fecha del dashboard).
     */
    @GetMapping("/promedio")
    public ApiResponse<PromedioDTO> promedio(
            @RequestParam(required = false) Integer anio) {
        return ApiResponse.ok(compraService.obtenerTicketPromedio(anio));
    }
}
