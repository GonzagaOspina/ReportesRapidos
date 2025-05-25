package co.edu.uniquindio.proyecto.mapper;

import co.edu.uniquindio.proyecto.dto.UbicacionDTO;
import co.edu.uniquindio.proyecto.dto.reportes.ReporteDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReporteMapperPersonalizado {

    private final UsuarioRepo usuarioRepo;

    public ReporteDTO toDTO(Reporte r) {
        String nombreUsuario = "Desconocido";

        if (r.getUsuarioId() != null) {
            nombreUsuario = usuarioRepo.findById(r.getUsuarioId())
                    .map(u -> u.getNombre())
                    .orElse("Desconocido");
        }

        return new ReporteDTO(
                r.getId().toString(),
                r.getTitulo(),
                r.getDescripcion(),
                r.getCategoriaId().toString(), // Aquí puedes hacer lo mismo si necesitas nombre de categoría
                r.getCiudad().name(),
                r.getFechaCreacion(), // ✅ Aquí arreglado
                r.getImagenes(),
                new UbicacionDTO(
                        r.getUbicacion().getLatitud(),
                        r.getUbicacion().getLongitud()
                ),
                nombreUsuario,
                r.getEstadoActual().toString()
        );
    }
}
