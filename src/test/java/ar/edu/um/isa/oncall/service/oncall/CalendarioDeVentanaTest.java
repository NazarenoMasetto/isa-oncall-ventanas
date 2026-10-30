package ar.edu.um.isa.oncall.service.oncall;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionVentana;
import ar.edu.um.isa.oncall.domain.enumeration.TipoRecurrencia;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Ventanas recurrentes, excepciones y horario de verano. */
class CalendarioDeVentanaTest {

    private static final String MADRID = "Europe/Madrid";

    private static Instant local(String fechaHora, String zona) {
        return ZonedDateTime.of(java.time.LocalDateTime.parse(fechaHora), ZoneId.of(zona)).toInstant();
    }

    /** Todos los martes de 02:00 a 04:00 hora de Madrid, desde el 6 de octubre de 2026. */
    private static VentanaDeMantenimiento martesDeMadrugada() {
        return new VentanaDeMantenimiento()
            .id(1L)
            .nombre("martes")
            .inicio(local("2026-10-06T02:00", MADRID))
            .fin(local("2026-10-06T04:00", MADRID))
            .zonaHoraria(MADRID)
            .recurrencia(TipoRecurrencia.SEMANAL)
            .diasSemana("TUE")
            .activa(true);
    }

    @Test
    void activaDentroDeUnaOcurrenciaSemanal() {
        assertThat(CalendarioDeVentana.ocurrenciaActiva(martesDeMadrugada(), List.of(), local("2026-10-13T03:00", MADRID))).isPresent();
    }

    @Test
    void inactivaOtroDiaDeLaSemana() {
        assertThat(CalendarioDeVentana.ocurrenciaActiva(martesDeMadrugada(), List.of(), local("2026-10-14T03:00", MADRID))).isEmpty();
    }

    @Test
    void conservaLaHoraLocalDespuesDelCambioDeHorario() {
        // El 25/10/2026 Madrid pasa de UTC+2 a UTC+1. El martes 27/10 la ventana
        // tiene que seguir siendo 02:00-04:00 hora local (01:00-03:00 UTC).
        VentanaDeMantenimiento v = martesDeMadrugada();
        assertThat(CalendarioDeVentana.ocurrenciaActiva(v, List.of(), Instant.parse("2026-10-27T01:30:00Z"))).isPresent();
        assertThat(CalendarioDeVentana.ocurrenciaActiva(v, List.of(), Instant.parse("2026-10-27T03:30:00Z"))).isEmpty();
    }

    @Test
    void unaCancelacionAnulaSoloEsaOcurrencia() {
        ExcepcionDeVentana cancelada = new ExcepcionDeVentana()
            .fechaOcurrencia(LocalDate.of(2026, 10, 13))
            .tipo(TipoExcepcionVentana.CANCELACION);
        VentanaDeMantenimiento v = martesDeMadrugada();
        assertThat(CalendarioDeVentana.ocurrenciaActiva(v, List.of(cancelada), local("2026-10-13T03:00", MADRID))).isEmpty();
        assertThat(CalendarioDeVentana.ocurrenciaActiva(v, List.of(cancelada), local("2026-10-20T03:00", MADRID))).isPresent();
    }

    @Test
    void unaExtensionAlargaLaOcurrencia() {
        ExcepcionDeVentana extension = new ExcepcionDeVentana()
            .fechaOcurrencia(LocalDate.of(2026, 10, 13))
            .tipo(TipoExcepcionVentana.EXTENSION)
            .nuevoFin(local("2026-10-13T06:00", MADRID));
        assertThat(
            CalendarioDeVentana.ocurrenciaActiva(martesDeMadrugada(), List.of(extension), local("2026-10-13T05:00", MADRID))
        ).isPresent();
    }

    @Test
    void unaReprogramacionMueveLaOcurrencia() {
        ExcepcionDeVentana movida = new ExcepcionDeVentana()
            .fechaOcurrencia(LocalDate.of(2026, 10, 13))
            .tipo(TipoExcepcionVentana.REPROGRAMACION)
            .nuevoInicio(local("2026-10-15T22:00", MADRID))
            .nuevoFin(local("2026-10-15T23:00", MADRID));
        VentanaDeMantenimiento v = martesDeMadrugada();
        assertThat(CalendarioDeVentana.ocurrenciaActiva(v, List.of(movida), local("2026-10-13T03:00", MADRID))).isEmpty();
        assertThat(CalendarioDeVentana.ocurrenciaActiva(v, List.of(movida), local("2026-10-15T22:30", MADRID))).isPresent();
    }

    @Test
    void noSeRepiteDespuesDeRecurrenciaHasta() {
        VentanaDeMantenimiento v = martesDeMadrugada().recurrenciaHasta(LocalDate.of(2026, 10, 15));
        assertThat(CalendarioDeVentana.ocurrenciaActiva(v, List.of(), local("2026-10-13T03:00", MADRID))).isPresent();
        assertThat(CalendarioDeVentana.ocurrenciaActiva(v, List.of(), local("2026-10-20T03:00", MADRID))).isEmpty();
    }

    @Test
    void unaVentanaQueCruzaLaMedianoche() {
        VentanaDeMantenimiento v = new VentanaDeMantenimiento()
            .id(2L)
            .inicio(local("2026-10-01T23:00", "America/Argentina/Mendoza"))
            .fin(local("2026-10-02T01:00", "America/Argentina/Mendoza"))
            .zonaHoraria("America/Argentina/Mendoza")
            .recurrencia(TipoRecurrencia.DIARIA)
            .activa(true);
        assertThat(CalendarioDeVentana.ocurrenciaActiva(v, List.of(), local("2026-10-10T00:30", "America/Argentina/Mendoza"))).isPresent();
    }

    @Test
    void unaVentanaDesactivadaNuncaEstaActiva() {
        VentanaDeMantenimiento v = martesDeMadrugada().activa(false);
        assertThat(CalendarioDeVentana.ocurrenciaActiva(v, List.of(), local("2026-10-13T03:00", MADRID))).isEmpty();
    }
}
