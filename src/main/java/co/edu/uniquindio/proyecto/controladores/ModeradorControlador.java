package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.moderadores.CategoriaDTO;

import co.edu.uniquindio.proyecto.dto.moderadores.InformeDTO;
import co.edu.uniquindio.proyecto.dto.reportes.HistorialReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.ReporteDTO;
import co.edu.uniquindio.proyecto.servicios.ModeradorServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moderador")
@SecurityRequirement(name = "bearerAuth")
public class ModeradorControlador {

    private final ModeradorServicio moderadorServicio;

    @PostMapping("/categorias")
    @Operation(summary = "crear categoria")
    public ResponseEntity<MensajeDTO<String>> crearCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) throws Exception {
        moderadorServicio. crearCategoria(categoriaDTO);
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Categoría creada satisfactoriamente"));
    }


    @GetMapping("/categorias")
    @Operation(summary = "consultar todas las categorias")
    public ResponseEntity<MensajeDTO<List<CategoriaDTO>>> obtenerCategorias() throws Exception {
        List<CategoriaDTO> categorias=moderadorServicio.obtenerCategorias();
        return ResponseEntity.ok(new MensajeDTO<>(false, categorias));
    }

    @PutMapping("/categorias/{id}")
    @Operation(summary = "modificar categoria")
    public ResponseEntity<MensajeDTO<String>> editarCategoria(@PathVariable String id, @Valid @RequestBody CategoriaDTO categoriaDTO) throws Exception {
        moderadorServicio.editarCategoria(id,categoriaDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Categoría editada exitosamente"));
    }

    @DeleteMapping("/categorias/{id}")
    @Operation(summary = "eliminar categoria")
    public ResponseEntity<MensajeDTO<String>> eliminarCategoria(@PathVariable String id) throws Exception {
        moderadorServicio.eliminarCategoria(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Categoría eliminada"));
    }

    @GetMapping("/informes")
    @Operation(summary = "informe")
    public ResponseEntity<MensajeDTO<List<InformeDTO>>> generarInforme(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String ciudad) throws Exception {


        List<InformeDTO> informes = moderadorServicio.generarInforme(ciudad, categoria, fechaInicio, fechaFin);

        return ResponseEntity.ok(new MensajeDTO<>(false, informes));
    }

    @GetMapping("/historialEstados/{idReporte}")
    @Operation(summary = "Obtener historial de un reporte")
    public ResponseEntity<MensajeDTO<List<HistorialReporteDTO>>> obtenerComentarios(@PathVariable String idReporte) throws Exception {
        List<HistorialReporteDTO> historialReporte =moderadorServicio.obtenerHistorial(idReporte);
        return ResponseEntity.ok(new MensajeDTO<>(false, historialReporte));
    }
    @GetMapping("/categorias/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<MensajeDTO<CategoriaDTO>> obtenerCategoriaPorId(@PathVariable String id) throws Exception {
        CategoriaDTO categoria = moderadorServicio.obtenerCategoria(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, categoria));
    }

}
