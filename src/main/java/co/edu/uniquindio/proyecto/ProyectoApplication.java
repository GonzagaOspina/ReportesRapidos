package co.edu.uniquindio.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ProyectoApplication {

    public static void main(String[] args) {

        /*Usuario usuario = new Usuario(........);
        usuarioRepository.save(usuario); //db.usuarios.insertOne(......)
        usuarioRepository.remove()
        usuarioRepository.findByEmail() // db.usuarios.find({email: "sdsdsdssd"})
        usuarioRepository.findAll()*/

        SpringApplication.run(ProyectoApplication.class, args);
    }


}
