package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.TipoRecurrencia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Rango horario programado en el que las alertas de los servicios
 * cubiertos no generan incidente. inicio/fin definen la PRIMERA
 * ocurrencia; si hay recurrencia, se repite con la misma duracion
 * hasta recurrenciaHasta. Las horas se interpretan en zonaHoraria
 * (igual que en Rotacion), asi \"martes 02:00\" sigue siendo 02:00
 * aunque cambie el horario de verano.
 */
@Entity
@Table(name = "ventana_de_mantenimiento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentanaDeMantenimiento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "nombre", length = 60, nullable = false, unique = true)
    private String nombre;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @NotNull
    @Column(name = "inicio", nullable = false)
    private Instant inicio;

    @NotNull
    @Column(name = "fin", nullable = false)
    private Instant fin;

    @NotNull
    @Size(max = 50)
    @Column(name = "zona_horaria", length = 50, nullable = false)
    private String zonaHoraria;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recurrencia", nullable = false)
    private TipoRecurrencia recurrencia;

    @Size(max = 30)
    @Column(name = "dias_semana", length = 30)
    private String diasSemana;

    @Column(name = "recurrencia_hasta")
    private LocalDate recurrenciaHasta;

    @NotNull
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicios", "rotacions", "ventanas" }, allowSetters = true)
    private Equipo equipo;

    @ManyToOne(optional = false)
    @NotNull
    private User creadaPor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ventana")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ventana", "servicio" }, allowSetters = true)
    private Set<ServicioEnVentana> servicioCubiertos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ventana")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ventana", "autorizadaPor" }, allowSetters = true)
    private Set<ExcepcionDeVentana> excepcions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "suprimidaPorVentana")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "servicio", "incidente", "suprimidaPorVentana" }, allowSetters = true)
    private Set<Alerta> alertaSuprimidas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VentanaDeMantenimiento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public VentanaDeMantenimiento nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public VentanaDeMantenimiento descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getInicio() {
        return this.inicio;
    }

    public VentanaDeMantenimiento inicio(Instant inicio) {
        this.setInicio(inicio);
        return this;
    }

    public void setInicio(Instant inicio) {
        this.inicio = inicio;
    }

    public Instant getFin() {
        return this.fin;
    }

    public VentanaDeMantenimiento fin(Instant fin) {
        this.setFin(fin);
        return this;
    }

    public void setFin(Instant fin) {
        this.fin = fin;
    }

    public String getZonaHoraria() {
        return this.zonaHoraria;
    }

    public VentanaDeMantenimiento zonaHoraria(String zonaHoraria) {
        this.setZonaHoraria(zonaHoraria);
        return this;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public TipoRecurrencia getRecurrencia() {
        return this.recurrencia;
    }

    public VentanaDeMantenimiento recurrencia(TipoRecurrencia recurrencia) {
        this.setRecurrencia(recurrencia);
        return this;
    }

    public void setRecurrencia(TipoRecurrencia recurrencia) {
        this.recurrencia = recurrencia;
    }

    public String getDiasSemana() {
        return this.diasSemana;
    }

    public VentanaDeMantenimiento diasSemana(String diasSemana) {
        this.setDiasSemana(diasSemana);
        return this;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

    public LocalDate getRecurrenciaHasta() {
        return this.recurrenciaHasta;
    }

    public VentanaDeMantenimiento recurrenciaHasta(LocalDate recurrenciaHasta) {
        this.setRecurrenciaHasta(recurrenciaHasta);
        return this;
    }

    public void setRecurrenciaHasta(LocalDate recurrenciaHasta) {
        this.recurrenciaHasta = recurrenciaHasta;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public VentanaDeMantenimiento activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Equipo getEquipo() {
        return this.equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public VentanaDeMantenimiento equipo(Equipo equipo) {
        this.setEquipo(equipo);
        return this;
    }

    public User getCreadaPor() {
        return this.creadaPor;
    }

    public void setCreadaPor(User user) {
        this.creadaPor = user;
    }

    public VentanaDeMantenimiento creadaPor(User user) {
        this.setCreadaPor(user);
        return this;
    }

    public Set<ServicioEnVentana> getServicioCubiertos() {
        return this.servicioCubiertos;
    }

    public void setServicioCubiertos(Set<ServicioEnVentana> servicioEnVentanas) {
        if (this.servicioCubiertos != null) {
            this.servicioCubiertos.forEach(i -> i.setVentana(null));
        }
        if (servicioEnVentanas != null) {
            servicioEnVentanas.forEach(i -> i.setVentana(this));
        }
        this.servicioCubiertos = servicioEnVentanas;
    }

    public VentanaDeMantenimiento servicioCubiertos(Set<ServicioEnVentana> servicioEnVentanas) {
        this.setServicioCubiertos(servicioEnVentanas);
        return this;
    }

    public VentanaDeMantenimiento addServicioCubierto(ServicioEnVentana servicioEnVentana) {
        this.servicioCubiertos.add(servicioEnVentana);
        servicioEnVentana.setVentana(this);
        return this;
    }

    public VentanaDeMantenimiento removeServicioCubierto(ServicioEnVentana servicioEnVentana) {
        this.servicioCubiertos.remove(servicioEnVentana);
        servicioEnVentana.setVentana(null);
        return this;
    }

    public Set<ExcepcionDeVentana> getExcepcions() {
        return this.excepcions;
    }

    public void setExcepcions(Set<ExcepcionDeVentana> excepcionDeVentanas) {
        if (this.excepcions != null) {
            this.excepcions.forEach(i -> i.setVentana(null));
        }
        if (excepcionDeVentanas != null) {
            excepcionDeVentanas.forEach(i -> i.setVentana(this));
        }
        this.excepcions = excepcionDeVentanas;
    }

    public VentanaDeMantenimiento excepcions(Set<ExcepcionDeVentana> excepcionDeVentanas) {
        this.setExcepcions(excepcionDeVentanas);
        return this;
    }

    public VentanaDeMantenimiento addExcepcion(ExcepcionDeVentana excepcionDeVentana) {
        this.excepcions.add(excepcionDeVentana);
        excepcionDeVentana.setVentana(this);
        return this;
    }

    public VentanaDeMantenimiento removeExcepcion(ExcepcionDeVentana excepcionDeVentana) {
        this.excepcions.remove(excepcionDeVentana);
        excepcionDeVentana.setVentana(null);
        return this;
    }

    public Set<Alerta> getAlertaSuprimidas() {
        return this.alertaSuprimidas;
    }

    public void setAlertaSuprimidas(Set<Alerta> alertas) {
        if (this.alertaSuprimidas != null) {
            this.alertaSuprimidas.forEach(i -> i.setSuprimidaPorVentana(null));
        }
        if (alertas != null) {
            alertas.forEach(i -> i.setSuprimidaPorVentana(this));
        }
        this.alertaSuprimidas = alertas;
    }

    public VentanaDeMantenimiento alertaSuprimidas(Set<Alerta> alertas) {
        this.setAlertaSuprimidas(alertas);
        return this;
    }

    public VentanaDeMantenimiento addAlertaSuprimida(Alerta alerta) {
        this.alertaSuprimidas.add(alerta);
        alerta.setSuprimidaPorVentana(this);
        return this;
    }

    public VentanaDeMantenimiento removeAlertaSuprimida(Alerta alerta) {
        this.alertaSuprimidas.remove(alerta);
        alerta.setSuprimidaPorVentana(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentanaDeMantenimiento)) {
            return false;
        }
        return getId() != null && getId().equals(((VentanaDeMantenimiento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentanaDeMantenimiento{" +
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
            "}";
    }
}
