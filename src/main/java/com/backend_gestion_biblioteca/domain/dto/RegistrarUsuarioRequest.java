package com.backend_gestion_biblioteca.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegistrarUsuarioRequest {

    @NotBlank
    private String nombreCompleto;

    @Email
    @NotBlank
    private String correo;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String clave;

    @NotEmpty
    private Set<String> roles;
}
