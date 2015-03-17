/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitado;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.naming.NamingException;
import unl.edu.bean.loginReg.LoginBean;
import unl.edu.jpa.controllers.RegistraJpaController;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Registra;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@RequestScoped
public class MisCapacitacionesCapacitadoBean{

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;
    private List<Registra> listaRegistroCapacitaciones;
    private final RegistraJpaController registraJpaController;
    private Capacitacion capacitacion;
    private Capacitado capacitado;
    private String activa;

    public MisCapacitacionesCapacitadoBean() throws NamingException {
        registraJpaController = new RegistraJpaController();
        listaRegistroCapacitaciones = new ArrayList<>();
        capacitado = new Capacitado();
        capacitacion = new Capacitacion();
        activa = "";
    }

    public void obtenerCap(Capacitacion c) {
        capacitacion = c;
        if (capacitacion.getActivo()) {
            activa = "activa";
        } else {
            activa = "inactiva";
        }
    }

    public String obtenerAsistencia(Registra registra1) {
        if (registra1.getAsistencia() == false) {
            return "no";
        } else {
            return "si";
        }
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public List<Registra> getListaCapacitaciones() {
        listaRegistroCapacitaciones = new ArrayList<>();
        capacitado = loginBean.getCapacitado();
        listaRegistroCapacitaciones = registraJpaController.registradoLista(capacitado);
        return listaRegistroCapacitaciones;
    }

    public void setListaCapacitaciones(List<Registra> listaCapacitaciones) {
        this.listaRegistroCapacitaciones = listaCapacitaciones;
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public String getActiva() {
        return activa;
    }

    public void setActiva(String activa) {
        this.activa = activa;
    }

}
