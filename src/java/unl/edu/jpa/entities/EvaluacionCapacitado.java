/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unl.edu.jpa.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ERIKA
 */
@Entity
@Table(name = "evaluacion_capacitado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionCapacitado.findAll", query = "SELECT e FROM EvaluacionCapacitado e"),
    @NamedQuery(name = "EvaluacionCapacitado.findById", query = "SELECT e FROM EvaluacionCapacitado e WHERE e.id = :id"),
    @NamedQuery(name = "EvaluacionCapacitado.findByFecha", query = "SELECT e FROM EvaluacionCapacitado e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "EvaluacionCapacitado.findByHora", query = "SELECT e FROM EvaluacionCapacitado e WHERE e.hora = :hora"),
    @NamedQuery(name = "EvaluacionCapacitado.findByCalificacionEstudiante", query = "SELECT e FROM EvaluacionCapacitado e WHERE e.calificacionEstudiante = :calificacionEstudiante")})
public class EvaluacionCapacitado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "calificacion_estudiante")
    private Double calificacionEstudiante;
    @JoinColumn(name = "evaluacion_id", referencedColumnName = "id")
    @ManyToOne
    private Evaluacion evaluacionId;
    @JoinColumn(name = "respuesta_id", referencedColumnName = "id")
    @ManyToOne
    private Respuesta respuestaId;
    @JoinColumn(name = "pregunta_id", referencedColumnName = "id")
    @ManyToOne
    private Pregunta preguntaId;
    @JoinColumn(name = "capacitado_id", referencedColumnName = "id")
    @ManyToOne
    private Capacitado capacitadoId;

    public EvaluacionCapacitado() {
    }

    public EvaluacionCapacitado(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Double getCalificacionEstudiante() {
        return calificacionEstudiante;
    }

    public void setCalificacionEstudiante(Double calificacionEstudiante) {
        this.calificacionEstudiante = calificacionEstudiante;
    }

    public Evaluacion getEvaluacionId() {
        return evaluacionId;
    }

    public void setEvaluacionId(Evaluacion evaluacionId) {
        this.evaluacionId = evaluacionId;
    }

    public Respuesta getRespuestaId() {
        return respuestaId;
    }

    public void setRespuestaId(Respuesta respuestaId) {
        this.respuestaId = respuestaId;
    }

    public Pregunta getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Pregunta preguntaId) {
        this.preguntaId = preguntaId;
    }

    public Capacitado getCapacitadoId() {
        return capacitadoId;
    }

    public void setCapacitadoId(Capacitado capacitadoId) {
        this.capacitadoId = capacitadoId;
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
        if (!(object instanceof EvaluacionCapacitado)) {
            return false;
        }
        EvaluacionCapacitado other = (EvaluacionCapacitado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unl.edu.jpa.entities.EvaluacionCapacitado[ id=" + id + " ]";
    }
    
}
