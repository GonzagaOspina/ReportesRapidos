package co.edu.uniquindio.proyecto.mapper;

import co.edu.uniquindio.proyecto.dto.moderadores.CategoriaDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    // ENTITY → DTO
    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    CategoriaDTO toDTO(Categoria categoria);

    // DTO → ENTITY
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId")
    Categoria toDocument(CategoriaDTO categoriaDTO);

    // --- Métodos auxiliares ---

    @Named("objectIdToString")
    default String objectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toString() : null;
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return id != null && !id.isBlank() ? new ObjectId(id) : null;
    }
}
