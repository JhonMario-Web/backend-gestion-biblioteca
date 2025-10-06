package com.backend_gestion_biblioteca.security.jwt;

import com.backend_gestion_biblioteca.domain.entity.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SecurityUserMapper {

    public UserDetails toUserDetails(Usuario usuario) {
        return User.withUsername(usuario.getCorreo())
                .password(usuario.getClave())
                .disabled(!usuario.isActivo())
                .authorities(usuario.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNombre()))
                        .collect(Collectors.toSet()))
                .build();
    }
}
