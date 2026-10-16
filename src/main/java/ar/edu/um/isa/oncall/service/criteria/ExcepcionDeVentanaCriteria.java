package ar.edu.um.isa.oncall.service.criteria;

import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionVentana;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ar.edu.um.isa.oncall.domain.ExcepcionDeVentana} entity. This class is used
 * in {@link ar.edu.um.isa.oncall.web.rest.ExcepcionDeVentanaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /excepcion-de-ventanas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExcepcionDeVentanaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoExcepcionVentana
     */
    public static class TipoExcepcionVentanaFilter extends Filter<TipoExcepcionVentana> {

        public TipoExcepcionVentanaFilter() {}

        public TipoExcepcionVentanaFilter(TipoExcepcionVentanaFilter filter) {
            super(filter);
        }

        @Override
        public TipoExcepcionVentanaFilter copy() {
            return new TipoExcepcionVentanaFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaOcurrencia;

    private TipoExcepcionVentanaFilter tipo;

    private InstantFilter nuevoInicio;

    private InstantFilter nuevoFin;

    private StringFilter motivo;

    private InstantFilter creadaEn;

    private LongFilter ventanaId;

    private LongFilter autorizadaPorId;

    private Boolean distinct;

    public ExcepcionDeVentanaCriteria() {}

    public ExcepcionDeVentanaCriteria(ExcepcionDeVentanaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fechaOcurrencia = other.optionalFechaOcurrencia().map(LocalDateFilter::copy).orElse(null);
        this.tipo = other.optionalTipo().map(TipoExcepcionVentanaFilter::copy).orElse(null);
        this.nuevoInicio = other.optionalNuevoInicio().map(InstantFilter::copy).orElse(null);
        this.nuevoFin = other.optionalNuevoFin().map(InstantFilter::copy).orElse(null);
        this.motivo = other.optionalMotivo().map(StringFilter::copy).orElse(null);
        this.creadaEn = other.optionalCreadaEn().map(InstantFilter::copy).orElse(null);
        this.ventanaId = other.optionalVentanaId().map(LongFilter::copy).orElse(null);
        this.autorizadaPorId = other.optionalAutorizadaPorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ExcepcionDeVentanaCriteria copy() {
        return new ExcepcionDeVentanaCriteria(this);
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

    public LocalDateFilter getFechaOcurrencia() {
        return fechaOcurrencia;
    }

    public Optional<LocalDateFilter> optionalFechaOcurrencia() {
        return Optional.ofNullable(fechaOcurrencia);
    }

    public LocalDateFilter fechaOcurrencia() {
        if (fechaOcurrencia == null) {
            setFechaOcurrencia(new LocalDateFilter());
        }
        return fechaOcurrencia;
    }

    public void setFechaOcurrencia(LocalDateFilter fechaOcurrencia) {
        this.fechaOcurrencia = fechaOcurrencia;
    }

    public TipoExcepcionVentanaFilter getTipo() {
        return tipo;
    }

    public Optional<TipoExcepcionVentanaFilter> optionalTipo() {
        return Optional.ofNullable(tipo);
    }

    public TipoExcepcionVentanaFilter tipo() {
        if (tipo == null) {
            setTipo(new TipoExcepcionVentanaFilter());
        }
        return tipo;
    }

    public void setTipo(TipoExcepcionVentanaFilter tipo) {
        this.tipo = tipo;
    }

    public InstantFilter getNuevoInicio() {
        return nuevoInicio;
    }

    public Optional<InstantFilter> optionalNuevoInicio() {
        return Optional.ofNullable(nuevoInicio);
    }

    public InstantFilter nuevoInicio() {
        if (nuevoInicio == null) {
            setNuevoInicio(new InstantFilter());
        }
        return nuevoInicio;
    }

    public void setNuevoInicio(InstantFilter nuevoInicio) {
        this.nuevoInicio = nuevoInicio;
    }

    public InstantFilter getNuevoFin() {
        return nuevoFin;
    }

    public Optional<InstantFilter> optionalNuevoFin() {
        return Optional.ofNullable(nuevoFin);
    }

    public InstantFilter nuevoFin() {
        if (nuevoFin == null) {
            setNuevoFin(new InstantFilter());
        }
        return nuevoFin;
    }

    public void setNuevoFin(InstantFilter nuevoFin) {
        this.nuevoFin = nuevoFin;
    }

    public StringFilter getMotivo() {
        return motivo;
    }

    public Optional<StringFilter> optionalMotivo() {
        return Optional.ofNullable(motivo);
    }

    public StringFilter motivo() {
        if (motivo == null) {
            setMotivo(new StringFilter());
        }
        return motivo;
    }

    public void setMotivo(StringFilter motivo) {
        this.motivo = motivo;
    }

    public InstantFilter getCreadaEn() {
        return creadaEn;
    }

    public Optional<InstantFilter> optionalCreadaEn() {
        return Optional.ofNullable(creadaEn);
    }

    public InstantFilter creadaEn() {
        if (creadaEn == null) {
            setCreadaEn(new InstantFilter());
        }
        return creadaEn;
    }

    public void setCreadaEn(InstantFilter creadaEn) {
        this.creadaEn = creadaEn;
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

    public LongFilter getAutorizadaPorId() {
        return autorizadaPorId;
    }

    public Optional<LongFilter> optionalAutorizadaPorId() {
        return Optional.ofNullable(autorizadaPorId);
    }

    public LongFilter autorizadaPorId() {
        if (autorizadaPorId == null) {
            setAutorizadaPorId(new LongFilter());
        }
        return autorizadaPorId;
    }

    public void setAutorizadaPorId(LongFilter autorizadaPorId) {
        this.autorizadaPorId = autorizadaPorId;
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
        final ExcepcionDeVentanaCriteria that = (ExcepcionDeVentanaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaOcurrencia, that.fechaOcurrencia) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(nuevoInicio, that.nuevoInicio) &&
            Objects.equals(nuevoFin, that.nuevoFin) &&
            Objects.equals(motivo, that.motivo) &&
            Objects.equals(creadaEn, that.creadaEn) &&
            Objects.equals(ventanaId, that.ventanaId) &&
            Objects.equals(autorizadaPorId, that.autorizadaPorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaOcurrencia, tipo, nuevoInicio, nuevoFin, motivo, creadaEn, ventanaId, autorizadaPorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExcepcionDeVentanaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFechaOcurrencia().map(f -> "fechaOcurrencia=" + f + ", ").orElse("") +
            optionalTipo().map(f -> "tipo=" + f + ", ").orElse("") +
            optionalNuevoInicio().map(f -> "nuevoInicio=" + f + ", ").orElse("") +
            optionalNuevoFin().map(f -> "nuevoFin=" + f + ", ").orElse("") +
            optionalMotivo().map(f -> "motivo=" + f + ", ").orElse("") +
            optionalCreadaEn().map(f -> "creadaEn=" + f + ", ").orElse("") +
            optionalVentanaId().map(f -> "ventanaId=" + f + ", ").orElse("") +
            optionalAutorizadaPorId().map(f -> "autorizadaPorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
