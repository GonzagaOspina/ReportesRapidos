package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.MensajeDTO;
import co.edu.uniquindio.proyecto.dto.notificaciones.NotificacionDTO;
import co.edu.uniquindio.proyecto.dto.notificaciones.NotificacionUbicacionDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface NotificacionSevicio {

    @PostMapping("/email")
    void enviarNotificacion(NotificacionDTO notificacionDTO) throws Exception;

   void enviarNotificacionUbicacion(@Valid @RequestBody NotificacionUbicacionDTO notificacionUbicacionDTO) throws Exception;

}
