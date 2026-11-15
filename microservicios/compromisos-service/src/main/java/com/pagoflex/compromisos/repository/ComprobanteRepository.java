package com.pagoflex.compromisos.repository;

import com.pagoflex.compromisos.model.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {

    List<Comprobante> findByRutClaveOrderByNumeroDesc(String rutClave);
}
