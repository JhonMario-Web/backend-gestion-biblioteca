package com.backend_gestion_biblioteca.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Getter
@Setter
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 120)
    private String autor;

    @Column(nullable = false, unique = true, length = 17)
    private String isbn;

    @Column(name = "anio_publicacion", nullable = false)
    private Year anioPublicacion;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(name = "copias_totales", nullable = false)
    private Integer copiasTotales;

    @Column(name = "copias_disponibles", nullable = false)
    private Integer copiasDisponibles;

    public Libro() {}

}
