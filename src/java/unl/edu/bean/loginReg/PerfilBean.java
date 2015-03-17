/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.loginReg;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import org.primefaces.context.RequestContext;
import unl.edu.bean.rol.RolBean;
import unl.edu.jpa.controllers.AdministradorJpaController;
import unl.edu.jpa.controllers.CapacitadoJpaController;
import unl.edu.jpa.controllers.CapacitadorJpaController;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Administrador;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Capacitador;
import unl.edu.jpa.entities.Metodos;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@SessionScoped
public class PerfilBean implements Serializable {

    private Administrador adm;
    AdministradorJpaController administradorJpaController;
    private Capacitador capacitador;
    CapacitadorJpaController capacitadorJpaController;
    private Capacitado capacitado;
    CapacitadoJpaController capacitadoJpaController;
    private String contrasenia;
    private String contrasenia2;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    @ManagedProperty("#{rolBean}")
    private RolBean rolBean;

    public PerfilBean() throws NamingException {
        adm = new Administrador();
        administradorJpaController = new AdministradorJpaController();
        capacitador = new Capacitador();
        capacitadorJpaController = new CapacitadorJpaController();
        capacitado = new Capacitado();
        capacitadoJpaController = new CapacitadoJpaController();
    }

    @PostConstruct
    public void init() {
        if (loginBean.getAdm().getNombre() != null) {
            adm = loginBean.getAdm();
        } else if (loginBean.getCapacitador().getNombre() != null) {
            capacitador = loginBean.getCapacitador();
        } else if (loginBean.getCapacitado().getNombre() != null) {
            capacitado = loginBean.getCapacitado();
        }
    }

