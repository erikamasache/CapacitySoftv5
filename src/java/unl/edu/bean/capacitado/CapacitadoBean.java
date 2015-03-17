/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitado;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import unl.edu.bean.capacitacion.SectorBean;
import unl.edu.jpa.controllers.CapacitadoJpaController;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Metodos;
import java.io.Serializable;
import java.util.ArrayList;
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
import unl.edu.bean.rol.RolBean;
import unl.edu.jpa.controllers.exceptions.NonexistentEntityException;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class CapacitadoBean implements Serializable {

    private Capacitado capacitado;
    private CapacitadoJpaController capacitadoJpaController;
    private List<Capacitado> listaCapacitados;
    private List<Capacitado> filterCapacitados;

    @ManagedProperty("#{rolBean}")
    private RolBean rolBean;

    @ManagedProperty("#{sectorBean}")
    private SectorBean sectorBean;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    public CapacitadoBean() throws NamingException {
        listaCapacitados = new ArrayList<>();
        capacitado = new Capacitado();
        capacitadoJpaController = new CapacitadoJpaController();
    }

    public void instanciarCapacitado() {
        capacitado = new Capacitado();
    }

    public void guardarCapacitado() throws Exception {
        String contraseniaSinEncriptar = "";
        if (capacitadoJpaController.cedulaRepetida(capacitado).isEmpty()) {
            if (capacitadoJpaController.celularRepetido(capacitado).isEmpty()) {
                if (capacitadoJpaController.correoRepetido(capacitado).isEmpty()) {
                    if (capacitado.getTipoIdentificacion() == 1) {
                        if (Metodos.validadorDeCedula(capacitado.getCedula())) {
                            contraseniaSinEncriptar = Metodos.generarContrasenia();
                            capacitado.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                            capacitadoJpaController.create(capacitado);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El Capacitado " + capacitado.getNombre()
                                    + " ha sido registrado", null));
                            rolBean.nuevoUsuario(capacitado.getCorreo(), capacitado.getContrasenia(),"capacitado");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "El número de cédula de " + capacitado.getNombre()
                                    + " es incorrecto", null));
                        }
                    } else {
                        contraseniaSinEncriptar = Metodos.generarContrasenia();
                        capacitado.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                        capacitadoJpaController.create(capacitado);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "El Capacitado " + capacitado.getNombre()
                                + " ha sido registrado", null));
                        rolBean.nuevoUsuario(capacitado.getCorreo(), capacitado.getContrasenia(),"capacitado");
                    }
                    try {
                        if (Metodos.enviarMail(capacitado.getCorreo(), contraseniaSinEncriptar)) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "La contraseña del capacitado " + capacitado.getNombre() + " " + capacitado.getApellido()
                                    + "ha sido enviada al correo " + capacitado.getCorreo(), null));
                        }
                    } catch (Exception e) {
//                        System.out.println("error en la conexion " + e);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Error de conexion, porfavor revice si su conexion a internet esta disponible", null));
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
        capacitado = new Capacitado();
    }

    public void editarCapacitado() throws NonexistentEntityException, RollbackFailureException, Exception {
        boolean cedulaExiste;
        Capacitado capacitadoAnterior = capacitadoJpaController.findCapacitado(capacitado.getId());
        if (!capacitadoJpaController.cedulaRepetida(capacitado).isEmpty()) {
            cedulaExiste = capacitadoJpaController.cedulaRepetida(capacitado).get(0).getId() == capacitado.getId();
        } else {
            cedulaExiste = capacitadoJpaController.cedulaRepetida(capacitado).isEmpty();
        }

        boolean celularExiste;
        if (!capacitadoJpaController.celularRepetido(capacitado).isEmpty()) {
            celularExiste = capacitadoJpaController.celularRepetido(capacitado).get(0).getId() == capacitado.getId();
        } else {
            celularExiste = capacitadoJpaController.celularRepetido(capacitado).isEmpty();
        }

        boolean correoExiste;
        if (!capacitadoJpaController.correoRepetido(capacitado).isEmpty()) {
            correoExiste = capacitadoJpaController.correoRepetido(capacitado).get(0).getId() == capacitado.getId();
        } else {
            correoExiste = capacitadoJpaController.correoRepetido(capacitado).isEmpty();
        }

        if (cedulaExiste) {
            if (celularExiste) {
                if (correoExiste) {
                    if (capacitado.getTipoIdentificacion() == 1) {
                        if (Metodos.validadorDeCedula(capacitado.getCedula())) {
                            capacitadoJpaController.edit(capacitado);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El Capacitado " + capacitado.getNombre() + " " + capacitado.getApellido()
                                    + " ha sido modificado", null));
                            rolBean.editarUsuario(capacitado.getCorreo(), capacitado.getContrasenia(), capacitadoAnterior.getCorreo(),"capacitado");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "El número de cédula de " + capacitado.getNombre() + " " + capacitado.getApellido()
                                    + " es incorrecto", null));
                        }
                    } else {
                        capacitadoJpaController.edit(capacitado);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "El Capacitado " + capacitado.getNombre() + " " + capacitado.getApellido()
                                + " ha sido modificado", null));
                        rolBean.editarUsuario(capacitado.getCorreo(), capacitado.getContrasenia(), capacitadoAnterior.getCorreo(),"capacitado");
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "El correo ingresado ya existe, ingrese uno nuevo", null));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "El numero de celular ingresado ya existe, ingrese uno nuevo", null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "La cédula ingresada ya existe", null));
        }
        capacitado =  new Capacitado();
    }

    public void eliminarCapacitado() throws NonexistentEntityException, RollbackFailureException, Exception {
        rolBean.eliminarUsuario(capacitado.getCorreo());
        capacitadoJpaController.destroy(capacitado.getId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "El Capacitado " + capacitado.getNombre() + " " + capacitado.getApellido()
                + " ha sido eliminado", null));
        capacitado = new Capacitado();
    }

    public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("txtRol", loginBean.getNombre());

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Jasper/Capacitados.jasper"));
        JasperPrint jasperPrint;
        if (filterCapacitados != null) {
            jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getFilterCapacitados()));
        } else {
            jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getListaCapacitados()));
        }

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=iformeSector.pdf");
        ServletOutputStream stream = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    // setters y getters
    public Capacitado getCapacitado() {
        return capacitado;
    }

    public void setCapacitado(Capacitado capacitado) {
        this.capacitado = capacitado;
    }

    public List<Capacitado> getListaCapacitados() {
        listaCapacitados = capacitadoJpaController.listarCapacitados();
        return listaCapacitados;
    }

    public void setListaCapacitados(List<Capacitado> listaCapacitados) {
        this.listaCapacitados = listaCapacitados;
    }

    public SectorBean getSectorBean() {
        return sectorBean;
    }

    public void setSectorBean(SectorBean sectorBean) {
        this.sectorBean = sectorBean;
    }

    public RolBean getRolBean() {
        return rolBean;
    }

    public void setRolBean(RolBean rolBean) {
        this.rolBean = rolBean;
    }

    public List<Capacitado> getFilterCapacitados() {
        return filterCapacitados;
    }

    public void setFilterCapacitados(List<Capacitado> filterCapacitados) {
        this.filterCapacitados = filterCapacitados;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

}
