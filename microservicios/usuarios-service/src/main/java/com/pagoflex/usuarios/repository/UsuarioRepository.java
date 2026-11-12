package com.pagoflex.usuarios.repository;

import com.pagoflex.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    boolean existsByRut(String rut);

    boolean existsByCorreoAndIdNot(String correo, Long id);

    boolean existsByRutAndIdNot(String rut, Long id);
}
