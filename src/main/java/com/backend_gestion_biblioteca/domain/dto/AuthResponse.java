package com.backend_gestion_biblioteca.domain.dto;

import com.backend_gestion_biblioteca.domain.entity.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

        private String token;
        private String refreshToken;
        private UsuarioResponse usuario;

        public AuthResponse() {
        }

        public AuthResponse(String token, String refreshToken,  UsuarioResponse usuario) {
                this.token = token;
                this.refreshToken = refreshToken;
                this.usuario = usuario;
        }
}
