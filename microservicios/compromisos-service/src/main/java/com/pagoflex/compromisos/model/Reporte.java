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
@Table(name = "reportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reporte {

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

    @Column(nullable = false, length = 60)
    private String motivo;

    @Column(nullable = false, length = 500)
    private String comentario;

    @Column(nullable = false, length = 10)
    private String fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoReporte estado;

    @Column(name = "agente_responsable", length = 10)
    private String agenteResponsable;

    @Column(length = 300)
    private String respuesta;
}
