package com.backend.estudiantes.controller;

import com.backend.estudiantes.dto.LoginRequest;
import com.backend.estudiantes.models.Usuario;
import com.backend.estudiantes.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginRequest loginRequest) {

        try {
            Usuario usuario = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(Map.of(
                    "message", "Login exitoso",
                    "email", usuario.getUsername(),
                    "rol", usuario.getRol()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
}
