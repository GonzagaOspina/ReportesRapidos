package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.TokenDTO;
import co.edu.uniquindio.proyecto.dto.login.LoginRequestDTO;
import co.edu.uniquindio.proyecto.dto.login.PasswordNuevoDTO;
import co.edu.uniquindio.proyecto.dto.login.PasswordOlvidadoDTO;
import co.edu.uniquindio.proyecto.servicios.LoginServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginControlador {

    private final LoginServicio loginServicio;

    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO loginRequest) throws Exception {
        TokenDTO tokenDTO=loginServicio.login(loginRequest);
        String token=tokenDTO.toString();
        return ResponseEntity.ok(token);
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
