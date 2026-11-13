package com.pagoflex.compromisos.controller;

import com.pagoflex.compromisos.dto.CambiarEstadoRequest;
import com.pagoflex.compromisos.dto.ComprobanteResponse;
import com.pagoflex.compromisos.dto.CompromisoResponse;
import com.pagoflex.compromisos.dto.NuevoCompromisoRequest;
import com.pagoflex.compromisos.dto.PagoRequest;
import com.pagoflex.compromisos.service.CompromisoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/compromisos")
@RequiredArgsConstructor
public class CompromisoController {

    private final CompromisoService compromisoService;

    @GetMapping
    public List<CompromisoResponse> listar(@RequestParam(required = false) String rut,
                                           @RequestParam(required = false) String empresa) {
        if (rut != null && !rut.isBlank()) {
            return compromisoService.listarPorUsuario(rut);
        }
        if (empresa != null && !empresa.isBlank()) {
            return compromisoService.listarPorEmpresa(empresa);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica el rut del usuario o el codigo de la empresa");
    }

    @GetMapping("/{folio}")
    public CompromisoResponse obtener(@PathVariable String folio) {
        return compromisoService.obtener(folio);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompromisoResponse registrar(@RequestBody NuevoCompromisoRequest datos) {
        return compromisoService.registrar(datos);
    }

    @PutMapping("/{folio}/estado")
    public CompromisoResponse cambiarEstado(@PathVariable String folio, @RequestBody CambiarEstadoRequest datos) {
        return compromisoService.cambiarEstado(folio, datos);
    }

    @PostMapping("/{folio}/pagar")
    @ResponseStatus(HttpStatus.CREATED)
    public ComprobanteResponse pagar(@PathVariable String folio, @RequestBody(required = false) PagoRequest datos) {
        return compromisoService.pagar(folio, datos);
    }
}
