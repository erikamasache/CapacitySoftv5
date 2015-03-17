/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitador;

import unl.edu.jpa.controllers.EvaluacionCapacitadoJpaController;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.EvaluacionCapacitado;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import org.primefaces.event.CellEditEvent;
import unl.edu.bean.loginReg.LoginBean;
import unl.edu.jpa.controllers.RegistraJpaController;
import unl.edu.jpa.entities.Registra;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class RegistradosBean implements Serializable {

    private Capacitado capacitado;
    private Capacitacion capacitacion;
    private List<Capacitado> listaCapacitados;

    private Registra registra;
    private RegistraJpaController registraJpaController;
    private List<Registra> listaRegistrados;

    private EvaluacionCapacitado evaluacionCapacitado;
    private EvaluacionCapacitadoJpaController evaluacionCapacitadoJpaController;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    public RegistradosBean() throws NamingException {
        listaCapacitados = new ArrayList<>();
        capacitado = new Capacitado();
        registra = new Registra();
        registraJpaController = new RegistraJpaController();
        listaRegistrados = new ArrayList<>();
        evaluacionCapacitado = new EvaluacionCapacitado();
        evaluacionCapacitadoJpaController = new EvaluacionCapacitadoJpaController();
    }

    public String obtenerCapacitacion(Capacitacion capacitacion1) throws Exception {
        capacitacion = capacitacion1;
        if (loginBean.getAdm().getNombre() != null) {
            return "capacitacionesRegistrados?faces-redirect=true";
        } else {
//            return "./../capacitacion/capacitacionesRegistrados?faces-redirect=true";
            return "capacitacionesRegistrados?faces-redirect=true";
        }
    }

    public void guardarAsistencia() throws RollbackFailureException, Exception {
        registraJpaController.edit(registra);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Asistencia guardada", null));
    }
    
    public String obtenerAsistencia(Registra registra1){
        if (registra1.getAsistencia()==false) {
            return "no";
        }else{
            return "si";
        }
    }

    public List<Capacitado> getListaCapacitados() {
        listaRegistrados = registraJpaController.numeroRegistrados(capacitacion);
        listaCapacitados = new ArrayList<>();
        for (int i = 0; i < listaRegistrados.size(); i++) {
            listaCapacitados.add(listaRegistrados.get(i).getCapacitadoId());
        }
        return listaCapacitados;
    }

    public void setListaCapacitados(List<Capacitado> listaCapacitados) {
        this.listaCapacitados = listaCapacitados;
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

    public void onCellEdit(CellEditEvent event) throws RollbackFailureException, Exception {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
//        obj.setAsistencia((String) event.getNewValue()+"1");
//        asistenciaJpaController.edit(obj);
    }
//    public void addMessage() {
//        String summary = registra.getAsistencia() ? "Checked" : "Unchecked";
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
//    }
//    public void onRowEdit(RowEditEvent event) {
//        System.out.println("cambio "+((Asistencia)event.getObject()).getAsistencia());
//        FacesMessage msg = new FacesMessage("Car Edited", ((Asistencia) event.getObject()).getId()+"");
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }

    public EvaluacionCapacitado getEvaluacionCapacitado() {
        return evaluacionCapacitado;
    }

    public void setEvaluacionCapacitado(EvaluacionCapacitado evaluacionCapacitado) {
        this.evaluacionCapacitado = evaluacionCapacitado;
    }

    public Registra getRegistra() {
        return registra;
    }

    public void setRegistra(Registra registra) {
        this.registra = registra;
    }

    public List<Registra> getListaRegistrados() {
        listaRegistrados = registraJpaController.numeroRegistrados(capacitacion);
        return listaRegistrados;
    }

    public void setListaRegistrados(List<Registra> listaRegistrados) {
        this.listaRegistrados = listaRegistrados;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

}
