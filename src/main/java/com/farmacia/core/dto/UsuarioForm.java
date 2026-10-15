
package com.farmacia.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
public class UsuarioForm {

    private Long id;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no debe superar los 50 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "El usuario solo puede contener letras, números, punto, guion y guion bajo"
    )
    private String username;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    @Pattern(
            regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$",
            message = "El correo no tiene un formato válido"
    )
    private String email;

    @Size(max = 64, message = "La contraseña no debe superar los 64 caracteres")
    @Pattern(
            regexp = "^$|^(?=.*[A-Za-z])(?=.*\\d).{8,64}$",
            message = "La contraseña debe tener entre 8 y 64 caracteres, con al menos una letra y un número"
    )
    private String password;

    @Size(max = 64, message = "La confirmación no debe superar los 64 caracteres")
    private String confirmarPassword;

    @NotEmpty(message = "Debe asignar al menos un rol")
    private Set<String> roles = new LinkedHashSet<>();

    private Boolean activo = true;
}
