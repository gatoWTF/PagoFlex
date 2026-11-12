package com.pagoflex.usuarios.dto;

public record ActualizarUsuarioRequest(
        String nombre,
        String rut,
        String correo
) {
}
