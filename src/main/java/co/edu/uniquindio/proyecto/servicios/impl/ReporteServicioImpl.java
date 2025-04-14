package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.comentarios.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.notificaciones.NotificacionDTO;
import co.edu.uniquindio.proyecto.dto.reportes.CrearReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.EditarReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.EstadoReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.ReporteDTO;
import co.edu.uniquindio.proyecto.excepciones.CategoriaNoEncontrada;
import co.edu.uniquindio.proyecto.excepciones.DatoRepetidoException;
import co.edu.uniquindio.proyecto.mapper.ComentarioMapper;
import co.edu.uniquindio.proyecto.mapper.ReporteMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.repositorios.CategoriaRepo;
import co.edu.uniquindio.proyecto.repositorios.ReporteRepo;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import co.edu.uniquindio.proyecto.servicios.ReporteServicio;
import co.edu.uniquindio.proyecto.modelo.vo.Ubicacion;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServicioImpl implements ReporteServicio {

    @Autowired
    private final ReporteRepo reporteRepo;
    private final ReporteMapper reporteMapper;
    private final UsuarioRepo usuarioRepo;
    private final UsuarioServicioImpl usuarioServicio;
    private final CategoriaRepo categoriaRepo;
    private final ComentarioMapper comentarioMapper;
    private final EmailServicio emailServicio;

    @Override
    public void crearReporte(CrearReporteDTO crearReporteDTO) throws Exception {
        String id = usuarioServicio.obtenerIdSesion();

        // Mapear DTO a documento y guardar en la base de datos
        Reporte reporte = reporteMapper.toDocument(crearReporteDTO);
        reporte.setUsuarioId(new ObjectId(id));
        // Buscar al usuario por su ID
        Usuario usuario = usuarioRepo.findById(new ObjectId(id)).orElseThrow(() -> new Exception("Usuario no encontrado"));
        reporte.setUsuarioId(usuario.getId());

        // Validar si el categoriaId es válido antes de intentar convertirlo
        if (crearReporteDTO.categoria() == null || !ObjectId.isValid(crearReporteDTO.categoria())) {
            throw new CategoriaNoEncontrada("Categoría no encontrada");
        }

        // Validar que la categoría existe antes de asignarla al reporte
        ObjectId categoriaId = new ObjectId(crearReporteDTO.categoria());
        Categoria categoria = categoriaRepo.findById(categoriaId).orElseThrow(() -> new CategoriaNoEncontrada("Categoría no encontrada"));

        // Asignar la categoría al reporte
        reporte.setCategoriaId(categoria.getId());

        reporteRepo.save(reporte);
//        NotificacionDTO notificacionDTO = new NotificacionDTO(
  //              "Nuevo Reporte",
    //            "Se acaba de crear un nuevo reporte: " + reporte.getTitulo(),
      //          "reports"
        //);


        //webSocketNotificationService.notificarClientes(notificacionDTO);

    }

    private boolean existeReporte(double latitud, double longitud, String descripcion) {
        return reporteRepo.findByUbicacion_LatitudAndUbicacion_LongitudAndDescripcion(latitud, longitud, descripcion).isPresent();

    }

    @Override
    public List<ReporteDTO> obtenerReportes() {
        return reporteRepo.obtenerReportes();
    }


    @Override
    public List<ReporteDTO> obtenerReportesUsuario() throws Exception {

        String usuarioId = usuarioServicio.obtenerIdSesion();
        return reporteRepo.obtenerReportesUsuario(new ObjectId(usuarioId));

    }

    @Override
    public List<ReporteDTO> obtenerReportesCerca(double latitud, double longitud) {
        return reporteRepo.obtenerReportesCerca(latitud, longitud);
    }

    @Override
    public List<ReporteDTO> obtenerTopReportes() throws Exception {
        return List.of();
    }

    @Override
    public void editarReporte(String id, EditarReporteDTO editarReporteDTO) throws Exception {
        if (!ObjectId.isValid(id)) {
            throw new Exception("No se encontró el reporte con el id " + id);
        }

        Optional<Reporte> reporteOptional = reporteRepo.findById(id);
        if (reporteOptional.isEmpty()) {
            throw new Exception("No se encontró el reporte " + id);
        }

        Reporte reporte = reporteOptional.get();

        // Obtener ID del usuario autenticado
        String usernameAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        ObjectId usuarioAutenticadoId = new ObjectId(usernameAutenticado);

        // Verificar que el reporte le pertenezca
        if (!reporte.getUsuarioId().equals(usuarioAutenticadoId)) {
            throw new Exception("No tienes permiso para editar este reporte.");
        }

        // Mapear y guardar
        reporteMapper.toDocument(editarReporteDTO, reporte);
        reporteRepo.save(reporte);
    }

    @Override
    public void eliminarReporte(String id) throws Exception {

        // Validamos el id del reporte
        if (!ObjectId.isValid(id)) {
            throw new Exception("No se encontró el reporte" + id);
        }

        Optional<Reporte> reporteOptional = reporteRepo.findById(id);

        if (reporteOptional.isEmpty()) {
            throw new Exception("No se encontró el reporte " + id);
        }

        // Obtenemos el reporte que se quiere eliminar
        Reporte reporte = reporteOptional.get();

        // Obtenemos el usuario autenticado
        String usernameAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        ObjectId usuarioAutenticadoId = new ObjectId(usernameAutenticado);

        // Verificamos si el reporte pertenece al usuario autenticado
        if (!reporte.getUsuarioId().equals(usuarioAutenticadoId)) {
            throw new AccessException("No tienes permiso para eliminar este reporte.");
        }

        // Eliminamos el reporte
        reporteRepo.deleteById(id);
    }

    @Override
    public ReporteDTO obtenerReporte(String id) throws Exception {

        // Validamos que el id sea válido
        if (!ObjectId.isValid(id)) {
            throw new Exception("No se encontró el reporte con el id " + id);
        }

        return reporteRepo.obtenerReporteId(new ObjectId(id));
    }

    @Override
    public void agregarComentario(String id, ComentarioDTO comentarioDTO) throws Exception {

    }

    @Override
    public List<ComentarioDTO> obtenerComentarios(String idReporte) throws Exception {
        return List.of();
    }

    @Override
    public void marcarImportante(String id) throws Exception {

    }

    @Override
    public void cambiarEstado(String id, EstadoReporteDTO estadoDTO) throws Exception {

    }
}
