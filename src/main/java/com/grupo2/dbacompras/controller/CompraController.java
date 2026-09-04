package com.grupo2.dbacompras.controller;

import com.grupo2.dbacompras.dto.ApiResponse;
import com.grupo2.dbacompras.dto.CompraPorAnioDTO;
import com.grupo2.dbacompras.dto.CompraPorMesDTO;
import com.grupo2.dbacompras.dto.PromedioDTO;
import com.grupo2.dbacompras.service.CompraService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping("/por-mes")
    public ApiResponse<List<CompraPorMesDTO>> porMes() {
        return ApiResponse.ok(compraService.obtenerVentasPorMes());
    }

    @GetMapping("/por-anio")
    public ApiResponse<List<CompraPorAnioDTO>> porAnio() {
        return ApiResponse.ok(compraService.obtenerVentasPorAnio());
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
