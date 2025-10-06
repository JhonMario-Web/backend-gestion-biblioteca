package com.backend_gestion_biblioteca.util;

import com.backend_gestion_biblioteca.domain.dto.PaginaResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginaMapper {
    public <T> PaginaResponse<T> toResponse(Page<T> page) {
        List<T> content = page.getContent();
        return new PaginaResponse<>(content, page.getTotalElements(), page.getTotalPages(), page.getNumber(), page.getSize());
    }
}
