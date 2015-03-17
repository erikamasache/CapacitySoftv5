/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitador;

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
import unl.edu.bean.loginReg.LoginBean;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class RecursosBean implements Serializable {

    private Capacitacion capacitacion;
    private Recurso recurso;
    private RecursoJpaController recursoJpaController;
    private List<Recurso> listaVideos;
    private List<Recurso> listaDocumentos;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    public RecursosBean() throws NamingException {
        capacitacion = new Capacitacion();
        recurso = new Recurso();
        recursoJpaController = new RecursoJpaController();
        listaVideos = new ArrayList<>();
        listaDocumentos = new ArrayList<>();
    }

    public String obtenerCapacitacion(Capacitacion capacitacion1) throws Exception {
        capacitacion = capacitacion1;
        return "capacitacionesRecursos?faces-redirect=true";
    }
//    
//    public void validarVideo(){
//        recurso = this.recurso;
//    }

    public void guardarRecurso() throws Exception {
        if (recurso.getPathPdf() == null) {
            if (recurso.getPathVideo().length() >= 43) {
                if (recurso.getPathVideo().substring(8, 24).equals("www.youtube.com/")) {
                    String enlace = recurso.getPathVideo().substring(32, 43);
                    if (recursoJpaController.videoRepetido(enlace, capacitacion).isEmpty()) {
                        recurso.setPathVideo(enlace);
                        recurso.setCapacitacionId(capacitacion);
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
                        recurso.setCapacitacionId(capacitacion);
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

        recurso = new Recurso();
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }

    public RecursoJpaController getRecursoJpaController() {
        return recursoJpaController;
    }

    public void setRecursoJpaController(RecursoJpaController recursoJpaController) {
        this.recursoJpaController = recursoJpaController;
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

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

}
