package co.edu.uniquindio.proyecto.repositorios;


import co.edu.uniquindio.proyecto.modelo.documentos.Comentario;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepo extends MongoRepository<Comentario, ObjectId> {
    List<Comentario> findAllByReporteId(ObjectId reporteId);
}

