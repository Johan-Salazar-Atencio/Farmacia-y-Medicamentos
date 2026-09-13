package com.farmacia.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class MedicamentoForm {

    private Long id;

    @NotBlank(message = "El código del medicamento es obligatorio")
    @Pattern(regexp = "^\\S{1,30}$", message = "El código no debe tener espacios y debe tener máximo 30 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre del medicamento es obligatorio")
    @Size(max = 150, message = "El nombre no debe superar los 150 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no debe superar los 255 caracteres")
    private String descripcion;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio de venta no puede ser negativo")
    private BigDecimal precioVenta;

    private Long categoriaId;
}