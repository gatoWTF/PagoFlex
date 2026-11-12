package com.pagoflex.usuarios.config;

import com.pagoflex.usuarios.model.RolId;
import com.pagoflex.usuarios.model.Usuario;
import com.pagoflex.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {

    private static final String CONTRASENA_INICIAL = "Pagoflex1";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }
        usuarioRepository.saveAll(List.of(
                cuenta("Camila Andrea Rojas Peña", "15.834.207-3", "crojas@correo.ejemplo", RolId.USUARIO_FINAL),
                cuenta("Luis Alberto Muñoz Tapia", "12.476.318-5", "lmunoz@correo.ejemplo", RolId.USUARIO_FINAL),
                cuenta("Rosa Elena Contreras Díaz", "6.893.541-5", "rcontreras@correo.ejemplo", RolId.USUARIO_FINAL),
                cuenta("Matías Ignacio Soto Vergara", "21.045.873-5", "msoto@correo.ejemplo", RolId.USUARIO_FINAL),
                cuenta("Fernanda Paz Godoy Lagos", "17.562.904-1", "fgodoy@correo.ejemplo", RolId.USUARIO_FINAL),
                cuenta("Héctor Manuel Pizarro Campos", "10.318.265-4", "hpizarro@correo.ejemplo", RolId.USUARIO_FINAL),
                cuenta("Javiera Constanza Reyes Molina", "19.287.430-0", "jreyes@correo.ejemplo", RolId.USUARIO_FINAL),
                cuenta("Benjamín Esteban Araya Fuentes", "18.704.396-4", "baraya@correo.ejemplo", RolId.USUARIO_FINAL),
                cuenta("Paula Andrea Saavedra Núñez", "14.629.853-2", "psaavedra@altosmaipo.ejemplo", RolId.EJECUTIVO),
                cuenta("Marco Antonio Fuentes Riquelme", "11.853.720-3", "mfuentes@bosquenativo.ejemplo", RolId.EJECUTIVO),
                cuenta("Claudia Beatriz Henríquez Soto", "15.370.946-7", "chenriquez@creditoandino.ejemplo", RolId.EJECUTIVO),
                cuenta("Felipe Ignacio Morales Tapia", "17.095.482-3", "fmorales@creditoandino.ejemplo", RolId.EJECUTIVO),
                cuenta("Gloria Inés Carrasco Vega", "9.846.217-1", "gcarrasco@loshalcones.ejemplo", RolId.EJECUTIVO),
                cuenta("Sebastián Andrés Núñez Olivares", "18.930.571-0", "snunez@itcordillera.ejemplo", RolId.EJECUTIVO),
                cuenta("Mónica Patricia Leiva Castro", "12.584.396-4", "mleiva@plazaoriente.ejemplo", RolId.EJECUTIVO),
                cuenta("Andrés Felipe Lagos Meza", "16.248.317-K", "alagos@pagoflex.ejemplo", RolId.AGENTE),
                cuenta("Daniela Sofía Ortiz Bravo", "18.352.091-1", "dortiz@pagoflex.ejemplo", RolId.AGENTE),
                cuenta("Ricardo José Salinas Cáceres", "13.907.264-2", "rsalinas@pagoflex.ejemplo", RolId.AGENTE),
                cuenta("Valentina Ignacia Paredes Silva", "17.781.435-0", "vparedes@pagoflex.ejemplo", RolId.AGENTE),
                cuenta("Tomás Alejandro Vidal Rojas", "19.564.028-9", "tvidal@pagoflex.ejemplo", RolId.AGENTE)
        ));
    }

    private Usuario cuenta(String nombre, String rut, String correo, Long rolId) {
        return Usuario.builder()
                .nombre(nombre)
                .rut(rut)
                .correo(correo)
                .contrasena(passwordEncoder.encode(CONTRASENA_INICIAL))
                .rolId(rolId)
                .build();
    }
}
