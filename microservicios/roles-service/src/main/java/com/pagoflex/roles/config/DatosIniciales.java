package com.pagoflex.roles.config;

import com.pagoflex.roles.model.Rol;
import com.pagoflex.roles.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {
        if (rolRepository.count() == 0) {
            rolRepository.saveAll(List.of(
                    Rol.builder().nombre("Usuario final").build(),
                    Rol.builder().nombre("Ejecutivo de empresa").build(),
                    Rol.builder().nombre("Agente PagoFlex").build()
            ));
        }
    }
}
