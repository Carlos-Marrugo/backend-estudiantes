package com.backend.estudiantes.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.estudiantes.models.RefreshToken;
import com.backend.estudiantes.models.Usuario;
import com.backend.estudiantes.repositories.RefreshTokenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RefreshTokenService {
    
    private Long refreshTokenDurationMs = 86400000L; // 1 día en milisegundos

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(Usuario usuario) {
        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshTokenRepository.save(refreshToken);
    }


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByUsuario(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);
    }

    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        
        refreshTokenRepository.delete(oldToken);

        return createRefreshToken(oldToken.getUsuario());
    }

    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens();
    }


    public boolean isTokenExpired(RefreshToken token) {
        return token.isExpired();
    }
}
