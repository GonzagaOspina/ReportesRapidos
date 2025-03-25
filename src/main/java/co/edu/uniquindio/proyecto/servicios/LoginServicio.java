package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.login.LoginRequestDTO;
import co.edu.uniquindio.proyecto.dto.login.PasswordNuevoDTO;
import co.edu.uniquindio.proyecto.dto.login.PasswordOlvidadoDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoginServicio {

    void login( LoginRequestDTO loginRequest) throws Exception ;

    void  recuperarPassword(PasswordOlvidadoDTO passwordOlvidadoDTO) throws Exception ;

    void  actualizarPassword(PasswordNuevoDTO passwordNuevoDTO) throws Exception;
}
