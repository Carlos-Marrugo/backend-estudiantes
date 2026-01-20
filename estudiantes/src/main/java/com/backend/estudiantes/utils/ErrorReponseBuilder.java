package com.backend.estudiantes.utils;

import com.backend.estudiantes.models.Usuario;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ErrorReponseBuilder {

    public static Map<String, Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", message);
        response.put("status", status.value());
        response.put("timestamp", System.currentTimeMillis());

        return response;
    }
}
