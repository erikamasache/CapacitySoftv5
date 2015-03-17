/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import unl.edu.jpa.controllers.RespuestaJpaController;
import unl.edu.jpa.entities.Pregunta;
import unl.edu.jpa.entities.Respuesta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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
public class RespuestaBean implements Serializable {

    private Respuesta respuesta;
    private RespuestaJpaController respuestaJpaController;
    private List<Respuesta> listaRespuestas;
    private Pregunta pregunta;
    private String respuestaCorrecta;

    public RespuestaBean() throws NamingException {
        respuesta = new Respuesta();
        respuestaJpaController = new RespuestaJpaController();
        pregunta = new Pregunta();
    }
    
    public void instanciarRespuesta(){
        respuesta = new Respuesta();
    }

    public String obtenerPregunta(Pregunta p) {
        pregunta = p;
        return "respuesta?faces-redirect=true";
    }
    int numeroRespustas;

    public void numeroRespuestas() {
        if (pregunta.getTipoPregunta().equalsIgnoreCase("simple")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Debe crear dos respuestas", null));
            numeroRespustas = 2;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Debe crear mas de dos respuestas", null));
            numeroRespustas = 4;
        }

    }

    public void guardarRespuesta() throws Exception {
        List<Respuesta> obtenerRespuestas = respuestaJpaController.findRespuestaEntities();
        List<Respuesta> listaRespuestasGuardar = new ArrayList<>();
        for (int i = 0; i < obtenerRespuestas.size(); i++) {
            if (obtenerRespuestas.get(i).getPreguntaId().equals(pregunta)) {
                listaRespuestasGuardar.add(obtenerRespuestas.get(i));
            }
        }
        if (listaRespuestasGuardar.size() < numeroRespustas) {
            respuesta.setPreguntaId(pregunta);
            respuestaJpaController.create(respuesta);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "La respuesta ha sido creada", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Ya no puede agregar mas respuestas", null));
        }
        respuesta = new Respuesta();
    }

    public void editarRespuesta() throws RollbackFailureException, Exception {
        respuestaJpaController.edit(respuesta);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "La respuesta ha sido modificado", null));
    }

    public void eliminarRespuesta() throws RollbackFailureException, Exception {
        respuestaJpaController.destroy(respuesta.getId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "La respuesta ha sido eliminada", null));
    }

    public void obtenerRespuesta(Respuesta resp) {
        if (resp.getCorrecto() == true) {
            respuestaCorrecta = "correcta";
        } else {
            respuestaCorrecta = "incorrecta";
        }
    }

    public void addMessage() {
        String summary = respuesta.getCorrecto() ? "Opcion correcta" : "Opcion incorrecta";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    // setters y getters
    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    public List<Respuesta> getListaRespuestas() {
        List<Respuesta> aux = respuestaJpaController.findRespuestaEntities();
        listaRespuestas = new ArrayList<>();
        for (int i = 0; i < aux.size(); i++) {
            if (pregunta.equals(aux.get(i).getPreguntaId())) {
                listaRespuestas.add(aux.get(i));
            }
        }
        return listaRespuestas;
    }

    public void setListaRespuestas(List<Respuesta> listaRespuestas) {
        this.listaRespuestas = listaRespuestas;
    }

}
