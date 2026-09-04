package com.grupo2.dbacompras.controller;

import com.grupo2.dbacompras.dto.ApiResponse;
import com.grupo2.dbacompras.dto.CreditoVsDebitoDTO;
import com.grupo2.dbacompras.dto.TarjetaMasUtilizadaDTO;
import com.grupo2.dbacompras.dto.TarjetasPorMarcaDTO;
import com.grupo2.dbacompras.service.TarjetaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para el modulo de Tarjetas (asignado a Albino Rosales).
 */
@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {

    private final TarjetaService tarjetaService;

    public TarjetaController(TarjetaService tarjetaService) {
        this.tarjetaService = tarjetaService;
    }

    @GetMapping("/mas-utilizadas")
    public ApiResponse<List<TarjetaMasUtilizadaDTO>> masUtilizadas(
            @RequestParam(defaultValue = "10") int limite) {
        return ApiResponse.ok(tarjetaService.obtenerTarjetasMasUtilizadas(limite));
    }

    @GetMapping("/credito-vs-debito")
    public ApiResponse<List<CreditoVsDebitoDTO>> creditoVsDebito(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Long idCliente) {
        return ApiResponse.ok(tarjetaService.obtenerCreditoVsDebito(fechaDesde, fechaHasta, idCliente));
    }

    @GetMapping("/por-marca")
    public ApiResponse<List<TarjetasPorMarcaDTO>> porMarca(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Long idCliente) {
        return ApiResponse.ok(tarjetaService.obtenerTarjetasPorMarca(fechaDesde, fechaHasta, idCliente));
    }
}
