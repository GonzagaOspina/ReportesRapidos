package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.login.LoginRequestDTO;
import co.edu.uniquindio.proyecto.dto.login.PasswordNuevoDTO;
import co.edu.uniquindio.proyecto.dto.login.PasswordOlvidadoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginControlador {

    @PostMapping
    public ResponseEntity<MensajeDTO<String>> login(@Valid @RequestBody LoginRequestDTO loginRequest) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "token"));
    }


    @PostMapping("/codigoValidacion")
    public ResponseEntity<MensajeDTO<String>> recuperarPassword(@Valid @RequestBody PasswordOlvidadoDTO passwordOlvidadoDTO) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "Código enviado al correo"));
    }

    @PostMapping("/password/nuevo")
    public ResponseEntity<MensajeDTO<String>> actualizarPassword(@Valid @RequestBody PasswordNuevoDTO passwordNuevoDTO) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, "Contraseña actualizada"));
    }
}
