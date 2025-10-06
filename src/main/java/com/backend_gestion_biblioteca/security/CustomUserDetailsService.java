package com.backend_gestion_biblioteca.security;

import com.backend_gestion_biblioteca.repository.UsuarioRepository;
import com.backend_gestion_biblioteca.security.jwt.SecurityUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final SecurityUserMapper securityUserMapper;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository,
                                    SecurityUserMapper securityUserMapper) {
        this.usuarioRepository = usuarioRepository;
        this.securityUserMapper = securityUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByCorreo(username)
                .map(securityUserMapper::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}
