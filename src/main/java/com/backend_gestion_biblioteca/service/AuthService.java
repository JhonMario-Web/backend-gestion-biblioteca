package com.backend_gestion_biblioteca.service;

import com.backend_gestion_biblioteca.domain.dto.AuthRequest;
import com.backend_gestion_biblioteca.domain.dto.AuthResponse;
import com.backend_gestion_biblioteca.domain.dto.RegistrarUsuarioRequest;
import com.backend_gestion_biblioteca.domain.dto.UsuarioResponse;

public interface AuthService {

    UsuarioResponse register(RegistrarUsuarioRequest request);

    AuthResponse authenticate(AuthRequest request);
}
