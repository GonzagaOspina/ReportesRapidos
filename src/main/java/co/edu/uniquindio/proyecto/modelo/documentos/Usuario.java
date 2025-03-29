package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.modelo.vo.CodigoValidacion;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import co.edu.uniquindio.proyecto.modelo.enums.Ciudad;

import java.time.LocalDateTime;

@Document(collection="usuarios")
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Usuario {

    @Id
    private ObjectId id;
    private String nombre;
    private String telefono;
    private Ciudad ciudad;
    private Rol rol;
    private EstadoUsuario estado;
    private String direccion;
    private String email;
    private String password;
    private CodigoValidacion codigoValidacion;
    private LocalDateTime fechaRegistro;

    @Builder
    public Usuario(String nombre, String telefono, Ciudad ciudad, Rol rol, EstadoUsuario estado, String direccion, String email, String password, CodigoValidacion codigoValidacion, LocalDateTime fechaRegistro) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.ciudad = ciudad;
        this.rol = rol;
        this.estado = estado;
        this.direccion = direccion;
        this.email = email;
        this.password = password;
        this.codigoValidacion = codigoValidacion;
        this.fechaRegistro = fechaRegistro;
    }

}
