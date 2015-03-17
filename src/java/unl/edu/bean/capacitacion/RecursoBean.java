/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import unl.edu.jpa.controllers.RecursoJpaController;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.Recurso;
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
public class RecursoBean implements Serializable {

    private Capacitacion capacitacion;
    private Recurso recurso;
    private RecursoJpaController recursoJpaController;
    private List<Recurso> listaVideos;
    private List<Recurso> listaDocumentos;
    private String enlace;

    @ManagedProperty("#{calendarioBean}")
    private CalendarioBean calendarioBean;

    public RecursoBean() throws NamingException {
        recurso = new Recurso();
        recursoJpaController = new RecursoJpaController();
        listaDocumentos = new ArrayList<>();
        listaVideos = new ArrayList<>();
    }

    public String obtenerCapacitacion(Capacitacion capacitacion1) throws Exception {
        capacitacion = capacitacion1;
        return "recursoCapacitado?faces-redirect=true";
    }

    public void guardarRecurso() throws Exception {
        if (calendarioBean.getCapacitacion().getId() != null) {
            if (recurso.getPathPdf() == null) {
                if (recurso.getPathVideo().length() >= 43) {
                    if (recurso.getPathVideo().substring(8, 24).equals("www.youtube.com/")) {
                        enlace = recurso.getPathVideo().substring(32, 43);
                        if (recursoJpaController.videoRepetido(enlace, calendarioBean.getCapacitacion()).isEmpty()) {
                            recurso.setPathVideo(enlace);
                            recurso.setCapacitacionId(calendarioBean.getCapacitacion());
                            recursoJpaController.create(recurso);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El recurso ha sido creado", null));
                            recurso = new Recurso();
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El video esta repetido", null));
                        }

                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Enlace inválido", null));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Enlace inválido", null));
                }
            } else {
                if (recurso.getPathVideo() == null) {
                    if (recurso.getPathPdf().length() >= 43) {
                        System.out.println(recurso.getPathPdf().substring(0, 20));
                        if (recurso.getPathPdf().substring(0, 21).equals("http://es.scribd.com/") || recurso.getPathPdf().substring(0, 22).equals("https://es.scribd.com/")) {
                            recurso.setPathVideo(recurso.getPathVideo());
                            recurso.setCapacitacionId(calendarioBean.getCapacitacion());
                            recursoJpaController.create(recurso);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El recurso ha sido creado", null));
                            recurso = new Recurso();
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Enlace inválido", null));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Enlace inválido", null));
                    }
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Guarde la capacitacion antes de asignar recursos", null));
        }
        recurso = new Recurso();
    }

    public void editarRecurso() throws RollbackFailureException, Exception {
        recursoJpaController.edit(recurso);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "El recurso ha sido modificado", null));
    }

    public void eliminarRecurso() throws RollbackFailureException, Exception {
        recursoJpaController.destroy(recurso.getId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "El recurso ha sido eliminado", null));
    }
    
    public String obtenerEnlace(){
        enlace = "https://www.youtube.com/v/"+this.recurso.getPathVideo();
        return enlace;
    }

    // setters y getters
    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public List<Recurso> getListaVideos() {
        List<Recurso> listaVideos2;
        listaVideos = new ArrayList<>();
        listaVideos2 = recursoJpaController.findRecursoEntities();
        for (int i = 0; i < listaVideos2.size(); i++) {
            if (capacitacion.equals(listaVideos2.get(i).getCapacitacionId())) {
                if (listaVideos2.get(i).getPathPdf() == null) {
                    listaVideos.add(listaVideos2.get(i));
                }
            }
        }
        return listaVideos;
    }

    public void setListaVideos(List<Recurso> listaVideos) {
        this.listaVideos = listaVideos;
    }

    public List<Recurso> getListaDocumentos() {
        List<Recurso> listaDocuemntos2;
        listaDocumentos = new ArrayList<>();
        listaDocuemntos2 = recursoJpaController.findRecursoEntities();
        for (int i = 0; i < listaDocuemntos2.size(); i++) {
            if (capacitacion.equals(listaDocuemntos2.get(i).getCapacitacionId()) && listaDocuemntos2.get(i).getPathVideo() == null) {
                listaDocumentos.add(listaDocuemntos2.get(i));
            }
        }
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Recurso> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public CalendarioBean getCalendarioBean() {
        return calendarioBean;
    }

    public void setCalendarioBean(CalendarioBean calendarioBean) {
        this.calendarioBean = calendarioBean;
    }

    public String getEnlace() {
        enlace = "";
        if (recurso.getPathVideo() != null) {
            enlace = "https://www.youtube.com/v/"+recurso.getPathVideo().substring(32, 43);
        }
        System.out.println(enlace);
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

}
