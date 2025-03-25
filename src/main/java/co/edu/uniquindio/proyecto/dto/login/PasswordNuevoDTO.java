package co.edu.uniquindio.proyecto.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordNuevoDTO(
        @NotBlank String codigo,
        @Email @NotBlank String email,
        @NotBlank String nuevoPassword
) {}