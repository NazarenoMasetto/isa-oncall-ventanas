package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.EquipoTestSamples.*;
import static ar.edu.um.isa.oncall.domain.RotacionTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioTestSamples.*;
import static ar.edu.um.isa.oncall.domain.VentanaDeMantenimientoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EquipoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipo.class);
        Equipo equipo1 = getEquipoSample1();
        Equipo equipo2 = new Equipo();
        assertThat(equipo1).isNotEqualTo(equipo2);

        equipo2.setId(equipo1.getId());
        assertThat(equipo1).isEqualTo(equipo2);

        equipo2 = getEquipoSample2();
        assertThat(equipo1).isNotEqualTo(equipo2);
    }

    @Test
    void servicioTest() {
        Equipo equipo = getEquipoRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        equipo.addServicio(servicioBack);
        assertThat(equipo.getServicios()).containsOnly(servicioBack);
        assertThat(servicioBack.getEquipo()).isEqualTo(equipo);

        equipo.removeServicio(servicioBack);
        assertThat(equipo.getServicios()).doesNotContain(servicioBack);
        assertThat(servicioBack.getEquipo()).isNull();

        equipo.servicios(new HashSet<>(Set.of(servicioBack)));
        assertThat(equipo.getServicios()).containsOnly(servicioBack);
        assertThat(servicioBack.getEquipo()).isEqualTo(equipo);

        equipo.setServicios(new HashSet<>());
        assertThat(equipo.getServicios()).doesNotContain(servicioBack);
        assertThat(servicioBack.getEquipo()).isNull();
    }

    @Test
    void rotacionTest() {
        Equipo equipo = getEquipoRandomSampleGenerator();
        Rotacion rotacionBack = getRotacionRandomSampleGenerator();

        equipo.addRotacion(rotacionBack);
        assertThat(equipo.getRotacions()).containsOnly(rotacionBack);
        assertThat(rotacionBack.getEquipo()).isEqualTo(equipo);

        equipo.removeRotacion(rotacionBack);
        assertThat(equipo.getRotacions()).doesNotContain(rotacionBack);
        assertThat(rotacionBack.getEquipo()).isNull();

        equipo.rotacions(new HashSet<>(Set.of(rotacionBack)));
        assertThat(equipo.getRotacions()).containsOnly(rotacionBack);
        assertThat(rotacionBack.getEquipo()).isEqualTo(equipo);

        equipo.setRotacions(new HashSet<>());
        assertThat(equipo.getRotacions()).doesNotContain(rotacionBack);
        assertThat(rotacionBack.getEquipo()).isNull();
    }

    @Test
    void ventanaTest() {
        Equipo equipo = getEquipoRandomSampleGenerator();
        VentanaDeMantenimiento ventanaDeMantenimientoBack = getVentanaDeMantenimientoRandomSampleGenerator();

        equipo.addVentana(ventanaDeMantenimientoBack);
        assertThat(equipo.getVentanas()).containsOnly(ventanaDeMantenimientoBack);
        assertThat(ventanaDeMantenimientoBack.getEquipo()).isEqualTo(equipo);

        equipo.removeVentana(ventanaDeMantenimientoBack);
        assertThat(equipo.getVentanas()).doesNotContain(ventanaDeMantenimientoBack);
        assertThat(ventanaDeMantenimientoBack.getEquipo()).isNull();

        equipo.ventanas(new HashSet<>(Set.of(ventanaDeMantenimientoBack)));
        assertThat(equipo.getVentanas()).containsOnly(ventanaDeMantenimientoBack);
        assertThat(ventanaDeMantenimientoBack.getEquipo()).isEqualTo(equipo);

        equipo.setVentanas(new HashSet<>());
        assertThat(equipo.getVentanas()).doesNotContain(ventanaDeMantenimientoBack);
        assertThat(ventanaDeMantenimientoBack.getEquipo()).isNull();
    }
}
