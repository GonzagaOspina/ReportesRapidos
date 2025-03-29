package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import co.edu.uniquindio.proyecto.modelo.vo.HistorialReporte;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import co.edu.uniquindio.proyecto.modelo.vo.Ubicacion;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "reportes")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Reporte {

    @Id
    private ObjectId id;

    private ObjectId usuarioId;

    private String titulo;
    private ObjectId CategoriaId;
    private String descripcion;
    private Ubicacion ubicacion;
    private LocalDateTime fecha;
    private LocalDateTime fechaCreacion;
    private List<HistorialReporte> historial;
    private EstadoReporte estadoActual;
    private List<String> imagenes;
    private int contadorImportante;
}