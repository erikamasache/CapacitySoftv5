/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import java.io.Serializable;
import unl.edu.jpa.controllers.TipoCapacitadoJpaController;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.TipoCapacitado;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class TipoCapacitadoBean implements Serializable {

    private TipoCapacitado tipoCapacitado;
    private TipoCapacitadoJpaController tipoCapacitadoJpaController;
    private List<TipoCapacitado> listaTipoCapacitados;
    private List<TipoCapacitado> filterTipoCapacitados;

    public TipoCapacitadoBean() throws NamingException {
        tipoCapacitado = new TipoCapacitado();
        tipoCapacitadoJpaController = new TipoCapacitadoJpaController();
    }

    public void instanciarCapacitado() {
        tipoCapacitado = new TipoCapacitado();
    }

    public void crearTipoCapacitado() throws RollbackFailureException, Exception {
        tipoCapacitadoJpaController.create(tipoCapacitado);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "El tipo " + tipoCapacitado.getNombre()
                + " ha sido creado", null));
    }

    public void editarTipoCapacitado() throws RollbackFailureException, Exception {
        tipoCapacitadoJpaController.edit(tipoCapacitado);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "El tipo " + tipoCapacitado.getNombre()
                + " ha sido modificado", null));
    }

    public void eliminarTipoCapacitado() throws RollbackFailureException, Exception {
        if (tipoCapacitadoJpaController.obtenerTipoCapacitado(tipoCapacitado).isEmpty()) {
            tipoCapacitadoJpaController.destroy(tipoCapacitado.getId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "El tipo " + tipoCapacitado.getNombre()
                    + " ha sido eliminado", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Para poder eliminar el tipoCapacitado este no debe estar asignado a ningun capacitado, "
                    + "asegurece de que ningun capacitado tenga como tipo " + tipoCapacitado.getNombre(), null));
        }

    }

    public TipoCapacitado getTipoCapacitado() {
        return tipoCapacitado;
    }

    public void setTipoCapacitado(TipoCapacitado tipoCapacitado) {
        this.tipoCapacitado = tipoCapacitado;
    }

    public List<TipoCapacitado> getListaTipoCapacitados() {
        listaTipoCapacitados = tipoCapacitadoJpaController.findTipoCapacitadoEntities();
        return listaTipoCapacitados;
    }

    public void setListaTipoCapacitados(List<TipoCapacitado> listaTipoCapacitados) {
        this.listaTipoCapacitados = listaTipoCapacitados;
    }

    public List<TipoCapacitado> getFilterTipoCapacitados() {
        return filterTipoCapacitados;
    }

    public void setFilterTipoCapacitados(List<TipoCapacitado> filterTipoCapacitados) {
        this.filterTipoCapacitados = filterTipoCapacitados;
    }

}
