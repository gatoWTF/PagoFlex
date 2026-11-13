package com.pagoflex.compromisos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comprobantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @Column(name = "compromiso_folio", nullable = false, length = 20)
    private String compromisoFolio;

    @Column(name = "rut_usuario", nullable = false, length = 12)
    private String rutUsuario;

    @Column(name = "rut_clave", nullable = false, length = 12)
    private String rutClave;

    @Column(name = "empresa_codigo", nullable = false, length = 10)
    private String empresaCodigo;

    @Column(name = "fecha_hora", nullable = false, length = 16)
    private String fechaHora;

    @Column(name = "monto_pagado", nullable = false)
    private Integer montoPagado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private CanalPago canal;

    @Column(name = "a_tiempo", nullable = false)
    private Boolean aTiempo;

    @Column(name = "medio_pago", nullable = false, length = 50)
    private String medioPago;
}
