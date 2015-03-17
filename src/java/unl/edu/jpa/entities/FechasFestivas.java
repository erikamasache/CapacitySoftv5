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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ERIKA
 */
@Entity
@Table(name = "fechas_festivas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FechasFestivas.findAll", query = "SELECT f FROM FechasFestivas f"),
    @NamedQuery(name = "FechasFestivas.findById", query = "SELECT f FROM FechasFestivas f WHERE f.id = :id"),
    @NamedQuery(name = "FechasFestivas.findByAcontecimiento", query = "SELECT f FROM FechasFestivas f WHERE f.acontecimiento = :acontecimiento"),
    @NamedQuery(name = "FechasFestivas.findByFechaInicio", query = "SELECT f FROM FechasFestivas f WHERE f.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "FechasFestivas.findByFechaFin", query = "SELECT f FROM FechasFestivas f WHERE f.fechaFin = :fechaFin"),
    @NamedQuery(name = "FechasFestivas.findByHoraInicio", query = "SELECT f FROM FechasFestivas f WHERE f.horaInicio = :horaInicio"),
    @NamedQuery(name = "FechasFestivas.findByHoraFin", query = "SELECT f FROM FechasFestivas f WHERE f.horaFin = :horaFin")})
public class FechasFestivas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "acontecimiento")
    private String acontecimiento;
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

    public FechasFestivas() {
    }

    public FechasFestivas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAcontecimiento() {
        return acontecimiento;
    }

    public void setAcontecimiento(String acontecimiento) {
        this.acontecimiento = acontecimiento;
    }

    public Date getFechaInicio() {
        if (horaInicio != null) {
            fechaInicio.setHours(horaInicio.getHours());
            fechaInicio.setMinutes(horaInicio.getMinutes());
        }
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        if (horaInicio != null) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FechasFestivas)) {
            return false;
        }
        FechasFestivas other = (FechasFestivas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unl.edu.jpa.entities.FechasFestivas[ id=" + id + " ]";
    }

}
