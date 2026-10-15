package com.farmacia.medicamentos.exception;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** Manejo global de excepciones para las vistas Thymeleaf: muestra templates/error.html. */
@ControllerAdvice(annotations = Controller.class)
public class WebExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class, BusinessException.class})
    public String error(RuntimeException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error";
    }
}
