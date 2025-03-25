package co.edu.uniquindio.proyecto.dto.usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UsuarioDTO(
        String nombre,
        String telefono,
        String direccion,
        String email
){

}