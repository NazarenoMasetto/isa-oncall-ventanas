package ar.edu.um.isa.oncall.service.criteria;

import ar.edu.um.isa.oncall.domain.enumeration.TipoRecurrencia;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento} entity. This class is used
 * in {@link ar.edu.um.isa.oncall.web.rest.VentanaDeMantenimientoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ventana-de-mantenimientos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentanaDeMantenimientoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoRecurrencia
     */
    public static class TipoRecurrenciaFilter extends Filter<TipoRecurrencia> {

        public TipoRecurrenciaFilter() {}

        public TipoRecurrenciaFilter(TipoRecurrenciaFilter filter) {
            super(filter);
        }

        @Override
        public TipoRecurrenciaFilter copy() {
            return new TipoRecurrenciaFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private InstantFilter inicio;

    private InstantFilter fin;

    private StringFilter zonaHoraria;

    private TipoRecurrenciaFilter recurrencia;

    private StringFilter diasSemana;

    private LocalDateFilter recurrenciaHasta;

    private BooleanFilter activa;

    private LongFilter equipoId;

    private LongFilter creadaPorId;

    private LongFilter servicioCubiertoId;

    private LongFilter excepcionId;

    private LongFilter alertaSuprimidaId;

    private Boolean distinct;

    public VentanaDeMantenimientoCriteria() {}

    public VentanaDeMantenimientoCriteria(VentanaDeMantenimientoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.inicio = other.optionalInicio().map(InstantFilter::copy).orElse(null);
        this.fin = other.optionalFin().map(InstantFilter::copy).orElse(null);
        this.zonaHoraria = other.optionalZonaHoraria().map(StringFilter::copy).orElse(null);
        this.recurrencia = other.optionalRecurrencia().map(TipoRecurrenciaFilter::copy).orElse(null);
        this.diasSemana = other.optionalDiasSemana().map(StringFilter::copy).orElse(null);
        this.recurrenciaHasta = other.optionalRecurrenciaHasta().map(LocalDateFilter::copy).orElse(null);
        this.activa = other.optionalActiva().map(BooleanFilter::copy).orElse(null);
        this.equipoId = other.optionalEquipoId().map(LongFilter::copy).orElse(null);
        this.creadaPorId = other.optionalCreadaPorId().map(LongFilter::copy).orElse(null);
        this.servicioCubiertoId = other.optionalServicioCubiertoId().map(LongFilter::copy).orElse(null);
        this.excepcionId = other.optionalExcepcionId().map(LongFilter::copy).orElse(null);
        this.alertaSuprimidaId = other.optionalAlertaSuprimidaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VentanaDeMantenimientoCriteria copy() {
        return new VentanaDeMantenimientoCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public InstantFilter getInicio() {
        return inicio;
    }

    public Optional<InstantFilter> optionalInicio() {
        return Optional.ofNullable(inicio);
    }

    public InstantFilter inicio() {
        if (inicio == null) {
            setInicio(new InstantFilter());
        }
        return inicio;
    }

    public void setInicio(InstantFilter inicio) {
        this.inicio = inicio;
    }

    public InstantFilter getFin() {
        return fin;
    }

    public Optional<InstantFilter> optionalFin() {
        return Optional.ofNullable(fin);
    }

    public InstantFilter fin() {
        if (fin == null) {
            setFin(new InstantFilter());
        }
        return fin;
    }

    public void setFin(InstantFilter fin) {
        this.fin = fin;
    }

    public StringFilter getZonaHoraria() {
        return zonaHoraria;
    }

    public Optional<StringFilter> optionalZonaHoraria() {
        return Optional.ofNullable(zonaHoraria);
    }

    public StringFilter zonaHoraria() {
        if (zonaHoraria == null) {
            setZonaHoraria(new StringFilter());
        }
        return zonaHoraria;
    }

    public void setZonaHoraria(StringFilter zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public TipoRecurrenciaFilter getRecurrencia() {
        return recurrencia;
    }

    public Optional<TipoRecurrenciaFilter> optionalRecurrencia() {
        return Optional.ofNullable(recurrencia);
    }

    public TipoRecurrenciaFilter recurrencia() {
        if (recurrencia == null) {
            setRecurrencia(new TipoRecurrenciaFilter());
        }
        return recurrencia;
    }

    public void setRecurrencia(TipoRecurrenciaFilter recurrencia) {
        this.recurrencia = recurrencia;
    }

    public StringFilter getDiasSemana() {
        return diasSemana;
    }

    public Optional<StringFilter> optionalDiasSemana() {
        return Optional.ofNullable(diasSemana);
    }

    public StringFilter diasSemana() {
        if (diasSemana == null) {
            setDiasSemana(new StringFilter());
        }
        return diasSemana;
    }

    public void setDiasSemana(StringFilter diasSemana) {
        this.diasSemana = diasSemana;
    }

    public LocalDateFilter getRecurrenciaHasta() {
        return recurrenciaHasta;
    }

    public Optional<LocalDateFilter> optionalRecurrenciaHasta() {
        return Optional.ofNullable(recurrenciaHasta);
    }

    public LocalDateFilter recurrenciaHasta() {
        if (recurrenciaHasta == null) {
            setRecurrenciaHasta(new LocalDateFilter());
        }
        return recurrenciaHasta;
    }

    public void setRecurrenciaHasta(LocalDateFilter recurrenciaHasta) {
        this.recurrenciaHasta = recurrenciaHasta;
    }

    public BooleanFilter getActiva() {
        return activa;
    }

    public Optional<BooleanFilter> optionalActiva() {
        return Optional.ofNullable(activa);
    }

    public BooleanFilter activa() {
        if (activa == null) {
            setActiva(new BooleanFilter());
        }
        return activa;
    }

    public void setActiva(BooleanFilter activa) {
        this.activa = activa;
    }

    public LongFilter getEquipoId() {
        return equipoId;
    }

    public Optional<LongFilter> optionalEquipoId() {
        return Optional.ofNullable(equipoId);
    }

    public LongFilter equipoId() {
        if (equipoId == null) {
            setEquipoId(new LongFilter());
        }
        return equipoId;
    }

    public void setEquipoId(LongFilter equipoId) {
        this.equipoId = equipoId;
    }

    public LongFilter getCreadaPorId() {
        return creadaPorId;
    }

    public Optional<LongFilter> optionalCreadaPorId() {
        return Optional.ofNullable(creadaPorId);
    }

    public LongFilter creadaPorId() {
        if (creadaPorId == null) {
            setCreadaPorId(new LongFilter());
        }
        return creadaPorId;
    }

    public void setCreadaPorId(LongFilter creadaPorId) {
        this.creadaPorId = creadaPorId;
    }

    public LongFilter getServicioCubiertoId() {
        return servicioCubiertoId;
    }

    public Optional<LongFilter> optionalServicioCubiertoId() {
        return Optional.ofNullable(servicioCubiertoId);
    }

    public LongFilter servicioCubiertoId() {
        if (servicioCubiertoId == null) {
            setServicioCubiertoId(new LongFilter());
        }
        return servicioCubiertoId;
    }

    public void setServicioCubiertoId(LongFilter servicioCubiertoId) {
        this.servicioCubiertoId = servicioCubiertoId;
    }

    public LongFilter getExcepcionId() {
        return excepcionId;
    }

    public Optional<LongFilter> optionalExcepcionId() {
        return Optional.ofNullable(excepcionId);
    }

    public LongFilter excepcionId() {
        if (excepcionId == null) {
            setExcepcionId(new LongFilter());
        }
        return excepcionId;
    }

    public void setExcepcionId(LongFilter excepcionId) {
        this.excepcionId = excepcionId;
    }

    public LongFilter getAlertaSuprimidaId() {
        return alertaSuprimidaId;
    }

    public Optional<LongFilter> optionalAlertaSuprimidaId() {
        return Optional.ofNullable(alertaSuprimidaId);
    }

    public LongFilter alertaSuprimidaId() {
        if (alertaSuprimidaId == null) {
            setAlertaSuprimidaId(new LongFilter());
        }
        return alertaSuprimidaId;
    }

    public void setAlertaSuprimidaId(LongFilter alertaSuprimidaId) {
        this.alertaSuprimidaId = alertaSuprimidaId;
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
        final VentanaDeMantenimientoCriteria that = (VentanaDeMantenimientoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(inicio, that.inicio) &&
            Objects.equals(fin, that.fin) &&
            Objects.equals(zonaHoraria, that.zonaHoraria) &&
            Objects.equals(recurrencia, that.recurrencia) &&
            Objects.equals(diasSemana, that.diasSemana) &&
            Objects.equals(recurrenciaHasta, that.recurrenciaHasta) &&
            Objects.equals(activa, that.activa) &&
            Objects.equals(equipoId, that.equipoId) &&
            Objects.equals(creadaPorId, that.creadaPorId) &&
            Objects.equals(servicioCubiertoId, that.servicioCubiertoId) &&
            Objects.equals(excepcionId, that.excepcionId) &&
            Objects.equals(alertaSuprimidaId, that.alertaSuprimidaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            descripcion,
            inicio,
            fin,
            zonaHoraria,
            recurrencia,
            diasSemana,
            recurrenciaHasta,
            activa,
            equipoId,
            creadaPorId,
            servicioCubiertoId,
            excepcionId,
            alertaSuprimidaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentanaDeMantenimientoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalInicio().map(f -> "inicio=" + f + ", ").orElse("") +
            optionalFin().map(f -> "fin=" + f + ", ").orElse("") +
            optionalZonaHoraria().map(f -> "zonaHoraria=" + f + ", ").orElse("") +
            optionalRecurrencia().map(f -> "recurrencia=" + f + ", ").orElse("") +
            optionalDiasSemana().map(f -> "diasSemana=" + f + ", ").orElse("") +
            optionalRecurrenciaHasta().map(f -> "recurrenciaHasta=" + f + ", ").orElse("") +
            optionalActiva().map(f -> "activa=" + f + ", ").orElse("") +
            optionalEquipoId().map(f -> "equipoId=" + f + ", ").orElse("") +
            optionalCreadaPorId().map(f -> "creadaPorId=" + f + ", ").orElse("") +
            optionalServicioCubiertoId().map(f -> "servicioCubiertoId=" + f + ", ").orElse("") +
            optionalExcepcionId().map(f -> "excepcionId=" + f + ", ").orElse("") +
            optionalAlertaSuprimidaId().map(f -> "alertaSuprimidaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
