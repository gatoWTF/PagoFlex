package com.pagoflex.compromisos.repository;

import com.pagoflex.compromisos.model.Compromiso;
import com.pagoflex.compromisos.model.EstadoCompromiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompromisoRepository extends JpaRepository<Compromiso, Long> {

    List<Compromiso> findByRutClave(String rutClave);

    List<Compromiso> findByEmpresaCodigo(String empresaCodigo);

    Optional<Compromiso> findByFolio(String folio);

    Optional<Compromiso> findByCodigoCreditoAndCuotaActual(String codigoCredito, Integer cuotaActual);

    List<Compromiso> findByEstado(EstadoCompromiso estado);
}
