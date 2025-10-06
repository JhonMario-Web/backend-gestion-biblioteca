package com.backend_gestion_biblioteca.service;

import com.backend_gestion_biblioteca.domain.dto.PaginaResponse;
import com.backend_gestion_biblioteca.domain.dto.RegistrarUsuarioRequest;
import com.backend_gestion_biblioteca.domain.dto.UsuarioResponse;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    UsuarioResponse crearUsuario(RegistrarUsuarioRequest request);

    PaginaResponse<UsuarioResponse> listarUsuarios(Pageable pageable);

    UsuarioResponse buscarUsuarioPorId(Long id);

    void desactivarUsuario(Long id);

}
