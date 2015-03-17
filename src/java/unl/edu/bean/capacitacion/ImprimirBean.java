/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.event.SelectEvent;
import unl.edu.bean.loginReg.LoginBean;
import unl.edu.datasource.CapacitacionesDataSource;
import unl.edu.jpa.controllers.CapacitacionJpaController;
import unl.edu.jpa.entities.Capacitacion;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class ImprimirBean implements Serializable {

    private Capacitacion capacitacion;
    private final CapacitacionJpaController capacitacionJpaController;
    private List<Capacitacion> listaCapacitaciones;
    private List<Capacitacion> listaCapacitacionesImprimir;
    //--- fechas
    private Date FechaInicio;
    private Date FechaFin;
    private Date maxFecha;
    private Date minFecha;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    public ImprimirBean() throws NamingException {
        capacitacion = new Capacitacion();
        capacitacionJpaController = new CapacitacionJpaController();
        listaCapacitaciones = new ArrayList<>();
        listaCapacitacionesImprimir = new ArrayList<>();
    }

    public void obtenerMaxFecha(SelectEvent event) {
        maxFecha = (Date) event.getObject();
        System.out.println("max fecha " + maxFecha);
    }

    public void obtenerMinFecha(SelectEvent event) {
        minFecha = (Date) event.getObject();
        System.out.println("min fecha" + minFecha);
    }

    public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (FechaFin == null && FechaInicio == null) {
            CapacitacionesDataSource datasource = new CapacitacionesDataSource();
            datasource.setListaCapacitaciones(capacitacionJpaController.findCapacitacionEntities());

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("txtRol", loginBean.getNombre());

            File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Jasper/Capacitaciones.jasper"));
            JasperPrint jasperPrint;

            jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros,datasource);

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment; filename=Capacitaciones.pdf");
            ServletOutputStream stream = response.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

            stream.flush();
            stream.close();
            FacesContext.getCurrentInstance().responseComplete();
        } else {
            CapacitacionesDataSource datasource = new CapacitacionesDataSource();
            datasource.setListaCapacitaciones(this.getListaCapacitacionesImprimir());

            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("txtRol", loginBean.getNombre());

            File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Jasper/Capacitaciones.jasper"));
            JasperPrint jasperPrint;

            jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, datasource);

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment; filename=Capacitaciones.pdf");
            ServletOutputStream stream = response.getOutputStream();

            JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

            stream.flush();
            stream.close();
            FacesContext.getCurrentInstance().responseComplete();
            FechaInicio = null;
            FechaFin = null;
        }
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

    public Date getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(Date FechaInicio) {
        this.FechaInicio = FechaInicio;
    }

    public Date getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(Date FechaFin) {
        this.FechaFin = FechaFin;
    }

    public List<Capacitacion> getListaCapacitacionesImprimir() {
        listaCapacitacionesImprimir = new ArrayList<>();
        List<Capacitacion> listaCapacitacionesI = capacitacionJpaController.findCapacitacionEntities();
        for (int i = 0; i < listaCapacitacionesI.size(); i++) {
            capacitacion = listaCapacitacionesI.get(i);
            if (FechaInicio.before(capacitacion.getFechaInicio()) && FechaFin.after(capacitacion.getFechaInicio())) {
                listaCapacitacionesImprimir.add(capacitacion);
            } else if (FechaInicio.equals(capacitacion.getFechaInicio()) || FechaFin.equals(capacitacion.getFechaInicio())) {
                listaCapacitacionesImprimir.add(capacitacion);
            }
        }
        return listaCapacitacionesImprimir;
    }

    public void setListaCapacitacionesImprimir(List<Capacitacion> listaCapacitacionesImprimir) {
        this.listaCapacitacionesImprimir = listaCapacitacionesImprimir;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public List<Capacitacion> getListaCapacitaciones() {
        return listaCapacitaciones;
    }

    public void setListaCapacitaciones(List<Capacitacion> listaCapacitaciones) {
        this.listaCapacitaciones = listaCapacitaciones;
    }

}
