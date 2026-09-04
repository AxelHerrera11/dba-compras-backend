package com.grupo2.dbacompras.controller;

import com.grupo2.dbacompras.dto.ApiResponse;
import com.grupo2.dbacompras.dto.CreditoVsDebitoDTO;
import com.grupo2.dbacompras.dto.TarjetaMasUtilizadaDTO;
import com.grupo2.dbacompras.dto.TarjetasPorMarcaDTO;
import com.grupo2.dbacompras.service.TarjetaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResponse<List<CreditoVsDebitoDTO>> creditoVsDebito() {
        return ApiResponse.ok(tarjetaService.obtenerCreditoVsDebito());
    }

    @GetMapping("/por-marca")
    public ApiResponse<List<TarjetasPorMarcaDTO>> porMarca() {
        return ApiResponse.ok(tarjetaService.obtenerTarjetasPorMarca());
    }
}
