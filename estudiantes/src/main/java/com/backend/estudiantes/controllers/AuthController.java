package com.backend.estudiantes.controllers;

import com.backend.estudiantes.dto.LoginRequest;
import com.backend.estudiantes.dto.RefreshTokenRequest;
import com.backend.estudiantes.models.RefreshToken;
import com.backend.estudiantes.models.Usuario;
import com.backend.estudiantes.services.AuthService;
import com.backend.estudiantes.services.JwtService;
import com.backend.estudiantes.services.RefreshTokenService;
import com.backend.estudiantes.utils.AuthReponseBuilder;
import com.backend.estudiantes.utils.ErrorReponseBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    public  AuthController(AuthService authService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    //Endpoint de login
    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        try{
            Usuario usuario = authService.authenticate(request.getEmail(), request.getPassword());

            refreshTokenService.deleteByUsuario(usuario);

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("rol", usuario.getRol());
            extraClaims.put("nombre", usuario.getNombre());
            extraClaims.put("email", usuario.getEmail());

            String jwtToken = jwtService.generateToken(extraClaims, usuario);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario);

            return ResponseEntity.ok(AuthReponseBuilder.buildAuthResponse(
                jwtToken,
                refreshToken.getToken(),
                usuario
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorReponseBuilder.buildErrorResponse(
                            e.getMessage(),
                            HttpStatus.UNAUTHORIZED
                    ));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            
            RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token no es valido!"));

                if (refreshTokenService.isTokenExpired(refreshToken)) {
                    refreshTokenService.deleteByUsuario(refreshToken.getUsuario());
                    throw new RuntimeException("Refresh token ha expirado");
                }

                RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);

                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("rol", refreshToken.getUsuario().getRol().name());
                extraClaims.put("nombre", refreshToken.getUsuario().getNombre());

                String newJwt = jwtService.generateToken(extraClaims, refreshToken.getUsuario());

                return ResponseEntity.ok(AuthReponseBuilder.buildAuthResponse(
                    newJwt,
                    newRefreshToken.getToken(),
                    refreshToken.getUsuario()
                ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorReponseBuilder.buildErrorResponse(
                            e.getMessage(),
                            HttpStatus.UNAUTHORIZED
                    ));
        }
    }
    

}