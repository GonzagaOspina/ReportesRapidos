package co.edu.uniquindio.proyecto.modelo.documentos;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categorias")
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    private ObjectId id;
    private String nombre;
}