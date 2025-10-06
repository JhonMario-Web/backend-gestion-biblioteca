package com.backend_gestion_biblioteca.controller;

import com.backend_gestion_biblioteca.domain.dto.AuthRequest;
import com.backend_gestion_biblioteca.domain.dto.AuthResponse;
import com.backend_gestion_biblioteca.domain.dto.RegistrarUsuarioRequest;
import com.backend_gestion_biblioteca.domain.dto.UsuarioResponse;
import com.backend_gestion_biblioteca.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

        private final AuthService authService;

        public AuthController(AuthService authService) {
                this.authService = authService;
        }

        @PostMapping("/registrar-usuario")
        public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody RegistrarUsuarioRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
                return ResponseEntity.ok(authService.authenticate(request));
        }
}
