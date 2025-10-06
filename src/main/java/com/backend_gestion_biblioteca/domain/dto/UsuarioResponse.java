package com.backend_gestion_biblioteca.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UsuarioResponse {

    private Long id;
    private String nombreCompleto;
    private String correo;
    private LocalDate fechaRegistro;
    private Set<String> roles;
    private boolean activo;

}
