package com.pagoflex.compromisos.controller;

import com.pagoflex.compromisos.dto.GestionReporteRequest;
import com.pagoflex.compromisos.dto.NuevoReporteRequest;
import com.pagoflex.compromisos.dto.ReporteResponse;
import com.pagoflex.compromisos.service.ReporteService;
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

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public List<ReporteResponse> listar(@RequestParam(required = false) String rut) {
        return reporteService.listar(rut);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReporteResponse crear(@RequestBody NuevoReporteRequest datos) {
        return reporteService.crear(datos);
    }

    @PutMapping("/{numero}/tomar")
    public ReporteResponse tomar(@PathVariable String numero, @RequestBody GestionReporteRequest datos) {
        return reporteService.tomar(numero, datos);
    }

    @PutMapping("/{numero}/resolver")
    public ReporteResponse resolver(@PathVariable String numero, @RequestBody GestionReporteRequest datos) {
        return reporteService.resolver(numero, datos);
    }
}
