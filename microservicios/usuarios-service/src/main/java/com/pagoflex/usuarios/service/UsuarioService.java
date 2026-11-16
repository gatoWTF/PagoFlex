package com.pagoflex.usuarios.service;

import com.pagoflex.usuarios.client.CompromisoClient;
import com.pagoflex.usuarios.client.RolClient;
import com.pagoflex.usuarios.dto.ActualizarUsuarioRequest;
import com.pagoflex.usuarios.dto.CambiarContrasenaRequest;
import com.pagoflex.usuarios.dto.ContactoRequest;
import com.pagoflex.usuarios.dto.LoginRequest;
import com.pagoflex.usuarios.dto.RegistroRequest;
import com.pagoflex.usuarios.dto.RolDto;
import com.pagoflex.usuarios.dto.UsuarioResponse;
import com.pagoflex.usuarios.model.RolId;
import com.pagoflex.usuarios.model.Usuario;
import com.pagoflex.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final int LARGO_MINIMO_CONTRASENA = 6;
    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$");

    private final UsuarioRepository usuarioRepository;
    private final RolClient rolClient;
    private final CompromisoClient compromisoClient;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponse obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    public UsuarioResponse registrar(RegistroRequest datos) {
        if (vacio(datos.nombre()) || vacio(datos.rut()) || vacio(datos.correo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre, RUT y correo son obligatorios");
        }
        validarContrasena(datos.contrasena());

        String correo = limpiarCorreo(datos.correo());
        String rut = datos.rut().trim().toUpperCase();
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una cuenta con este correo");
        }
        if (usuarioRepository.existsByRut(rut)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una cuenta con este RUT");
        }
        validarRol(RolId.USUARIO_FINAL);

        Usuario nuevo = Usuario.builder()
                .nombre(datos.nombre().trim())
                .rut(rut)
                .correo(correo)
                .contrasena(passwordEncoder.encode(datos.contrasena()))
                .rolId(RolId.USUARIO_FINAL)
                .build();
        return aRespuesta(usuarioRepository.save(nuevo));
    }

    public UsuarioResponse iniciarSesion(LoginRequest datos) {
        if (vacio(datos.correo()) || vacio(datos.contrasena())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingresa tu correo y tu contraseña");
        }
        Usuario usuario = usuarioRepository.findByCorreo(limpiarCorreo(datos.correo()))
                .filter(u -> passwordEncoder.matches(datos.contrasena(), u.getContrasena()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo y/o contraseña incorrectos"));
        return aRespuesta(usuario);
    }

    public UsuarioResponse actualizar(Long id, ActualizarUsuarioRequest datos) {
        Usuario usuario = buscar(id);

        if (vacio(datos.nombre()) || vacio(datos.rut()) || vacio(datos.correo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre, RUT y correo son obligatorios");
        }
        String correo = limpiarCorreo(datos.correo());
        String rut = datos.rut().trim().toUpperCase();
        if (usuarioRepository.existsByCorreoAndIdNot(correo, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una cuenta con este correo");
        }
        if (usuarioRepository.existsByRutAndIdNot(rut, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una cuenta con este RUT");
        }

        usuario.setNombre(datos.nombre().trim());
        usuario.setRut(rut);
        usuario.setCorreo(correo);
        Usuario guardado = usuarioRepository.save(usuario);
        compromisoClient.actualizarNombreTitular(guardado.getRut(), guardado.getNombre());
        return aRespuesta(guardado);
    }

    public UsuarioResponse guardarContacto(Long id, ContactoRequest datos) {
        Usuario usuario = buscar(id);
        String correo2 = datos == null || vacio(datos.correoSecundario()) ? null : limpiarCorreo(datos.correoSecundario());
        String telefono = datos == null || vacio(datos.telefono()) ? null : datos.telefono().trim();

        if (correo2 != null && !PATRON_CORREO.matcher(correo2).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correo secundario no válido");
        }
        if (telefono != null && !telefonoValido(telefono)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teléfono no válido");
        }

        usuario.setCorreoSecundario(correo2);
        usuario.setTelefono(telefono);
        return aRespuesta(usuarioRepository.save(usuario));
    }

    public void cambiarContrasena(Long id, CambiarContrasenaRequest datos) {
        Usuario usuario = buscar(id);

        if (vacio(datos.actual()) || !passwordEncoder.matches(datos.actual(), usuario.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La contraseña actual es incorrecta");
        }
        validarContrasena(datos.nueva());
        if (passwordEncoder.matches(datos.nueva(), usuario.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña debe ser distinta a la actual");
        }

        usuario.setContrasena(passwordEncoder.encode(datos.nueva()));
        usuarioRepository.save(usuario);
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private void validarContrasena(String contrasena) {
        boolean valida = contrasena != null
                && contrasena.length() >= LARGO_MINIMO_CONTRASENA
                && contrasena.chars().anyMatch(Character::isUpperCase)
                && contrasena.chars().anyMatch(Character::isLowerCase)
                && contrasena.chars().anyMatch(Character::isDigit);
        if (!valida) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos " + LARGO_MINIMO_CONTRASENA
                            + " caracteres, una mayúscula, una minúscula y un número");
        }
    }

    private void validarRol(Long rolId) {
        if (rolClient.obtener(rolId) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol indicado no existe");
        }
    }

    private UsuarioResponse aRespuesta(Usuario usuario) {
        RolDto rol = rolClient.obtener(usuario.getRolId());
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getRut(),
                usuario.getCorreo(),
                usuario.getRolId(),
                rol != null ? rol.nombre() : "",
                usuario.getCorreoSecundario(),
                usuario.getTelefono()
        );
    }

    private boolean telefonoValido(String telefono) {
        long digitos = telefono.chars().filter(Character::isDigit).count();
        return telefono.matches("[0-9+\\s-]+") && digitos >= 8 && digitos <= 15;
    }

    private String limpiarCorreo(String correo) {
        return correo.trim().toLowerCase();
    }

    private boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
