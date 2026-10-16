package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.*; // for static metamodels
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import ar.edu.um.isa.oncall.repository.VentanaDeMantenimientoRepository;
import ar.edu.um.isa.oncall.service.criteria.VentanaDeMantenimientoCriteria;
import ar.edu.um.isa.oncall.service.dto.VentanaDeMantenimientoDTO;
import ar.edu.um.isa.oncall.service.mapper.VentanaDeMantenimientoMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link VentanaDeMantenimiento} entities in the database.
 * The main input is a {@link VentanaDeMantenimientoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link VentanaDeMantenimientoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VentanaDeMantenimientoQueryService extends QueryService<VentanaDeMantenimiento> {

    private static final Logger LOG = LoggerFactory.getLogger(VentanaDeMantenimientoQueryService.class);

    private final VentanaDeMantenimientoRepository ventanaDeMantenimientoRepository;

    private final VentanaDeMantenimientoMapper ventanaDeMantenimientoMapper;

    public VentanaDeMantenimientoQueryService(
        VentanaDeMantenimientoRepository ventanaDeMantenimientoRepository,
        VentanaDeMantenimientoMapper ventanaDeMantenimientoMapper
    ) {
        this.ventanaDeMantenimientoRepository = ventanaDeMantenimientoRepository;
        this.ventanaDeMantenimientoMapper = ventanaDeMantenimientoMapper;
    }

    /**
     * Return a {@link Page} of {@link VentanaDeMantenimientoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VentanaDeMantenimientoDTO> findByCriteria(VentanaDeMantenimientoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VentanaDeMantenimiento> specification = createSpecification(criteria);
        return ventanaDeMantenimientoRepository.findAll(specification, page).map(ventanaDeMantenimientoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VentanaDeMantenimientoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<VentanaDeMantenimiento> specification = createSpecification(criteria);
        return ventanaDeMantenimientoRepository.count(specification);
    }

    /**
     * Function to convert {@link VentanaDeMantenimientoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VentanaDeMantenimiento> createSpecification(VentanaDeMantenimientoCriteria criteria) {
        Specification<VentanaDeMantenimiento> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(VentanaDeMantenimiento_.equipo, JoinType.LEFT);
                root.fetch(VentanaDeMantenimiento_.creadaPor, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), VentanaDeMantenimiento_.id),
                    buildStringSpecification(criteria.getNombre(), VentanaDeMantenimiento_.nombre),
                    buildStringSpecification(criteria.getDescripcion(), VentanaDeMantenimiento_.descripcion),
                    buildRangeSpecification(criteria.getInicio(), VentanaDeMantenimiento_.inicio),
                    buildRangeSpecification(criteria.getFin(), VentanaDeMantenimiento_.fin),
                    buildStringSpecification(criteria.getZonaHoraria(), VentanaDeMantenimiento_.zonaHoraria),
                    buildSpecification(criteria.getRecurrencia(), VentanaDeMantenimiento_.recurrencia),
                    buildStringSpecification(criteria.getDiasSemana(), VentanaDeMantenimiento_.diasSemana),
                    buildRangeSpecification(criteria.getRecurrenciaHasta(), VentanaDeMantenimiento_.recurrenciaHasta),
                    buildSpecification(criteria.getActiva(), VentanaDeMantenimiento_.activa),
                    buildSpecification(criteria.getEquipoId(), root ->
                        root.join(VentanaDeMantenimiento_.equipo, JoinType.LEFT).get(Equipo_.id)
                    ),
                    buildSpecification(criteria.getCreadaPorId(), root ->
                        root.join(VentanaDeMantenimiento_.creadaPor, JoinType.LEFT).get(User_.id)
                    ),
                    buildSpecification(criteria.getServicioCubiertoId(), root ->
                        root.join(VentanaDeMantenimiento_.servicioCubiertos, JoinType.LEFT).get(ServicioEnVentana_.id)
                    ),
                    buildSpecification(criteria.getExcepcionId(), root ->
                        root.join(VentanaDeMantenimiento_.excepcions, JoinType.LEFT).get(ExcepcionDeVentana_.id)
                    ),
                    buildSpecification(criteria.getAlertaSuprimidaId(), root ->
                        root.join(VentanaDeMantenimiento_.alertaSuprimidas, JoinType.LEFT).get(Alerta_.id)
                    )
                )
            );
        }
        return specification;
    }
}
