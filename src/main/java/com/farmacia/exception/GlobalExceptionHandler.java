package com.farmacia.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Captura excepciones que no fueron manejadas puntualmente en un controlador
 * (por ejemplo, un id inexistente al abrir un formulario de edición por GET,
 * donde no hay un flujo de "volver atrás" natural) y muestra una página de error.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String manejarNoEncontrado(ResourceNotFoundException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/404";
    }
}
