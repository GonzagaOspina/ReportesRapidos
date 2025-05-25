package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.comentarios.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.comentarios.ComentarioRespuestaDTO;
import co.edu.uniquindio.proyecto.dto.comentarios.CrearComentarioDTO;
import co.edu.uniquindio.proyecto.dto.reportes.CrearReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.EditarReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.EstadoReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.ReporteDTO;
import co.edu.uniquindio.proyecto.modelo.vo.Ubicacion;
import org.bson.types.ObjectId;

import java.util.List;

public interface ReporteServicio {

    void crearReporte( CrearReporteDTO crearReporteDTO) throws Exception ;

    List<ReporteDTO> obtenerReportes() throws Exception;

    List<ReporteDTO> obtenerReportesUsuario() throws Exception ;

    List<ReporteDTO> obtenerReportesCerca(double latitud,double longitud) throws Exception ;

    List<ReporteDTO> obtenerTopReportes() throws Exception ;

    void editarReporte(String id, EditarReporteDTO reporteDTO) throws Exception ;

    void eliminarReporte( String id) throws Exception ;

    ReporteDTO obtenerReporte( String id) throws Exception ;

    void agregarComentario(String id, ComentarioDTO comentarioDTO) throws Exception ;

    List<ComentarioDTO> obtenerComentarios( String idReporte) throws Exception ;

   void marcarImportante( String id) throws Exception ;

    void cambiarEstado(String idReporte, String nuevoEstado, String motivo, String idModerador) throws Exception;
    public ReporteDTO obtenerReporteId(String id);

    List<ComentarioRespuestaDTO> obtenerPorIdReporte(String idReporte);

    void editarComentario(ObjectId idComentario, String nuevoMensaje, String idUsuario) throws Exception;

    void eliminarComentario(ObjectId idComentario, String idUsuario) throws Exception;
}
