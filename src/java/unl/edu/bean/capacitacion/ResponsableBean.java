/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import unl.edu.jpa.controllers.ResponsableJpaController;
import unl.edu.jpa.entities.Responsable;
import unl.edu.jpa.entities.Sector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Metodos;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class ResponsableBean implements Serializable {

    private Responsable responsable;
    private ResponsableJpaController responsableJpaController;
    private List<Responsable> listaResponsables;
    private Sector sector;

    public ResponsableBean() throws NamingException {
        responsable = new Responsable();
        responsableJpaController = new ResponsableJpaController();
    }
    
    public void instanciarResponsable(){
        responsable = new Responsable();
    }
    
    public String obtenerSector(Sector sect) throws Exception {
        sector = sect;
        return "responsable?faces-redirect=true";
    }

    public void guardarResponsable() throws Exception {
        if (responsableJpaController.cedulaRepetida(responsable).isEmpty()) {
            if (responsableJpaController.celularRepetido(responsable).isEmpty()) {
                if (responsableJpaController.correoRepetido(responsable).isEmpty()) {
                    if (responsable.getTipoIdentificacion() == 1) {
                        if (Metodos.validadorDeCedula(responsable.getCedula())) {
                            responsable.setSectorId(sector);
                            responsableJpaController.create(responsable);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El respondable" + responsable.getNombre()
                                    + " ha sido creado", null));
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "El número de cédula de " + responsable.getNombre() + " " + responsable.getApellido()
                                    + " es incorrecto", null));
                        }
                    } else {
                        responsable.setSectorId(sector);
                        responsableJpaController.create(responsable);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "El Responsable " + responsable.getNombre() + " " + responsable.getApellido()
                                + " ha sido creado", null));
                    }
                    responsable = new Responsable();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "El correo ingresado ya existe, ingrese uno nuevo", null));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "El número de celular ingresado ya existe, ingrese uno nuevo", null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "La cédula ingresada ya existe", null));
        }

    }
    
    public void editarResponsable() throws RollbackFailureException, Exception {
        boolean cedulaExiste;
        if (!responsableJpaController.cedulaRepetida(responsable).isEmpty()) {
            cedulaExiste = responsableJpaController.cedulaRepetida(responsable).get(0).getId() == responsable.getId();
        } else {
            cedulaExiste = responsableJpaController.cedulaRepetida(responsable).isEmpty();
        }

        boolean celularExiste;
        if (!responsableJpaController.celularRepetido(responsable).isEmpty()) {
            celularExiste = responsableJpaController.celularRepetido(responsable).get(0).getId() == responsable.getId();
        } else {
            celularExiste = responsableJpaController.celularRepetido(responsable).isEmpty();
        }

        boolean correoExiste;
        if (!responsableJpaController.correoRepetido(responsable).isEmpty()) {
            correoExiste = responsableJpaController.correoRepetido(responsable).get(0).getId() == responsable.getId();
        } else {
            correoExiste = responsableJpaController.correoRepetido(responsable).isEmpty();
        }

        if (cedulaExiste) {
            if (celularExiste) {
                if (correoExiste) {
                    if (responsable.getTipoIdentificacion() == 1) {
                        if (Metodos.validadorDeCedula(responsable.getCedula())) {
                            responsableJpaController.edit(responsable);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El respondable" + responsable.getNombre()
                                    + " ha sido modificado", null));
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "El número de cédula de " + responsable.getNombre() + " " + responsable.getApellido()
                                    + " es incorrecto", null));
                        }
                    } else {
                        responsableJpaController.edit(responsable);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "El Capacitador " + responsable.getNombre() + " " + responsable.getApellido()
                                + " ha sido modificado", null));
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "El correo ingresado ya existe, ingrese uno nuevo", null));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "El número de celular ingresado ya existe, ingrese uno nuevo", null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "La cédula ingresada ya existe", null));
        }
    }
    
    

    public void eliminarResponsable() throws RollbackFailureException, Exception {
        responsableJpaController.destroy(responsable.getId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "El respondable" + responsable.getNombre()
                + " ha sido eliminado", null));
    }

    // setters y getters
    public Responsable getResponsable() {
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
    }

    public List<Responsable> getListaResponsables() {
        listaResponsables = new ArrayList<>();
        List<Responsable> aux = responsableJpaController.findResponsableEntities();
        for (int i = 0; i < aux.size(); i++) {
            if (sector.equals(aux.get(i).getSectorId())) {
                listaResponsables.add(aux.get(i));
            }
        }
        return listaResponsables;
    }

    public void setListaResponsables(List<Responsable> listaResponsables) {
        this.listaResponsables = listaResponsables;
    }

}
