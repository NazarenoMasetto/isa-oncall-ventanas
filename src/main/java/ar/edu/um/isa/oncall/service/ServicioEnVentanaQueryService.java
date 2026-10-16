package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.*; // for static metamodels
import ar.edu.um.isa.oncall.domain.ServicioEnVentana;
import ar.edu.um.isa.oncall.repository.ServicioEnVentanaRepository;
import ar.edu.um.isa.oncall.service.criteria.ServicioEnVentanaCriteria;
import ar.edu.um.isa.oncall.service.dto.ServicioEnVentanaDTO;
import ar.edu.um.isa.oncall.service.mapper.ServicioEnVentanaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ServicioEnVentana} entities in the database.
 * The main input is a {@link ServicioEnVentanaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ServicioEnVentanaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServicioEnVentanaQueryService extends QueryService<ServicioEnVentana> {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioEnVentanaQueryService.class);

    private final ServicioEnVentanaRepository servicioEnVentanaRepository;

    private final ServicioEnVentanaMapper servicioEnVentanaMapper;

    public ServicioEnVentanaQueryService(
        ServicioEnVentanaRepository servicioEnVentanaRepository,
        ServicioEnVentanaMapper servicioEnVentanaMapper
    ) {
        this.servicioEnVentanaRepository = servicioEnVentanaRepository;
        this.servicioEnVentanaMapper = servicioEnVentanaMapper;
    }

    /**
     * Return a {@link List} of {@link ServicioEnVentanaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ServicioEnVentanaDTO> findByCriteria(ServicioEnVentanaCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ServicioEnVentana> specification = createSpecification(criteria);
        return servicioEnVentanaMapper.toDto(servicioEnVentanaRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServicioEnVentanaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ServicioEnVentana> specification = createSpecification(criteria);
        return servicioEnVentanaRepository.count(specification);
    }

    /**
     * Function to convert {@link ServicioEnVentanaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServicioEnVentana> createSpecification(ServicioEnVentanaCriteria criteria) {
        Specification<ServicioEnVentana> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ServicioEnVentana_.ventana, JoinType.LEFT);
                root.fetch(ServicioEnVentana_.servicio, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ServicioEnVentana_.id),
                    buildSpecification(criteria.getSuprimirAlertas(), ServicioEnVentana_.suprimirAlertas),
                    buildSpecification(criteria.getSuprimirEscalamiento(), ServicioEnVentana_.suprimirEscalamiento),
                    buildStringSpecification(criteria.getNotas(), ServicioEnVentana_.notas),
                    buildSpecification(criteria.getVentanaId(), root ->
                        root.join(ServicioEnVentana_.ventana, JoinType.LEFT).get(VentanaDeMantenimiento_.id)
                    ),
                    buildSpecification(criteria.getServicioId(), root ->
                        root.join(ServicioEnVentana_.servicio, JoinType.LEFT).get(Servicio_.id)
                    )
                )
            );
        }
        return specification;
    }
}
