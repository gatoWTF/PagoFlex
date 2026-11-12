package com.pagoflex.usuarios.dto;

public record RegistroRequest(
        String nombre,
        String rut,
        String correo,
        String contrasena
) {
}
