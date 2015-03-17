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
@Table(name = "registra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registra.findAll", query = "SELECT r FROM Registra r"),
    @NamedQuery(name = "Registra.findById", query = "SELECT r FROM Registra r WHERE r.id = :id"),
    @NamedQuery(name = "Registra.findByFecha", query = "SELECT r FROM Registra r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "Registra.findByHora", query = "SELECT r FROM Registra r WHERE r.hora = :hora"),
    @NamedQuery(name = "Registra.findByAsistencia", query = "SELECT r FROM Registra r WHERE r.asistencia = :asistencia")})
public class Registra implements Serializable {
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
    @Column(name = "asistencia")
    private Boolean asistencia;
    @JoinColumn(name = "capacitado_id", referencedColumnName = "id")
    @ManyToOne
    private Capacitado capacitadoId;
    @JoinColumn(name = "capacitacion_id", referencedColumnName = "id")
    @ManyToOne
    private Capacitacion capacitacionId;

    public Registra() {
    }

    public Registra(Integer id) {
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

    public Boolean getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Boolean asistencia) {
        this.asistencia = asistencia;
    }

    public Capacitado getCapacitadoId() {
        return capacitadoId;
    }

    public void setCapacitadoId(Capacitado capacitadoId) {
        this.capacitadoId = capacitadoId;
    }

    public Capacitacion getCapacitacionId() {
        return capacitacionId;
    }

    public void setCapacitacionId(Capacitacion capacitacionId) {
        this.capacitacionId = capacitacionId;
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
        if (!(object instanceof Registra)) {
            return false;
        }
        Registra other = (Registra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unl.edu.jpa.entities.Registra[ id=" + id + " ]";
    }
    
}
