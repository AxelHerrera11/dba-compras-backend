package com.grupo2.dbacompras.service;

import com.grupo2.dbacompras.dto.CreditoVsDebitoDTO;
import com.grupo2.dbacompras.dto.TarjetaMasUtilizadaDTO;
import com.grupo2.dbacompras.dto.TarjetasPorMarcaDTO;
import com.grupo2.dbacompras.exception.ApiException;
import com.grupo2.dbacompras.repository.TarjetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TarjetaServiceTest {

    private TarjetaRepository tarjetaRepository;
    private TarjetaService tarjetaService;

    @BeforeEach
    void setUp() {
        tarjetaRepository = mock(TarjetaRepository.class);
        tarjetaService = new TarjetaService(tarjetaRepository);
    }

    @Test
    void obtenerTarjetasMasUtilizadas_mapsFilasYEnmascaraNumero() {
        when(tarjetaRepository.findTarjetasMasUtilizadas(10)).thenReturn(List.<Object[]>of(
                new Object[]{14L, "4033339984528265", "Visa", "DEBITO", "JORGE PACHECO", 17L, 28315.00}
        ));

        List<TarjetaMasUtilizadaDTO> resultado = tarjetaService.obtenerTarjetasMasUtilizadas(10);

        assertEquals(1, resultado.size());
        TarjetaMasUtilizadaDTO dto = resultado.get(0);
        assertEquals(14L, dto.idTarjeta());
        assertEquals("************8265", dto.numeroTarjeta());
        assertEquals("Visa", dto.marca());
        assertEquals("DEBITO", dto.tipoTarjeta());
        assertEquals("JORGE PACHECO", dto.titular());
        assertEquals(17L, dto.totalCompras());
        assertEquals(28315.00, dto.montoTotal());
    }

    @Test
    void obtenerTarjetasMasUtilizadas_soportaTiposNumericosDeOracle() {
        // En Oracle las funciones agregadas SUM/COUNT pueden venir como BigDecimal
        when(tarjetaRepository.findTarjetasMasUtilizadas(5)).thenReturn(List.<Object[]>of(
                new Object[]{BigDecimal.valueOf(1), "4517323179150104", "American Express", "CREDITO", "HENRY PEREZ", BigDecimal.valueOf(14), BigDecimal.valueOf(23560.50)}
        ));

        List<TarjetaMasUtilizadaDTO> resultado = tarjetaService.obtenerTarjetasMasUtilizadas(5);

        assertEquals(1, resultado.size());
        TarjetaMasUtilizadaDTO dto = resultado.get(0);
        assertEquals(1L, dto.idTarjeta());
        assertEquals("************0104", dto.numeroTarjeta());
        assertEquals(14L, dto.totalCompras());
        assertEquals(23560.50, dto.montoTotal());
    }

    @Test
    void obtenerTarjetasMasUtilizadas_rechazaLimiteInvalido() {
        assertThrows(ApiException.class, () -> tarjetaService.obtenerTarjetasMasUtilizadas(0));
        assertThrows(ApiException.class, () -> tarjetaService.obtenerTarjetasMasUtilizadas(-5));
        assertThrows(ApiException.class, () -> tarjetaService.obtenerTarjetasMasUtilizadas(101));
    }

    @Test
    void enmascararNumeroTarjeta_casosBorde() {
        assertEquals("****", tarjetaService.enmascararNumeroTarjeta(null));
        assertEquals("****", tarjetaService.enmascararNumeroTarjeta("   "));
        assertEquals("123", tarjetaService.enmascararNumeroTarjeta("123"));
        assertEquals("1234", tarjetaService.enmascararNumeroTarjeta("1234"));
        assertEquals("*1234", tarjetaService.enmascararNumeroTarjeta("01234"));
        assertEquals("************1234", tarjetaService.enmascararNumeroTarjeta("4000123456781234"));
    }

    @Test
    void obtenerCreditoVsDebito_mapsFilas() {
        when(tarjetaRepository.findCreditoVsDebito()).thenReturn(List.<Object[]>of(
                new Object[]{"CREDITO", 81L, 206L, 340887.00},
                new Object[]{"DEBITO", 81L, 213L, 428288.00}
        ));

        List<CreditoVsDebitoDTO> resultado = tarjetaService.obtenerCreditoVsDebito();

        assertEquals(2, resultado.size());
        CreditoVsDebitoDTO credito = resultado.get(0);
        assertEquals("CREDITO", credito.tipoTarjeta());
        assertEquals(81L, credito.cantidadTarjetas());
        assertEquals(206L, credito.totalCompras());
        assertEquals(340887.00, credito.montoTotal());

        CreditoVsDebitoDTO debito = resultado.get(1);
        assertEquals("DEBITO", debito.tipoTarjeta());
        assertEquals(81L, debito.cantidadTarjetas());
        assertEquals(213L, debito.totalCompras());
        assertEquals(428288.00, debito.montoTotal());
    }

    @Test
    void obtenerTarjetasPorMarca_mapsFilas() {
        when(tarjetaRepository.findTarjetasPorMarca()).thenReturn(List.<Object[]>of(
                new Object[]{"Visa", 45L, 141L, 244189.00},
                new Object[]{"American Express", 41L, 110L, 193069.00}
        ));

        List<TarjetasPorMarcaDTO> resultado = tarjetaService.obtenerTarjetasPorMarca();

        assertEquals(2, resultado.size());
        TarjetasPorMarcaDTO visa = resultado.get(0);
        assertEquals("Visa", visa.marca());
        assertEquals(45L, visa.cantidadTarjetas());
        assertEquals(141L, visa.totalCompras());
        assertEquals(244189.00, visa.montoTotal());

        TarjetasPorMarcaDTO amex = resultado.get(1);
        assertEquals("American Express", amex.marca());
        assertEquals(41L, amex.cantidadTarjetas());
        assertEquals(110L, amex.totalCompras());
        assertEquals(193069.00, amex.montoTotal());
    }
}
