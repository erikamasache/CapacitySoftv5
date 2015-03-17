/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.loginReg;

import unl.edu.jpa.controllers.AdministradorJpaController;
import unl.edu.jpa.controllers.CapacitadoJpaController;
import unl.edu.jpa.controllers.CapacitadorJpaController;
import unl.edu.jpa.entities.Administrador;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Capacitador;
import unl.edu.jpa.entities.Metodos;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private static final Logger log = Logger.getLogger(LogoutBean.class.getName());

    private String correo;
    private String contrasenia;
    private String nombre;
//    private boolean logueado;


    private Administrador adm;
    AdministradorJpaController administradorJpaController;
    private Capacitador capacitador;
    CapacitadorJpaController capacitadorJpaController;
    private Capacitado capacitado;
    CapacitadoJpaController capacitadoJpaController;
    private HttpServletRequest request;

    public LoginBean() throws NamingException {
        adm = new Administrador();
        administradorJpaController = new AdministradorJpaController();
        capacitador = new Capacitador();
        capacitadorJpaController = new CapacitadorJpaController();
        capacitado = new Capacitado();
        capacitadoJpaController = new CapacitadoJpaController();
    }

    public String login() throws Exception {
        String destination = "/login?faces-redirect=true";

        FacesContext context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.login(correo, contrasenia);
            if (request.isUserInRole("Administrador")) {
                if (!administradorJpaController.logueado(correo, Metodos.encripta(contrasenia)).isEmpty()) {
                    adm = administradorJpaController.logueado(correo, Metodos.encripta(contrasenia)).get(0);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Administrador Logueado", null));
                    nombre = adm.getNombre() + " " + adm.getApellido();
                    return "administrador/capacitador?faces-redirect=true";
                }
            } else if (request.isUserInRole("Capacitador")) {
                if (!capacitadorJpaController.logueadoCapacitador(correo, Metodos.encripta(contrasenia)).isEmpty()) {
                    capacitador = capacitadorJpaController.logueadoCapacitador(correo, Metodos.encripta(contrasenia)).get(0);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Capacitador Logueado", null));
                    nombre = capacitador.getNombre() + " " + capacitador.getApellido();
                    return "capacitacion/capacitacion?faces-redirect=true";
                }
            } else if (request.isUserInRole("Capacitado")) {
                if (!capacitadoJpaController.logueadoCapacitado(correo, Metodos.encripta(contrasenia)).isEmpty()) {
                    capacitado = capacitadoJpaController.logueadoCapacitado(correo, Metodos.encripta(contrasenia)).get(0);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Capacitado Logueado", null));
                    nombre = capacitado.getNombre() + " " + capacitado.getApellido();
                    return "capacitado/induccion?faces-redirect=true";
                }
            }
        } catch (ServletException e) {
            log.log(Level.SEVERE, "Failed to login user!", e);
            destination = "/loginerror?faces-redirect=true";
        }

        return destination;

    }

    
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContasenia() {
        return contrasenia;
    }

    public void setContasenia(String contasenia) {
        this.contrasenia = contasenia;
    }

    public Administrador getAdm() {
        return adm;
    }

    public void setAdm(Administrador adm) {
        this.adm = adm;
    }

    public Capacitador getCapacitador() {
        return capacitador;
    }

    public void setCapacitador(Capacitador capacitador) {
        this.capacitador = capacitador;
    }

    public Capacitado getCapacitado() {
        return capacitado;
    }

    public void setCapacitado(Capacitado capacitado) {
        this.capacitado = capacitado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }


}
