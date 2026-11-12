package com.pagoflex.usuarios.client;

import com.pagoflex.usuarios.dto.RolDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class RolClient {

    private final WebClient webClient;

    public RolClient(@Value("${roles.service.url}") String rolesUrl) {
        this.webClient = WebClient.builder().baseUrl(rolesUrl).build();
    }

    public RolDto obtener(Long id) {
        try {
            return webClient.get()
                    .uri("/api/roles/{id}", id)
                    .retrieve()
                    .bodyToMono(RolDto.class)
                    .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty())
                    .block();
        } catch (WebClientRequestException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "El servicio de roles no está disponible");
        }
    }
}
