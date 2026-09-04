package com.grupo2.dbacompras.controller;

import com.grupo2.dbacompras.dto.ApiResponse;
import com.grupo2.dbacompras.dto.ProductoCategoriaDTO;
import com.grupo2.dbacompras.dto.ProductoTop10DTO;
import com.grupo2.dbacompras.entity.Producto;
import com.grupo2.dbacompras.service.ProductoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /** Catalogo/busqueda de productos, para poblar el select/autocompletar del frontend. */
    @GetMapping
    public ApiResponse<List<Producto>> listar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long idCategoria) {
        return ApiResponse.ok(productoService.buscarProductos(q, idCategoria));
    }

    @GetMapping("/top10")
    public ApiResponse<List<ProductoTop10DTO>> top10(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Long idCliente,
            @RequestParam(required = false) Long idCategoria) {
        return ApiResponse.ok(productoService.obtenerTop10Productos(fechaDesde, fechaHasta, idCliente, idCategoria));
    }

    @GetMapping("/sin-ventas")
    public ApiResponse<List<Producto>> sinVentas() {
        return ApiResponse.ok(productoService.obtenerProductosSinVentas());
    }

    @GetMapping("/por-categoria")
    public ApiResponse<List<ProductoCategoriaDTO>> porCategoria(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Long idCliente) {
        return ApiResponse.ok(productoService.obtenerVentasPorCategoria(fechaDesde, fechaHasta, idCliente));
    }
}
