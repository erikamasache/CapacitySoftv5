/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitador;

import java.io.Serializable;
import unl.edu.bean.loginReg.LoginBean;
import unl.edu.jpa.controllers.CapacitacionJpaController;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.Capacitador;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.naming.NamingException;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@RequestScoped
public class MisCapacitacionesBean implements Serializable{

    private Capacitador capacitador;
    private Capacitacion capacitacion;
    private CapacitacionJpaController capacitacionJpaController;
    private List<Capacitacion> listaCapacitaciones;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    public MisCapacitacionesBean() throws NamingException {
        capacitacion = new Capacitacion();
        capacitacionJpaController = new CapacitacionJpaController();
        listaCapacitaciones = new ArrayList<>();
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public CapacitacionJpaController getCapacitacionJpaController() {
        return capacitacionJpaController;
    }

    public void setCapacitacionJpaController(CapacitacionJpaController capacitacionJpaController) {
        this.capacitacionJpaController = capacitacionJpaController;
    }

    public List<Capacitacion> getListaCapacitaciones() {

        listaCapacitaciones = new ArrayList<>();
        List<Capacitacion> lista = new ArrayList<>();
        lista = capacitacionJpaController.findCapacitacionEntities();
            capacitador = new Capacitador();
            capacitador = loginBean.getCapacitador();
            int id = capacitador.getId();
            for (int i = 0; i < lista.size(); i++) {
                for (int j = 0; j < lista.get(i).getCapacitacionCapacitadorList().size(); j++) {
                    if (lista.get(i).getCapacitacionCapacitadorList().get(j).getCapacitadorId().getId() == id) {
                        listaCapacitaciones.add(lista.get(i));
                    }
                }
            }

        return listaCapacitaciones;
    }

    public void setListaCapacitaciones(List<Capacitacion> listaCapacitaciones) {
        this.listaCapacitaciones = listaCapacitaciones;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

}
