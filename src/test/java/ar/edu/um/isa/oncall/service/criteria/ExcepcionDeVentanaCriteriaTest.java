package ar.edu.um.isa.oncall.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExcepcionDeVentanaCriteriaTest {

    @Test
    void newExcepcionDeVentanaCriteriaHasAllFiltersNullTest() {
        var excepcionDeVentanaCriteria = new ExcepcionDeVentanaCriteria();
        assertThat(excepcionDeVentanaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void excepcionDeVentanaCriteriaFluentMethodsCreatesFiltersTest() {
        var excepcionDeVentanaCriteria = new ExcepcionDeVentanaCriteria();

        setAllFilters(excepcionDeVentanaCriteria);

        assertThat(excepcionDeVentanaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void excepcionDeVentanaCriteriaCopyCreatesNullFilterTest() {
        var excepcionDeVentanaCriteria = new ExcepcionDeVentanaCriteria();
        var copy = excepcionDeVentanaCriteria.copy();

        assertThat(excepcionDeVentanaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(excepcionDeVentanaCriteria)
        );
    }

    @Test
    void excepcionDeVentanaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var excepcionDeVentanaCriteria = new ExcepcionDeVentanaCriteria();
        setAllFilters(excepcionDeVentanaCriteria);

        var copy = excepcionDeVentanaCriteria.copy();

        assertThat(excepcionDeVentanaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(excepcionDeVentanaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var excepcionDeVentanaCriteria = new ExcepcionDeVentanaCriteria();

        assertThat(excepcionDeVentanaCriteria).hasToString("ExcepcionDeVentanaCriteria{}");
    }

    private static void setAllFilters(ExcepcionDeVentanaCriteria excepcionDeVentanaCriteria) {
        excepcionDeVentanaCriteria.id();
        excepcionDeVentanaCriteria.fechaOcurrencia();
        excepcionDeVentanaCriteria.tipo();
        excepcionDeVentanaCriteria.nuevoInicio();
        excepcionDeVentanaCriteria.nuevoFin();
        excepcionDeVentanaCriteria.motivo();
        excepcionDeVentanaCriteria.creadaEn();
        excepcionDeVentanaCriteria.ventanaId();
        excepcionDeVentanaCriteria.autorizadaPorId();
        excepcionDeVentanaCriteria.distinct();
    }

    private static Condition<ExcepcionDeVentanaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFechaOcurrencia()) &&
                condition.apply(criteria.getTipo()) &&
                condition.apply(criteria.getNuevoInicio()) &&
                condition.apply(criteria.getNuevoFin()) &&
                condition.apply(criteria.getMotivo()) &&
                condition.apply(criteria.getCreadaEn()) &&
                condition.apply(criteria.getVentanaId()) &&
                condition.apply(criteria.getAutorizadaPorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExcepcionDeVentanaCriteria> copyFiltersAre(
        ExcepcionDeVentanaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFechaOcurrencia(), copy.getFechaOcurrencia()) &&
                condition.apply(criteria.getTipo(), copy.getTipo()) &&
                condition.apply(criteria.getNuevoInicio(), copy.getNuevoInicio()) &&
                condition.apply(criteria.getNuevoFin(), copy.getNuevoFin()) &&
                condition.apply(criteria.getMotivo(), copy.getMotivo()) &&
                condition.apply(criteria.getCreadaEn(), copy.getCreadaEn()) &&
                condition.apply(criteria.getVentanaId(), copy.getVentanaId()) &&
                condition.apply(criteria.getAutorizadaPorId(), copy.getAutorizadaPorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
