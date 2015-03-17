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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "evaluacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evaluacion.findAll", query = "SELECT e FROM Evaluacion e"),
    @NamedQuery(name = "Evaluacion.findById", query = "SELECT e FROM Evaluacion e WHERE e.id = :id"),
    @NamedQuery(name = "Evaluacion.findByTema", query = "SELECT e FROM Evaluacion e WHERE e.tema = :tema"),
    @NamedQuery(name = "Evaluacion.findByCalificacion", query = "SELECT e FROM Evaluacion e WHERE e.calificacion = :calificacion"),
    @NamedQuery(name = "Evaluacion.findByDescripcion", query = "SELECT e FROM Evaluacion e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "Evaluacion.findByFechaInicio", query = "SELECT e FROM Evaluacion e WHERE e.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Evaluacion.findByFechaFin", query = "SELECT e FROM Evaluacion e WHERE e.fechaFin = :fechaFin"),
    @NamedQuery(name = "Evaluacion.findByTiempoDuracion", query = "SELECT e FROM Evaluacion e WHERE e.tiempoDuracion = :tiempoDuracion"),
    @NamedQuery(name = "Evaluacion.findByActivo", query = "SELECT e FROM Evaluacion e WHERE e.activo = :activo")})
public class Evaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "tema")
    private String tema;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "calificacion")
    private Double calificacion;
    @Size(max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "tiempo_duracion")
    @Temporal(TemporalType.TIME)
    private Date tiempoDuracion;
    @Column(name = "activo")
    private Boolean activo;
    @OneToMany(mappedBy = "evaluacionId")
    private List<Capacitacion> capacitacionList;
    @OneToMany(mappedBy = "evaluacionId")
    private List<Pregunta> preguntaList;
    @OneToMany(mappedBy = "evaluacionId")
    private List<EvaluacionCapacitado> evaluacionCapacitadoList;

    public Evaluacion() {
    }

    public Evaluacion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getTiempoDuracion() {
        return tiempoDuracion;
    }

    public void setTiempoDuracion(Date tiempoDuracion) {
        this.tiempoDuracion = tiempoDuracion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<Capacitacion> getCapacitacionList() {
        return capacitacionList;
    }

    public void setCapacitacionList(List<Capacitacion> capacitacionList) {
        this.capacitacionList = capacitacionList;
    }

    @XmlTransient
    public List<Pregunta> getPreguntaList() {
        return preguntaList;
    }

    public void setPreguntaList(List<Pregunta> preguntaList) {
        this.preguntaList = preguntaList;
    }

    @XmlTransient
    public List<EvaluacionCapacitado> getEvaluacionCapacitadoList() {
        return evaluacionCapacitadoList;
    }

    public void setEvaluacionCapacitadoList(List<EvaluacionCapacitado> evaluacionCapacitadoList) {
        this.evaluacionCapacitadoList = evaluacionCapacitadoList;
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
        if (!(object instanceof Evaluacion)) {
            return false;
        }
        Evaluacion other = (Evaluacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unl.edu.jpa.entities.Evaluacion[ id=" + id + " ]";
    }
    
}
