package com.grupo2.dbacompras.service;

import com.grupo2.dbacompras.dto.CompraPorAnioDTO;
import com.grupo2.dbacompras.dto.CompraPorMesDTO;
import com.grupo2.dbacompras.dto.PromedioDTO;
import com.grupo2.dbacompras.exception.ApiException;
import com.grupo2.dbacompras.repository.CompraRepository;
import com.grupo2.dbacompras.util.ValidacionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    public List<CompraPorMesDTO> obtenerVentasPorMes(LocalDate fechaDesde, LocalDate fechaHasta,
                                                       Long idCliente, Long idCategoria, Long idProducto) {
        ValidacionUtil.validarRangoFechas(fechaDesde, fechaHasta);
        return compraRepository.ventasPorMes(fechaDesde, fechaHasta, idCliente, idCategoria, idProducto).stream()
                .map(fila -> new CompraPorMesDTO(
                        ((Number) fila[0]).intValue(),
                        ((Number) fila[1]).intValue(),
                        ((Number) fila[2]).longValue(),
                        ((Number) fila[3]).doubleValue()
                ))
                .toList();
    }

    public List<CompraPorAnioDTO> obtenerVentasPorAnio() {
        return compraRepository.ventasPorAnio().stream()
                .map(fila -> new CompraPorAnioDTO(
                        ((Number) fila[0]).intValue(),
                        ((Number) fila[1]).longValue(),
                        ((Number) fila[2]).doubleValue()
                ))
                .toList();
    }

    public PromedioDTO obtenerTicketPromedio(Integer anio) {
        if (anio != null && (anio < 2000 || anio > 2100)) {
            throw new ApiException("El parametro 'anio' no es valido");
        }

        List<Object[]> resultado = compraRepository.ticketPromedio(anio);
        Object[] fila = resultado.get(0);

        Double promedio = fila[0] != null ? ((Number) fila[0]).doubleValue() : 0.0;
        Long cantidad = fila[1] != null ? ((Number) fila[1]).longValue() : 0L;

        return new PromedioDTO(promedio, cantidad);
    }

    public com.grupo2.dbacompras.dto.TotalComprasDTO obtenerTotalCompras() {
        List<Object[]> resultado = compraRepository.totalCompras();
        Object[] fila = resultado.get(0);

        Long total = fila[0] != null ? ((Number) fila[0]).longValue() : 0L;
        Double monto = fila[1] != null ? ((Number) fila[1]).doubleValue() : 0.0;

        return new com.grupo2.dbacompras.dto.TotalComprasDTO(total, monto);
    }
}
