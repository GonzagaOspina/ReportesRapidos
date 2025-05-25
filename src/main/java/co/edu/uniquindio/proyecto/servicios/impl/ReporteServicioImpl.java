package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.comentarios.ComentarioDTO;
import co.edu.uniquindio.proyecto.dto.comentarios.ComentarioRespuestaDTO;
import co.edu.uniquindio.proyecto.dto.comentarios.CrearComentarioDTO;
import co.edu.uniquindio.proyecto.dto.notificaciones.NotificacionDTO;
import co.edu.uniquindio.proyecto.dto.reportes.CrearReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.EditarReporteDTO;
import co.edu.uniquindio.proyecto.dto.reportes.ReporteDTO;
import co.edu.uniquindio.proyecto.excepciones.CategoriaNoEncontrada;
import co.edu.uniquindio.proyecto.mapper.ComentarioMapper;
import co.edu.uniquindio.proyecto.mapper.ReporteMapper;
import co.edu.uniquindio.proyecto.mapper.ReporteMapperPersonalizado;
import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import co.edu.uniquindio.proyecto.modelo.documentos.Reporte;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoReporte;
import co.edu.uniquindio.proyecto.modelo.vo.HistorialReporte;
import co.edu.uniquindio.proyecto.repositorios.CategoriaRepo;
import co.edu.uniquindio.proyecto.repositorios.ComentarioRepo;
import co.edu.uniquindio.proyecto.repositorios.ReporteRepo;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import co.edu.uniquindio.proyecto.servicios.ReporteServicio;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final ReporteMapperPersonalizado repMapper;
    private final ComentarioRepo comentarioRepo;


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
        NotificacionDTO notificacionDTO = new NotificacionDTO(
                "Nuevo Reporte",
                "Se acaba de crear un nuevo reporte: " + reporte.getTitulo(),
                "reports"
        );


        //webSocketNotificationService.notificarClientes(notificacionDTO);

    }

    private boolean existeReporte(double latitud, double longitud, String descripcion) {
        return reporteRepo.findByUbicacion_LatitudAndUbicacion_LongitudAndDescripcion(latitud, longitud, descripcion).isPresent();

    }

    @Override
    public List<ReporteDTO> obtenerReportes() {
        return reporteRepo.findAll()
                .stream()
                .map(repMapper::toDTO)
                .toList();
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
    public ReporteDTO obtenerReporteId(String id)  {
        ObjectId objId = new ObjectId(id);
        return reporteRepo.obtenerReporteId(objId);  // ← Aquí se conecta con tu repositorio
    }
    @Override
    public void eliminarReporte(String id) throws Exception {
        if (!ObjectId.isValid(id)) {
            throw new Exception("ID inválido");
        }

        Optional<Reporte> optional = reporteRepo.findById(id);

        if (optional.isEmpty()) {
            throw new Exception("Reporte no encontrado");
        }

        Reporte reporte = optional.get();

        // Validación de usuario autenticado
        String idUsuarioToken = SecurityContextHolder.getContext().getAuthentication().getName();
        ObjectId idUsuario = new ObjectId(idUsuarioToken);

        if (!reporte.getUsuarioId().equals(idUsuario)) {
            throw new AccessException("No tienes permiso para eliminar este reporte.");
        }

        // ⚠️ Aquí cambiamos el estado
        reporte.setEstadoActual(EstadoReporte.ELIMINADO);
        reporteRepo.save(reporte);
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
    public void agregarComentario(String idReporte, ComentarioDTO comentarioDTO) throws Exception {
        if (!ObjectId.isValid(idReporte) || !ObjectId.isValid(comentarioDTO.idUsuario())) {
            throw new IllegalArgumentException("ID de usuario o reporte inválido");
        }

        ObjectId usuarioId = new ObjectId(comentarioDTO.idUsuario());
        ObjectId reporteObjectId = new ObjectId(idReporte);

        String nombreUsuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new Exception("Usuario no encontrado"))
                .getNombre();

        Comentario comentario = new Comentario(
                new ObjectId(),
                usuarioId,
                nombreUsuario,
                reporteObjectId,
                comentarioDTO.comentario(),
                LocalDateTime.now()
        );
        comentarioRepo.save(comentario);
    }



    @Override
    public List<ComentarioDTO> obtenerComentarios(String idReporte) throws Exception {
        return List.of();
    }

    @Override
    public void marcarImportante(String id) throws Exception {
        // Verifica que el ID es válido
        if (!ObjectId.isValid(id)) {
            throw new Exception("ID de reporte inválido");
        }

        // Obtener ID del usuario autenticado
        String usuarioId = usuarioServicio.obtenerIdSesion();
        ObjectId userObjectId = new ObjectId(usuarioId);

        // Obtener reporte
        Reporte reporte = reporteRepo.findById(id)
                .orElseThrow(() -> new Exception("Reporte no encontrado"));

        // Inicializar lista si está nula
        if (reporte.getContadorImportante() == null) {
            reporte.setContadorImportante(new ArrayList<>());
        }

        // Evitar votos duplicados
        if (!reporte.getContadorImportante().contains(userObjectId)) {
            reporte.getContadorImportante().add(userObjectId);
            reporteRepo.save(reporte);
        } else {
            throw new Exception("Ya has marcado este reporte como importante");
        }
    }


    @Override
    public void cambiarEstado(String idReporte, String nuevoEstado, String motivo, String idModerador) throws Exception {
        Optional<Reporte> optional = reporteRepo.findById(new ObjectId(idReporte).toString());
        if (optional.isEmpty()) {
            throw new Exception("No se encontró el reporte con id: " + idReporte);
        }

        Reporte reporte = optional.get();

        EstadoReporte nuevo = EstadoReporte.valueOf(nuevoEstado);

        reporte.setEstadoActual(nuevo);


        HistorialReporte historial = HistorialReporte.builder()
                .estado(nuevo)
                .fecha(LocalDateTime.now())
                .motivo(motivo)
                .idUsuario(new ObjectId(idModerador))
                .build();

        if (reporte.getHistorialReporte() == null) {
            reporte.setHistorialReporte(new ArrayList<>());
        }

        reporte.getHistorialReporte().add(historial);

        reporteRepo.save(reporte);
    }

    @Override
    public List<ComentarioRespuestaDTO> obtenerPorIdReporte(String idReporte) {
        ObjectId reporteObjectId = new ObjectId(idReporte);

        List<Comentario> comentarios = comentarioRepo.findAllByReporteId(reporteObjectId);

        return comentarios.stream()
                .map(comentario -> new ComentarioRespuestaDTO(
                        comentario.getId().toString(),
                        comentario.getMensaje(),
                        comentario.getNombreUsuario(),
                        comentario.getClienteId().toHexString(), // 👈 Aquí obtenemos el idUsuario
                        comentario.getFechaCreacion()
                ))
                .collect(Collectors.toList());
    }
    @Override
    public void editarComentario(ObjectId idComentario, String nuevoMensaje, String idUsuario) throws Exception {
        Comentario comentario = comentarioRepo.findById(idComentario)
                .orElseThrow(() -> new Exception("Comentario no encontrado"));

        if (!comentario.getClienteId().toHexString().equals(idUsuario)) {
            throw new SecurityException("No puedes editar este comentario");
        }

        comentario.setMensaje(nuevoMensaje);
        comentarioRepo.save(comentario);
    }
    @Override
    public void eliminarComentario(ObjectId idComentario, String idUsuario) throws Exception {
        Comentario comentario = comentarioRepo.findById(idComentario)
                .orElseThrow(() -> new Exception("Comentario no encontrado"));

        if (!comentario.getClienteId().toHexString().equals(idUsuario)) {
            throw new SecurityException("No puedes eliminar este comentario");
        }

        comentarioRepo.delete(comentario);
    }

}