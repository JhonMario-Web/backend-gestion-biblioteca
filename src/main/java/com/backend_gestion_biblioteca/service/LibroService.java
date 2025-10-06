package com.backend_gestion_biblioteca.service;

import com.backend_gestion_biblioteca.domain.dto.LibroRequest;
import com.backend_gestion_biblioteca.domain.dto.LibroResponse;
import com.backend_gestion_biblioteca.domain.dto.PaginaResponse;
import org.springframework.data.domain.Pageable;

import java.time.Year;

public interface LibroService {

    LibroResponse crearLibro(LibroRequest request);

    LibroResponse actualizarLibro(Long id, LibroRequest request);

    void eliminarLibro(Long id);

    LibroResponse buscarPorId(Long id);

    PaginaResponse<LibroResponse> listarLibros(Pageable pageable);

    PaginaResponse<LibroResponse> buscarLibro(String title, String author, String category, Year publicationYear, Pageable pageable);
}
