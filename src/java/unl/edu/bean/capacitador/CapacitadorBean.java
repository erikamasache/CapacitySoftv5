package unl.edu.bean.capacitador;

import java.io.File;
import java.io.IOException;
import unl.edu.jpa.controllers.AdministradorJpaController;
import unl.edu.jpa.controllers.CapacitadorJpaController;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Administrador;
import unl.edu.jpa.entities.Capacitador;
import unl.edu.jpa.entities.Metodos;
import java.io.Serializable;
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

/**
 *
 * @author ErikaM
 */
@ManagedBean
@SessionScoped
public class CapacitadorBean implements Serializable {

    private Administrador admin;
    private AdministradorJpaController administradorJpaController;
    private List<Administrador> listaAdministradores;

    private Capacitador capacitador;
    private List<Capacitador> listaCapacitadores;
    private List<Capacitador> filterCapacitadores;
    private CapacitadorJpaController capacitadorJpaController;

    @ManagedProperty("#{rolBean}")
    private RolBean rolBean;
    
    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;
    
    private String contrasenia;
    private String mensaje;

    // constructor instanciamos los objetos de las clases necesarias
    public CapacitadorBean() throws NamingException {
        capacitador = new Capacitador();
        capacitador.setTipoIdentificacion(1);
        capacitadorJpaController = new CapacitadorJpaController();
        admin = new Administrador();
        administradorJpaController = new AdministradorJpaController();
    }

    public void instanciarCapacitador() {
        capacitador = new Capacitador();
    }

