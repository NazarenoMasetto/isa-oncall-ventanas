package ar.edu.um.isa.oncall.service.oncall;

import ar.edu.um.isa.oncall.domain.ServicioEnVentana;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.repository.ServicioRepository;
import ar.edu.um.isa.oncall.repository.oncall.VentanaLookupRepository;
import ar.edu.um.isa.oncall.service.oncall.dto.MantenimientoActivoDTO;
import ar.edu.um.isa.oncall.service.oncall.dto.MantenimientoActivoDTO.VentanaActivaDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Extension 5: dado un servicio y un instante, indica si esta en mantenimiento.
 * Si hay varias ventanas activas a la vez, se suprime lo que pida cualquiera de ellas.
 */
@Service
@Transactional(readOnly = true)
public class MantenimientoService {

    private final ServicioRepository servicioRepository;
    private final VentanaLookupRepository ventanaLookupRepository;

    public MantenimientoService(ServicioRepository servicioRepository, VentanaLookupRepository ventanaLookupRepository) {
        this.servicioRepository = servicioRepository;
        this.ventanaLookupRepository = ventanaLookupRepository;
    }

    /** Vacio si el servicio no existe. */
    public Optional<MantenimientoActivoDTO> estado(Long servicioId, Instant instante) {
        if (!servicioRepository.existsById(servicioId)) {
            return Optional.empty();
        }
        List<VentanaActivaDTO> activas = new ArrayList<>();
        boolean suprimirAlertas = false;
        boolean suprimirEscalamiento = false;
        for (ServicioEnVentana cobertura : ventanaLookupRepository.findCoberturasActivasDeServicio(servicioId)) {
            VentanaDeMantenimiento v = cobertura.getVentana();
            Optional<CalendarioDeVentana.Ocurrencia> ocurrencia = CalendarioDeVentana.ocurrenciaActiva(
                v,
                ventanaLookupRepository.findExcepcionesDeVentana(v.getId()),
                instante
            );
            if (ocurrencia.isPresent()) {
                CalendarioDeVentana.Ocurrencia o = ocurrencia.get();
                activas.add(new VentanaActivaDTO(v.getId(), v.getNombre(), o.desde(), o.hasta(), o.origen()));
                suprimirAlertas |= Boolean.TRUE.equals(cobertura.getSuprimirAlertas());
                suprimirEscalamiento |= Boolean.TRUE.equals(cobertura.getSuprimirEscalamiento());
            }
        }
        return Optional.of(
            new MantenimientoActivoDTO(servicioId, instante, !activas.isEmpty(), suprimirAlertas, suprimirEscalamiento, activas)
        );
    }
}
