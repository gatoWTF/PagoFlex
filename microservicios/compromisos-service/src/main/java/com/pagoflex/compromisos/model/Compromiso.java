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
@Table(name = "compromisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compromiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String folio;

    @Column(name = "rut_usuario", nullable = false, length = 12)
    private String rutUsuario;

    @Column(name = "rut_clave", nullable = false, length = 12)
    private String rutClave;

    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "empresa_codigo", nullable = false, length = 10)
    private String empresaCodigo;

    @Column(nullable = false, length = 100)
    private String concepto;

    @Column(name = "numero_cuota", length = 10)
    private String numeroCuota;

    @Column(nullable = false)
    private Integer monto;

    @Column(name = "fecha_vencimiento", nullable = false, length = 10)
    private String fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoCompromiso estado;

    @Column(name = "recargo_aplicado", nullable = false)
    private Integer recargoAplicado;

    @Column(name = "registrado_por", nullable = false, length = 20)
    private String registradoPor;
}
