/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.loginReg;

import unl.edu.jpa.controllers.CapacitadoJpaController;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Metodos;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import unl.edu.bean.rol.RolBean;
import unl.edu.jpa.controllers.AdministradorJpaController;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class RegistrarseBean implements Serializable {

    private String clave;
    private String correo;
    private Capacitado capacitado;
    private CapacitadoJpaController capacitadoJpaController;
    private AdministradorJpaController administradorJpaController;

    @ManagedProperty("#{rolBean}")
    private RolBean rolBean;
    
    public RegistrarseBean() throws NamingException {
        capacitado = new Capacitado();
        capacitadoJpaController = new CapacitadoJpaController();
        administradorJpaController = new AdministradorJpaController();        
    }

    public void guardarCapacitado() throws Exception {
        String contraseniaSinEncriptar = "";
        if (clave.equals(administradorJpaController.findAdministradorEntities().get(0).getClaveAcceso())) {
            if (capacitadoJpaController.cedulaRepetida(capacitado).isEmpty()) {
                if (capacitadoJpaController.celularRepetido(capacitado).isEmpty()) {
                    if (capacitadoJpaController.correoRepetido(capacitado).isEmpty()) {
                        if (capacitado.getTipoIdentificacion() == 1) {
                            if (Metodos.validadorDeCedula(capacitado.getCedula())) {
                                contraseniaSinEncriptar = capacitado.getContrasenia();
                                capacitado.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                                capacitadoJpaController.create(capacitado);
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                        "El Capacitado " + capacitado.getNombre()
                                        + " ha sido creado", null));
                                rolBean.nuevoUsuario(capacitado.getCorreo(), capacitado.getContrasenia(),"capacitado");
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "El número de cédula de " + capacitado.getNombre()
                                        + " es incorrecto", null));
                            }
                        } else {
                            contraseniaSinEncriptar = capacitado.getContrasenia();
                            capacitado.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                            capacitadoJpaController.create(capacitado);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El Capacitado " + capacitado.getNombre()
                                    + " ha sido creado", null));
                            rolBean.nuevoUsuario(capacitado.getCorreo(), capacitado.getContrasenia(),"capacitado");
                        }                        
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "El correo ingresado ya existe, ingrese uno nuevo", null));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "El número de celular ingresado ya existe, ingrese uno nuevo", null));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "La cédula ingresada ya existe", null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Contraseña de acceso incorrecta", null));
        }
        
        capacitado = new Capacitado();
    }

    public void recuperarContrasenia() {
        List<Capacitado> listaCapacitados = capacitadoJpaController.correoRepetido1(correo);
        int id;
        if (listaCapacitados.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Usted no se encuentra registrado en el sistema", null));
        } else {
            id = listaCapacitados.get(0).getId();
            System.out.println("hola"+id);
            capacitado = capacitadoJpaController.findCapacitado(id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "nose como recuperar jajaja", null));
        }
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Capacitado getCapacitado() {
        return capacitado;
    }

    public void setCapacitado(Capacitado capacitado) {
        this.capacitado = capacitado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public RolBean getRolBean() {
        return rolBean;
    }

    public void setRolBean(RolBean rolBean) {
        this.rolBean = rolBean;
    }

}
