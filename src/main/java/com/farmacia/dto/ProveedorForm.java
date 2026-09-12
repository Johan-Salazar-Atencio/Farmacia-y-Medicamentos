package com.farmacia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProveedorForm {

    private Long id;

    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(regexp = "\\d{11}", message = "El RUC debe tener 11 dígitos numéricos")
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 150, message = "La razón social no debe superar los 150 caracteres")
    private String razonSocial;

    @Pattern(regexp = "^$|^[0-9+\\-\\s]{6,20}$", message = "El teléfono no tiene un formato válido")
    private String telefono;

    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    private String email;

    @Size(max = 200, message = "La dirección no debe superar los 200 caracteres")
    private String direccion;
}