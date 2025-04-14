package co.edu.uniquindio.proyecto.excepciones;

public class DatosNoValidosException extends RuntimeException {
    public DatosNoValidosException(String message) {
        super(message);
    }
}
