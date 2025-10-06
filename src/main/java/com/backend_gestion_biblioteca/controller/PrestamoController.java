package com.backend_gestion_biblioteca.controller;

import com.backend_gestion_biblioteca.domain.dto.PaginaResponse;
import com.backend_gestion_biblioteca.domain.dto.PrestamoRequest;
import com.backend_gestion_biblioteca.domain.dto.PrestamoResponse;
import com.backend_gestion_biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService loanService) {
        this.prestamoService = loanService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<PrestamoResponse> crearPrestamo(@Valid @RequestBody PrestamoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.crearPrestamo(request));
    }

    @PutMapping("/{id}/retornar-libro")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<PrestamoResponse> retonarLibro(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.retonarLibro(id));
    }

    @GetMapping

    public ResponseEntity<PaginaResponse<PrestamoResponse>> listarPrestamos(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(prestamoService.listarPrestamos(pageable));
    }
}
