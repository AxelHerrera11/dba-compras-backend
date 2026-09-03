package com.grupo2.dbacompras.exception;

/**
 * Excepcion para errores de negocio/validacion controlados
 * (ej. parametro fuera de rango, filtro invalido).
 * El GlobalExceptionHandler la captura y responde con HTTP 400.
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
