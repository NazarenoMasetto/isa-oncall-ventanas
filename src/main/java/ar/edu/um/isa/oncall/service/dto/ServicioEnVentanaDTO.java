package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.ServicioEnVentana} entity.
 */
@Schema(description = "Que servicio cubre una ventana y que se suprime durante ella.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioEnVentanaDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean suprimirAlertas;

    @NotNull
    private Boolean suprimirEscalamiento;

    @Size(max = 255)
    private String notas;

    @NotNull
    private VentanaDeMantenimientoDTO ventana;

    @NotNull
    private ServicioDTO servicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSuprimirAlertas() {
        return suprimirAlertas;
    }

    public void setSuprimirAlertas(Boolean suprimirAlertas) {
        this.suprimirAlertas = suprimirAlertas;
    }

    public Boolean getSuprimirEscalamiento() {
        return suprimirEscalamiento;
    }

    public void setSuprimirEscalamiento(Boolean suprimirEscalamiento) {
        this.suprimirEscalamiento = suprimirEscalamiento;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public VentanaDeMantenimientoDTO getVentana() {
        return ventana;
    }

    public void setVentana(VentanaDeMantenimientoDTO ventana) {
        this.ventana = ventana;
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = servicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioEnVentanaDTO)) {
            return false;
        }

        ServicioEnVentanaDTO servicioEnVentanaDTO = (ServicioEnVentanaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicioEnVentanaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioEnVentanaDTO{" +
            "id=" + getId() +
            ", suprimirAlertas='" + getSuprimirAlertas() + "'" +
            ", suprimirEscalamiento='" + getSuprimirEscalamiento() + "'" +
            ", notas='" + getNotas() + "'" +
            ", ventana=" + getVentana() +
            ", servicio=" + getServicio() +
            "}";
    }
}
