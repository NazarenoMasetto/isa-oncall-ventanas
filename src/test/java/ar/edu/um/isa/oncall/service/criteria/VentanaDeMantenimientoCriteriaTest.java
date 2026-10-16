package ar.edu.um.isa.oncall.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VentanaDeMantenimientoCriteriaTest {

    @Test
    void newVentanaDeMantenimientoCriteriaHasAllFiltersNullTest() {
        var ventanaDeMantenimientoCriteria = new VentanaDeMantenimientoCriteria();
        assertThat(ventanaDeMantenimientoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void ventanaDeMantenimientoCriteriaFluentMethodsCreatesFiltersTest() {
        var ventanaDeMantenimientoCriteria = new VentanaDeMantenimientoCriteria();

        setAllFilters(ventanaDeMantenimientoCriteria);

        assertThat(ventanaDeMantenimientoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void ventanaDeMantenimientoCriteriaCopyCreatesNullFilterTest() {
        var ventanaDeMantenimientoCriteria = new VentanaDeMantenimientoCriteria();
        var copy = ventanaDeMantenimientoCriteria.copy();

        assertThat(ventanaDeMantenimientoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(ventanaDeMantenimientoCriteria)
        );
    }

    @Test
    void ventanaDeMantenimientoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ventanaDeMantenimientoCriteria = new VentanaDeMantenimientoCriteria();
        setAllFilters(ventanaDeMantenimientoCriteria);

        var copy = ventanaDeMantenimientoCriteria.copy();

        assertThat(ventanaDeMantenimientoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(ventanaDeMantenimientoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ventanaDeMantenimientoCriteria = new VentanaDeMantenimientoCriteria();

        assertThat(ventanaDeMantenimientoCriteria).hasToString("VentanaDeMantenimientoCriteria{}");
    }

    private static void setAllFilters(VentanaDeMantenimientoCriteria ventanaDeMantenimientoCriteria) {
        ventanaDeMantenimientoCriteria.id();
        ventanaDeMantenimientoCriteria.nombre();
        ventanaDeMantenimientoCriteria.descripcion();
        ventanaDeMantenimientoCriteria.inicio();
        ventanaDeMantenimientoCriteria.fin();
        ventanaDeMantenimientoCriteria.zonaHoraria();
        ventanaDeMantenimientoCriteria.recurrencia();
        ventanaDeMantenimientoCriteria.diasSemana();
        ventanaDeMantenimientoCriteria.recurrenciaHasta();
        ventanaDeMantenimientoCriteria.activa();
        ventanaDeMantenimientoCriteria.equipoId();
        ventanaDeMantenimientoCriteria.creadaPorId();
        ventanaDeMantenimientoCriteria.servicioCubiertoId();
        ventanaDeMantenimientoCriteria.excepcionId();
        ventanaDeMantenimientoCriteria.alertaSuprimidaId();
        ventanaDeMantenimientoCriteria.distinct();
    }

    private static Condition<VentanaDeMantenimientoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getInicio()) &&
                condition.apply(criteria.getFin()) &&
                condition.apply(criteria.getZonaHoraria()) &&
                condition.apply(criteria.getRecurrencia()) &&
                condition.apply(criteria.getDiasSemana()) &&
                condition.apply(criteria.getRecurrenciaHasta()) &&
                condition.apply(criteria.getActiva()) &&
                condition.apply(criteria.getEquipoId()) &&
                condition.apply(criteria.getCreadaPorId()) &&
                condition.apply(criteria.getServicioCubiertoId()) &&
                condition.apply(criteria.getExcepcionId()) &&
                condition.apply(criteria.getAlertaSuprimidaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VentanaDeMantenimientoCriteria> copyFiltersAre(
        VentanaDeMantenimientoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getInicio(), copy.getInicio()) &&
                condition.apply(criteria.getFin(), copy.getFin()) &&
                condition.apply(criteria.getZonaHoraria(), copy.getZonaHoraria()) &&
                condition.apply(criteria.getRecurrencia(), copy.getRecurrencia()) &&
                condition.apply(criteria.getDiasSemana(), copy.getDiasSemana()) &&
                condition.apply(criteria.getRecurrenciaHasta(), copy.getRecurrenciaHasta()) &&
                condition.apply(criteria.getActiva(), copy.getActiva()) &&
                condition.apply(criteria.getEquipoId(), copy.getEquipoId()) &&
                condition.apply(criteria.getCreadaPorId(), copy.getCreadaPorId()) &&
                condition.apply(criteria.getServicioCubiertoId(), copy.getServicioCubiertoId()) &&
                condition.apply(criteria.getExcepcionId(), copy.getExcepcionId()) &&
                condition.apply(criteria.getAlertaSuprimidaId(), copy.getAlertaSuprimidaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
