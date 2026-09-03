package com.grupo2.dbacompras.exception;

import com.grupo2.dbacompras.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de errores. TODOS los controllers de la API (Clientes,
 * Productos, Tarjetas, Compras) heredan este comportamiento automaticamente:
 * ningun endpoint deberia devolver un stack trace crudo al frontend.
 *
 * Cada quien puede agregar aqui nuevos @ExceptionHandler si su modulo
 * necesita un tipo de error especifico.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errores de negocio/validacion manual (ApiException lanzada a proposito)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
    }

    // Errores de @Valid en el cuerpo de la peticion (si algun endpoint usa POST/PUT)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Parametros invalidos");
        return ResponseEntity.badRequest().body(ApiResponse.error(mensaje));
    }

    // Parametros de tipo incorrecto en la URL (ej. ?limite=abc en vez de un numero)
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.error("Parametro '" + ex.getName() + "' invalido"));
    }

    // Cualquier otro error no controlado (ultima linea de defensa)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error interno: " + ex.getMessage()));
    }
}
