package com.backend_gestion_biblioteca.service.impl;

import com.backend_gestion_biblioteca.domain.dto.LibroRequest;
import com.backend_gestion_biblioteca.domain.dto.LibroResponse;
import com.backend_gestion_biblioteca.domain.dto.PaginaResponse;
import com.backend_gestion_biblioteca.domain.entity.Libro;
import com.backend_gestion_biblioteca.exception.BusinessException;
import com.backend_gestion_biblioteca.exception.ResourceNotFoundException;
import com.backend_gestion_biblioteca.repository.LibroRepository;
import com.backend_gestion_biblioteca.service.LibroService;
import com.backend_gestion_biblioteca.util.LibraryOperationTracker;
import com.backend_gestion_biblioteca.util.LibroMapper;
import com.backend_gestion_biblioteca.util.PaginaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;
    private final PaginaMapper paginaMapper;
    private final LibraryOperationTracker operationTracker;
    private final Deque<String> recentlyUpdatedCategories = new ArrayDeque<>();

    public LibroServiceImpl(LibroRepository libroRepository,
                            LibroMapper libroMapper,
                            PaginaMapper paginaMapper,
                            LibraryOperationTracker operationTracker) {
        this.libroRepository = libroRepository;
        this.libroMapper = libroMapper;
        this.paginaMapper = paginaMapper;
        this.operationTracker = operationTracker;
    }

    @Override
    public LibroResponse crearLibro(LibroRequest request) {
        libroRepository.findByIsbn(request.getIsbn()).ifPresent(book -> {
            throw new BusinessException("A workbook already exists with the ISBN provided");
        });

        Libro libro = libroMapper.toEntity(request);
        Libro guardado = libroRepository.save(libro);
        trackCategory(guardado.getCategoria());

        operationTracker.recordOperation("Book Log: " + guardado.getTitulo());
        return libroMapper.toResponse(guardado);
    }

    @Override
    public LibroResponse actualizarLibro(Long id, LibroRequest request) {
        Libro libro = getLibro(id);

        libroRepository.findByIsbn(request.getIsbn()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BusinessException("ISBN is already associated with another workbook");
            }
        });

        libroMapper.updateEntity(libro, request);
        trackCategory(libro.getCategoria());
        operationTracker.recordOperation("Book Update: " + libro.getTitulo());
        return libroMapper.toResponse(libroRepository.save(libro));
    }

    @Override
    public void eliminarLibro(Long id) {
        Libro libro = getLibro(id);
        libroRepository.delete(libro);
        operationTracker.recordOperation("Book deletion: " + libro.getTitulo());
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponse buscarPorId(Long id) {
        Libro libro = getLibro(id);
        return libroMapper.toResponse(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponse<LibroResponse> listarLibros(Pageable pageable) {
        Page<Libro> page = libroRepository.findAll(pageable);
        Page<LibroResponse> mapped = page.map(libroMapper::toResponse);
        return paginaMapper.toResponse(mapped);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponse<LibroResponse> buscarLibro(String title, String author, String category, Year publicationYear, Pageable pageable) {
        List<Libro> results = libroRepository.searchBooks(emptyToNull(title), emptyToNull(author), emptyToNull(category), publicationYear);
        LinkedList<LibroResponse> responses = new LinkedList<>();
        results.forEach(book -> responses.add(libroMapper.toResponse(book)));
        Page<LibroResponse> page = new PageImpl<>(responses, pageable, responses.size());
        operationTracker.recordOperation("Book search: " + (title != null ? title : ""));
        return paginaMapper.toResponse(page);
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private Libro getLibro(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book Not Found"));
    }

    private void trackCategory(String category) {
        if (category == null) {
            return;
        }
        recentlyUpdatedCategories.addFirst(category);
        if (recentlyUpdatedCategories.size() > 5) {
            recentlyUpdatedCategories.removeLast();
        }
    }
}
