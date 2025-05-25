package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.moderadores.CategoriaDTO;
import co.edu.uniquindio.proyecto.dto.moderadores.InformeDTO;
import co.edu.uniquindio.proyecto.dto.reportes.HistorialReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.ReporteDTO;
import jakarta.validation.Valid;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public interface ModeradorServicio  {

    void crearCategoria(CategoriaDTO categoriaDTO) throws Exception;

    List<CategoriaDTO> obtenerCategorias() throws Exception;

    void editarCategoria( String id, CategoriaDTO categoriaDTO) throws Exception;


    CategoriaDTO obtenerCategoria(String id) throws Exception;

    void eliminarCategoria(String id) throws Exception;

    List<InformeDTO> generarInforme(String ciudad, String categoria, LocalDate fechaInicio, LocalDate fechaFin)throws Exception;

    List<HistorialReporteDTO> obtenerHistorial(String idReporte) throws Exception ;
}



