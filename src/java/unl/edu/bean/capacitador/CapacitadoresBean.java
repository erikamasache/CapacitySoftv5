/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitador;

import unl.edu.jpa.controllers.CapacitadorJpaController;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.CapacitacionCapacitador;
import unl.edu.jpa.entities.Capacitador;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.naming.NamingException;
import unl.edu.bean.loginReg.LoginBean;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class CapacitadoresBean implements Serializable {

    private Capacitacion capacitacion;
    private final CapacitadorJpaController capacitadorJpaController;
    private List<Capacitador> listaCapacitadores;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    public CapacitadoresBean() throws NamingException {
        capacitadorJpaController = new CapacitadorJpaController();
        listaCapacitadores = new ArrayList<>();
    }

    public String obtenerCapacitacion(Capacitacion capacitacion1) throws Exception {
        capacitacion = capacitacion1;
        return "capacitacionesCapacitadores?faces-redirect=true";

    }

    public List<Capacitador> getListaCapacitadores() {
        listaCapacitadores = new ArrayList<>();
        List<CapacitacionCapacitador> lista = capacitacion.getCapacitacionCapacitadorList();
        for (int i = 0; i < lista.size(); i++) {
            listaCapacitadores.add(lista.get(i).getCapacitadorId());
        }
        return listaCapacitadores;
    }

    public void setListaCapacitadores(List<Capacitador> listaCapacitadores) {
        this.listaCapacitadores = listaCapacitadores;
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

}
