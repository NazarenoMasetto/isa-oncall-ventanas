package ar.edu.um.isa.oncall.repository.oncall;

import ar.edu.um.isa.oncall.domain.ExcepcionDeVentana;
import ar.edu.um.isa.oncall.domain.ServicioEnVentana;
import ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Consultas propias para resolver ventanas de mantenimiento activas.
 */
@Repository
public interface VentanaLookupRepository extends JpaRepository<VentanaDeMantenimiento, Long> {
    @Query("select s from ServicioEnVentana s join fetch s.ventana v " + "where s.servicio.id = :servicioId and v.activa = true")
    List<ServicioEnVentana> findCoberturasActivasDeServicio(@Param("servicioId") Long servicioId);

    @Query("select e from ExcepcionDeVentana e where e.ventana.id = :ventanaId")
    List<ExcepcionDeVentana> findExcepcionesDeVentana(@Param("ventanaId") Long ventanaId);
}
