/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import unl.edu.jpa.controllers.CarreraJpaController;
import unl.edu.jpa.entities.Carrera;
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

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class CarreraBean implements Serializable {

    private Carrera carrera;
    private List<Carrera> listaCarreras;
    private final CarreraJpaController carreraJpaController;
    private Sector sector;
    private List<Carrera> aux;

    public CarreraBean() throws NamingException {
        carrera = new Carrera();
        carreraJpaController = new CarreraJpaController();
    }
    
    public void instanciarCarrera(){
        carrera = new Carrera();
    }

    public String obtenerSector(Sector sect) throws Exception {
        sector = sect;
        return "carrera?faces-redirect=true";
    } 

    public void guardarCarrera() throws Exception {
        if (carreraJpaController.nombreRepetido(carrera).isEmpty()) {
            carrera.setSectorId(sector);
            carreraJpaController.create(carrera);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "La carrera " + carrera.getNombre()
                    + " ha sido creada", null));
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "La carrera " + carrera.getNombre()
                    + " ya existe", null));
        }

    }
    
    public void editarCarrera() throws RollbackFailureException, Exception{
        boolean nombreExiste;
        if (!carreraJpaController.nombreRepetido(carrera).isEmpty()) {
            nombreExiste = carreraJpaController.nombreRepetido(carrera).get(0).getId()==carrera.getId();
        }else{
            nombreExiste = carreraJpaController.nombreRepetido(carrera).isEmpty();
        }
        if(nombreExiste){
        carreraJpaController.edit(carrera);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "La carrera " + carrera.getNombre()
                + " ha sido modificada", null));
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "La carrera " + carrera.getNombre()
                + " ya existe", null));
        }
    }
    
    public void eliminarCarrera() throws RollbackFailureException, Exception{
        carreraJpaController.destroy(carrera.getId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "La carrera " + carrera.getNombre()
                + " ha sido eliminada", null));
    }

    // setters y getters
    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public List<Carrera> getListaCarreras() {
        listaCarreras = new ArrayList<>();
        aux = carreraJpaController.findCarreraEntities();
        for (int i = 0; i < aux.size(); i++) {
            if (sector.equals(aux.get(i).getSectorId())) {
                listaCarreras.add(aux.get(i));
            }
        }
        return listaCarreras;
    }

    public void setListaCarreras(List<Carrera> listaCarreras) {
        this.listaCarreras = listaCarreras;
    }

}
