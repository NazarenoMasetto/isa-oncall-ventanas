package ar.edu.um.isa.oncall.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ar.edu.um.isa.oncall.domain.ServicioEnVentana} entity. This class is used
 * in {@link ar.edu.um.isa.oncall.web.rest.ServicioEnVentanaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /servicio-en-ventanas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioEnVentanaCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter suprimirAlertas;

    private BooleanFilter suprimirEscalamiento;

    private StringFilter notas;

    private LongFilter ventanaId;

    private LongFilter servicioId;

    private Boolean distinct;

    public ServicioEnVentanaCriteria() {}

    public ServicioEnVentanaCriteria(ServicioEnVentanaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.suprimirAlertas = other.optionalSuprimirAlertas().map(BooleanFilter::copy).orElse(null);
        this.suprimirEscalamiento = other.optionalSuprimirEscalamiento().map(BooleanFilter::copy).orElse(null);
        this.notas = other.optionalNotas().map(StringFilter::copy).orElse(null);
        this.ventanaId = other.optionalVentanaId().map(LongFilter::copy).orElse(null);
        this.servicioId = other.optionalServicioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ServicioEnVentanaCriteria copy() {
        return new ServicioEnVentanaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getSuprimirAlertas() {
        return suprimirAlertas;
    }

    public Optional<BooleanFilter> optionalSuprimirAlertas() {
        return Optional.ofNullable(suprimirAlertas);
    }

    public BooleanFilter suprimirAlertas() {
        if (suprimirAlertas == null) {
            setSuprimirAlertas(new BooleanFilter());
        }
        return suprimirAlertas;
    }

    public void setSuprimirAlertas(BooleanFilter suprimirAlertas) {
        this.suprimirAlertas = suprimirAlertas;
    }

    public BooleanFilter getSuprimirEscalamiento() {
        return suprimirEscalamiento;
    }

    public Optional<BooleanFilter> optionalSuprimirEscalamiento() {
        return Optional.ofNullable(suprimirEscalamiento);
    }

    public BooleanFilter suprimirEscalamiento() {
        if (suprimirEscalamiento == null) {
            setSuprimirEscalamiento(new BooleanFilter());
        }
        return suprimirEscalamiento;
    }

    public void setSuprimirEscalamiento(BooleanFilter suprimirEscalamiento) {
        this.suprimirEscalamiento = suprimirEscalamiento;
    }

    public StringFilter getNotas() {
        return notas;
    }

    public Optional<StringFilter> optionalNotas() {
        return Optional.ofNullable(notas);
    }

    public StringFilter notas() {
        if (notas == null) {
            setNotas(new StringFilter());
        }
        return notas;
    }

    public void setNotas(StringFilter notas) {
        this.notas = notas;
    }

    public LongFilter getVentanaId() {
        return ventanaId;
    }

    public Optional<LongFilter> optionalVentanaId() {
        return Optional.ofNullable(ventanaId);
    }

    public LongFilter ventanaId() {
        if (ventanaId == null) {
            setVentanaId(new LongFilter());
        }
        return ventanaId;
    }

    public void setVentanaId(LongFilter ventanaId) {
        this.ventanaId = ventanaId;
    }

    public LongFilter getServicioId() {
        return servicioId;
    }

    public Optional<LongFilter> optionalServicioId() {
        return Optional.ofNullable(servicioId);
    }

    public LongFilter servicioId() {
        if (servicioId == null) {
            setServicioId(new LongFilter());
        }
        return servicioId;
    }

    public void setServicioId(LongFilter servicioId) {
        this.servicioId = servicioId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ServicioEnVentanaCriteria that = (ServicioEnVentanaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(suprimirAlertas, that.suprimirAlertas) &&
            Objects.equals(suprimirEscalamiento, that.suprimirEscalamiento) &&
            Objects.equals(notas, that.notas) &&
            Objects.equals(ventanaId, that.ventanaId) &&
            Objects.equals(servicioId, that.servicioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, suprimirAlertas, suprimirEscalamiento, notas, ventanaId, servicioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioEnVentanaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSuprimirAlertas().map(f -> "suprimirAlertas=" + f + ", ").orElse("") +
            optionalSuprimirEscalamiento().map(f -> "suprimirEscalamiento=" + f + ", ").orElse("") +
            optionalNotas().map(f -> "notas=" + f + ", ").orElse("") +
            optionalVentanaId().map(f -> "ventanaId=" + f + ", ").orElse("") +
            optionalServicioId().map(f -> "servicioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
