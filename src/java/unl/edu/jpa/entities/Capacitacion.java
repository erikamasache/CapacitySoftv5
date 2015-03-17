/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unl.edu.jpa.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ERIKA
 */
@Entity
@Table(name = "capacitacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Capacitacion.findAll", query = "SELECT c FROM Capacitacion c"),
    @NamedQuery(name = "Capacitacion.findById", query = "SELECT c FROM Capacitacion c WHERE c.id = :id"),
    @NamedQuery(name = "Capacitacion.findByCodigo", query = "SELECT c FROM Capacitacion c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "Capacitacion.findByTema", query = "SELECT c FROM Capacitacion c WHERE c.tema = :tema"),
    @NamedQuery(name = "Capacitacion.findByLugar", query = "SELECT c FROM Capacitacion c WHERE c.lugar = :lugar"),
    @NamedQuery(name = "Capacitacion.findByFinesSemana", query = "SELECT c FROM Capacitacion c WHERE c.finesSemana = :finesSemana"),
    @NamedQuery(name = "Capacitacion.findByFechaInicio", query = "SELECT c FROM Capacitacion c WHERE c.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Capacitacion.findByFechaFin", query = "SELECT c FROM Capacitacion c WHERE c.fechaFin = :fechaFin"),
    @NamedQuery(name = "Capacitacion.findByHoraInicio", query = "SELECT c FROM Capacitacion c WHERE c.horaInicio = :horaInicio"),
    @NamedQuery(name = "Capacitacion.findByHoraFin", query = "SELECT c FROM Capacitacion c WHERE c.horaFin = :horaFin"),
    @NamedQuery(name = "Capacitacion.findByActivo", query = "SELECT c FROM Capacitacion c WHERE c.activo = :activo"),
    @NamedQuery(name = "Capacitacion.findByNumMaxCapacitados", query = "SELECT c FROM Capacitacion c WHERE c.numMaxCapacitados = :numMaxCapacitados"),
    @NamedQuery(name = "Capacitacion.findByObservaciones", query = "SELECT c FROM Capacitacion c WHERE c.observaciones = :observaciones")})
public class Capacitacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 90)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 255)
    @Column(name = "tema")
    private String tema;
    @Size(max = 45)
    @Column(name = "lugar")
    private String lugar;
    @Column(name = "fines_semana")
    private Boolean finesSemana;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "hora_inicio")
    @Temporal(TemporalType.TIME)
    private Date horaInicio;
    @Column(name = "hora_fin")
    @Temporal(TemporalType.TIME)
    private Date horaFin;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "num_max_capacitados")
    private Integer numMaxCapacitados;
    @Size(max = 255)
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "sector_id", referencedColumnName = "id")
    @ManyToOne
    private Sector sectorId;
    @JoinColumn(name = "evaluacion_id", referencedColumnName = "id")
    @ManyToOne
    private Evaluacion evaluacionId;
    @OneToMany(mappedBy = "capacitacionId", cascade = CascadeType.ALL)
    private List<CapacitacionCapacitador> capacitacionCapacitadorList;
    @OneToMany(mappedBy = "capacitacionId", cascade = CascadeType.ALL)
    private List<Registra> registraList;
    @OneToMany(mappedBy = "capacitacionId", cascade = CascadeType.ALL)
    private List<Recurso> recursoList;
    @OneToMany(mappedBy = "capacitacionId", cascade = CascadeType.ALL)
    private List<CapacitacionCapacitado> capacitacionCapacitadoList;

    public Capacitacion() {
    }

    public Capacitacion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Boolean getFinesSemana() {
        return finesSemana;
    }

    public void setFinesSemana(Boolean finesSemana) {
        this.finesSemana = finesSemana;
    }

    public Date getFechaInicio() {
        if (horaInicio!=null) {
            fechaInicio.setHours(horaInicio.getHours());
            fechaInicio.setMinutes(horaInicio.getMinutes());
        }
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        if (horaInicio!=null) {
            fechaFin.setHours(horaFin.getHours());
            fechaFin.setMinutes(horaFin.getMinutes());
        }
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getNumMaxCapacitados() {
        return numMaxCapacitados;
    }

    public void setNumMaxCapacitados(Integer numMaxCapacitados) {
        this.numMaxCapacitados = numMaxCapacitados;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Sector getSectorId() {
        return sectorId;
    }

    public void setSectorId(Sector sectorId) {
        this.sectorId = sectorId;
    }

    public Evaluacion getEvaluacionId() {
        return evaluacionId;
    }

    public void setEvaluacionId(Evaluacion evaluacionId) {
        this.evaluacionId = evaluacionId;
    }

    @XmlTransient
    public List<CapacitacionCapacitador> getCapacitacionCapacitadorList() {
        return capacitacionCapacitadorList;
    }

    public void setCapacitacionCapacitadorList(List<CapacitacionCapacitador> capacitacionCapacitadorList) {
        this.capacitacionCapacitadorList = capacitacionCapacitadorList;
    }

    @XmlTransient
    public List<Registra> getRegistraList() {
        return registraList;
    }

    public void setRegistraList(List<Registra> registraList) {
        this.registraList = registraList;
    }

    @XmlTransient
    public List<Recurso> getRecursoList() {
        return recursoList;
    }

    public void setRecursoList(List<Recurso> recursoList) {
        this.recursoList = recursoList;
    }

    @XmlTransient
    public List<CapacitacionCapacitado> getCapacitacionCapacitadoList() {
        return capacitacionCapacitadoList;
    }

    public void setCapacitacionCapacitadoList(List<CapacitacionCapacitado> capacitacionCapacitadoList) {
        this.capacitacionCapacitadoList = capacitacionCapacitadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Capacitacion)) {
            return false;
        }
        Capacitacion other = (Capacitacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unl.edu.jpa.entities.Capacitacion[ id=" + id + " ]";
    }
    
}
