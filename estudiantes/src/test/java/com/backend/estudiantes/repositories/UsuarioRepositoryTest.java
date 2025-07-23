package com.backend.estudiantes.repositories;

import com.backend.estudiantes.models.Role;
import com.backend.estudiantes.models.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UsuarioRepositoryTest {


    @Autowired
    private UsuarioRepository repository; //inyeccion de depencias para poder usar y hacer los test

    @Test
    void findByEmail_UsuarioExistente_RetornaUsuario() {
        //contructor de usuario

        Usuario testUser = new Usuario(
                "Test",
                "User",
                "test@unicolombo.edu.co",
                "123",
                Role.ESTUDIANTE
        );


        //guardando usuario temporalmente
        Usuario guardado = repository.save(testUser);

        //estamos haciendo busqueda y devolviendolo en un optinal
        Optional<Usuario> encontradoOptional = repository.findByEmail(guardado.getEmail());

        //validando por si falla
        assertTrue(encontradoOptional.isPresent(), "EL Usuario deberia existrir en la base de datos.");

        //obtener realmente el suuario
        Usuario encontrado = encontradoOptional.get();

        //comporando emails, a ver si coinciden.
        assertEquals("test@unicolombo.edu.co", encontrado.getEmail(),
                "El email del usuario encontrado deberia coincidir con el de prueba");


    }



}
