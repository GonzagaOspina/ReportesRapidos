package co.edu.uniquindio.proyecto.dto.reportes;

import jakarta.validation.constraints.NotBlank;

public record EstadoSinUsuarioDTO(
        @NotBlank String nuevoEstado,
        @NotBlank String motivo
) {}
