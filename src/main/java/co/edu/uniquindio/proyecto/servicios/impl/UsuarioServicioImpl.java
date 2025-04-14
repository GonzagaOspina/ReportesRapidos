package co.edu.uniquindio.proyecto.servicios.impl;


import co.edu.uniquindio.proyecto.dto.notificaciones.EmailDTO;
import co.edu.uniquindio.proyecto.dto.usuarios.*;
import co.edu.uniquindio.proyecto.excepciones.EmailRepetidoException;
import co.edu.uniquindio.proyecto.excepciones.UsuarioNoEncotradoException;
import co.edu.uniquindio.proyecto.mapper.UsuarioMapper;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;

import co.edu.uniquindio.proyecto.modelo.vo.CodigoValidacion;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    @Autowired
    private final UsuarioRepo usuarioRepo;
    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
    private final UsuarioMapper usuarioMapper;
    private final EmailServicio emailServicio;

    @Override
    public void crear(CrearUsuarioDTO crearUsuarioDTO) throws Exception {
        
        if(existeEmail(crearUsuarioDTO.email())) throw new EmailRepetidoException("El email ya existe");

        String codigoGenerado=generarCodigoAleatorio();
        Usuario usuario = usuarioMapper.toDocument(crearUsuarioDTO);

        // Se codifica la contraseña
        usuario.setPassword(passwordEncoder.encode(crearUsuarioDTO.password()));


        usuarioRepo.save(usuario);
        
        CodigoValidacion codigo= new CodigoValidacion(
                LocalDateTime.now(),
                codigoGenerado
        );

        usuario.setCodigoValidacion(codigo);
        usuarioRepo.save(usuario);
        String cuerpoCorreo = "Tu código de activación es: " + codigoGenerado;
        EmailDTO emailDTO = new EmailDTO("Código de Activación", cuerpoCorreo, usuario.getEmail());
        emailServicio.enviarEmail(emailDTO); // Enviar el correo con el código
    }

    private boolean existeEmail(String email) {
        return usuarioRepo.existsByEmail(email);
    }

    public String generarCodigoAleatorio(){
        String digitos="0123456789";
        StringBuilder codigo=new StringBuilder();
        for(int i=0;i<4;i++){
            int indice=(int) (Math.random()*digitos.length());
            codigo.append(digitos.charAt(indice));
        }
        return codigo.toString();
    }

    @Override 
    public void enviarCodigoActivacion(UsuarioNuevoCodDTO usuarioNuevoCodDTO) throws Exception {
        // 1. Generar un nuevo código de activación
        String nuevoCodigo = generarCodigoAleatorio();

        // 2. Buscar el usuario por su correo
        Optional<Usuario> usuarioOptional = usuarioRepo.findByEmail(usuarioNuevoCodDTO.email());
        if (usuarioOptional.isEmpty()) {
            throw new Exception("No se encontró un usuario con el email " + usuarioNuevoCodDTO.email());
        }

        // 3. Obtener el usuario
        Usuario usuario = usuarioOptional.get();

        // 4. Crear un nuevo código de validación
        CodigoValidacion codigo = new CodigoValidacion(
                LocalDateTime.now(),
                nuevoCodigo
        );

        // 5. Asignar el nuevo código al usuario
        usuario.setCodigoValidacion(codigo);
        usuarioRepo.save(usuario); // Guardar los cambios en la base de datos

        // 6. Enviar el código de activación por correo
        String cuerpoCorreo = "Tu código de activación es: " + nuevoCodigo;
        EmailDTO emailDTO = new EmailDTO("Código de Activación", cuerpoCorreo, usuarioNuevoCodDTO.email());
        emailServicio.enviarEmail(emailDTO); // Enviar el correo con el código
    }

    @Override
    public void activarCuenta(UsuarioActivacionDTO usuarioActivacionDTO) throws Exception {

        Optional<Usuario> usuarioOptional = usuarioRepo.findByEmail(usuarioActivacionDTO.email());

        if (usuarioOptional.isEmpty()) {
            throw new Exception("No se encontró un usuario con el email " + usuarioActivacionDTO.email());
        }

        Usuario usuario = usuarioOptional.get();

        CodigoValidacion codigoValidacion = usuario.getCodigoValidacion();

        if (codigoValidacion == null) {
            throw new Exception("No se ha generado un código de activación para este usuario.");
        }

        LocalDateTime tiempoCreacion = codigoValidacion.getFecha();
        LocalDateTime tiempoActual = LocalDateTime.now();

        if (tiempoCreacion.plusMinutes(15).isBefore(tiempoActual)) {
            throw new Exception("El código de activación ha expirado. Solicite uno nuevo.");
        }

        // Verificamos si el código ingresado es correcto
        if (!codigoValidacion.getCodigo().equals(usuarioActivacionDTO.codigo())) {
            throw new Exception("El código de activación es incorrecto.");
        }

        // Activamos la cuenta y eliminamos el código de validación
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setCodigoValidacion(null);
        usuarioRepo.save(usuario);
    }

    @Override
    public void editar(EditarUsuarioDTO editarUsuarioDTO) throws Exception {
        String id = obtenerIdSesion();
        if (!ObjectId.isValid(id)) {
            throw new Exception("No se encontró el usuario: "+id);
        }


        ObjectId objectId = new ObjectId(id);
        Optional<Usuario> usuarioOptional = usuarioRepo.findById(objectId);


        //Si no se encontró el usuario, lanzamos una excepción
        if(usuarioOptional.isEmpty()){
            throw new Exception("No se encontró el usuario: "+id);
        }


        Usuario usuario = usuarioOptional.get();
        usuarioMapper.toDocument(editarUsuarioDTO, usuario);


        usuarioRepo.save(usuario);
    }


    @Override
    public void cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws Exception {
    String id = obtenerIdSesion();

    //Validamos id
        if(!ObjectId.isValid(id)){
            throw new UsuarioNoEncotradoException("No se encontro el usuario: "+ id);
        }

        ObjectId objectId = new ObjectId(id);
        Optional<Usuario> usuarioOptional = usuarioRepo.findById(objectId);

        //Si no hay usuario, se lanza
        if(usuarioOptional.isEmpty()){
            throw new Exception("No se encontro el usuario: "+ id);
        }

        Usuario usuario = usuarioOptional.get();

        //Validacion contrasena
        if(!passwordEncoder.matches(cambiarPasswordDTO.actualPassword(), usuario.getPassword())){
            throw new Exception("No se encontro usuario: "+ id);
        }

        //Encriptacion nueva contrasena
        String nuevaPasswordEncript = passwordEncoder.encode(cambiarPasswordDTO.nuevoPassword());
        //Actualziar usuario
        usuario.setPassword(nuevaPasswordEncript);

        usuarioRepo.save(usuario);
    }

    @Override
    public void eliminar() throws Exception {

        String id = obtenerIdSesion();

        if (!ObjectId.isValid(id)) {
            throw new UsuarioNoEncotradoException("No se encontró el usuario: "+id);
        }


        ObjectId objectId = new ObjectId(id);
        Optional<Usuario> usuarioOptional = usuarioRepo.findById(objectId);


        if(usuarioOptional.isEmpty()){
            throw new Exception("No se encontró el usuario: "+ id);
        }


        Usuario usuario = usuarioOptional.get();

        //Si es encontrasdo, se elimina
        if(usuario.getEstado().equals(EstadoUsuario.INACTIVO)){
            throw new Exception("El usuario ya esta eliminado");
        }

        usuario.setEstado(EstadoUsuario.ELIMINADO);

        usuarioRepo.save(usuario);
    }

    @Override
    public UsuarioDTO obtener() throws Exception {

        String id = obtenerIdSesion();
        //Validamos el id
        if (!ObjectId.isValid(id)) {
            throw new Exception("No se encontró el usuario: "+id);
        }

        //Buscamos el usuario
        ObjectId objectId = new ObjectId(id);
        Optional<Usuario> usuarioOptional = usuarioRepo.findById(objectId);

        //Si no se encontró, lanzamos una excepción
        if(usuarioOptional.isEmpty()){
            throw new Exception("No se encontró el usuario con el id "+id);
        }

        //Retornamos el usuario encontrado convertido a DTO
        return usuarioMapper.toDTO(usuarioOptional.get());

    }
    public String obtenerIdSesion(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }
    public String obtenerRol() throws Exception{
        String id = obtenerIdSesion();
        Usuario usuario = usuarioRepo.findById(new ObjectId(id)).orElseThrow(() -> new UsuarioNoEncotradoException("No se encontro el usuario: "+id));
        return usuario.getRol().toString();
    }
}
