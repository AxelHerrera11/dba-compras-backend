package com.grupo2.dbacompras.service;

import com.grupo2.dbacompras.dto.ClientePorGeneroDTO;
import com.grupo2.dbacompras.dto.ClienteTop10DTO;
import com.grupo2.dbacompras.entity.Cliente;
import com.grupo2.dbacompras.exception.ApiException;
import com.grupo2.dbacompras.repository.ClienteRepository;
import com.grupo2.dbacompras.util.ValidacionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteTop10DTO> obtenerTop10PorMonto(int limite, LocalDate fechaDesde, LocalDate fechaHasta,
                                                        Long idCategoria, Long idProducto) {
        if (limite <= 0 || limite > 100) {
            throw new ApiException("El parametro 'limite' debe estar entre 1 y 100");
        }
        ValidacionUtil.validarRangoFechas(fechaDesde, fechaHasta);

        return clienteRepository.findTop10PorMontoComprado(limite, fechaDesde, fechaHasta, idCategoria, idProducto).stream()
                .map(fila -> new ClienteTop10DTO(
                        ((Number) fila[0]).longValue(),
                        (String) fila[1],
                        (String) fila[2],
                        ((Number) fila[3]).doubleValue()
                ))
                .toList();
    }

    public List<Cliente> obtenerClientesSinCompras() {
        return clienteRepository.findClientesSinCompras();
    }

    public ClienteTop10DTO obtenerClienteMayorConsumo() {
        return clienteRepository.findClienteMayorConsumo().stream()
                .findFirst()
                .map(fila -> new ClienteTop10DTO(
                        ((Number) fila[0]).longValue(),
                        (String) fila[1],
                        (String) fila[2],
                        ((Number) fila[3]).doubleValue()
                ))
                .orElseThrow(() -> new ApiException("No hay clientes con compras registradas"));
    }

    public List<ClientePorGeneroDTO> obtenerClientesPorGenero() {
        return clienteRepository.findClientesPorGenero().stream()
                .map(fila -> new ClientePorGeneroDTO(
                        asString(fila[0]),
                        ((Number) fila[1]).longValue()
                ))
                .toList();
    }

    private String asString(Object valor) {
        return valor == null ? null : valor.toString();
    }

    public com.grupo2.dbacompras.dto.ClientesConComprasDTO obtenerClientesConCompras() {
        Long cantidad = clienteRepository.countClientesConCompras();
        return new com.grupo2.dbacompras.dto.ClientesConComprasDTO(cantidad != null ? cantidad : 0L);
    }
}
