package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.AlertaTestSamples.*;
import static ar.edu.um.isa.oncall.domain.EquipoTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ExcepcionDeVentanaTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioEnVentanaTestSamples.*;
import static ar.edu.um.isa.oncall.domain.VentanaDeMantenimientoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VentanaDeMantenimientoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VentanaDeMantenimiento.class);
        VentanaDeMantenimiento ventanaDeMantenimiento1 = getVentanaDeMantenimientoSample1();
        VentanaDeMantenimiento ventanaDeMantenimiento2 = new VentanaDeMantenimiento();
        assertThat(ventanaDeMantenimiento1).isNotEqualTo(ventanaDeMantenimiento2);

        ventanaDeMantenimiento2.setId(ventanaDeMantenimiento1.getId());
        assertThat(ventanaDeMantenimiento1).isEqualTo(ventanaDeMantenimiento2);

        ventanaDeMantenimiento2 = getVentanaDeMantenimientoSample2();
        assertThat(ventanaDeMantenimiento1).isNotEqualTo(ventanaDeMantenimiento2);
    }

    @Test
    void equipoTest() {
        VentanaDeMantenimiento ventanaDeMantenimiento = getVentanaDeMantenimientoRandomSampleGenerator();
        Equipo equipoBack = getEquipoRandomSampleGenerator();

        ventanaDeMantenimiento.setEquipo(equipoBack);
        assertThat(ventanaDeMantenimiento.getEquipo()).isEqualTo(equipoBack);

        ventanaDeMantenimiento.equipo(null);
        assertThat(ventanaDeMantenimiento.getEquipo()).isNull();
    }

    @Test
    void servicioCubiertoTest() {
        VentanaDeMantenimiento ventanaDeMantenimiento = getVentanaDeMantenimientoRandomSampleGenerator();
        ServicioEnVentana servicioEnVentanaBack = getServicioEnVentanaRandomSampleGenerator();

        ventanaDeMantenimiento.addServicioCubierto(servicioEnVentanaBack);
        assertThat(ventanaDeMantenimiento.getServicioCubiertos()).containsOnly(servicioEnVentanaBack);
        assertThat(servicioEnVentanaBack.getVentana()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.removeServicioCubierto(servicioEnVentanaBack);
        assertThat(ventanaDeMantenimiento.getServicioCubiertos()).doesNotContain(servicioEnVentanaBack);
        assertThat(servicioEnVentanaBack.getVentana()).isNull();

        ventanaDeMantenimiento.servicioCubiertos(new HashSet<>(Set.of(servicioEnVentanaBack)));
        assertThat(ventanaDeMantenimiento.getServicioCubiertos()).containsOnly(servicioEnVentanaBack);
        assertThat(servicioEnVentanaBack.getVentana()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.setServicioCubiertos(new HashSet<>());
        assertThat(ventanaDeMantenimiento.getServicioCubiertos()).doesNotContain(servicioEnVentanaBack);
        assertThat(servicioEnVentanaBack.getVentana()).isNull();
    }

    @Test
    void excepcionTest() {
        VentanaDeMantenimiento ventanaDeMantenimiento = getVentanaDeMantenimientoRandomSampleGenerator();
        ExcepcionDeVentana excepcionDeVentanaBack = getExcepcionDeVentanaRandomSampleGenerator();

        ventanaDeMantenimiento.addExcepcion(excepcionDeVentanaBack);
        assertThat(ventanaDeMantenimiento.getExcepcions()).containsOnly(excepcionDeVentanaBack);
        assertThat(excepcionDeVentanaBack.getVentana()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.removeExcepcion(excepcionDeVentanaBack);
        assertThat(ventanaDeMantenimiento.getExcepcions()).doesNotContain(excepcionDeVentanaBack);
        assertThat(excepcionDeVentanaBack.getVentana()).isNull();

        ventanaDeMantenimiento.excepcions(new HashSet<>(Set.of(excepcionDeVentanaBack)));
        assertThat(ventanaDeMantenimiento.getExcepcions()).containsOnly(excepcionDeVentanaBack);
        assertThat(excepcionDeVentanaBack.getVentana()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.setExcepcions(new HashSet<>());
        assertThat(ventanaDeMantenimiento.getExcepcions()).doesNotContain(excepcionDeVentanaBack);
        assertThat(excepcionDeVentanaBack.getVentana()).isNull();
    }

    @Test
    void alertaSuprimidaTest() {
        VentanaDeMantenimiento ventanaDeMantenimiento = getVentanaDeMantenimientoRandomSampleGenerator();
        Alerta alertaBack = getAlertaRandomSampleGenerator();

        ventanaDeMantenimiento.addAlertaSuprimida(alertaBack);
        assertThat(ventanaDeMantenimiento.getAlertaSuprimidas()).containsOnly(alertaBack);
        assertThat(alertaBack.getSuprimidaPorVentana()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.removeAlertaSuprimida(alertaBack);
        assertThat(ventanaDeMantenimiento.getAlertaSuprimidas()).doesNotContain(alertaBack);
        assertThat(alertaBack.getSuprimidaPorVentana()).isNull();

        ventanaDeMantenimiento.alertaSuprimidas(new HashSet<>(Set.of(alertaBack)));
        assertThat(ventanaDeMantenimiento.getAlertaSuprimidas()).containsOnly(alertaBack);
        assertThat(alertaBack.getSuprimidaPorVentana()).isEqualTo(ventanaDeMantenimiento);

        ventanaDeMantenimiento.setAlertaSuprimidas(new HashSet<>());
        assertThat(ventanaDeMantenimiento.getAlertaSuprimidas()).doesNotContain(alertaBack);
        assertThat(alertaBack.getSuprimidaPorVentana()).isNull();
    }
}
