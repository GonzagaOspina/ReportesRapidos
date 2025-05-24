package co.edu.uniquindio.proyecto.modelo.vo;


import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class HistorialReporte {
    private String motivo;
    private EstadoReporte estado;
    private LocalDateTime fecha;
    private ObjectId idUsuario;

}
