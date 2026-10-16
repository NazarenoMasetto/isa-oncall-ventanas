package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.TipoExcepcionVentana;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Modifica UNA ocurrencia puntual de una ventana recurrente:
 * cancelarla, moverla o extenderla. Es el equivalente al
 * TurnoDeGuardia con esReemplazo de las rotaciones.
 */
@Entity
@Table(name = "excepcion_de_ventana")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExcepcionDeVentana implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_ocurrencia", nullable = false)
    private LocalDate fechaOcurrencia;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoExcepcionVentana tipo;

    @Column(name = "nuevo_inicio")
    private Instant nuevoInicio;

    @Column(name = "nuevo_fin")
    private Instant nuevoFin;

    @NotNull
    @Size(max = 255)
    @Column(name = "motivo", length = 255, nullable = false)
    private String motivo;

    @NotNull
    @Column(name = "creada_en", nullable = false)
    private Instant creadaEn;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "equipo", "creadaPor", "servicioCubiertos", "excepcions", "alertaSuprimidas" }, allowSetters = true)
    private VentanaDeMantenimiento ventana;

    @ManyToOne(fetch = FetchType.LAZY)
    private User autorizadaPor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExcepcionDeVentana id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaOcurrencia() {
        return this.fechaOcurrencia;
    }

    public ExcepcionDeVentana fechaOcurrencia(LocalDate fechaOcurrencia) {
        this.setFechaOcurrencia(fechaOcurrencia);
        return this;
    }

    public void setFechaOcurrencia(LocalDate fechaOcurrencia) {
        this.fechaOcurrencia = fechaOcurrencia;
    }

    public TipoExcepcionVentana getTipo() {
        return this.tipo;
    }

    public ExcepcionDeVentana tipo(TipoExcepcionVentana tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoExcepcionVentana tipo) {
        this.tipo = tipo;
    }

    public Instant getNuevoInicio() {
        return this.nuevoInicio;
    }

    public ExcepcionDeVentana nuevoInicio(Instant nuevoInicio) {
        this.setNuevoInicio(nuevoInicio);
        return this;
    }

    public void setNuevoInicio(Instant nuevoInicio) {
        this.nuevoInicio = nuevoInicio;
    }

    public Instant getNuevoFin() {
        return this.nuevoFin;
    }

    public ExcepcionDeVentana nuevoFin(Instant nuevoFin) {
        this.setNuevoFin(nuevoFin);
        return this;
    }

    public void setNuevoFin(Instant nuevoFin) {
        this.nuevoFin = nuevoFin;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public ExcepcionDeVentana motivo(String motivo) {
        this.setMotivo(motivo);
        return this;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Instant getCreadaEn() {
        return this.creadaEn;
    }

    public ExcepcionDeVentana creadaEn(Instant creadaEn) {
        this.setCreadaEn(creadaEn);
        return this;
    }

    public void setCreadaEn(Instant creadaEn) {
        this.creadaEn = creadaEn;
    }

    public VentanaDeMantenimiento getVentana() {
        return this.ventana;
    }

    public void setVentana(VentanaDeMantenimiento ventanaDeMantenimiento) {
        this.ventana = ventanaDeMantenimiento;
    }

    public ExcepcionDeVentana ventana(VentanaDeMantenimiento ventanaDeMantenimiento) {
        this.setVentana(ventanaDeMantenimiento);
        return this;
    }

    public User getAutorizadaPor() {
        return this.autorizadaPor;
    }

    public void setAutorizadaPor(User user) {
        this.autorizadaPor = user;
    }

    public ExcepcionDeVentana autorizadaPor(User user) {
        this.setAutorizadaPor(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExcepcionDeVentana)) {
            return false;
        }
        return getId() != null && getId().equals(((ExcepcionDeVentana) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExcepcionDeVentana{" +
            "id=" + getId() +
            ", fechaOcurrencia='" + getFechaOcurrencia() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", nuevoInicio='" + getNuevoInicio() + "'" +
            ", nuevoFin='" + getNuevoFin() + "'" +
            ", motivo='" + getMotivo() + "'" +
            ", creadaEn='" + getCreadaEn() + "'" +
            "}";
    }
}
