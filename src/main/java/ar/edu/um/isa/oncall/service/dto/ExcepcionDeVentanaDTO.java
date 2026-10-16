package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionVentana;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.ExcepcionDeVentana} entity.
 */
@Schema(
    description = "Modifica UNA ocurrencia puntual de una ventana recurrente:\ncancelarla, moverla o extenderla. Es el equivalente al\nTurnoDeGuardia con esReemplazo de las rotaciones."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExcepcionDeVentanaDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fechaOcurrencia;

    @NotNull
    private TipoExcepcionVentana tipo;

    private Instant nuevoInicio;

    private Instant nuevoFin;

    @NotNull
    @Size(max = 255)
    private String motivo;

    @NotNull
    private Instant creadaEn;

    @NotNull
    private VentanaDeMantenimientoDTO ventana;

    private UserDTO autorizadaPor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaOcurrencia() {
        return fechaOcurrencia;
    }

    public void setFechaOcurrencia(LocalDate fechaOcurrencia) {
        this.fechaOcurrencia = fechaOcurrencia;
    }

    public TipoExcepcionVentana getTipo() {
        return tipo;
    }

    public void setTipo(TipoExcepcionVentana tipo) {
        this.tipo = tipo;
    }

    public Instant getNuevoInicio() {
        return nuevoInicio;
    }

    public void setNuevoInicio(Instant nuevoInicio) {
        this.nuevoInicio = nuevoInicio;
    }

    public Instant getNuevoFin() {
        return nuevoFin;
    }

    public void setNuevoFin(Instant nuevoFin) {
        this.nuevoFin = nuevoFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Instant getCreadaEn() {
        return creadaEn;
    }

    public void setCreadaEn(Instant creadaEn) {
        this.creadaEn = creadaEn;
    }

    public VentanaDeMantenimientoDTO getVentana() {
        return ventana;
    }

    public void setVentana(VentanaDeMantenimientoDTO ventana) {
        this.ventana = ventana;
    }

    public UserDTO getAutorizadaPor() {
        return autorizadaPor;
    }

    public void setAutorizadaPor(UserDTO autorizadaPor) {
        this.autorizadaPor = autorizadaPor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExcepcionDeVentanaDTO)) {
            return false;
        }

        ExcepcionDeVentanaDTO excepcionDeVentanaDTO = (ExcepcionDeVentanaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, excepcionDeVentanaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExcepcionDeVentanaDTO{" +
            "id=" + getId() +
            ", fechaOcurrencia='" + getFechaOcurrencia() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", nuevoInicio='" + getNuevoInicio() + "'" +
            ", nuevoFin='" + getNuevoFin() + "'" +
            ", motivo='" + getMotivo() + "'" +
            ", creadaEn='" + getCreadaEn() + "'" +
            ", ventana=" + getVentana() +
            ", autorizadaPor=" + getAutorizadaPor() +
            "}";
    }
}
