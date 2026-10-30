package ar.edu.um.isa.oncall.repository.oncall;

import ar.edu.um.isa.oncall.domain.TurnoDeGuardia;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Consultas propias de la regla C. Vive en el paquete oncall/ para que
 * una regeneracion de JHipster no lo sobrescriba.
 */
@Repository
public interface GuardiaLookupRepository extends JpaRepository<TurnoDeGuardia, Long> {
    /**
     * Turnos de la rotacion que cubren el instante t.
     * Intervalo semiabierto [desde, hasta): un turno que termina a las 09:00
     * ya no cubre las 09:00 en punto.
     */
    @Query(
        "select t from TurnoDeGuardia t left join fetch t.responsable " +
            "where t.rotacion.id = :rotacionId and t.desde <= :instante and t.hasta > :instante"
    )
    List<TurnoDeGuardia> findTurnosQueCubren(@Param("rotacionId") Long rotacionId, @Param("instante") Instant instante);
}
