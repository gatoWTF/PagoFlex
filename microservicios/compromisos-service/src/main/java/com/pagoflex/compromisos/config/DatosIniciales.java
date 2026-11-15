package com.pagoflex.compromisos.config;

import com.pagoflex.compromisos.model.CanalPago;
import com.pagoflex.compromisos.model.Comprobante;
import com.pagoflex.compromisos.model.Compromiso;
import com.pagoflex.compromisos.model.Empresa;
import com.pagoflex.compromisos.model.EstadoCompromiso;
import com.pagoflex.compromisos.model.MotivoAnulacion;
import com.pagoflex.compromisos.model.Rut;
import com.pagoflex.compromisos.repository.ComprobanteRepository;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {

    private static final String CAMILA = "15.834.207-3";
    private static final String LUIS = "12.476.318-5";
    private static final String ROSA = "6.893.541-5";
    private static final String MATIAS = "21.045.873-5";
    private static final String FERNANDA = "17.562.904-1";
    private static final String HECTOR = "10.318.265-4";
    private static final String JAVIERA = "19.287.430-0";

    private final CompromisoRepository compromisoRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final EmpresaRepository empresaRepository;

    @Override
    public void run(String... args) {
        if (empresaRepository.count() == 0) {
            empresaRepository.saveAll(List.of(
                    new Empresa("EC-01", "Altos del Maipo", 8500),
                    new Empresa("EC-02", "Colegio Bosque Nativo", 5000),
                    new Empresa("EC-03", "Crédito Andino", 12000),
                    new Empresa("EC-04", "Horizonte Sur", 3000),
                    new Empresa("EC-05", "Manos Abiertas", 0),
                    new Empresa("EC-06", "Club Los Halcones", 2000),
                    new Empresa("EC-07", "Instituto Cordillera", 6000),
                    new Empresa("EC-08", "Plaza Oriente", 7500)
            ));
        }
        if (compromisoRepository.count() == 0) {
            compromisoRepository.saveAll(List.of(
                    compromiso("CP-2026-0101", "CR-2026-0001", CAMILA, "Camila Andrea Rojas Peña", "EC-08", "Arriendo departamento", 10, 12, 520000, "10-10-2026", EstadoCompromiso.PENDIENTE, 0, "EJ-07"),
                    compromiso("CP-2026-0102", "CR-2026-0002", CAMILA, "Camila Andrea Rojas Peña", "EC-06", "Cuota social", 10, 12, 15000, "05-10-2026", EstadoCompromiso.VENCIDO, 2000, "EJ-05"),
                    compromiso("CP-2026-0103", "CR-2026-0003", CAMILA, "Camila Andrea Rojas Peña", "EC-05", "Aporte mensual", 10, 12, 10000, "15-10-2026", EstadoCompromiso.PENDIENTE, 0, "PagoFlex"),
                    compromiso("CP-2026-0104", "CR-2026-0004", CAMILA, "Camila Andrea Rojas Peña", "EC-03", "Crédito de consumo", 7, 24, 86400, "25-09-2026", EstadoCompromiso.PAGADO, 0, "EJ-03"),
                    compromiso("CP-2026-0105", "CR-2026-0004", CAMILA, "Camila Andrea Rojas Peña", "EC-03", "Crédito de consumo", 8, 24, 86400, "25-10-2026", EstadoCompromiso.PENDIENTE, 0, "EJ-03"),
                    compromiso("CP-2026-0106", "CR-2026-0005", LUIS, "Luis Alberto Muñoz Tapia", "EC-01", "Cuota pie departamento", 14, 36, 310000, "30-09-2026", EstadoCompromiso.VENCIDO, 8500, "EJ-01"),
                    compromiso("CP-2026-0108", "CR-2026-0006", ROSA, "Rosa Elena Contreras Díaz", "EC-04", "Seguro de hogar", 10, 12, 18900, "01-10-2026", EstadoCompromiso.PAGADO, 0, "PagoFlex"),
                    compromiso("CP-2026-0113", "CR-2026-0006", ROSA, "Rosa Elena Contreras Díaz", "EC-04", "Seguro de hogar", 11, 12, 18900, "01-11-2026", EstadoCompromiso.PENDIENTE, 0, "PagoFlex"),
                    compromiso("CP-2026-0109", "CR-2026-0007", MATIAS, "Matías Ignacio Soto Vergara", "EC-07", "Arancel", 8, 10, 142000, "05-10-2026", EstadoCompromiso.EN_REVISION, 0, "EJ-06"),
                    compromiso("CP-2026-0110", "CR-2026-0008", FERNANDA, "Fernanda Paz Godoy Lagos", "EC-02", "Mensualidad", 8, 10, 185000, "10-10-2026", EstadoCompromiso.PENDIENTE, 0, "EJ-02"),
                    anulado(compromiso("CP-2026-0111", "CR-2026-0009", HECTOR, "Héctor Manuel Pizarro Campos", "EC-03", "Crédito de consumo", 19, 36, 54200, "20-09-2026", EstadoCompromiso.ANULADO, 0, "EJ-04"),
                            MotivoAnulacion.REPACTACION, "EJ-04", "22-09-2026 10:00"),
                    compromiso("CP-2026-0112", "CR-2026-0010", JAVIERA, "Javiera Constanza Reyes Molina", "EC-06", "Cuota social", 10, 12, 15000, "05-10-2026", EstadoCompromiso.PAGADO, 0, "EJ-05"),
                    compromiso("CP-2026-0114", "CR-2026-0010", JAVIERA, "Javiera Constanza Reyes Molina", "EC-06", "Cuota social", 11, 12, 15000, "05-11-2026", EstadoCompromiso.PENDIENTE, 0, "EJ-05")
            ));
        }
        if (comprobanteRepository.count() == 0) {
            comprobanteRepository.saveAll(List.of(
                    comprobante("CPR-000377", "CP-2026-0072", LUIS, "EC-01", "02-09-2026 21:14", 318500, CanalPago.AGENTE_CONVERSACIONAL, false, "Pago simulado"),
                    comprobante("CPR-000398", "CP-2026-0089", CAMILA, "EC-08", "09-09-2026 13:05", 520000, CanalPago.PORTAL, true, "Pago simulado"),
                    comprobante("CPR-000402", "CP-2026-0090", CAMILA, "EC-06", "07-09-2026 18:40", 17000, CanalPago.PORTAL, false, "Pago simulado"),
                    comprobante("CPR-000405", "CP-2026-0091", CAMILA, "EC-05", "15-09-2026 08:22", 10000, CanalPago.AGENTE_CONVERSACIONAL, true, "Pago simulado"),
                    comprobante("CPR-000447", "CP-2026-0098", FERNANDA, "EC-02", "10-09-2026 22:31", 185000, CanalPago.PORTAL, true, "Pago simulado"),
                    comprobante("CPR-000451", "CP-2026-0104", CAMILA, "EC-03", "24-09-2026 10:42", 86400, CanalPago.PORTAL, true, "Tarjeta de débito"),
                    comprobante("CPR-000452", "CP-2026-0108", ROSA, "EC-04", "30-09-2026 19:05", 18900, CanalPago.AGENTE_CONVERSACIONAL, true, "Pago simulado"),
                    comprobante("CPR-000453", "CP-2026-0112", JAVIERA, "EC-06", "04-10-2026 08:15", 15000, CanalPago.APP, true, "Pago simulado")
            ));
        }
    }

    private Compromiso compromiso(String folio, String credito, String rut, String nombre, String empresa,
                                  String concepto, int cuota, int totalCuotas, int monto, String vencimiento,
                                  EstadoCompromiso estado, int recargo, String registradoPor) {
        return Compromiso.builder()
                .folio(folio)
                .codigoCredito(credito)
                .rutUsuario(rut)
                .rutClave(Rut.clave(rut))
                .nombreUsuario(nombre)
                .empresaCodigo(empresa)
                .concepto(concepto)
                .cuotaActual(cuota)
                .totalCuotas(totalCuotas)
                .monto(monto)
                .fechaVencimiento(vencimiento)
                .estado(estado)
                .recargoAplicado(recargo)
                .registradoPor(registradoPor)
                .build();
    }

    private Compromiso anulado(Compromiso compromiso, MotivoAnulacion motivo, String ejecutivo, String fecha) {
        compromiso.setMotivoAnulacion(motivo);
        compromiso.setAnuladoPor(ejecutivo);
        compromiso.setFechaAnulacion(fecha);
        return compromiso;
    }

    private Comprobante comprobante(String numero, String folio, String rut, String empresa, String fechaHora,
                                    int monto, CanalPago canal, boolean aTiempo, String medio) {
        return Comprobante.builder()
                .numero(numero)
                .compromisoFolio(folio)
                .rutUsuario(rut)
                .rutClave(Rut.clave(rut))
                .empresaCodigo(empresa)
                .fechaHora(fechaHora)
                .montoPagado(monto)
                .canal(canal)
                .aTiempo(aTiempo)
                .medioPago(medio)
                .build();
    }
}
