package com.backend_gestion_biblioteca.util;

import com.backend_gestion_biblioteca.domain.dto.LibroRequest;
import com.backend_gestion_biblioteca.domain.dto.LibroResponse;
import com.backend_gestion_biblioteca.domain.entity.Libro;
import org.springframework.stereotype.Component;

@Component
public class LibroMapper {

    public Libro toEntity(LibroRequest request) {
        Libro libro = new Libro();
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setIsbn(request.getIsbn());
        libro.setAnioPublicacion(request.getAnioPublicacion());
        libro.setCategoria(request.getCategoria());
        libro.setCopiasTotales(request.getCopiasTotales());
        libro.setCopiasDisponibles(request.getCopiasDisponibles());
        return libro;
    }

    public void updateEntity(Libro libro, LibroRequest request) {
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setIsbn(request.getIsbn());
        libro.setAnioPublicacion(request.getAnioPublicacion());
        libro.setCategoria(request.getCategoria());
        libro.setCopiasTotales(request.getCopiasTotales());
        libro.setCopiasDisponibles(request.getCopiasDisponibles());
    }

    public LibroResponse toResponse(Libro libro) {
        LibroResponse response = new LibroResponse();
        response.setId(libro.getId());
        response.setTitulo(libro.getTitulo());
        response.setAutor(libro.getAutor());
        response.setIsbn(libro.getIsbn());
        response.setAnioPublicacion(libro.getAnioPublicacion());
        response.setCategoria(libro.getCategoria());
        response.setCopiasTotales(libro.getCopiasTotales());
        response.setCopiasDisponibles(libro.getCopiasDisponibles());
        return response;
    }
}
