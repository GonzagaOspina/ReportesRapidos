package co.edu.uniquindio.proyecto.modelo.documentos;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comentarios")
@Data // Genera getters, setters, equals, toString, etc.
@NoArgsConstructor // Necesario para frameworks como Spring
@AllArgsConstructor
public class Comentario {

    @Id
    private ObjectId id;

    private ObjectId clienteId;
    private String nombreUsuario; // 👈 necesario para el nombre


    private ObjectId reporteId;

    private String mensaje;

    private LocalDateTime fechaCreacion;
}
