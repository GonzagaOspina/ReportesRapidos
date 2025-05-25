package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.comentarios.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.comentarios.ComentarioRespuestaDTO;
import co.edu.uniquindio.proyecto.dto.comentarios.CrearComentarioDTO;
import co.edu.uniquindio.proyecto.dto.comentarios.EditarComentarioDTO;
import co.edu.uniquindio.proyecto.dto.reportes.*;
import co.edu.uniquindio.proyecto.seguridad.JWTUtils;
import co.edu.uniquindio.proyecto.servicios.ReporteServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/reportes")
public class ReporteControlador{

    private final ReporteServicio reporteServicio; // Inyectar servicio
    @Autowired
    private JWTUtils jwtUtils;


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

    @GetMapping("/mis-reportes")
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
    @Operation(summary = "editar reportes dado")
    public ResponseEntity<MensajeDTO<String>> editarReporte(
            @PathVariable String id,
            @Valid @RequestBody EditarReporteDTO reporteDTO) throws Exception {

        reporteServicio.editarReporte(id, reporteDTO);

        return ResponseEntity.ok(new MensajeDTO<>(false, "Reporte actualizado"));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un reporte por ID")
    public ResponseEntity<MensajeDTO<String>> eliminarReporte(@PathVariable String id) throws Exception {
        reporteServicio.eliminarReporte(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Reporte eliminado correctamente"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un reporte por su ID")
    public ResponseEntity<MensajeDTO<ReporteDTO>> obtenerReporte(@PathVariable String id) throws Exception {
        ReporteDTO dto = reporteServicio.obtenerReporteId(id);  // ← Este método debe devolver el DTO con toda la info
        return ResponseEntity.ok(new MensajeDTO<>(false, dto)); // ← Esto sí es lo que espera el frontend
    }


    @PostMapping("/{id}/comentarios")
    public ResponseEntity<MensajeDTO<String>> agregarComentario(
            @PathVariable String id,
            @Valid @RequestBody ComentarioDTO comentarioDTO) throws Exception {

        reporteServicio.agregarComentario(id, comentarioDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Comentario agregado con éxito"));
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<MensajeDTO<List<ComentarioRespuestaDTO>>> obtenerComentarios(@PathVariable String id) throws Exception {
        List<ComentarioRespuestaDTO> comentarios = reporteServicio.obtenerPorIdReporte(id);
        return ResponseEntity.ok(new MensajeDTO<List<ComentarioRespuestaDTO>>(false, comentarios));
    }

    @PutMapping("/{id}/importante")
    @Operation(summary = "Marcar un reporte como importante")
    public ResponseEntity<MensajeDTO<String>> marcarImportante(@PathVariable String id) throws Exception {
        reporteServicio.marcarImportante(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Reporte marcado como importante"));
    }


    @PostMapping("/{id}/estado")
    public ResponseEntity<MensajeDTO<String>> cambiarEstado(
            @PathVariable String id,
            @RequestBody EstadoSinUsuarioDTO dto,
            HttpServletRequest request
    ) throws Exception {
        String token = request.getHeader("Authorization");
        String idModerador = jwtUtils.obtenerIdUsuarioDesdeToken(token);

        reporteServicio.cambiarEstado(id, dto.nuevoEstado(), dto.motivo(), idModerador);
        return ResponseEntity.ok(new MensajeDTO<>(false, "✅ Estado actualizado correctamente"));
    }
    @PutMapping("/comentarios/{id}")
    public ResponseEntity<MensajeDTO<String>> editarComentario(
            @PathVariable String id,
            @Valid @RequestBody EditarComentarioDTO editarDTO,
            HttpServletRequest request) throws Exception {

        String token = request.getHeader("Authorization");
        String idUsuario = jwtUtils.obtenerIdUsuarioDesdeToken(token);
        ObjectId objectId = new ObjectId(id);

        reporteServicio.editarComentario(objectId, editarDTO.mensaje(), idUsuario);

        return ResponseEntity.ok(new MensajeDTO<>(false, "Comentario editado exitosamente"));
    }


    @DeleteMapping("/comentarios/{id}")
    @Operation(summary = "Eliminar un comentario por su ID")
    public ResponseEntity<MensajeDTO<String>> eliminarComentario(
            @PathVariable String id,
            HttpServletRequest request) throws Exception {

        String token = request.getHeader("Authorization");
        String idUsuario = jwtUtils.obtenerIdUsuarioDesdeToken(token);
        ObjectId objectId = new ObjectId(id);
        reporteServicio.eliminarComentario(objectId                             , idUsuario);

        return ResponseEntity.ok(new MensajeDTO<>(false, "Comentario eliminado exitosamente"));
    }


}
