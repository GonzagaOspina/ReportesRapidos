package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.moderadores.CategoriaDTO;
import co.edu.uniquindio.proyecto.servicios.CategoriaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaControlador {

    private final CategoriaServicio categoriaServicio;

    @GetMapping("/listarCategorias")
    public ResponseEntity<MensajeDTO<List<CategoriaDTO>>> obtenerCategorias() {
        List<CategoriaDTO> lista = categoriaServicio.obtenerCategorias();
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }
}
