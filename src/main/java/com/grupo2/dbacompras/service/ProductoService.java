package com.grupo2.dbacompras.service;

import com.grupo2.dbacompras.dto.ProductoCategoriaDTO;
import com.grupo2.dbacompras.dto.ProductoTop10DTO;
import com.grupo2.dbacompras.entity.Producto;
import com.grupo2.dbacompras.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoTop10DTO> obtenerTop10Productos() {
        return productoRepository.findTop10Productos()
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

    public List<ProductoCategoriaDTO> obtenerVentasPorCategoria() {
        return productoRepository.findVentasPorCategoria()
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
