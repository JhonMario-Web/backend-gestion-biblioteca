package com.backend_gestion_biblioteca.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

        @Email
        @NotBlank
        private String correo;

        @NotBlank
        private String clave;
}
