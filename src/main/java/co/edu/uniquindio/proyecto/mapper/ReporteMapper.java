package co.edu.uniquindio.proyecto.mapper;

import co.edu.uniquindio.proyecto.dto.UbicacionDTO;
import co.edu.uniquindio.proyecto.dto.reportes.CrearReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.EditarReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.ReporteDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.vo.Ubicacion;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ReporteMapper {
    @Mapping(target = "fechaCreacion", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "estadoActual", constant = "PENDIENTE")
    Reporte toDocument(CrearReporteDTO reporteDTO);

    ReporteDTO toDTO(Reporte reporte);

    // Método para mapear de ObjectId a String
    default String map(ObjectId value) {
        return value != null ? value.toString() : null;
    }

    // Método para actualizar un Reporte
    default void toDocument(EditarReporteDTO dto, @MappingTarget Reporte reporte) {
        reporte.setTitulo(dto.titulo());
        reporte.setDescripcion(dto.descripcion());
        reporte.setUbicacion(map(dto.ubicacion()));
        reporte.setCategoriaId(new ObjectId(dto.categoria()));
        reporte.setImagenes(new java.util.ArrayList<>(dto.imagen())); // 🔥 AQUI está el FIX
    }
    //Mapeo explícito
    default Ubicacion map(UbicacionDTO ubicacionDTO) {
        return ubicacionDTO != null ? new Ubicacion(ubicacionDTO.latitud(), ubicacionDTO.longitud()) : null;
    }

    //Mapeo explícito
    default UbicacionDTO map(Ubicacion ubicacion) {
        return ubicacion != null ? new UbicacionDTO(ubicacion.getLatitud(), ubicacion.getLongitud()) : null;
    }

    default String map(LocalDateTime fecha) {
        if (fecha == null) return null;

        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy h:mm a", new java.util.Locale("es", "CO"));

        // conversor "PM" a "pm", etc.
        return fecha.format(formatter).replace("AM", "am").replace("PM", "pm");
    }
}