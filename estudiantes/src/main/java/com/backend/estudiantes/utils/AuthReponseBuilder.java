package com.backend.estudiantes.utils;

import com.backend.estudiantes.models.Usuario;

import java.util.HashMap;
import java.util.Map;

public class AuthReponseBuilder {

    public static Map<String, Object> buildAuthResponse(String token, Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("usuario", buildUsuarioResponse(usuario));
        return response;
    }

    private static Map<String, Object> buildUsuarioResponse(Usuario usuario) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", usuario.getId());
        userMap.put("email", usuario.getEmail());
        userMap.put("rol", usuario.getRol().name());
        userMap.put("name", usuario.getNombre());
        userMap.put("apellido", usuario.getApellido());
        return userMap;
    }

}