    public void guardarCapacitador() throws Exception {
        boolean enviado = false;
        String contraseniaSinEncriptar = "";
        listaAdministradores = administradorJpaController.findAdministradorEntities();
        if (capacitadorJpaController.cedulaRepetida(capacitador).isEmpty()) {
            if (capacitadorJpaController.celularRepetido(capacitador).isEmpty()) {
                if (capacitadorJpaController.correoRepetido(capacitador).isEmpty()) {
                    if (capacitador.getTipoIdentificacion() == 1) {
                        if (Metodos.validadorDeCedula(capacitador.getCedula())) {
                            admin = listaAdministradores.get(0);
                            capacitador.setAdministradorId(admin);
                            contraseniaSinEncriptar = Metodos.generarContrasenia();
                            capacitador.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                            capacitadorJpaController.create(capacitador);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El Capacitador " + capacitador.getNombre() + " " + capacitador.getApellido()
                                    + " ha sido creado", null));
                            enviado = true;
                            rolBean.nuevoUsuario(capacitador.getCorreo(), capacitador.getContrasenia(),"capacitador");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "El número de cédula de " + capacitador.getNombre() + " " + capacitador.getApellido()
                                    + " es incorrecto", null));
                        }

                    } else {
                        admin = listaAdministradores.get(0);
                        capacitador.setAdministradorId(admin);
                        contraseniaSinEncriptar = Metodos.generarContrasenia();
                        capacitador.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                        capacitadorJpaController.create(capacitador);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "El Capacitador " + capacitador.getNombre() + " " + capacitador.getApellido()
                                + " ha sido creado", null));
                        enviado = true;
                        rolBean.nuevoUsuario(capacitador.getCorreo(), capacitador.getContrasenia(),"capacitador");
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
        if (enviado) {
            try {
                if (Metodos.enviarMail(capacitador.getCorreo(), contraseniaSinEncriptar)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "La contraseña del capacitador " + capacitador.getNombre() + " " + capacitador.getApellido()
                            + "ha sido enviada al correo " + capacitador.getCorreo(), null));
                }
            } catch (Exception e) {
                System.out.println("error en la conexion " + e);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Error de conexion porfavor revice si su conexion a internet esta disponible", null));
            }
        }
        capacitador = new Capacitador();
    }

    public void editarCapacitador() throws NonexistentEntityException, RollbackFailureException, Exception {
        boolean cedulaExiste;
        boolean celularExiste;
        boolean correoExiste;
        Capacitador capacitadorAnterior = capacitadorJpaController.findCapacitador(capacitador.getId());
        if (!capacitadorJpaController.cedulaRepetida(capacitador).isEmpty()) {
            cedulaExiste = capacitadorJpaController.cedulaRepetida(capacitador).get(0).getId() == capacitador.getId();
        } else {
            cedulaExiste = capacitadorJpaController.cedulaRepetida(capacitador).isEmpty();
        }
        if (!capacitadorJpaController.celularRepetido(capacitador).isEmpty()) {
            celularExiste = capacitadorJpaController.celularRepetido(capacitador).get(0).getId() == capacitador.getId();
        } else {
            celularExiste = capacitadorJpaController.celularRepetido(capacitador).isEmpty();
        }
        if (!capacitadorJpaController.correoRepetido(capacitador).isEmpty()) {
            correoExiste = capacitadorJpaController.correoRepetido(capacitador).get(0).getId() == capacitador.getId();
        } else {
            correoExiste = capacitadorJpaController.correoRepetido(capacitador).isEmpty();
        }

        if (cedulaExiste) {
            if (celularExiste) {
                if (correoExiste) {
                    if (capacitador.getTipoIdentificacion() == 1) {
                        if (Metodos.validadorDeCedula(capacitador.getCedula())) {
                            capacitadorJpaController.edit(capacitador);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El Capacitador " + capacitador.getNombre() + " " + capacitador.getApellido()
                                    + " ha sido modificado", null));
                            rolBean.editarUsuario(capacitador.getCorreo(), capacitador.getContrasenia(), capacitadorAnterior.getCorreo(),"capacitador");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "El número de cédula de " + capacitador.getNombre() + " " + capacitador.getApellido()
                                    + " es incorrecto", null));
                        }
                    } else {
                        capacitadorJpaController.edit(capacitador);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "El Capacitador " + capacitador.getNombre() + " " + capacitador.getApellido()
                                + " ha sido modificado", null));
                        rolBean.editarUsuario(capacitador.getCorreo(), capacitador.getContrasenia(), capacitadorAnterior.getCorreo(),"capacitador");
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
        capacitador = new Capacitador();
    }

    public void eliminarCapacitador() throws NonexistentEntityException, RollbackFailureException, Exception {
        rolBean.eliminarUsuario(capacitador.getCorreo());
        capacitadorJpaController.destroy(capacitador.getId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "El Capacitador " + capacitador.getNombre() + " " + capacitador.getApellido()
                + " ha sido eliminado", null));
        capacitador = new Capacitador();

    }
    
    public void exportarPDF()throws JRException, IOException  {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("txtRol", loginBean.getNombre());

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Jasper/Capacitadores.jasper"));
        JasperPrint jasperPrint;
        if (filterCapacitadores != null) {
            jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getFilterCapacitadores()));
        } else {
            jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getListaCapacitadores()));
        }

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=informeCapacitadores.pdf");
        ServletOutputStream stream = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void reenviarContrasenia(Capacitador cap) throws Exception {
        String contraseniaSinEncriptar = "";
        try {
            contraseniaSinEncriptar = Metodos.generarContrasenia();
            if (Metodos.enviarMail(cap.getCorreo() + "", contraseniaSinEncriptar)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "La contraseña del capacitador " + cap.getNombre() + " " + cap.getApellido()
                        + "ha sido enviada al correo " + cap.getCorreo(), null));
                cap.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                capacitadorJpaController.edit(cap);
            }
        } catch (Exception e) {
            System.out.println("error en la conexion " + e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Error de conexion porfavor revice si su conexion a internet esta disponible", null));
        }
    }
    
    public void enviarEmail(){
        Metodos.enviarMailCapacitador(capacitador.getCorreo(), loginBean.getCapacitado().getCorreo(), contrasenia, mensaje);
    }

// generacion de los set y get
    public Capacitador getCapacitador() {
        return capacitador;
    }

    public void setCapacitador(Capacitador capacitador) {
        this.capacitador = capacitador;
    }

    public List<Capacitador> getListaCapacitadores() {
        listaCapacitadores = capacitadorJpaController.listarCapacitadores();
        return listaCapacitadores;
    }

    public void setListaCapacitadores(List<Capacitador> listaCapacitadores) {
        this.listaCapacitadores = listaCapacitadores;
    }

    public List<Capacitador> getFilterCapacitadores() {
        return filterCapacitadores;
    }

    public void setFilterCapacitadores(List<Capacitador> filterCapacitadores) {
        this.filterCapacitadores = filterCapacitadores;
    }

    public RolBean getRolBean() {
        return rolBean;
    }

    public void setRolBean(RolBean rolBean) {
        this.rolBean = rolBean;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
