package com.pagoflex.usuarios.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
public class CompromisoClient {

    private final WebClient webClient;

    public CompromisoClient(@Value("${compromisos.service.url}") String compromisosUrl) {
        this.webClient = WebClient.builder().baseUrl(compromisosUrl).build();
    }

    // Si compromisos-service esta apagado, el cambio de nombre igual queda guardado en usuarios-service.
    public void actualizarNombreTitular(String rut, String nombre) {
        try {
            webClient.put()
                    .uri("/api/compromisos/titular/{rut}", rut)
                    .bodyValue(Map.of("nombre", nombre))
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            log.warn("No se pudo actualizar el nombre en compromisos-service: {}", e.getMessage());
        }
    }
}