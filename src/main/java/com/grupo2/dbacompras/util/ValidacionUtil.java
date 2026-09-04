package com.grupo2.dbacompras.util;

import com.grupo2.dbacompras.exception.ApiException;

import java.time.LocalDate;

/**
 * Validaciones de negocio compartidas entre los distintos Service
 * (evita repetir la misma validacion de rango de fechas 4 veces).
 */
public final class ValidacionUtil {

    private ValidacionUtil() {
    }

    public static void validarRangoFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new ApiException("El parametro 'fechaDesde' no puede ser posterior a 'fechaHasta'");
        }
    }
}
