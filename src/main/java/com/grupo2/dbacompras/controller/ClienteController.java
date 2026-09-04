package com.grupo2.dbacompras.controller;

import com.grupo2.dbacompras.dto.ApiResponse;
import com.grupo2.dbacompras.dto.ClientePorGeneroDTO;
import com.grupo2.dbacompras.dto.ClienteTop10DTO;
import com.grupo2.dbacompras.entity.Cliente;
import com.grupo2.dbacompras.service.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de EJEMPLO para el modulo de Clientes (asignado a Gerson).
 * Sirve como plantilla de estilo para los controllers de Productos (Javier),
 * Tarjetas (Albino) y Compras (Axel): mismo patron de capas y de respuesta.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/top10")
    public ApiResponse<List<ClienteTop10DTO>> top10(
            @RequestParam(defaultValue = "10") int limite) {
        return ApiResponse.ok(clienteService.obtenerTop10PorMonto(limite));
    }

    @GetMapping("/sin-compras")
    public ApiResponse<List<Cliente>> sinCompras() {
        return ApiResponse.ok(clienteService.obtenerClientesSinCompras());
    }

    @GetMapping("/mayor-consumo")
    public ApiResponse<ClienteTop10DTO> mayorConsumo() {
        return ApiResponse.ok(clienteService.obtenerClienteMayorConsumo());
    }

    @GetMapping("/por-genero")
    public ApiResponse<List<ClientePorGeneroDTO>> porGenero() {
        return ApiResponse.ok(clienteService.obtenerClientesPorGenero());
    }
}
