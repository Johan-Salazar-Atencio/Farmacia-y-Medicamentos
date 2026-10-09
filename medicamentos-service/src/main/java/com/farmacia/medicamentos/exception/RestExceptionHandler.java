package com.farmacia.medicamentos.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/** Manejo global de excepciones para la API REST (respuestas JSON uniformes). */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = org.springframework.web.bind.annotation.RestController.class)
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(ResourceNotFoundException ex) {
        return cuerpo(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> negocio(BusinessException ex) {
        return cuerpo(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b).orElse("Datos inválidos");
        return cuerpo(HttpStatus.BAD_REQUEST, msg);
    }

    private ResponseEntity<Map<String, Object>> cuerpo(HttpStatus status, String mensaje) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("timestamp", LocalDateTime.now().toString());
        m.put("status", status.value());
        m.put("error", status.getReasonPhrase());
        m.put("mensaje", mensaje);
        return ResponseEntity.status(status).body(m);
    }
}
