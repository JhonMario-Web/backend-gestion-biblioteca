package com.backend_gestion_biblioteca.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Getter
@Setter
public class LibroResponse {

    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String autor;

    @NotBlank
    @Pattern(regexp = "[0-9\\-]{10,17}", message = "El ISBN debe contener entre 10 y 17 caracteres numéricos")
    private String isbn;

    @NotNull
    private Year anioPublicacion;

    @NotBlank
    private String categoria;

    @NotNull
    @Min(1)
    private Integer copiasTotales;

    @NotNull
    @Min(0)
    private Integer copiasDisponibles;
}
