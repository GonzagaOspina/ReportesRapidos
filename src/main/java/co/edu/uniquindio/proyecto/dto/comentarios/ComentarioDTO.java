package co.edu.uniquindio.proyecto.dto.comentarios;

import jakarta.validation.constraints.NotBlank;

public record ComentarioDTO(
        @NotBlank String comentario,
        @NotBlank String idReporte,
        @NotBlank String idUsuario
) {}
