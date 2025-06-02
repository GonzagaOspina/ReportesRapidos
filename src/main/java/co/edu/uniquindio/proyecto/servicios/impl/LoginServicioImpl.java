package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.TokenDTO;
import co.edu.uniquindio.proyecto.dto.login.LoginRequestDTO;
import co.edu.uniquindio.proyecto.dto.login.PasswordNuevoDTO;
import co.edu.uniquindio.proyecto.dto.login.PasswordOlvidadoDTO;
import co.edu.uniquindio.proyecto.dto.notificaciones.EmailDTO;
import co.edu.uniquindio.proyecto.dto.usuarios.UsuarioNuevoCodDTO;
import co.edu.uniquindio.proyecto.excepciones.DatosNoValidosException;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoUsuario;
import co.edu.uniquindio.proyecto.modelo.vo.CodigoValidacion;
import co.edu.uniquindio.proyecto.repositorios.UsuarioRepo;
import co.edu.uniquindio.proyecto.seguridad.JWTUtils;
import co.edu.uniquindio.proyecto.servicios.EmailServicio;
import co.edu.uniquindio.proyecto.servicios.LoginServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import com.mongodb.annotations.Sealed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServicioImpl implements LoginServicio {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JWTUtils jwtUtils;
    private final UsuarioServicioImpl usuarioServicioImp;
    private final UsuarioRepo usuarioRepo;
    private final EmailServicio emailServicio;

    @Override
    public TokenDTO login(LoginRequestDTO loginDTO) throws Exception {
        System.out.println("Buscando usuario: " + loginDTO.email());
        Optional<Usuario> optionalUsuario = usuarioRepo.findByEmail(loginDTO.email());

        if(optionalUsuario.isEmpty()){
            throw new Exception("El usuario no existe en la base de datos");
        }

        Usuario usuario = optionalUsuario.get();
        System.out.println("Usuario encontrado con estado: " + usuario.getEstado());

        if(usuario.getEstado()!= EstadoUsuario.ACTIVO){
            throw new Exception("El usuario no se encuentra activo");
        }

        System.out.println("Validando contraseña");
        if(!passwordEncoder.matches(loginDTO.password(), usuario.getPassword())){
            throw new Exception("Contrasena no correcta");
        }

        System.out.println("Generando token para usuario ID: " + usuario.getId());
        String token = jwtUtils.generateToken(usuario.getId().toString(), crearClaims(usuario));
        System.out.println("Token generado: " + token);
        return new TokenDTO(token);
    }


    private Map<String, String> crearClaims(Usuario usuario){
        return Map.of(
                "email", usuario.getEmail(),
                "nombre", usuario.getNombre(),
                "rol", "ROLE_"+usuario.getRol().name()
        );
    }


    @Override
    public void recuperarPassword(UsuarioNuevoCodDTO usuarioNuevoCodDTO) throws Exception {
        //Generar codigo

        String nuevoCodigo = usuarioServicioImp.generarCodigoAleatorio();

        Optional<Usuario> usuarioOptional = usuarioRepo.findByEmail(usuarioNuevoCodDTO.email());
        if (usuarioOptional.isEmpty()) {
            throw new Exception("No se encontró un usuario con el email " + usuarioNuevoCodDTO.email());
        }

        Usuario usuario = usuarioOptional.get();
        CodigoValidacion codigo = new CodigoValidacion(
                LocalDateTime.now(),
                nuevoCodigo
        );

        usuario.setCodigoValidacion(codigo);
        usuarioRepo.save(usuario); // Guardar los cambios en la base de datos

        String cuerpoCorreo = "Tu código de activación es: " + nuevoCodigo;
        EmailDTO emailDTO = new EmailDTO("Código de Activación", cuerpoCorreo, usuarioNuevoCodDTO.email());
        emailServicio.enviarEmail(emailDTO); // Enviar el correo con el código
    }

    @Override
    public void actualizarPassword(PasswordNuevoDTO passwordNuevoDTO) throws Exception {
        Optional<Usuario> usuarioOptional = usuarioRepo.findByEmail(passwordNuevoDTO.email());
        if (usuarioOptional.isEmpty()) {
            throw new Exception("No se encontró un usuario con el email " + passwordNuevoDTO.email());
        }

        Usuario usuario = usuarioOptional.get();

        if(!usuario.getCodigoValidacion().getCodigo().equals(passwordNuevoDTO.codigo())){
            throw new DatosNoValidosException("El codigo no coincide");
        }

        usuario.setPassword(passwordEncoder.encode(passwordNuevoDTO.nuevoPassword()));
        usuario.setCodigoValidacion(null);
        usuarioRepo.save(usuario);
    }
}
