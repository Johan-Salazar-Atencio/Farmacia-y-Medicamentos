
package com.farmacia.core.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(
            ResourceNotFoundException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> reglaDeNegocio(
            BusinessException ex) {
        return respuesta(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(
            MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();

        for (FieldError error :
                ex.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(
                    error.getField(), error.getDefaultMessage());
        }

        return respuesta(
                HttpStatus.BAD_REQUEST,
                "Errores de validación",
                campos);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> inesperado(
            Exception ex) {
        return respuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                null);
    }

    private ResponseEntity<Map<String, Object>> respuesta(
            HttpStatus estado,
            String mensaje,
            Map<String, String> campos) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();

        cuerpo.put("timestamp", LocalDateTime.now().toString());
        cuerpo.put("status", estado.value());
        cuerpo.put("error", estado.getReasonPhrase());
        cuerpo.put("mensaje", mensaje);

        if (campos != null && !campos.isEmpty()) {
            cuerpo.put("campos", campos);
        }

        return ResponseEntity.status(estado).body(cuerpo);
    }
}
