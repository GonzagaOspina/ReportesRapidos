package co.edu.uniquindio.proyecto.modelo.vo;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstadisticaUsuario {

    private int totalReportes;
    private int reportesAprobados;
    private int reportesRechazados;
    private int reportesResultados;
    private int totalComentarios;
    private LocalDateTime fechaActualizacion;
}