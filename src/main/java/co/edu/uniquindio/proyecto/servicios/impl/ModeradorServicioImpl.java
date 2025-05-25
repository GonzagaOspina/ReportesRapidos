package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.moderadores.CategoriaDTO;
import co.edu.uniquindio.proyecto.dto.moderadores.InformeDTO;
import co.edu.uniquindio.proyecto.dto.reportes.HistorialReporteDTO;
import co.edu.uniquindio.proyecto.excepciones.CategoriaNoEncontrada;
import co.edu.uniquindio.proyecto.excepciones.DatoRepetidoException;
import co.edu.uniquindio.proyecto.mapper.CategoriaMapper;
import co.edu.uniquindio.proyecto.mapper.ReporteMapper;
import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import co.edu.uniquindio.proyecto.repositorios.CategoriaRepo;
import co.edu.uniquindio.proyecto.repositorios.HistorialRepo;
import co.edu.uniquindio.proyecto.repositorios.InformeRepo;
import co.edu.uniquindio.proyecto.servicios.ModeradorServicio;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ModeradorServicioImpl implements ModeradorServicio {

    private final CategoriaMapper categoriaMapper;
    private final CategoriaRepo categoriaRepo;
    private final ReporteMapper reporteMapper;
    private final InformeRepo informeRepo;
    private final HistorialRepo historialRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void crearCategoria(CategoriaDTO categoriaDTO) throws Exception {
        if(this.existeCategoria(categoriaDTO.nombre())) throw new DatoRepetidoException("Categoria existente");

        Categoria categoria = categoriaMapper.toDocument(categoriaDTO);
        categoriaRepo.save(categoria);
    }

    public boolean existeCategoria(String nombre) {
        return categoriaRepo.existsByNombre(nombre);
    }

    @Override
    public  List<CategoriaDTO> obtenerCategorias() throws Exception {
        List<Categoria> categorias = categoriaRepo.findAll();

        return categorias.stream()
                .map(categoriaMapper::toDTO)
                .collect(Collectors.toList());    }

    @Override
    public void editarCategoria(String id, CategoriaDTO categoriaDTO) throws Exception {

        ObjectId objectId = new ObjectId(id);
        Optional<Categoria> categoriaOptional = categoriaRepo.findById(objectId);

        if (categoriaOptional.isEmpty()) {
            throw new CategoriaNoEncontrada("No se encontró la categoría: " + id);
        }

        Categoria categoria = categoriaOptional.get();
        categoria.setNombre(categoriaDTO.nombre());
        categoria.setDescripcion(categoriaDTO.descripcion()); // ← ¡Faltaba esto!

        categoriaRepo.save(categoria);
        System.out.println("✅ Categoría actualizada: " + categoria.getId());

    }

    @Override
    public CategoriaDTO obtenerCategoria(String id) throws Exception {
        Categoria categoria = categoriaRepo.findById(new ObjectId(id))
                .orElseThrow(() -> new Exception("Categoría no encontrada"));


        return new CategoriaDTO(
                categoria.getId().toHexString(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }

    @Override
    public void eliminarCategoria(String id) throws Exception {
        if (!ObjectId.isValid(id)) {
            throw new CategoriaNoEncontrada("No se encontró la categoria: "+id);
        }
        ObjectId objectId = new ObjectId(id);
        Optional<Categoria> categoriaOptional = categoriaRepo.findById(objectId);

        if(categoriaOptional.isEmpty()){
            throw new CategoriaNoEncontrada("No se encontró la categoria: "+id);
        }

        //Obtenemos el usuario que se quiere eliminar y le asignamos el estado eliminado
        Categoria categoria = categoriaOptional.get();
        categoriaRepo.delete(categoria);
    }

    @Override
    public List<InformeDTO> generarInforme(String ciudad, String categoria, LocalDate fechaInicio, LocalDate fechaFin)throws Exception{
        Date fechaInicioDate = java.sql.Timestamp.valueOf(fechaInicio.atStartOfDay());
        Date fechaFinDate = java.sql.Timestamp.valueOf(fechaFin.atStartOfDay());
        List<InformeDTO> reportes = informeRepo.findReportesByCiudadAndFecha(ciudad,categoria, fechaInicioDate, fechaFinDate);

        return reportes;
    }

    @Override
    public List<HistorialReporteDTO> obtenerHistorial(String idReporte) throws Exception {
        if (!ObjectId.isValid(idReporte)) {
            throw new Exception("No se encontró el reporte: "+idReporte);
        }

        return
                historialRepo.obtenerHistorial(new ObjectId(idReporte));    }
}
