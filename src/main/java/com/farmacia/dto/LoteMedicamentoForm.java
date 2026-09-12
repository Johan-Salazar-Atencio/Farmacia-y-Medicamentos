package com.farmacia.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoteMedicamentoForm {

    private Long id;

    @NotNull(message = "Debe seleccionar un medicamento")
    private Long medicamentoId;

    @NotNull(message = "Debe seleccionar un proveedor")
    private Long proveedorId;

    @NotBlank(message = "El número de lote es obligatorio")
    @Size(max = 50, message = "El número de lote no debe superar los 50 caracteres")
    private String numeroLote;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    /**
     * RF regla 2: la fecha de vencimiento no puede ser menor o igual a la fecha actual.
     * @Future ya rechaza tanto el día de hoy como fechas pasadas.
     */
    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Future(message = "La fecha de vencimiento debe ser posterior a la fecha actual")
    private LocalDate fechaVencimiento;

    @DecimalMin(value = "0.0", message = "El precio de compra no puede ser negativo")
    private BigDecimal precioCompra;
}
