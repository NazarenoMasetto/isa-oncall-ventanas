package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.*; // for static metamodels
import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.repository.ExcepcionDeVentanaRepository;
import ar.edu.um.isa.oncall.service.criteria.ExcepcionDeVentanaCriteria;
import ar.edu.um.isa.oncall.service.dto.ExcepcionDeVentanaDTO;
import ar.edu.um.isa.oncall.service.mapper.ExcepcionDeVentanaMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ExcepcionDeVentana} entities in the database.
 * The main input is a {@link ExcepcionDeVentanaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExcepcionDeVentanaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExcepcionDeVentanaQueryService extends QueryService<ExcepcionDeVentana> {

    private static final Logger LOG = LoggerFactory.getLogger(ExcepcionDeVentanaQueryService.class);

    private final ExcepcionDeVentanaRepository excepcionDeVentanaRepository;

    private final ExcepcionDeVentanaMapper excepcionDeVentanaMapper;

    public ExcepcionDeVentanaQueryService(
        ExcepcionDeVentanaRepository excepcionDeVentanaRepository,
        ExcepcionDeVentanaMapper excepcionDeVentanaMapper
    ) {
        this.excepcionDeVentanaRepository = excepcionDeVentanaRepository;
        this.excepcionDeVentanaMapper = excepcionDeVentanaMapper;
    }

    /**
     * Return a {@link List} of {@link ExcepcionDeVentanaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExcepcionDeVentanaDTO> findByCriteria(ExcepcionDeVentanaCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ExcepcionDeVentana> specification = createSpecification(criteria);
        return excepcionDeVentanaMapper.toDto(excepcionDeVentanaRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExcepcionDeVentanaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExcepcionDeVentana> specification = createSpecification(criteria);
        return excepcionDeVentanaRepository.count(specification);
    }

    /**
     * Function to convert {@link ExcepcionDeVentanaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExcepcionDeVentana> createSpecification(ExcepcionDeVentanaCriteria criteria) {
        Specification<ExcepcionDeVentana> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ExcepcionDeVentana_.ventana, JoinType.LEFT);
                root.fetch(ExcepcionDeVentana_.autorizadaPor, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ExcepcionDeVentana_.id),
                    buildRangeSpecification(criteria.getFechaOcurrencia(), ExcepcionDeVentana_.fechaOcurrencia),
                    buildSpecification(criteria.getTipo(), ExcepcionDeVentana_.tipo),
                    buildRangeSpecification(criteria.getNuevoInicio(), ExcepcionDeVentana_.nuevoInicio),
                    buildRangeSpecification(criteria.getNuevoFin(), ExcepcionDeVentana_.nuevoFin),
                    buildStringSpecification(criteria.getMotivo(), ExcepcionDeVentana_.motivo),
                    buildRangeSpecification(criteria.getCreadaEn(), ExcepcionDeVentana_.creadaEn),
                    buildSpecification(criteria.getVentanaId(), root ->
                        root.join(ExcepcionDeVentana_.ventana, JoinType.LEFT).get(VentanaDeMantenimiento_.id)
                    ),
                    buildSpecification(criteria.getAutorizadaPorId(), root ->
                        root.join(ExcepcionDeVentana_.autorizadaPor, JoinType.LEFT).get(User_.id)
                    )
                )
            );
        }
        return specification;
    }
}
