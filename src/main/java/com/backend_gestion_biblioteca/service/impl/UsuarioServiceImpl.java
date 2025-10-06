package com.backend_gestion_biblioteca.service.impl;

import com.backend_gestion_biblioteca.domain.dto.PaginaResponse;
import com.backend_gestion_biblioteca.domain.dto.RegistrarUsuarioRequest;
import com.backend_gestion_biblioteca.domain.dto.UsuarioResponse;
import com.backend_gestion_biblioteca.domain.entity.Rol;
import com.backend_gestion_biblioteca.domain.entity.Usuario;
import com.backend_gestion_biblioteca.exception.BusinessException;
import com.backend_gestion_biblioteca.exception.ResourceNotFoundException;
import com.backend_gestion_biblioteca.repository.RolRepository;
import com.backend_gestion_biblioteca.repository.UsuarioRepository;
import com.backend_gestion_biblioteca.service.UsuarioService;
import com.backend_gestion_biblioteca.util.LibraryOperationTracker;
import com.backend_gestion_biblioteca.util.PaginaMapper;
import com.backend_gestion_biblioteca.util.UsuarioMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final PaginaMapper paginaMapper;
    private final LibraryOperationTracker operationTracker;
    private final Deque<Long> recentlyDeactivated = new ArrayDeque<>();

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder,
                              UsuarioMapper usuarioMapper,
                              PaginaMapper paginaMapper,
                           LibraryOperationTracker operationTracker) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.paginaMapper = paginaMapper;
        this.operationTracker = operationTracker;
    }

    @Override
    public UsuarioResponse crearUsuario(RegistrarUsuarioRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new BusinessException("Email is already registered");
        }

        Set<Rol> roles = resolveRoles(request.getRoles());
        Usuario usuario = usuarioMapper.toEntity(request, roles);
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        Usuario guardado = usuarioRepository.save(usuario);
        operationTracker.recordOperation("User Log: " + guardado.getCorreo());
        return usuarioMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponse<UsuarioResponse> listarUsuarios(Pageable pageable) {
        Page<UsuarioResponse> page = usuarioRepository.findAll(pageable)
                .map(usuarioMapper::toResponse);
        return paginaMapper.toResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarUsuarioPorId(Long id) {
        return usuarioMapper.toResponse(getUsuario(id));
    }

    @Override
    public void desactivarUsuario(Long id) {
        Usuario usuario = getUsuario(id);
        usuario.setActivo(false);
        recentlyDeactivated.push(usuario.getId());
        if (recentlyDeactivated.size() > 10) {
            recentlyDeactivated.removeLast();
        }
        operationTracker.recordOperation("User deactivation: " + usuario.getCorreo());
    }

    private Usuario getUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    private Set<Rol> resolveRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            throw new BusinessException("At least one role must be assigned");
        }
        return roleNames.stream()
                .map(nombre -> rolRepository.findByNombre(nombre.toUpperCase()).orElseGet(() -> rolRepository.save(new Rol(nombre.toUpperCase()))))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
