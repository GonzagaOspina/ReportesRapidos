package co.edu.uniquindio.proyecto.dto.comentarios;



import jakarta.validation.constraints.NotBlank;

public record EditarComentarioDTO(
        @NotBlank String mensaje
) {}