    public String presentarDialog() {
        RequestContext context = RequestContext.getCurrentInstance();
        String contraseniaEncriptada = Metodos.encripta(contrasenia);
        adm = loginBean.getAdm();
        capacitador = loginBean.getCapacitador();
        capacitado = loginBean.getCapacitado();
        if (adm.getId() != null) {
            if (adm.getContrasenia().equals(contraseniaEncriptada)) {
                context.execute("PF('CrearAdministrador').show();");

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Contraseña anterior incorrecta", null));
            }
        } else if (capacitador.getId() != null) {
            if (capacitador.getContrasenia().equals(contraseniaEncriptada)) {
                context.execute("PF('CrearCapacitador').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Contraseña anterior incorrecta", null));
            }
        } else if (capacitado.getId() != null) {
            if (capacitado.getContrasenia().equals(contraseniaEncriptada)) {
                context.execute("PF('CrearCapacitado').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Contraseña anterior incorrecta", null));
            }
        }
        contrasenia = "";
        return "";
    }

    public void cambiarClave() throws RollbackFailureException, Exception {
        RequestContext context = RequestContext.getCurrentInstance();
        String contraseniaEncriptada = Metodos.encripta(contrasenia);
        if (adm.getContrasenia().equals(contraseniaEncriptada)) {
            administradorJpaController.edit(adm);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Clave de acceso ha sido modificada", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Contraseña anterior incorrecta", null));
        }
        contrasenia = "";
    }

    public void guardarContrasenia() throws RollbackFailureException, Exception {
        RequestContext context = RequestContext.getCurrentInstance();
        String contraseniaEncriptada1 = Metodos.encripta(contrasenia);
        String contraseniaEncriptada2 = Metodos.encripta(contrasenia2);
        if (adm.getNombre() != null) {
            if (adm.getContrasenia().equals(contraseniaEncriptada1)) {
                adm.setContrasenia(contraseniaEncriptada2);
                administradorJpaController.edit(adm);
                rolBean.editarUsuario(adm.getCorreo(), adm.getContrasenia(), adm.getCorreo(), "administrador");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Contraseña ha sido modificada", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Contraseña anterior incorrecta", null));
            }
        } else if (capacitador.getNombre() != null) {
            if (capacitador.getContrasenia().equals(contraseniaEncriptada1)) {
                capacitador.setContrasenia(contraseniaEncriptada2);
                capacitadorJpaController.edit(capacitador);
                rolBean.editarUsuario(capacitador.getCorreo(), capacitador.getContrasenia(), capacitador.getCorreo(), "capacitador");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Contraseña ha sido modificada", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Contraseña anterios incorrecta", null));
            }
        } else if (capacitado.getNombre() != null) {
            if (capacitado.getContrasenia().equals(contraseniaEncriptada1)) {
                capacitado.setContrasenia(contraseniaEncriptada2);
                capacitadoJpaController.edit(capacitado);
                rolBean.editarUsuario(capacitado.getCorreo(), capacitado.getContrasenia(), capacitado.getCorreo(), "capacitado");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Contraseña ha sido modificada", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Contraseña anterios incorrecta", null));
            }
        }
        contrasenia = "";
    }

    public void guardarAdministrador() throws RollbackFailureException, Exception {
        System.out.println(adm.getApellido());
        boolean cedulaExiste;
        boolean celularExiste;
        boolean correoExiste;
        Administrador administradorAnterior = administradorJpaController.findAdministrador(adm.getId());
        if (!administradorJpaController.cedulaRepetida(adm).isEmpty()) {
            cedulaExiste = administradorJpaController.cedulaRepetida(adm).get(0).getId() == adm.getId();
        } else {
            cedulaExiste = administradorJpaController.cedulaRepetida(adm).isEmpty();
        }
        if (!administradorJpaController.celularRepetido(adm).isEmpty()) {
            celularExiste = administradorJpaController.celularRepetido(adm).get(0).getId() == adm.getId();
        } else {
            celularExiste = administradorJpaController.celularRepetido(adm).isEmpty();
        }
        if (!administradorJpaController.correoRepetido(adm).isEmpty()) {
            correoExiste = administradorJpaController.correoRepetido(adm).get(0).getId() == adm.getId();
        } else {
            correoExiste = administradorJpaController.correoRepetido(adm).isEmpty();
        }

        if (cedulaExiste) {
            if (celularExiste) {
                if (correoExiste) {
                    if (adm.getTipoIdentificacion() == 1) {
                        if (Metodos.validadorDeCedula(adm.getCedula())) {
                            administradorJpaController.edit(adm);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El Capacitador " + adm.getNombre() + " " + adm.getApellido()
                                    + " ha sido modificado", null));
                            rolBean.editarUsuario(adm.getCorreo(), adm.getContrasenia(), administradorAnterior.getCorreo(), "administrador");
                            loginBean.setNombre(adm.getNombre() + " " + adm.getApellido());
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "El número de cédula de " + adm.getNombre() + " " + adm.getApellido()
                                    + " es incorrecto", null));
                        }
                    } else {
                        administradorJpaController.edit(adm);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "El Capacitador " + adm.getNombre() + " " + adm.getApellido()
                                + " ha sido modificado", null));
                        rolBean.editarUsuario(adm.getCorreo(), adm.getContrasenia(), administradorAnterior.getCorreo(), "administrador");
                        loginBean.setNombre(adm.getNombre() + " " + adm.getApellido());
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

    public void guardarCapacitador() throws RollbackFailureException, Exception {
        System.out.println("hola");
        System.out.println("  " + capacitador.getNombre() + " " + capacitador.getApellido());

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
                            rolBean.editarUsuario(capacitador.getCorreo(), capacitador.getContrasenia(), capacitadorAnterior.getCorreo(), "capacitador");
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
                        rolBean.editarUsuario(capacitador.getCorreo(), capacitador.getContrasenia(), capacitadorAnterior.getCorreo(), "capacitador");
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

    public void guardarCapacitado() throws RollbackFailureException, Exception {
        System.out.println("hola");
        System.out.println("  " + capacitador.getNombre());
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
                            rolBean.editarUsuario(capacitado.getCorreo(), capacitado.getContrasenia(), capacitadoAnterior.getCorreo(), "capacitado");
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
                        rolBean.editarUsuario(capacitado.getCorreo(), capacitado.getContrasenia(), capacitadoAnterior.getCorreo(), "capacitado");
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
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public Administrador getAdm() {
        adm = this.adm;
        return adm;
    }

    public void setAdm(Administrador adm) {
        this.adm = adm;
    }

    public Capacitador getCapacitador() {
        return capacitador;
    }

    public void setCapacitador(Capacitador capacitador) {
        this.capacitador = capacitador;
    }

    public Capacitado getCapacitado() {
        return capacitado;
    }

    public void setCapacitado(Capacitado capacitado) {
        this.capacitado = capacitado;
    }

    public RolBean getRolBean() {
        return rolBean;
    }

    public void setRolBean(RolBean rolBean) {
        this.rolBean = rolBean;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getContrasenia2() {
        return contrasenia2;
    }

    public void setContrasenia2(String contrasenia2) {
        this.contrasenia2 = contrasenia2;
    }

}
