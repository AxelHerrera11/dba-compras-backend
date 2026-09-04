package com.grupo2.dbacompras.service;

import com.grupo2.dbacompras.dto.ClientePorGeneroDTO;
import com.grupo2.dbacompras.dto.ClienteTop10DTO;
import com.grupo2.dbacompras.entity.Cliente;
import com.grupo2.dbacompras.exception.ApiException;
import com.grupo2.dbacompras.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClienteServiceTest {

    private ClienteRepository clienteRepository;
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteRepository = mock(ClienteRepository.class);
        clienteService = new ClienteService(clienteRepository);
    }

    @Test
    void obtenerTop10PorMonto_mapsFilas() {
        when(clienteRepository.findTop10PorMontoComprado(10)).thenReturn(List.<Object[]>of(
                new Object[]{1L, "Gerson", "Orellana", 1500.50}
        ));

        List<ClienteTop10DTO> resultado = clienteService.obtenerTop10PorMonto(10);

        assertEquals(1, resultado.size());
        ClienteTop10DTO dto = resultado.get(0);
        assertEquals(1L, dto.idCliente());
        assertEquals("Gerson", dto.primerNombre());
        assertEquals("Orellana", dto.primerApellido());
        assertEquals(1500.50, dto.totalComprado());
    }

    @Test
    void obtenerTop10PorMonto_rechazaLimiteInvalido() {
        assertThrows(ApiException.class, () -> clienteService.obtenerTop10PorMonto(0));
        assertThrows(ApiException.class, () -> clienteService.obtenerTop10PorMonto(101));
    }

    @Test
    void obtenerClientesSinCompras_devuelveClientes() {
        Cliente cliente = new Cliente();
        cliente.setId(7L);
        when(clienteRepository.findClientesSinCompras()).thenReturn(List.of(cliente));

        List<Cliente> resultado = clienteService.obtenerClientesSinCompras();

        assertEquals(1, resultado.size());
        assertEquals(7L, resultado.get(0).getId());
    }

    @Test
    void obtenerClienteMayorConsumo_mapsFila() {
        when(clienteRepository.findClienteMayorConsumo()).thenReturn(List.<Object[]>of(
                new Object[]{3L, "Maria", "Lopez", 9800.00}
        ));

        ClienteTop10DTO dto = clienteService.obtenerClienteMayorConsumo();

        assertEquals(3L, dto.idCliente());
        assertEquals("Maria", dto.primerNombre());
        assertEquals("Lopez", dto.primerApellido());
        assertEquals(9800.00, dto.totalComprado());
    }

    @Test
    void obtenerClienteMayorConsumo_lanzaApiExceptionSinDatos() {
        when(clienteRepository.findClienteMayorConsumo()).thenReturn(List.of());

        assertThrows(ApiException.class, () -> clienteService.obtenerClienteMayorConsumo());
    }

    @Test
    void obtenerClientesPorGenero_manejaCharDeOracle() {
        when(clienteRepository.findClientesPorGenero()).thenReturn(List.<Object[]>of(
                new Object[]{'M', 12L},
                new Object[]{'F', 8L}
        ));

        List<ClientePorGeneroDTO> resultado = clienteService.obtenerClientesPorGenero();

        assertEquals(2, resultado.size());
        assertEquals("M", resultado.get(0).genero());
        assertEquals(12L, resultado.get(0).total());
        assertEquals("F", resultado.get(1).genero());
        assertEquals(8L, resultado.get(1).total());
    }

    @Test
    void obtenerClientesPorGenero_manejaStringTambien() {
        when(clienteRepository.findClientesPorGenero()).thenReturn(List.<Object[]>of(
                new Object[]{"M", 5L}
        ));

        List<ClientePorGeneroDTO> resultado = clienteService.obtenerClientesPorGenero();

        assertEquals(1, resultado.size());
        assertEquals("M", resultado.get(0).genero());
        assertEquals(5L, resultado.get(0).total());
    }

    @Test
    void obtenerClientesPorGenero_manejaGeneroNulo() {
        when(clienteRepository.findClientesPorGenero()).thenReturn(List.<Object[]>of(
                new Object[]{null, 2L}
        ));

        List<ClientePorGeneroDTO> resultado = clienteService.obtenerClientesPorGenero();

        assertEquals(1, resultado.size());
        assertEquals(null, resultado.get(0).genero());
        assertEquals(2L, resultado.get(0).total());
    }
}