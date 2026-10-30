package ar.edu.um.isa.oncall.service.oncall;

import ar.edu.um.isa.oncall.domain.Rotacion;
import ar.edu.um.isa.oncall.domain.TurnoDeGuardia;
import ar.edu.um.isa.oncall.repository.RotacionRepository;
import ar.edu.um.isa.oncall.repository.oncall.GuardiaLookupRepository;
import ar.edu.um.isa.oncall.service.oncall.dto.GuardiaActualDTO;
import ar.edu.um.isa.oncall.service.oncall.dto.GuardiaActualDTO.ResponsableDTO;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regla C: dada una rotacion y un instante, quien esta de guardia.
 * Compone el repositorio generado (RotacionRepository) con consultas propias,
 * sin modificar ningun archivo de JHipster.
 */
@Service
@Transactional(readOnly = true)
public class GuardiaService {

    private final RotacionRepository rotacionRepository;
    private final GuardiaLookupRepository guardiaLookupRepository;

    public GuardiaService(RotacionRepository rotacionRepository, GuardiaLookupRepository guardiaLookupRepository) {
        this.rotacionRepository = rotacionRepository;
        this.guardiaLookupRepository = guardiaLookupRepository;
    }

    /** Vacio si la rotacion no existe. */
    public Optional<GuardiaActualDTO> quienEstaDeGuardia(Long rotacionId, Instant instante) {
        return rotacionRepository.findById(rotacionId).map(rotacion -> resolver(rotacion, instante));
    }

    private GuardiaActualDTO resolver(Rotacion rotacion, Instant t) {
        ZoneId zona = CalendarioDeVentana.zona(rotacion.getZonaHoraria());
        String local = t.atZone(zona).toLocalDateTime().toString();

        if (!Boolean.TRUE.equals(rotacion.getActiva())) {
            return new GuardiaActualDTO(
                rotacion.getId(),
                rotacion.getNombre(),
                t,
                local,
                zona.getId(),
                false,
                "ROTACION_INACTIVA",
                List.of()
            );
        }

        List<TurnoDeGuardia> mandan = ResolutorDeGuardia.resolver(guardiaLookupRepository.findTurnosQueCubren(rotacion.getId(), t), t);
        if (mandan.isEmpty()) {
            return new GuardiaActualDTO(rotacion.getId(), rotacion.getNombre(), t, local, zona.getId(), false, "SIN_COBERTURA", List.of());
        }

        List<ResponsableDTO> responsables = new ArrayList<>();
        for (int i = 0; i < mandan.size(); i++) {
            TurnoDeGuardia turno = mandan.get(i);
            responsables.add(
                new ResponsableDTO(
                    turno.getResponsable() != null ? turno.getResponsable().getLogin() : null,
                    i == 0 ? ResolutorDeGuardia.PRIMARIO : ResolutorDeGuardia.SECUNDARIO,
                    ResolutorDeGuardia.esReemplazo(turno),
                    turno.getId(),
                    turno.getDesde(),
                    turno.getHasta()
                )
            );
        }
        String motivo = ResolutorDeGuardia.esReemplazo(mandan.get(0)) ? "REEMPLAZO" : "TURNO_REGULAR";
        return new GuardiaActualDTO(rotacion.getId(), rotacion.getNombre(), t, local, zona.getId(), true, motivo, responsables);
    }
}
