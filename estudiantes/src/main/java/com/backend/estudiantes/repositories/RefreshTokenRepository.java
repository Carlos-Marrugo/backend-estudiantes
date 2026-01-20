package com.backend.estudiantes.repositories;

import com.backend.estudiantes.models.RefreshToken;
import com.backend.estudiantes.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUsuario(Usuario usuario);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.usuario = :usuario")
    void deleteByUsuario(@Param("usuario") Usuario usuario);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();
}
