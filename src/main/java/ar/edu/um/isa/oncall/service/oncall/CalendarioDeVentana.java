package ar.edu.um.isa.oncall.service.oncall;

import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionVentana;
import ar.edu.um.isa.oncall.domain.enumeration.TipoRecurrencia;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * Logica pura para saber si una ventana de mantenimiento (posiblemente recurrente)
 * esta activa en un instante. Es el mismo problema temporal que una rotacion:
 * un patron en hora local que se materializa en ocurrencias concretas.
 *
 * Reglas (ver docs/decisiones.md):
 *  - inicio/fin definen la primera ocurrencia; su duracion se repite.
 *  - La hora de inicio se mantiene en hora LOCAL de zonaHoraria: "martes 02:00"
 *    sigue siendo 02:00 aunque cambie el horario de verano.
 *  - Cada ocurrencia se identifica por su fecha local de inicio, que es la que
 *    usan las excepciones (fechaOcurrencia).
 *  - Intervalo semiabierto [inicio, fin).
 */
public final class CalendarioDeVentana {

    private CalendarioDeVentana() {}

    /** Una ocurrencia concreta de la ventana. */
    public record Ocurrencia(LocalDate fecha, Instant desde, Instant hasta, String origen) {
        boolean cubre(Instant t) {
            return !desde.isAfter(t) && hasta.isAfter(t);
        }
    }

    public static Optional<Ocurrencia> ocurrenciaActiva(VentanaDeMantenimiento v, List<ExcepcionDeVentana> excepciones, Instant t) {
        if (!Boolean.TRUE.equals(v.getActiva()) || v.getInicio() == null || v.getFin() == null || !v.getFin().isAfter(v.getInicio())) {
            return Optional.empty();
        }
        ZoneId zona = zona(v.getZonaHoraria());
        ZonedDateTime primerInicio = v.getInicio().atZone(zona);
        LocalDate primeraFecha = primerInicio.toLocalDate();
        LocalTime horaInicio = primerInicio.toLocalTime();
        Duration duracion = Duration.between(v.getInicio(), v.getFin());

        // 1) Excepciones que mueven una ocurrencia a otro horario (pueden caer en cualquier dia).
        for (ExcepcionDeVentana e : excepciones) {
            if (e.getTipo() == TipoExcepcionVentana.REPROGRAMACION && e.getNuevoInicio() != null && e.getNuevoFin() != null) {
                Ocurrencia o = new Ocurrencia(e.getFechaOcurrencia(), e.getNuevoInicio(), e.getNuevoFin(), "REPROGRAMACION");
                if (o.cubre(t)) {
                    return Optional.of(o);
                }
            }
        }

        // 2) Ocurrencias regulares: alcanza con mirar las que empezaron en los ultimos dias
        //    que abarca la duracion, mas un margen por las extensiones.
        LocalDate fechaT = t.atZone(zona).toLocalDate();
        long diasAtras = duracion.toDays() + 2;
        for (long i = 0; i <= diasAtras; i++) {
            LocalDate fecha = fechaT.minusDays(i);
            if (!correspondeOcurrencia(v, primeraFecha, fecha)) {
                continue;
            }
            Optional<ExcepcionDeVentana> excepcion = excepciones
                .stream()
                .filter(e -> fecha.equals(e.getFechaOcurrencia()))
                .findFirst();
            if (excepcion.isPresent() && excepcion.get().getTipo() != TipoExcepcionVentana.EXTENSION) {
                continue; // cancelada o reprogramada (la reprogramada ya se evaluo arriba)
            }
            LocalDateTime inicioLocal = LocalDateTime.of(fecha, horaInicio);
            Instant desde = inicioLocal.atZone(zona).toInstant();
            Instant hasta = inicioLocal.plus(duracion).atZone(zona).toInstant();
            String origen = "REGULAR";
            if (excepcion.isPresent() && excepcion.get().getNuevoFin() != null && excepcion.get().getNuevoFin().isAfter(hasta)) {
                hasta = excepcion.get().getNuevoFin();
                origen = "EXTENSION";
            }
            Ocurrencia o = new Ocurrencia(fecha, desde, hasta, origen);
            if (o.cubre(t)) {
                return Optional.of(o);
            }
        }
        return Optional.empty();
    }

    /** Indica si el patron de la ventana genera una ocurrencia que empieza en esa fecha local. */
    static boolean correspondeOcurrencia(VentanaDeMantenimiento v, LocalDate primeraFecha, LocalDate fecha) {
        if (fecha.isBefore(primeraFecha)) {
            return false;
        }
        if (v.getRecurrenciaHasta() != null && fecha.isAfter(v.getRecurrenciaHasta())) {
            return false;
        }
        TipoRecurrencia r = v.getRecurrencia() == null ? TipoRecurrencia.NINGUNA : v.getRecurrencia();
        return switch (r) {
            case NINGUNA -> fecha.equals(primeraFecha);
            case DIARIA -> true;
            case SEMANAL -> dias(v.getDiasSemana(), primeraFecha.getDayOfWeek()).contains(fecha.getDayOfWeek());
            // Si el dia no existe en el mes (31 de febrero), esa ocurrencia no ocurre.
            case MENSUAL -> fecha.getDayOfMonth() == primeraFecha.getDayOfMonth();
        };
    }

    /** Parsea "MON,TUE" o "LUN,MAR". Vacio = el mismo dia de la semana que la primera ocurrencia. */
    static Set<DayOfWeek> dias(String diasSemana, DayOfWeek porDefecto) {
        Set<DayOfWeek> dias = EnumSet.noneOf(DayOfWeek.class);
        if (diasSemana != null) {
            for (String parte : diasSemana.split("[,; ]+")) {
                DayOfWeek d = dia(parte.trim().toUpperCase(Locale.ROOT));
                if (d != null) {
                    dias.add(d);
                }
            }
        }
        if (dias.isEmpty()) {
            dias.add(porDefecto);
        }
        return dias;
    }

    private static DayOfWeek dia(String s) {
        if (s.length() < 2) {
            return null;
        }
        return switch (s.length() >= 3 ? s.substring(0, 3) : s) {
            case "MON", "LUN", "LU" -> DayOfWeek.MONDAY;
            case "TUE", "MAR", "MA" -> DayOfWeek.TUESDAY;
            case "WED", "MIE", "MIÉ", "MI" -> DayOfWeek.WEDNESDAY;
            case "THU", "JUE", "JU" -> DayOfWeek.THURSDAY;
            case "FRI", "VIE", "VI" -> DayOfWeek.FRIDAY;
            case "SAT", "SAB", "SÁB", "SA" -> DayOfWeek.SATURDAY;
            case "SUN", "DOM", "DO" -> DayOfWeek.SUNDAY;
            default -> null;
        };
    }

    static ZoneId zona(String zonaHoraria) {
        try {
            return zonaHoraria == null || zonaHoraria.isBlank() ? ZoneId.of("UTC") : ZoneId.of(zonaHoraria.trim());
        } catch (Exception e) {
            return ZoneId.of("UTC");
        }
    }
}
