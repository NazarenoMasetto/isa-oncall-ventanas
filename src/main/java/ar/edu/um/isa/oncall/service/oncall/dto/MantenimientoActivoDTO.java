package ar.edu.um.isa.oncall.service.oncall.dto;

import java.time.Instant;
import java.util.List;

/**
 * Indica si un servicio esta dentro de una ventana de mantenimiento en un instante.
 */
public record MantenimientoActivoDTO(
    Long servicioId,
    Instant instante,
    boolean enMantenimiento,
    boolean suprimirAlertas,
    boolean suprimirEscalamiento,
    List<VentanaActivaDTO> ventanas
) {
    public record VentanaActivaDTO(Long ventanaId, String nombre, Instant desde, Instant hasta, String origen) {}
}
