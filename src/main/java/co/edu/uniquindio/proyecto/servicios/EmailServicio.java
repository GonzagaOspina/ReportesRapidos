package co.edu.uniquindio.proyecto.servicios;

import co.edu.uniquindio.proyecto.dto.notificaciones.EmailDTO;

public interface EmailServicio {
    void enviarEmail(EmailDTO emailDTO);

    void enviarEmailComentarioReporte(String nombreUsuario, String comentario, String destinatario);

}
