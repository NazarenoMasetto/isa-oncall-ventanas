package ar.edu.um.isa.oncall.service.oncall;

import ar.edu.um.isa.oncall.domain.TurnoDeGuardia;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * Logica pura de la regla C, sin base de datos: dado un conjunto de turnos y un
 * instante, decide quien esta de guardia. Esta separada del servicio para poder
 * probarla con tests unitarios simples.
 *
 * Reglas (ver docs/decisiones.md):
 *  1. Un turno cubre t si desde <= t < hasta (intervalo semiabierto).
 *  2. Si algun turno que cubre t es reemplazo, los turnos regulares quedan pisados.
 *  3. Entre los que quedan, el primario es el que empezo mas tarde (el ultimo en
 *     tomar la guardia); el resto son secundarios.
 *  4. Si ningun turno cubre t, la lista vuelve vacia (hueco de cobertura).
 */
public final class ResolutorDeGuardia {

    public static final String PRIMARIO = "PRIMARIO";
    public static final String SECUNDARIO = "SECUNDARIO";

    private ResolutorDeGuardia() {}

    public static boolean cubre(TurnoDeGuardia turno, Instant t) {
        return turno.getDesde() != null && turno.getHasta() != null && !turno.getDesde().isAfter(t) && turno.getHasta().isAfter(t);
    }

    /** Devuelve los turnos que mandan en t, ordenados: el primero es el primario. */
    public static List<TurnoDeGuardia> resolver(List<TurnoDeGuardia> turnos, Instant t) {
        List<TurnoDeGuardia> activos = turnos
            .stream()
            .filter(turno -> cubre(turno, t))
            .toList();
        boolean hayReemplazo = activos.stream().anyMatch(ResolutorDeGuardia::esReemplazo);
        return activos
            .stream()
            .filter(turno -> !hayReemplazo || esReemplazo(turno))
            .sorted(
                Comparator.comparing(TurnoDeGuardia::getDesde)
                    .reversed()
                    .thenComparing(TurnoDeGuardia::getId, Comparator.nullsLast(Comparator.naturalOrder()))
            )
            .toList();
    }

    public static boolean esReemplazo(TurnoDeGuardia turno) {
        return Boolean.TRUE.equals(turno.getEsReemplazo());
    }
}
