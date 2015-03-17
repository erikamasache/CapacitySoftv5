/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import unl.edu.jpa.controllers.PreguntaJpaController;
import unl.edu.jpa.entities.Evaluacion;
import unl.edu.jpa.entities.Pregunta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class PreguntaBean implements Serializable {

    private Pregunta pregunta;
    private PreguntaJpaController preguntaJpaController;
    private Evaluacion evaluacion;
    private List<Pregunta> listaPreguntas;

    @ManagedProperty("#{respuestaBean}")
    private RespuestaBean respuestaBean;

    public PreguntaBean() throws NamingException {
        pregunta = new Pregunta();
        preguntaJpaController = new PreguntaJpaController();
        evaluacion = new Evaluacion();
    }
    
        
    public void instanciarPregunta() throws NamingException{
        pregunta = new Pregunta();
    }

    public String obtenerEvaluacion(Evaluacion eval) throws Exception {
        evaluacion = eval;
        return "pregunta?faces-redirect=true";
    }

    public String obtenerEvaluacion() throws Exception {
        return "pregunta?faces-redirect=true";
    }

    public void guardarPregunta() throws Exception {
        pregunta.setEvaluacionId(evaluacion);
        preguntaJpaController.create(pregunta);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "La pregunta ha sido creada correctamente", null));
        pregunta = new Pregunta();
    }

    public void editarPregunta() throws RollbackFailureException, Exception {
        preguntaJpaController.edit(pregunta);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "La pregunta ha sido modificada", null));
    }

    public void eliminarPregunta() throws RollbackFailureException, Exception {
        preguntaJpaController.destroy(pregunta.getId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "La pregunta ha sido eliminada", null));
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public Evaluacion getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
    }

    public List<Pregunta> getListaPreguntas() {
        List<Pregunta> aux = preguntaJpaController.findPreguntaEntities();
        listaPreguntas = new ArrayList<>();
        for (int i = 0; i < aux.size(); i++) {
            if (evaluacion.equals(aux.get(i).getEvaluacionId())) {
                listaPreguntas.add(aux.get(i));
            }
        }
        return listaPreguntas;
    }

    public void setListaPreguntas(List<Pregunta> ListaPreguntas) {
        this.listaPreguntas = ListaPreguntas;
    }

    public RespuestaBean getRespuestaBean() {
        return respuestaBean;
    }

    public void setRespuestaBean(RespuestaBean respuestaBean) {
        this.respuestaBean = respuestaBean;
    }

}
