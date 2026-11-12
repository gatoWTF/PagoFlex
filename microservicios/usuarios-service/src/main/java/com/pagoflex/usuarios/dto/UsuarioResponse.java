package com.pagoflex.usuarios.dto;

public record UsuarioResponse(
        Long id,
        String nombre,
        String rut,
        String correo,
        Long rolId,
        String rolNombre
) {
}
