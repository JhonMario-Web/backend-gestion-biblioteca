package com.backend_gestion_biblioteca.domain.dto;

import com.backend_gestion_biblioteca.domain.enums.EstadosPrestamo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PrestamoResponse {

    private Long id;
    private Long libroId;
    private Long usuarioId;
    private String tituloLibro;
    private String nombreUsuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;
    private EstadosPrestamo estado;

}
