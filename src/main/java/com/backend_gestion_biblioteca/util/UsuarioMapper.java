package com.backend_gestion_biblioteca.util;

import com.backend_gestion_biblioteca.domain.dto.RegistrarUsuarioRequest;
import com.backend_gestion_biblioteca.domain.dto.UsuarioResponse;
import com.backend_gestion_biblioteca.domain.entity.Rol;
import com.backend_gestion_biblioteca.domain.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public Usuario toEntity(RegistrarUsuarioRequest request, Set<Rol> roles) {
        Usuario user = new Usuario();
        user.setNombreCompleto(request.getNombreCompleto());
        user.setCorreo(request.getCorreo());
        user.setClave(request.getClave());
        user.setRoles(new LinkedHashSet<>(roles));
        return user;
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setCorreo(usuario.getCorreo());
        response.setFechaRegistro(usuario.getFechaRegistro());
        response.setActivo(usuario.isActivo());
        response.setRoles(usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toCollection(LinkedHashSet::new)));
        return response;
    }
}
