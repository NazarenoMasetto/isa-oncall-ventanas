package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.TipoRecurrencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.VentanaDeMantenimiento} entity.
 */
@Schema(
    description = "Rango horario programado en el que las alertas de los servicios\ncubiertos no generan incidente. inicio/fin definen la PRIMERA\nocurrencia; si hay recurrencia, se repite con la misma duracion\nhasta recurrenciaHasta. Las horas se interpretan en zonaHoraria\n(igual que en Rotacion), asi \"martes 02:00\" sigue siendo 02:00\naunque cambie el horario de verano."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentanaDeMantenimientoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 60)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    @NotNull
    private Instant inicio;

    @NotNull
    private Instant fin;

    @NotNull
    @Size(max = 50)
    private String zonaHoraria;

    @NotNull
    private TipoRecurrencia recurrencia;

    @Size(max = 30)
    private String diasSemana;

    private LocalDate recurrenciaHasta;

    @NotNull
    private Boolean activa;

    @NotNull
    private EquipoDTO equipo;

    @NotNull
    private UserDTO creadaPor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getInicio() {
        return inicio;
    }

    public void setInicio(Instant inicio) {
        this.inicio = inicio;
    }

    public Instant getFin() {
        return fin;
    }

    public void setFin(Instant fin) {
        this.fin = fin;
    }

    public String getZonaHoraria() {
        return zonaHoraria;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public TipoRecurrencia getRecurrencia() {
        return recurrencia;
    }

    public void setRecurrencia(TipoRecurrencia recurrencia) {
        this.recurrencia = recurrencia;
    }

    public String getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

    public LocalDate getRecurrenciaHasta() {
        return recurrenciaHasta;
    }

    public void setRecurrenciaHasta(LocalDate recurrenciaHasta) {
        this.recurrenciaHasta = recurrenciaHasta;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public EquipoDTO getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoDTO equipo) {
        this.equipo = equipo;
    }

    public UserDTO getCreadaPor() {
        return creadaPor;
    }

    public void setCreadaPor(UserDTO creadaPor) {
        this.creadaPor = creadaPor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentanaDeMantenimientoDTO)) {
            return false;
        }

        VentanaDeMantenimientoDTO ventanaDeMantenimientoDTO = (VentanaDeMantenimientoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ventanaDeMantenimientoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentanaDeMantenimientoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", inicio='" + getInicio() + "'" +
            ", fin='" + getFin() + "'" +
            ", zonaHoraria='" + getZonaHoraria() + "'" +
            ", recurrencia='" + getRecurrencia() + "'" +
            ", diasSemana='" + getDiasSemana() + "'" +
            ", recurrenciaHasta='" + getRecurrenciaHasta() + "'" +
            ", activa='" + getActiva() + "'" +
            ", equipo=" + getEquipo() +
            ", creadaPor=" + getCreadaPor() +
            "}";
    }
}
