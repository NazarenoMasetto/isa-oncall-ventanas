package ar.edu.um.isa.oncall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Que servicio cubre una ventana y que se suprime durante ella.
 */
@Entity
@Table(name = "servicio_en_ventana")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioEnVentana implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "suprimir_alertas", nullable = false)
    private Boolean suprimirAlertas;

    @NotNull
    @Column(name = "suprimir_escalamiento", nullable = false)
    private Boolean suprimirEscalamiento;

    @Size(max = 255)
    @Column(name = "notas", length = 255)
    private String notas;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "equipo", "creadaPor", "servicioCubiertos", "excepcions", "alertaSuprimidas" }, allowSetters = true)
    private VentanaDeMantenimiento ventana;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "equipo", "objetivos", "alertas", "politicas", "ventanaAsignadas", "incidentes" }, allowSetters = true)
    private Servicio servicio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServicioEnVentana id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSuprimirAlertas() {
        return this.suprimirAlertas;
    }

    public ServicioEnVentana suprimirAlertas(Boolean suprimirAlertas) {
        this.setSuprimirAlertas(suprimirAlertas);
        return this;
    }

    public void setSuprimirAlertas(Boolean suprimirAlertas) {
        this.suprimirAlertas = suprimirAlertas;
    }

    public Boolean getSuprimirEscalamiento() {
        return this.suprimirEscalamiento;
    }

    public ServicioEnVentana suprimirEscalamiento(Boolean suprimirEscalamiento) {
        this.setSuprimirEscalamiento(suprimirEscalamiento);
        return this;
    }

    public void setSuprimirEscalamiento(Boolean suprimirEscalamiento) {
        this.suprimirEscalamiento = suprimirEscalamiento;
    }

    public String getNotas() {
        return this.notas;
    }

    public ServicioEnVentana notas(String notas) {
        this.setNotas(notas);
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public VentanaDeMantenimiento getVentana() {
        return this.ventana;
    }

    public void setVentana(VentanaDeMantenimiento ventanaDeMantenimiento) {
        this.ventana = ventanaDeMantenimiento;
    }

    public ServicioEnVentana ventana(VentanaDeMantenimiento ventanaDeMantenimiento) {
        this.setVentana(ventanaDeMantenimiento);
        return this;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public ServicioEnVentana servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioEnVentana)) {
            return false;
        }
        return getId() != null && getId().equals(((ServicioEnVentana) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioEnVentana{" +
            "id=" + getId() +
            ", suprimirAlertas='" + getSuprimirAlertas() + "'" +
            ", suprimirEscalamiento='" + getSuprimirEscalamiento() + "'" +
            ", notas='" + getNotas() + "'" +
            "}";
    }
}
