package com.backend_gestion_biblioteca.service;

import com.backend_gestion_biblioteca.domain.dto.PaginaResponse;
import com.backend_gestion_biblioteca.domain.dto.PrestamoRequest;
import com.backend_gestion_biblioteca.domain.dto.PrestamoResponse;
import org.springframework.data.domain.Pageable;

public interface PrestamoService {

    PrestamoResponse crearPrestamo(PrestamoRequest request);

    PrestamoResponse retonarLibro(Long prestamoId);

    PaginaResponse<PrestamoResponse> listarPrestamos(Pageable pageable);
}
