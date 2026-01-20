package com.backend.estudiantes.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Setter
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    public RefreshToken() {}

    public boolean ixExpiryDate() {
        return Instant.now().isAfter(expiryDate);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    
}
