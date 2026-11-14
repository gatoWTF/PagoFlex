package com.pagoflex.compromisos.repository;

import com.pagoflex.compromisos.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {
}
