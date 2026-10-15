package com.farmacia.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * El código (RF-FAR-06) y el stock NO forman parte de este DTO: el código lo genera el
 * servicio y el stock solo se modifica al registrar lotes. Así se evita que el cliente
 * pueda inyectar esos valores (mass assignment).
 */
@Getter
@Setter
public class MedicamentoForm {

    private Long id;

    @NotBlank(message = "El nombre del medicamento es obligatorio")
    @Size(max = 150, message = "El nombre no debe superar los 150 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no debe superar los 255 caracteres")
    private String descripcion;

    @NotBlank(message = "La presentación es obligatoria")
    @Size(max = 80, message = "La presentación no debe superar los 80 caracteres")
    private String presentacion;

    @Size(max = 80, message = "La concentración no debe superar los 80 caracteres")
    private String concentracion;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a 0")
    private BigDecimal precioVenta;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;

    @NotNull(message = "Debe seleccionar una categoría")
    private Long categoriaId;
}
