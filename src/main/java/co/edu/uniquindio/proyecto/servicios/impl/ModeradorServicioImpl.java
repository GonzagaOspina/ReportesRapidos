package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.moderadores.CategoriaDTO;
import co.edu.uniquindio.proyecto.dto.reportes.ReporteDTO;
import co.edu.uniquindio.proyecto.servicios.ModeradorServicio;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ModeradorServicioImpl implements ModeradorServicio {
    @Override
    public void crearCategoria(CategoriaDTO categoriaDTO) throws Exception {

    }

    @Override
    public ResponseEntity<MensajeDTO<String>> obtenerCategorias() throws Exception {
        return null;
    }

    @Override
    public void editarCategoria(String id, CategoriaDTO categoriaDTO) throws Exception {

    }

    @Override
    public void eliminarCategoria(String id) throws Exception {

    }

    @Override
    public List<ReporteDTO> generarInforme(String fechaInicio, String fechaFin, String categoria, Double latitud, Double longitud, int radio) throws Exception {
        return List.of();
    }
}
