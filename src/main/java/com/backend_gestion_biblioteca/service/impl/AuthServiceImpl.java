package com.backend_gestion_biblioteca.service.impl;

import com.backend_gestion_biblioteca.domain.dto.AuthRequest;
import com.backend_gestion_biblioteca.domain.dto.AuthResponse;
import com.backend_gestion_biblioteca.domain.dto.RegistrarUsuarioRequest;
import com.backend_gestion_biblioteca.domain.dto.UsuarioResponse;
import com.backend_gestion_biblioteca.domain.entity.Usuario;
import com.backend_gestion_biblioteca.exception.BusinessException;
import com.backend_gestion_biblioteca.repository.UsuarioRepository;
import com.backend_gestion_biblioteca.security.jwt.JwtService;
import com.backend_gestion_biblioteca.service.AuthService;
import com.backend_gestion_biblioteca.service.UsuarioService;
import com.backend_gestion_biblioteca.util.LibraryOperationTracker;
import com.backend_gestion_biblioteca.util.UsuarioMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@Transactional
public class AuthServiceImpl  implements AuthService {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final LibraryOperationTracker operationTracker;
    private final UsuarioMapper usuarioMapper;

    public AuthServiceImpl(UsuarioService usuarioService,
                           AuthenticationManager authenticationManager,
                           UsuarioRepository usuarioRepository,
                           JwtService jwtService,
                           LibraryOperationTracker operationTracker, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.operationTracker = operationTracker;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UsuarioResponse register(RegistrarUsuarioRequest request) {
        UsuarioResponse user = usuarioService.crearUsuario(request);
        operationTracker.recordOperation("Registration via authentication API: " + user.getCorreo());
        return user;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getClave()));

        if (!authentication.isAuthenticated()) {
            throw new BusinessException("Credenciales inválidas");
        }

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));

        String token = jwtService.generateToken(usuario, new HashMap<>());

        operationTracker.recordOperation("User authentication: " + usuario.getCorreo());
        return new AuthResponse(token, token, usuarioMapper.toResponse(usuario));
    }
}
