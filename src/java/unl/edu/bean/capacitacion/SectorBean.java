/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import unl.edu.jpa.controllers.SectorJpaController;
import unl.edu.jpa.entities.Sector;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
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
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import unl.edu.bean.loginReg.LoginBean;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class SectorBean implements Serializable {

    private Sector sector;
    private SectorJpaController sectorJpaController;
    private List<Sector> listaSectores;
    private List<Sector> filterSectores;

    @ManagedProperty("#{carreraBean}")
    private CarreraBean carreraBean;

    @ManagedProperty("#{responsableBean}")
    private ResponsableBean responsableBean;
    
    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    public SectorBean() throws NamingException {
        sector = new Sector();
        sectorJpaController = new SectorJpaController();
    }

    public void guardarSector() throws Exception {
        if (sectorJpaController.nombreRepetido(sector).isEmpty()) {
            sectorJpaController.create(sector);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "El sector " + sector.getNombre()
                    + " ha sido creado", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "El sector " + sector.getNombre()
                    + " ya existe", null));
        }

    }

    public void editarSector() throws RollbackFailureException, Exception {
        boolean nombreExiste;
        if (!sectorJpaController.nombreRepetido(sector).isEmpty()) {
            nombreExiste = sectorJpaController.nombreRepetido(sector).get(0).getId() == sector.getId();
        } else {
            nombreExiste = sectorJpaController.nombreRepetido(sector).isEmpty();
        }
        if (nombreExiste) {
            sectorJpaController.edit(sector);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "El sector " + sector.getNombre()
                    + " ha sido modificado", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "El sector " + sector.getNombre()
                    + " ya existe", null));
        }
    }

    public void eliminarSector() throws RollbackFailureException, Exception {
        sectorJpaController.destroy(sector.getId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "El sector " + sector.getNombre()
                + " ha sido eliminado", null));
    }

    public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("txtRol", loginBean.getNombre());

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Jasper/Sectores.jasper"));
        JasperPrint jasperPrint;
        if (filterSectores != null) {
            jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getFilterSectores()));
        } else {
            jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getListaSectores()));
        }

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=Sectores.pdf");
        ServletOutputStream stream = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

//    public void postProcessXLS(Object document) throws IOException, DocumentException {
//        final Document pdf = (Document) document;
//        pdf.setPageSize(PageSize.A4);
//        pdf.addAuthor("Erika Masache");
//    }
//
//    public void preProcessPDF(Object document) throws IOException, DocumentException {
//
//        final Document pdf = (Document) document;
//
//        pdf.setPageSize(PageSize.A4);
//        pdf.open();
//        pdf.addAuthor("Erika Masache");
//        pdf.addTitle("Lista de sectores a capacitar");
//        Phrase phrase = new Phrase("Universidad Nacional de Loja");
//        pdf.add(phrase);
//    }
    // setters y getters
    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public List<Sector> getListaSectores() {
        listaSectores = new ArrayList<>();
        listaSectores = sectorJpaController.findSectorEntities();
        return listaSectores;
    }

    public void setListaSectores(List<Sector> listaSectores) {
        this.listaSectores = listaSectores;
    }

    public CarreraBean getCarreraBean() {
        return carreraBean;
    }

    public void setCarreraBean(CarreraBean carreraBean) {
        this.carreraBean = carreraBean;
    }

    public ResponsableBean getResponsableBean() {
        return responsableBean;
    }

    public void setResponsableBean(ResponsableBean responsableBean) {
        this.responsableBean = responsableBean;
    }

    private Collection<?> getPersonas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Sector> getFilterSectores() {
        return filterSectores;
    }

    public void setFilterSectores(List<Sector> filterSectores) {
        this.filterSectores = filterSectores;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

}
