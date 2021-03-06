/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unl.edu.jpa.entities;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ERIKA
 */
@Entity
@Table(name = "capacitacion_capacitado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CapacitacionCapacitado.findAll", query = "SELECT c FROM CapacitacionCapacitado c"),
    @NamedQuery(name = "CapacitacionCapacitado.findById", query = "SELECT c FROM CapacitacionCapacitado c WHERE c.id = :id")})
public class CapacitacionCapacitado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "capacitado_id", referencedColumnName = "id")
    @ManyToOne
    private Capacitado capacitadoId;
    @JoinColumn(name = "capacitacion_id", referencedColumnName = "id")
    @ManyToOne
    private Capacitacion capacitacionId;

    public CapacitacionCapacitado() {
    }

    public CapacitacionCapacitado(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof CapacitacionCapacitado)) {
            return false;
        }
        CapacitacionCapacitado other = (CapacitacionCapacitado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unl.edu.jpa.entities.CapacitacionCapacitado[ id=" + id + " ]";
    }
    
}
