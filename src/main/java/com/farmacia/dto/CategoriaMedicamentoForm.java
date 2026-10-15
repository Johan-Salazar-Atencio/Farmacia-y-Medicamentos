package com.farmacia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de entrada para crear/editar categorías. Evita exponer la entidad directamente
 * en el binding del formulario (mass assignment).
 */
@Getter
@Setter
public class CategoriaMedicamentoForm {

    private Long id;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no debe superar los 255 caracteres")
    private String descripcion;
}
