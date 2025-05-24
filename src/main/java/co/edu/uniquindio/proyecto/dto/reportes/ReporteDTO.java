package co.edu.uniquindio.proyecto.dto.reportes;

import co.edu.uniquindio.proyecto.dto.UbicacionDTO;
import co.edu.uniquindio.proyecto.modelo.vo.Ubicacion;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record ReporteDTO(
        String id,
        String titulo,
        String descripcion,
        String categoria,
        String ciudad,
        LocalDateTime fechaCreacion,
        List<String> imagenes,
        UbicacionDTO ubicacion,
        String nombreUsuario,
        String estadoActual// 👈 Esto debes tener
) {}

