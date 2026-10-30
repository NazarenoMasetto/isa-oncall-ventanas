package ar.edu.um.isa.oncall.service.oncall;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.domain.TurnoDeGuardia;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Casos borde de la regla C, sin base de datos. */
class ResolutorDeGuardiaTest {

    private static long ids = 1;

    private static TurnoDeGuardia turno(String desde, String hasta, boolean reemplazo) {
        return new TurnoDeGuardia().id(ids++).desde(Instant.parse(desde)).hasta(Instant.parse(hasta)).esReemplazo(reemplazo);
    }

    @Test
    void turnoUnicoQueCubreElInstante() {
        TurnoDeGuardia a = turno("2026-10-30T00:00:00Z", "2026-10-31T00:00:00Z", false);
        assertThat(ResolutorDeGuardia.resolver(List.of(a), Instant.parse("2026-10-30T12:00:00Z"))).containsExactly(a);
    }

    @Test
    void elFinDelTurnoNoCuentaComoCubierto() {
        TurnoDeGuardia a = turno("2026-10-30T00:00:00Z", "2026-10-30T09:00:00Z", false);
        assertThat(ResolutorDeGuardia.resolver(List.of(a), Instant.parse("2026-10-30T09:00:00Z"))).isEmpty();
    }

    @Test
    void huecoSinCoberturaDevuelveListaVaciaSinExcepcion() {
        TurnoDeGuardia a = turno("2026-10-30T00:00:00Z", "2026-10-30T08:00:00Z", false);
        TurnoDeGuardia b = turno("2026-10-30T10:00:00Z", "2026-10-30T18:00:00Z", false);
        assertThat(ResolutorDeGuardia.resolver(List.of(a, b), Instant.parse("2026-10-30T09:00:00Z"))).isEmpty();
    }

    @Test
    void enUnSolapamientoElPrimarioEsElQueEntroUltimo() {
        TurnoDeGuardia saliente = turno("2026-10-30T00:00:00Z", "2026-10-30T09:00:00Z", false);
        TurnoDeGuardia entrante = turno("2026-10-30T08:30:00Z", "2026-10-30T17:00:00Z", false);
        assertThat(ResolutorDeGuardia.resolver(List.of(saliente, entrante), Instant.parse("2026-10-30T08:45:00Z"))).containsExactly(
            entrante,
            saliente
        );
    }

    @Test
    void unReemplazoPisaAlTurnoRegular() {
        TurnoDeGuardia regular = turno("2026-10-30T00:00:00Z", "2026-10-31T00:00:00Z", false);
        TurnoDeGuardia reemplazo = turno("2026-10-30T12:00:00Z", "2026-10-30T14:00:00Z", true);
        assertThat(ResolutorDeGuardia.resolver(List.of(regular, reemplazo), Instant.parse("2026-10-30T13:00:00Z"))).containsExactly(
            reemplazo
        );
        // Fuera del horario del reemplazo, vuelve el regular.
        assertThat(ResolutorDeGuardia.resolver(List.of(regular, reemplazo), Instant.parse("2026-10-30T15:00:00Z"))).containsExactly(
            regular
        );
    }
}
