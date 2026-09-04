package com.grupo2.dbacompras.service;

import com.grupo2.dbacompras.dto.ProductoCategoriaDTO;
import com.grupo2.dbacompras.dto.ProductoTop10DTO;
import com.grupo2.dbacompras.entity.Producto;
import com.grupo2.dbacompras.repository.ProductoRepository;
import com.grupo2.dbacompras.util.ValidacionUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoTop10DTO> obtenerTop10Productos(LocalDate fechaDesde, LocalDate fechaHasta,
                                                          Long idCliente, Long idCategoria) {
        ValidacionUtil.validarRangoFechas(fechaDesde, fechaHasta);
        return productoRepository.findTop10Productos(fechaDesde, fechaHasta, idCliente, idCategoria)
                .stream()
                .map(fila -> new ProductoTop10DTO(
                        ((Number) fila[0]).longValue(),
                        (String) fila[1],
                        ((Number) fila[2]).longValue(),
                        convertirBigDecimal(fila[3])
                ))
                .toList();
    }

    public List<Producto> obtenerProductosSinVentas() {
        return productoRepository.findProductosSinVentas();
    }

    public List<Producto> buscarProductos(String q, Long idCategoria) {
        String texto = (q == null || q.isBlank()) ? null : q.trim();
        return productoRepository.buscarProductos(texto, idCategoria);
    }

    public List<ProductoCategoriaDTO> obtenerVentasPorCategoria(LocalDate fechaDesde, LocalDate fechaHasta,
                                                                  Long idCliente) {
        ValidacionUtil.validarRangoFechas(fechaDesde, fechaHasta);
        return productoRepository.findVentasPorCategoria(fechaDesde, fechaHasta, idCliente)
                .stream()
                .map(fila -> new ProductoCategoriaDTO(
                        ((Number) fila[0]).longValue(),
                        (String) fila[1],
                        ((Number) fila[2]).longValue(),
                        convertirBigDecimal(fila[3])
                ))
                .toList();
    }

    private BigDecimal convertirBigDecimal(Object valor) {
        if (valor instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }

        return new BigDecimal(valor.toString());
    }
}
