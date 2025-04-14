package co.edu.uniquindio.proyecto.dto.moderadores;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(@NotBlank String nombre, @NotBlank String descripcion) {}
