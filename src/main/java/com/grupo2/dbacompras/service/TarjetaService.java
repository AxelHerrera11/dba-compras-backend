package com.grupo2.dbacompras.service;

import com.grupo2.dbacompras.dto.CreditoVsDebitoDTO;
import com.grupo2.dbacompras.dto.TarjetaMasUtilizadaDTO;
import com.grupo2.dbacompras.dto.TarjetasPorMarcaDTO;
import com.grupo2.dbacompras.exception.ApiException;
import com.grupo2.dbacompras.repository.TarjetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarjetaService {

    private final TarjetaRepository tarjetaRepository;

    public TarjetaService(TarjetaRepository tarjetaRepository) {
        this.tarjetaRepository = tarjetaRepository;
    }

    public List<TarjetaMasUtilizadaDTO> obtenerTarjetasMasUtilizadas(int limite) {
        if (limite <= 0 || limite > 100) {
            throw new ApiException("El parametro 'limite' debe estar entre 1 y 100");
        }

        return tarjetaRepository.findTarjetasMasUtilizadas(limite).stream()
                .map(fila -> new TarjetaMasUtilizadaDTO(
                        ((Number) fila[0]).longValue(),
                        enmascararNumeroTarjeta((String) fila[1]),
                        (String) fila[2],
                        (String) fila[3],
                        (String) fila[4],
                        ((Number) fila[5]).longValue(),
                        ((Number) fila[6]).doubleValue()
                ))
                .toList();
    }

    public List<CreditoVsDebitoDTO> obtenerCreditoVsDebito() {
        return tarjetaRepository.findCreditoVsDebito().stream()
                .map(fila -> new CreditoVsDebitoDTO(
                        (String) fila[0],
                        ((Number) fila[1]).longValue(),
                        ((Number) fila[2]).longValue(),
                        ((Number) fila[3]).doubleValue()
                ))
                .toList();
    }

    public List<TarjetasPorMarcaDTO> obtenerTarjetasPorMarca() {
        return tarjetaRepository.findTarjetasPorMarca().stream()
                .map(fila -> new TarjetasPorMarcaDTO(
                        (String) fila[0],
                        ((Number) fila[1]).longValue(),
                        ((Number) fila[2]).longValue(),
                        ((Number) fila[3]).doubleValue()
                ))
                .toList();
    }

    /**
     * Enmascara el numero de tarjeta dejando visibles unicamente los ultimos 4 digitos.
     */
    String enmascararNumeroTarjeta(String numeroTarjeta) {
        if (numeroTarjeta == null || numeroTarjeta.isBlank()) {
            return "****";
        }
        String limpio = numeroTarjeta.trim();
        if (limpio.length() <= 4) {
            return limpio;
        }
        int visibles = 4;
        int ocultos = limpio.length() - visibles;
        return "*".repeat(ocultos) + limpio.substring(ocultos);
    }
}
