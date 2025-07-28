package com.backend.estudiantes.services;

import com.backend.estudiantes.models.Role;
import com.backend.estudiantes.models.Usuario;
import com.backend.estudiantes.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // habilitar mockito para las pruebas
public class UsuarioDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioDetailsService usuarioDetailsService;

    @Test
    public void cuandoUsuarioNoExiste_lanzarUsernameNotFoundException() {

        String emailInexistente = "noexiste@email.test.com";

        //simulacion de optional vacio
        when(usuarioRepository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername(emailInexistente)
        );

        verify(usuarioRepository, times(1)).findByEmail(emailInexistente);

    }

    @Test
    public void cuandoUsuarioExiste_retornaUserDetailsCorrecto() {

        String email = "testuserencontrado@gmail.com";
        String password = "password123";
        Role rol = Role.ADMIN;

        Usuario usuarioMock = new Usuario();
        usuarioMock.setEmail(email);
        usuarioMock.setPassword(password);
        usuarioMock.setRol(rol);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioMock));

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

        //verificando que no sea nulo
        assertNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        //AUTORIDAD DE SPRING SECURITY ROLE_ADMIN
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + rol.name()))
        );
    }
}