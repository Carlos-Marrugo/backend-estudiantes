package com.backend.estudiantes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;

public class LoginRequest {

    @NotBlank(message = "Email es obligatorio")
    @Email(message = "El formato debe ser valido")
    private String email;

    @NotBlank(message = "El password es obligatorio")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
