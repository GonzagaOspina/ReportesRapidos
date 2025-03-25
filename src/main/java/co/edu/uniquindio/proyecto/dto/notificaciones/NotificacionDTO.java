package co.edu.uniquindio.proyecto.dto.notificaciones;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NotificacionDTO(
        @Email @NotBlank String email,
        @NotBlank String asunto,
        @NotBlank String mensaje
) {}