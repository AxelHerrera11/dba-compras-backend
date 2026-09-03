package com.grupo2.dbacompras.dto;

import java.time.Instant;

/**
 * Envoltorio estandar para TODAS las respuestas de la API, para que el
 * frontend siempre reciba la misma forma de JSON sin importar el endpoint.
 *
 * Ejemplo de uso en un controller:
 *   return ResponseEntity.ok(ApiResponse.ok(listaDeClientes));
 *
 * Ejemplo de error (normalmente lo arma GlobalExceptionHandler):
 *   ApiResponse.error("Parametro 'fecha' invalido")
 */
public record ApiResponse<T>(
        boolean success,
        T data,
        String message,
        Instant timestamp
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message, Instant.now());
    }
}
