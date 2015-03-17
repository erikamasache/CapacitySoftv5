/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unl.edu.jpa.entities;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ERIKA
 */
@Entity
@Table(name = "pregunta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pregunta.findAll", query = "SELECT p FROM Pregunta p"),
    @NamedQuery(name = "Pregunta.findById", query = "SELECT p FROM Pregunta p WHERE p.id = :id"),
    @NamedQuery(name = "Pregunta.findByCalificacion", query = "SELECT p FROM Pregunta p WHERE p.calificacion = :calificacion"),
    @NamedQuery(name = "Pregunta.findByDescripcion", query = "SELECT p FROM Pregunta p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Pregunta.findByIdentificador", query = "SELECT p FROM Pregunta p WHERE p.identificador = :identificador"),
    @NamedQuery(name = "Pregunta.findByTipoPregunta", query = "SELECT p FROM Pregunta p WHERE p.tipoPregunta = :tipoPregunta")})
public class Pregunta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "calificacion")
    private Double calificacion;
    @Size(max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 45)
    @Column(name = "identificador")
    private String identificador;
    @Size(max = 45)
    @Column(name = "tipo_pregunta")
    private String tipoPregunta;
    @OneToMany(mappedBy = "preguntaId")
    private List<Respuesta> respuestaList;
    @JoinColumn(name = "evaluacion_id", referencedColumnName = "id")
    @ManyToOne
    private Evaluacion evaluacionId;
    @OneToMany(mappedBy = "preguntaId")
    private List<EvaluacionCapacitado> evaluacionCapacitadoList;

    public Pregunta() {
    }

    public Pregunta(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(String tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    @XmlTransient
    public List<Respuesta> getRespuestaList() {
        return respuestaList;
    }

    public void setRespuestaList(List<Respuesta> respuestaList) {
        this.respuestaList = respuestaList;
    }

    public Evaluacion getEvaluacionId() {
        return evaluacionId;
    }

    public void setEvaluacionId(Evaluacion evaluacionId) {
        this.evaluacionId = evaluacionId;
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
        if (!(object instanceof Pregunta)) {
            return false;
        }
        Pregunta other = (Pregunta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unl.edu.jpa.entities.Pregunta[ id=" + id + " ]";
    }
    
}
