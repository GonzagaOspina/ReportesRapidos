package co.edu.uniquindio.proyecto.excepciones;

public class CategoriaNoEncontrada extends RuntimeException {
    public CategoriaNoEncontrada(String message) {
        super(message);
    }
}
