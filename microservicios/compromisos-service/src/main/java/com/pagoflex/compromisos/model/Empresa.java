package com.pagoflex.compromisos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @Column(length = 10)
    private String codigo;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(name = "recargo_atraso", nullable = false)
    private Integer recargoAtraso;
}
