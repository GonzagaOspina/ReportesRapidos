package co.edu.uniquindio.proyecto.dto.comentarios;

import java.time.LocalDateTime;

public record ComentarioRespuestaDTO(
        String id,
        String mensaje,
        String nombre,
        String idUsuario, // 👈 este campo es el CLAVE que te falta
        LocalDateTime fecha
) {}
