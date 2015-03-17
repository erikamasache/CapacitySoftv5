/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitador;

import unl.edu.jpa.controllers.CapacitacionCapacitadoJpaController;
import unl.edu.jpa.controllers.CapacitadoJpaController;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.CapacitacionCapacitado;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Metodos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import unl.edu.bean.loginReg.LoginBean;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class CapacitadosBean implements Serializable {

    private Capacitacion capacitacion;
    private Capacitado capacitado;
    private CapacitadoJpaController capacitadoJpaController;
    private List<Capacitado> listaCapacitados;
    List<CapacitacionCapacitado> lista;
    CapacitacionCapacitado cap;
    CapacitacionCapacitadoJpaController capacitacionCapacitadoJpaController;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    public CapacitadosBean() throws NamingException {
        capacitado = new Capacitado();
        capacitadoJpaController = new CapacitadoJpaController();
        listaCapacitados = new ArrayList<>();
        lista = new ArrayList<>();
        cap = new CapacitacionCapacitado();
        capacitacionCapacitadoJpaController = new CapacitacionCapacitadoJpaController();
    }

    public String obtenerCapacitacion(Capacitacion capacitacion1) throws Exception {
        capacitacion = capacitacion1;
        return "capacitacionesCapacitados?faces-redirect=true";
    }


    public void guardarCapacitado() throws Exception {
        String contraseniaSinEncriptar = "";
        if (capacitadoJpaController.cedulaRepetida(capacitado).isEmpty()) {
            if (capacitadoJpaController.celularRepetido(capacitado).isEmpty()) {
                if (capacitadoJpaController.correoRepetido(capacitado).isEmpty()) {
                    if (capacitado.getTipoIdentificacion() == 1) {
                        if (Metodos.validadorDeCedula(capacitado.getCedula())) {
                            contraseniaSinEncriptar = Metodos.generarContrasenia();
                            capacitado.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                            capacitadoJpaController.create(capacitado);
                            System.out.println("capacitacion " + capacitacion.getId());
                            System.out.println("capacitado " + capacitado.getId());
                            cap.setCapacitacionId(capacitacion);
                            cap.setCapacitadoId(capacitado);
                            capacitacionCapacitadoJpaController.create(cap);
//                            lista.add(cap);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El Capacitado " + capacitado.getNombre()
                                    + " ha sido registrado", null));
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "El número de cédula de " + capacitado.getNombre()
                                    + " es incorrecto", null));
                        }
                    } else {
                        capacitadoJpaController.create(capacitado);
                        System.out.println("capacitacion " + capacitacion.getId());
                        System.out.println("capacitado " + capacitado.getId());
                        cap.setCapacitacionId(capacitacion);
                        cap.setCapacitadoId(capacitado);
                        capacitacionCapacitadoJpaController.create(cap);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "El Capacitado " + capacitado.getNombre()
                                + " ha sido registrado", null));
                    }
                    try {
                        if (Metodos.enviarMail(capacitado.getCorreo(), contraseniaSinEncriptar)) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "La contraseña del capacitado " + capacitado.getNombre() + " " + capacitado.getApellido()
                                    + "ha sido enviada al correo " + capacitado.getCorreo(), null));
                        }
                    } catch (Exception e) {
                        System.out.println("error en la conexion " + e);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Error de conexion, porfavor revice si su conexion a internet esta disponible", null));
                    }
                    capacitado = new Capacitado();
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
        capacitado = new Capacitado();
        cap = new CapacitacionCapacitado();
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public Capacitado getCapacitado() {
        return capacitado;
    }

    public void setCapacitado(Capacitado capacitado) {
        this.capacitado = capacitado;
    }

    public CapacitadoJpaController getCapacitadoJpaController() {
        return capacitadoJpaController;
    }

    public void setCapacitadoJpaController(CapacitadoJpaController capacitadoJpaController) {
        this.capacitadoJpaController = capacitadoJpaController;
    }

    public List<Capacitado> getListaCapacitados() {
        listaCapacitados = new ArrayList<>();
        lista = capacitacion.getCapacitacionCapacitadoList();
        for (int i = 0; i < lista.size(); i++) {
            listaCapacitados.add(lista.get(i).getCapacitadoId());
        }
        return listaCapacitados;
    }

    public void setListaCapacitados(List<Capacitado> listaCapacitados) {
        this.listaCapacitados = listaCapacitados;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

}
