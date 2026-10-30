package ar.edu.um.isa.oncall.service.oncall.dto;

import java.time.Instant;
import java.util.List;

/**
 * Respuesta de la regla C. Si no hay cobertura, cubierto = false y la lista
 * viene vacia: un hueco es una respuesta valida, no un error.
 */
public record GuardiaActualDTO(
    Long rotacionId,
    String rotacion,
    Instant instante,
    String instanteLocal,
    String zonaHoraria,
    boolean cubierto,
    String motivo,
    List<ResponsableDTO> responsables
) {
    public record ResponsableDTO(String login, String rol, boolean esReemplazo, Long turnoId, Instant desde, Instant hasta) {}
}
