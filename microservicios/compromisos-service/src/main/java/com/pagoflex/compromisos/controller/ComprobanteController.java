package com.pagoflex.compromisos.controller;

import com.pagoflex.compromisos.dto.ComprobanteResponse;
import com.pagoflex.compromisos.service.CompromisoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comprobantes")
@RequiredArgsConstructor
public class ComprobanteController {

    private final CompromisoService compromisoService;

    @GetMapping
    public List<ComprobanteResponse> listar(@RequestParam String rut) {
        return compromisoService.listarComprobantes(rut);
    }
}
