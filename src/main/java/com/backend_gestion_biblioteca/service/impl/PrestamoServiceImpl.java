package com.backend_gestion_biblioteca.service.impl;

import com.backend_gestion_biblioteca.config.LibraryProperties;
import com.backend_gestion_biblioteca.domain.dto.PaginaResponse;
import com.backend_gestion_biblioteca.domain.dto.PrestamoRequest;
import com.backend_gestion_biblioteca.domain.dto.PrestamoResponse;
import com.backend_gestion_biblioteca.domain.entity.Libro;
import com.backend_gestion_biblioteca.domain.entity.Prestamo;
import com.backend_gestion_biblioteca.domain.entity.Usuario;
import com.backend_gestion_biblioteca.domain.enums.EstadosPrestamo;
import com.backend_gestion_biblioteca.exception.BusinessException;
import com.backend_gestion_biblioteca.exception.ResourceNotFoundException;
import com.backend_gestion_biblioteca.repository.LibroRepository;
import com.backend_gestion_biblioteca.repository.PrestamoRepository;
import com.backend_gestion_biblioteca.repository.UsuarioRepository;
import com.backend_gestion_biblioteca.service.PrestamoService;
import com.backend_gestion_biblioteca.util.LibraryOperationTracker;
import com.backend_gestion_biblioteca.util.PaginaMapper;
import com.backend_gestion_biblioteca.util.PrestamoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoMapper prestamoMapper;
    private final PaginaMapper paginaMapper;
    private final LibraryOperationTracker operationTracker;
    private final LibraryProperties libraryProperties;
    private final Map<Long, Deque<Long>> waitingLists = new ConcurrentHashMap<>();

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository, LibroRepository libroRepository, UsuarioRepository usuarioRepository,
                           PrestamoMapper prestamoMapper,
                           PaginaMapper paginaMapper,
                           LibraryOperationTracker operationTracker,
                           LibraryProperties libraryProperties) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoMapper = prestamoMapper;
        this.paginaMapper = paginaMapper;
        this.operationTracker = operationTracker;
        this.libraryProperties = libraryProperties;
    }

    @Override
    public PrestamoResponse crearPrestamo(PrestamoRequest request) {
        Libro libro = getLibro(request.getLibroId());
        Usuario usuario = getUsuario(request.getUsuarioId());

        validarDisponibilidadPrestamo(usuario,libro);

        if (libro.getCopiasDisponibles() <= 0) {
            waitingLists.computeIfAbsent(libro.getId(), id -> new ArrayDeque<>()).add(usuario.getId());
            throw new BusinessException("No copies available. The user was added to the waiting list");
        }

        libro.setCopiasDisponibles(libro.getCopiasDisponibles() - 1);

        int dias = request.getDiasPrestamo() > 0 ? request.getDiasPrestamo() : libraryProperties.getDiasPrestamo();
        LocalDate now = LocalDate.now();

        Prestamo prestamo = new Prestamo();

        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo(now);
        prestamo.setFechaDevolucion(now.plusDays(dias));
        prestamo.setEstado(EstadosPrestamo.ACTIVO);

        Prestamo guardado = prestamoRepository.save(prestamo);
        operationTracker.recordOperation("Préstamo de libro " + libro.getTitulo() + " a " + usuario.getCorreo());
        return prestamoMapper.toResponse(guardado);
    }

    @Override
    public PrestamoResponse retonarLibro(Long prestamoId) {
        Prestamo prestamo = getPrestamo(prestamoId);
        if (prestamo.getEstado() == EstadosPrestamo.DEVUELTO) {
            throw new BusinessException("The loan has already been repaid");
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setEstado(prestamo.getFechaVencimiento().isBefore(prestamo.getFechaDevolucion()) ? EstadosPrestamo.TARDE : EstadosPrestamo.DEVUELTO);

        Libro libro = prestamo.getLibro();
        libro.setCopiasDisponibles(libro.getCopiasDisponibles() + 1);

        Prestamo guardado = prestamoRepository.save(prestamo);
        operationTracker.recordOperation("Devolución de libro " + libro.getTitulo());
        aignarSiguienteUsuarioEspera(libro);
        return prestamoMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponse<PrestamoResponse> listarPrestamos(Pageable pageable) {
        Page<PrestamoResponse> page = prestamoRepository.findAll(pageable)
                .map(prestamoMapper::toResponse);
        return paginaMapper.toResponse(page);
    }

    private void aignarSiguienteUsuarioEspera(Libro libro) {
        Deque<Long> queue = waitingLists.get(libro.getId());

        if (queue == null || queue.isEmpty() || libro.getCopiasDisponibles() <= 0) {
            if (queue != null && queue.isEmpty()) {
                waitingLists.remove(libro.getId());
            }
            return;
        }

        Long nextUserId = queue.poll();
        if (nextUserId == null) {
            return;
        }

        Usuario siguenteUsuario = usuarioRepository.findById(nextUserId).orElse(null);
        if (siguenteUsuario == null || !siguenteUsuario.isActivo()) {
            aignarSiguienteUsuarioEspera(libro);
            return;
        }

        PrestamoRequest request = new PrestamoRequest();
        request.setLibroId(libro.getId());
        request.setUsuarioId(siguenteUsuario.getId());
        request.setDiasPrestamo(libraryProperties.getDiasPrestamo());
        try {
            crearPrestamo(request);
            operationTracker.recordOperation("Automatic loan for standby user" + siguenteUsuario.getCorreo());
        } catch (BusinessException ex) {
            operationTracker.recordError("Could not assign auto loan: " + ex.getMessage());
        }
    }

    private void validarDisponibilidadPrestamo(Usuario usuario, Libro book) {
        if (!usuario.isActivo()) {
            throw new BusinessException("User is not active");
        }
        long activeLoans = prestamoRepository.countByUsuarioAndEstado(usuario, EstadosPrestamo.ACTIVO);
        if (activeLoans >= libraryProperties.getMaxActive()) {
            throw new BusinessException("User reached active borrowing limit");
        }
    }

    private Prestamo getPrestamo(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan Not Found"));
    }

    private Libro getLibro(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book Not Found"));
    }

    private Usuario getUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }
}
