package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.comentarios.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.reportes.CrearReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.EditarReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.EstadoReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.ReporteDTO;
import co.edu.uniquindio.proyecto.servicios.ReporteServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/reportes")
public class ReporteControlador{

    private final ReporteServicio reporteServicio; // Inyectar servicio

    @PostMapping("/crearReporte")
    @Operation(summary = "crear reporte")
    public ResponseEntity<MensajeDTO<String>> crearReporte(@Valid @RequestBody CrearReporteDTO crearReporteDTO) throws Exception {
        reporteServicio.crearReporte(crearReporteDTO);
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Reporte creado exitosamente"));
    }

    @GetMapping
    @Operation(summary = "mostrar todos los Reportes")
    public ResponseEntity<MensajeDTO<List<ReporteDTO>>> obtenerReportes() throws Exception{
        List<ReporteDTO> reportes=reporteServicio.obtenerReportes();
        return ResponseEntity.ok(new MensajeDTO<>(false,reportes));
    }

    @GetMapping("/usuario")
    @Operation(summary = "mostrar reportes usario dado")
    public ResponseEntity<MensajeDTO<List<ReporteDTO>>> obtenerReportesUsuario() throws Exception {
        List<ReporteDTO> reportes=reporteServicio.obtenerReportesUsuario();
        return ResponseEntity.ok(new MensajeDTO<>(false, reportes));
    }

    @GetMapping("/ubicacion")
    public ResponseEntity<MensajeDTO<String>> obtenerReportesCerca() throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "reportes"));
    }

    @GetMapping("/topImportantes")
    public ResponseEntity<MensajeDTO<String>> obtenerTopReportes() throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "reportes"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> editarReporte(
            @PathVariable String id,
            @Valid @RequestBody EditarReporteDTO reporteDTO) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "Reporte actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarReporte(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "Reporte eliminado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> obtenerReporte(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "reporte"));
    }

    @PostMapping("/{id}/comentario")
    public ResponseEntity<MensajeDTO<String>> agregarComentario(
            @PathVariable String id,
            @Valid @RequestBody ComentarioDTO comentarioDTO) throws Exception {
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Comentario creado exitosamente"));
    }

    @GetMapping("/{idReporte}/comentarios")
    public ResponseEntity<MensajeDTO<String>> obtenerComentarios(@PathVariable String idReporte) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "comentarios"));
    }

    @PutMapping("/{id}/importante")
    public ResponseEntity<MensajeDTO<String>> marcarImportante(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "Reporte marcado como importante"));
    }

    @PostMapping("/{id}/estado")
    public ResponseEntity<MensajeDTO<String>> cambiarEstado(
            @PathVariable String id,
            @Valid @RequestBody EstadoReporteDTO estadoDTO) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "Estado del reporte actualizado"));
    }

}
