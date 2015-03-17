/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import unl.edu.jpa.controllers.EvaluacionJpaController;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.Evaluacion;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class EvaluacionBean implements Serializable {

    private Evaluacion evaluacion;
    private List<Evaluacion> listaEvaluaciones;
    private EvaluacionJpaController evaluacionJpaController;
     //--- fechas
    private Date maxFecha;
    private Date minFecha;
    
    @ManagedProperty("#{preguntaBean}")
    private PreguntaBean preguntaBean;

    public EvaluacionBean() throws NamingException {
        evaluacion = new Evaluacion();
        evaluacionJpaController = new EvaluacionJpaController();
    }

    public void editarEvaluacionCap(Capacitacion cap, Evaluacion eval) throws RollbackFailureException, Exception {
        Evaluacion ev;
        ev = cap.getEvaluacionId();
        ev.setTema(eval.getTema());
        ev.setFechaInicio(eval.getFechaInicio());
        ev.setFechaFin(eval.getFechaFin());
        ev.setCalificacion(eval.getCalificacion());
        ev.setTiempoDuracion(eval.getTiempoDuracion());
        evaluacionJpaController.edit(ev);
    }
    
    public void eliminarEvaluacion() throws RollbackFailureException, Exception {
        evaluacionJpaController.destroy(evaluacion.getId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "La evaluación ha sido eliminada", null));
    }


    // setters y getters
    public Evaluacion getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
    }

    public List<Evaluacion> getListaEvaluaciones() {
        listaEvaluaciones = evaluacionJpaController.findEvaluacionEntities();
        return listaEvaluaciones;
    }

    public void setListaEvaluaciones(List<Evaluacion> listaEvaluaciones) {
        this.listaEvaluaciones = listaEvaluaciones;
    }

    public PreguntaBean getPreguntaBean() {
        return preguntaBean;
    }

    public void setPreguntaBean(PreguntaBean preguntaBean) {
        this.preguntaBean = preguntaBean;
    }

    public EvaluacionJpaController getEvaluacionJpaController() {
        return evaluacionJpaController;
    }

    public void setEvaluacionJpaController(EvaluacionJpaController evaluacionJpaController) {
        this.evaluacionJpaController = evaluacionJpaController;
    }

    
}
