package com.backend_gestion_biblioteca.controller;

import com.backend_gestion_biblioteca.domain.dto.LibroRequest;
import com.backend_gestion_biblioteca.domain.dto.LibroResponse;
import com.backend_gestion_biblioteca.domain.dto.PaginaResponse;
import com.backend_gestion_biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<LibroResponse> crearLibro(@Valid @RequestBody LibroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.crearLibro(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<LibroResponse> actualizarLibro(@PathVariable Long id, @Valid @RequestBody LibroRequest request) {
        return ResponseEntity.ok(libroService.actualizarLibro(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<PaginaResponse<LibroResponse>> listarLibros(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "titulo") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(libroService.listarLibros(pageable));
    }

    @GetMapping("/buscar-libro")
    public ResponseEntity<PaginaResponse<LibroResponse>> buscarLibro(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Year publicationYear = year != null ? Year.of(year) : null;
        return ResponseEntity.ok(libroService.buscarLibro(title, author, category, publicationYear, pageable));
    }
}
