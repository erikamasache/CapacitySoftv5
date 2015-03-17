/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitador;

import unl.edu.jpa.controllers.CapacitacionJpaController;
import unl.edu.jpa.controllers.EvaluacionJpaController;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.Evaluacion;
import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class EvaluacionCapacitacionBean implements Serializable {

    private Evaluacion evaluacion;
    private final EvaluacionJpaController evaluacionJpaController;
    private Capacitacion capacitacion;
    private final CapacitacionJpaController capacitacionJpaController;
    private Date minFechaI;
    private Date maxFecha;
    private Date minFecha;

    public EvaluacionCapacitacionBean() throws NamingException {
        evaluacion = new Evaluacion();
        evaluacionJpaController = new EvaluacionJpaController();
        capacitacion = new Capacitacion();
        capacitacionJpaController = new CapacitacionJpaController();
//        minFecha = capacitacion.getFechaFin();
    }

    public void obtenerEvaluacion() {
        if (capacitacion.getEvaluacionId() != null) {
            evaluacion = capacitacion.getEvaluacionId();
            maxFecha = evaluacion.getFechaFin();
            minFecha = evaluacion.getFechaFin();
        } else {
            evaluacion = new Evaluacion();
        }

    }

    public Evaluacion getEvaluacion() {
        return evaluacion;
    }

    public void guardarEvaluacion() throws Exception {
        if (evaluacion.getId() == null) {
            evaluacion.setTema(capacitacion.getTema());
            evaluacionJpaController.create(evaluacion);
            capacitacion.setEvaluacionId(evaluacion);
            capacitacionJpaController.edit(capacitacion);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "La evaluación " + evaluacion.getTema()
                            + " ha sido creada", null));
        } else {
            System.out.println("no existe");
            evaluacion.setTema(capacitacion.getTema());
            evaluacionJpaController.edit(evaluacion);
            capacitacion.setEvaluacionId(evaluacion);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "La evaluación " + evaluacion.getTema()
                            + " ha sido modificada", null));
        }
    }

    public void obtenerMaxFechaEval(SelectEvent event) {
        maxFecha = (Date) event.getObject();
    }

    public void obtenerMinFechaEval(SelectEvent event) {
        minFecha = (Date) event.getObject();
    }

    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public Date getMaxFecha() {
        return maxFecha;
    }

    public void setMaxFecha(Date maxFecha) {
        this.maxFecha = maxFecha;
    }

    public Date getMinFecha() {
        return minFecha;
    }

    public void setMinFecha(Date minFecha) {
        this.minFecha = minFecha;
    }

    public Date getMinFechaI() {
        minFechaI = capacitacion.getFechaFin();
        return minFechaI;
    }

    public void setMinFechaI(Date minFechaI) {
        this.minFechaI = minFechaI;
    }

}
