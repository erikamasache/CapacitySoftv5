/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unl.edu.jpa.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "administrador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Administrador.findAll", query = "SELECT a FROM Administrador a"),
    @NamedQuery(name = "Administrador.findById", query = "SELECT a FROM Administrador a WHERE a.id = :id"),
    @NamedQuery(name = "Administrador.findByNombre", query = "SELECT a FROM Administrador a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Administrador.findByApellido", query = "SELECT a FROM Administrador a WHERE a.apellido = :apellido"),
    @NamedQuery(name = "Administrador.findByTipoIdentificacion", query = "SELECT a FROM Administrador a WHERE a.tipoIdentificacion = :tipoIdentificacion"),
    @NamedQuery(name = "Administrador.findByCedula", query = "SELECT a FROM Administrador a WHERE a.cedula = :cedula"),
    @NamedQuery(name = "Administrador.findByDireccion", query = "SELECT a FROM Administrador a WHERE a.direccion = :direccion"),
    @NamedQuery(name = "Administrador.findByTelefonoDomicilio", query = "SELECT a FROM Administrador a WHERE a.telefonoDomicilio = :telefonoDomicilio"),
    @NamedQuery(name = "Administrador.findByCelular", query = "SELECT a FROM Administrador a WHERE a.celular = :celular"),
    @NamedQuery(name = "Administrador.findByCorreo", query = "SELECT a FROM Administrador a WHERE a.correo = :correo"),
    @NamedQuery(name = "Administrador.findByCargo", query = "SELECT a FROM Administrador a WHERE a.cargo = :cargo"),
    @NamedQuery(name = "Administrador.findByTelefonoOficina", query = "SELECT a FROM Administrador a WHERE a.telefonoOficina = :telefonoOficina"),
    @NamedQuery(name = "Administrador.findByExtTelefonoOficina", query = "SELECT a FROM Administrador a WHERE a.extTelefonoOficina = :extTelefonoOficina"),
    @NamedQuery(name = "Administrador.findByContrasenia", query = "SELECT a FROM Administrador a WHERE a.contrasenia = :contrasenia"),
    @NamedQuery(name = "Administrador.findByClaveAcceso", query = "SELECT a FROM Administrador a WHERE a.claveAcceso = :claveAcceso")})
public class Administrador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 45)
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "tipo_identificacion")
    private Integer tipoIdentificacion;
    @Size(max = 12)
    @Column(name = "cedula")
    private String cedula;
    @Size(max = 45)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 13)
    @Column(name = "telefono_domicilio")
    private String telefonoDomicilio;
    @Size(max = 13)
    @Column(name = "celular")
    private String celular;
    @Size(max = 45)
    @Column(name = "correo")
    private String correo;
    @Size(max = 45)
    @Column(name = "cargo")
    private String cargo;
    @Size(max = 13)
    @Column(name = "telefono_oficina")
    private String telefonoOficina;
    @Column(name = "ext_telefono_oficina")
    private Integer extTelefonoOficina;
    @Size(max = 256)
    @Column(name = "contrasenia")
    private String contrasenia;
    @Size(min = 1, max = 256)
    @Column(name = "clave_acceso")
    private String claveAcceso;
    @OneToMany(mappedBy = "administradorId", cascade = CascadeType.ALL)
    private List<Capacitador> capacitadorList;

    public Administrador() {
    }

    public Administrador(Integer id) {
        this.id = id;
    }

    public Administrador(Integer id, String claveAcceso) {
        this.id = id;
        this.claveAcceso = claveAcceso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(Integer tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefonoDomicilio() {
        return telefonoDomicilio;
    }

    public void setTelefonoDomicilio(String telefonoDomicilio) {
        this.telefonoDomicilio = telefonoDomicilio;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTelefonoOficina() {
        return telefonoOficina;
    }

    public void setTelefonoOficina(String telefonoOficina) {
        this.telefonoOficina = telefonoOficina;
    }

    public Integer getExtTelefonoOficina() {
        return extTelefonoOficina;
    }

    public void setExtTelefonoOficina(Integer extTelefonoOficina) {
        this.extTelefonoOficina = extTelefonoOficina;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    @XmlTransient
    public List<Capacitador> getCapacitadorList() {
        return capacitadorList;
    }

    public void setCapacitadorList(List<Capacitador> capacitadorList) {
        this.capacitadorList = capacitadorList;
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
        if (!(object instanceof Administrador)) {
            return false;
        }
        Administrador other = (Administrador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "unl.edu.jpa.entities.Administrador[ id=" + id + " ]";
    }
    
}
