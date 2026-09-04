package com.grupo2.dbacompras.controller;

import com.grupo2.dbacompras.dto.ApiResponse;
import com.grupo2.dbacompras.dto.ProductoCategoriaDTO;
import com.grupo2.dbacompras.dto.ProductoTop10DTO;
import com.grupo2.dbacompras.entity.Producto;
import com.grupo2.dbacompras.service.ProductoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/top10")
    public ApiResponse<List<ProductoTop10DTO>> top10() {
        return ApiResponse.ok(productoService.obtenerTop10Productos());
    }

    @GetMapping("/sin-ventas")
    public ApiResponse<List<Producto>> sinVentas() {
        return ApiResponse.ok(productoService.obtenerProductosSinVentas());
    }

    @GetMapping("/por-categoria")
    public ApiResponse<List<ProductoCategoriaDTO>> porCategoria() {
        return ApiResponse.ok(productoService.obtenerVentasPorCategoria());
    }
}
