package ar.edu.um.isa.oncall.web.rest.oncall;

import ar.edu.um.isa.oncall.service.oncall.GuardiaService;
import ar.edu.um.isa.oncall.service.oncall.MantenimientoService;
import ar.edu.um.isa.oncall.service.oncall.dto.GuardiaActualDTO;
import ar.edu.um.isa.oncall.service.oncall.dto.MantenimientoActivoDTO;
import java.time.Instant;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints propios de la regla C. Conviven con los generados sin pisarlos.
 *
 *  GET /api/oncall/guardia/{rotacionId}?instante=2026-10-30T20:00:00Z
 *  GET /api/oncall/mantenimiento/{servicioId}?instante=2026-10-30T20:00:00Z
 *
 * Si no se manda instante, se usa el momento actual.
 */
@RestController
@RequestMapping("/api/oncall")
public class GuardiaResource {

    private final GuardiaService guardiaService;
    private final MantenimientoService mantenimientoService;

    public GuardiaResource(GuardiaService guardiaService, MantenimientoService mantenimientoService) {
        this.guardiaService = guardiaService;
        this.mantenimientoService = mantenimientoService;
    }

    @GetMapping("/guardia/{rotacionId}")
    public ResponseEntity<GuardiaActualDTO> quienEstaDeGuardia(
        @PathVariable Long rotacionId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant instante
    ) {
        Instant t = instante != null ? instante : Instant.now();
        return guardiaService.quienEstaDeGuardia(rotacionId, t).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mantenimiento/{servicioId}")
    public ResponseEntity<MantenimientoActivoDTO> estadoDeMantenimiento(
        @PathVariable Long servicioId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant instante
    ) {
        Instant t = instante != null ? instante : Instant.now();
        return mantenimientoService.estado(servicioId, t).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
