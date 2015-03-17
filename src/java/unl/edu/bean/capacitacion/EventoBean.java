/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import org.primefaces.event.SelectEvent;
import unl.edu.jpa.controllers.FechasFestivasJpaController;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.FechasFestivas;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class EventoBean implements Serializable {

    private FechasFestivas fechaFestiva;
    private FechasFestivasJpaController fechasFestivasJpaController;

    @ManagedProperty("#{calendarioBean}")
    private CalendarioBean calendarioBean;

    //--- fechas
    private Date maxFecha;
    private Date minFecha;
    private int horaInicio;
    private int horaFin;

    public EventoBean() throws NamingException {
        horaFin = 19;
        horaInicio = 7;
        fechasFestivasJpaController = new FechasFestivasJpaController();
        fechaFestiva = new FechasFestivas();
    }

    public void crearEvento() throws RollbackFailureException, Exception {
        if (fechaFestiva.getId() != null) {
            fechasFestivasJpaController.edit(fechaFestiva);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Evento modificado", null));
        } else {
            fechasFestivasJpaController.create(fechaFestiva);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Evento agregado", null));
        }
        calendarioBean.init();
        fechaFestiva = new FechasFestivas();
        maxFecha=null;
        minFecha=null;
    }

    public void obtenerFF() {
        fechaFestiva = calendarioBean.getFechasFestivas();
    }

    public void obtenerMaxFecha(SelectEvent event) {
        maxFecha = (Date) event.getObject();
    }

    public void obtenerMinFecha(SelectEvent event) {
        minFecha = (Date) event.getObject();
    }

    public void obtenerHoraInicio(SelectEvent event) {
        horaInicio = ((Date) event.getObject()).getHours();
        System.out.println("hora inicio " + horaInicio);
    }

    public void obtenerHoraFin(SelectEvent event) {
        horaFin = ((Date) event.getObject()).getHours();
        System.out.println("hora fin " + horaFin);
    }

    // setters y getters
    public FechasFestivas getFechaFestiva() {
        return fechaFestiva;
    }

    public void setFechaFestiva(FechasFestivas fechaFestiva) {
        this.fechaFestiva = fechaFestiva;
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

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public CalendarioBean getCalendarioBean() {
        return calendarioBean;
    }

    public void setCalendarioBean(CalendarioBean calendarioBean) {
        this.calendarioBean = calendarioBean;
    }

}
