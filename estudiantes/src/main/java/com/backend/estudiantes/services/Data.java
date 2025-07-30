package com.backend.estudiantes.services;

import com.backend.estudiantes.models.Role;
import com.backend.estudiantes.models.Usuario;
import com.backend.estudiantes.repositories.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Data {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        /*Usuario admin = new Usuario();
        admin.setEmail("carlos.marrugo@unicolombo.edu.co");
        admin.setPassword(passwordEncoder.encode("Admin123"));
        admin.setRol(Role.ADMIN);
        admin.setNombre("Carlos");
        admin.setApellido("Marrugo");
        usuarioRepository.save(admin);*/
    }

}
