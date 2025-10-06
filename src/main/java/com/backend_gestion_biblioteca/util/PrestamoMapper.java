package com.backend_gestion_biblioteca.util;

import com.backend_gestion_biblioteca.domain.dto.PrestamoResponse;
import com.backend_gestion_biblioteca.domain.entity.Prestamo;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class PrestamoMapper {

    private final Queue<PrestamoResponse> lastResponses = new LinkedList<>();

    public PrestamoResponse toResponse(Prestamo prestamo) {
        PrestamoResponse response = new PrestamoResponse();
        response.setId(prestamo.getId());
        response.setLibroId(prestamo.getLibro().getId());
        response.setLibroId(prestamo.getUsuario().getId());
        response.setTituloLibro(prestamo.getLibro().getTitulo());
        response.setNombreUsuario(prestamo.getUsuario().getNombreCompleto());
        response.setFechaPrestamo(prestamo.getFechaPrestamo());
        response.setFechaVencimiento(prestamo.getFechaVencimiento());
        response.setFechaDevolucion(prestamo.getFechaDevolucion());
        response.setEstado(prestamo.getEstado());
        remember(response);
        return response;
    }

    private void remember(PrestamoResponse response) {
        lastResponses.add(response);
        if (lastResponses.size() > 10) {
            lastResponses.poll();
        }
    }

    public Queue<PrestamoResponse> getLastResponses() {
        return new LinkedList<>(lastResponses);
    }

}
