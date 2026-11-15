package com.pagoflex.compromisos.repository;

import com.pagoflex.compromisos.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    Optional<Reporte> findByNumero(String numero);

    List<Reporte> findByRutClave(String rutClave);
}
