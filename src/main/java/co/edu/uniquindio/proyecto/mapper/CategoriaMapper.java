package co.edu.uniquindio.proyecto.mapper;


import co.edu.uniquindio.proyecto.dto.moderadores.CategoriaDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    Categoria toDocument(CategoriaDTO categoriaDTO);

    CategoriaDTO toDTO(Categoria categoria);

    // Método para mapear de ObjectId a String
    default String map(ObjectId value) {
        return value != null ? value.toString() : null;
    }
}
