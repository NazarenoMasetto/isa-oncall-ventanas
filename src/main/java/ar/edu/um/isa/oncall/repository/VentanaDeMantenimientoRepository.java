package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VentanaDeMantenimiento entity.
 */
@Repository
public interface VentanaDeMantenimientoRepository
    extends JpaRepository<VentanaDeMantenimiento, Long>, JpaSpecificationExecutor<VentanaDeMantenimiento>
{
    @Query(
        "select ventanaDeMantenimiento from VentanaDeMantenimiento ventanaDeMantenimiento where ventanaDeMantenimiento.creadaPor.login = ?#{authentication.name}"
    )
    List<VentanaDeMantenimiento> findByCreadaPorIsCurrentUser();

    default Optional<VentanaDeMantenimiento> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<VentanaDeMantenimiento> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<VentanaDeMantenimiento> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select ventanaDeMantenimiento from VentanaDeMantenimiento ventanaDeMantenimiento left join fetch ventanaDeMantenimiento.equipo left join fetch ventanaDeMantenimiento.creadaPor",
        countQuery = "select count(ventanaDeMantenimiento) from VentanaDeMantenimiento ventanaDeMantenimiento"
    )
    Page<VentanaDeMantenimiento> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select ventanaDeMantenimiento from VentanaDeMantenimiento ventanaDeMantenimiento left join fetch ventanaDeMantenimiento.equipo left join fetch ventanaDeMantenimiento.creadaPor"
    )
    List<VentanaDeMantenimiento> findAllWithToOneRelationships();

    @Query(
        "select ventanaDeMantenimiento from VentanaDeMantenimiento ventanaDeMantenimiento left join fetch ventanaDeMantenimiento.equipo left join fetch ventanaDeMantenimiento.creadaPor where ventanaDeMantenimiento.id =:id"
    )
    Optional<VentanaDeMantenimiento> findOneWithToOneRelationships(@Param("id") Long id);
}
