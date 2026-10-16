package ar.edu.um.isa.oncall.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ServicioEnVentanaCriteriaTest {

    @Test
    void newServicioEnVentanaCriteriaHasAllFiltersNullTest() {
        var servicioEnVentanaCriteria = new ServicioEnVentanaCriteria();
        assertThat(servicioEnVentanaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void servicioEnVentanaCriteriaFluentMethodsCreatesFiltersTest() {
        var servicioEnVentanaCriteria = new ServicioEnVentanaCriteria();

        setAllFilters(servicioEnVentanaCriteria);

        assertThat(servicioEnVentanaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void servicioEnVentanaCriteriaCopyCreatesNullFilterTest() {
        var servicioEnVentanaCriteria = new ServicioEnVentanaCriteria();
        var copy = servicioEnVentanaCriteria.copy();

        assertThat(servicioEnVentanaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(servicioEnVentanaCriteria)
        );
    }

    @Test
    void servicioEnVentanaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var servicioEnVentanaCriteria = new ServicioEnVentanaCriteria();
        setAllFilters(servicioEnVentanaCriteria);

        var copy = servicioEnVentanaCriteria.copy();

        assertThat(servicioEnVentanaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(servicioEnVentanaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var servicioEnVentanaCriteria = new ServicioEnVentanaCriteria();

        assertThat(servicioEnVentanaCriteria).hasToString("ServicioEnVentanaCriteria{}");
    }

    private static void setAllFilters(ServicioEnVentanaCriteria servicioEnVentanaCriteria) {
        servicioEnVentanaCriteria.id();
        servicioEnVentanaCriteria.suprimirAlertas();
        servicioEnVentanaCriteria.suprimirEscalamiento();
        servicioEnVentanaCriteria.notas();
        servicioEnVentanaCriteria.ventanaId();
        servicioEnVentanaCriteria.servicioId();
        servicioEnVentanaCriteria.distinct();
    }

    private static Condition<ServicioEnVentanaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSuprimirAlertas()) &&
                condition.apply(criteria.getSuprimirEscalamiento()) &&
                condition.apply(criteria.getNotas()) &&
                condition.apply(criteria.getVentanaId()) &&
                condition.apply(criteria.getServicioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ServicioEnVentanaCriteria> copyFiltersAre(
        ServicioEnVentanaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSuprimirAlertas(), copy.getSuprimirAlertas()) &&
                condition.apply(criteria.getSuprimirEscalamiento(), copy.getSuprimirEscalamiento()) &&
                condition.apply(criteria.getNotas(), copy.getNotas()) &&
                condition.apply(criteria.getVentanaId(), copy.getVentanaId()) &&
                condition.apply(criteria.getServicioId(), copy.getServicioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
