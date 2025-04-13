package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.usuarios.*;
import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioControlador {
    private final UsuarioServicio usuarioServicio;

    @PostMapping
    @Operation(summary = "Crear usuario")
    public ResponseEntity<MensajeDTO<String>> crear(@Valid @RequestBody CrearUsuarioDTO cuenta) throws Exception{
        usuarioServicio.crear(cuenta);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Su registro ha sido exitoso"));
    }

    @PostMapping("/notificacion")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoActivacion(@Valid @RequestBody UsuarioActivacionDTO usuarioActivacionDTO) throws Exception {
        usuarioServicio.enviarCodigoActivacion(usuarioActivacionDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Código enviado exitosamente"));
    }

    @PostMapping("/activar")
    @Operation(summary = "Activar usuario")
    public ResponseEntity<MensajeDTO<String>> activarCuenta(@Valid @RequestBody UsuarioActivacionDTO usuarioActivacionDTO) throws Exception {
        usuarioServicio.activarCuenta(usuarioActivacionDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Activado exitosamente"));
    }

    @SecurityRequirement(name="bearerAuth")
    @PutMapping
    @Operation(summary = "Editar usuario")
    public ResponseEntity<MensajeDTO<String>> editar(@Valid @RequestBody EditarUsuarioDTO cuenta) throws Exception{
        usuarioServicio.editar(cuenta);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta editada satisfactoriamente"));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/password")
    @Operation(summary = "Cambiar contraseña usuario")
    public ResponseEntity<MensajeDTO<String>> cambiarPassword(@RequestBody CambiarPasswordDTO cambiarPasswordDTO) throws Exception {
        usuarioServicio.cambiarPassword(cambiarPasswordDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Contraseña cambiada satisfactoriamente"));
    }



    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/eliminar")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<MensajeDTO<String>> eliminar() throws Exception{
        usuarioServicio.eliminar();
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada satisfactoriamente"));
    }


    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    @Operation(summary = "Consultar usuario")
    public ResponseEntity<MensajeDTO<UsuarioDTO>> obtener() throws Exception{
        UsuarioDTO info = usuarioServicio.obtener();
        return ResponseEntity.ok(new MensajeDTO<>(false, info));
    }

}
