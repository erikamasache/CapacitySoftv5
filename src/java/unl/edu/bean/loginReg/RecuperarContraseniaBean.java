/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.loginReg;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import unl.edu.jpa.controllers.AdministradorJpaController;
import unl.edu.jpa.controllers.CapacitadoJpaController;
import unl.edu.jpa.controllers.CapacitadorJpaController;
import unl.edu.jpa.controllers.UsuariosJpaController;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Administrador;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Capacitador;
import unl.edu.jpa.entities.Metodos;
import unl.edu.jpa.entities.Usuarios;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@RequestScoped
public class RecuperarContraseniaBean implements Serializable {

    private String correo;
    private Administrador adm;
    private AdministradorJpaController administradorJpaController;
    private Capacitado capacitado;
    private CapacitadoJpaController capacitadoJpaController;
    private Capacitador capacitador;
    private CapacitadorJpaController capacitadorJpaController;
    private Usuarios usuario;
    private UsuariosJpaController usuarioJpaController;

    public RecuperarContraseniaBean() throws NamingException {
        adm = new Administrador();
        administradorJpaController = new AdministradorJpaController();
        capacitado = new Capacitado();
        capacitadoJpaController = new CapacitadoJpaController();
        capacitador = new Capacitador();
        capacitadorJpaController = new CapacitadorJpaController();
        usuario = new Usuarios();
        usuarioJpaController = new UsuariosJpaController();
    }

    public void recuperarContrasenia() throws RollbackFailureException, Exception {
        String contraseniaSinEncriptar;
        if (!administradorJpaController.obtenerCorreo(correo).isEmpty()) {
            adm = administradorJpaController.obtenerCorreo(correo).get(0);
            contraseniaSinEncriptar = Metodos.generarContrasenia();
            try {
                if (Metodos.enviarMail(adm.getCorreo(), contraseniaSinEncriptar)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Revise su correo electrónico " + adm.getCorreo(), null));
                    adm.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                    administradorJpaController.edit(adm);
                    usuario = usuarioJpaController.findUsuarios(correo);
                    usuario.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                    usuarioJpaController.edit(usuario);
                }

            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Error de conexion, porfavor revice si su conexion a internet esta disponible", null));
            }
        } else {
            if (!capacitadorJpaController.obtenerCorreo(correo).isEmpty()) {
                capacitador = capacitadorJpaController.obtenerCorreo(correo).get(0);
                contraseniaSinEncriptar = Metodos.generarContrasenia();
                try {
                    if (Metodos.enviarMail(capacitador.getCorreo(), contraseniaSinEncriptar)) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Revise su correo electrónico " + capacitador.getCorreo(), null));
                        capacitado.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                        capacitadoJpaController.edit(capacitado);
                        usuario = usuarioJpaController.findUsuarios(correo);
                        usuario.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                        usuarioJpaController.edit(usuario);
                    }

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Error de conexion, porfavor revice si su conexion a internet esta disponible", null));
                }
            } else {
                if (!capacitadoJpaController.obtenerCorreo(correo).isEmpty()) {
                    capacitado = capacitadoJpaController.obtenerCorreo(correo).get(0);
                    contraseniaSinEncriptar = Metodos.generarContrasenia();
                    try {
                        if (Metodos.enviarMail(capacitado.getCorreo(), contraseniaSinEncriptar)) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Revise su correo electrónico " + capacitado.getCorreo(), null));
                            capacitador.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                            capacitadorJpaController.edit(capacitador);
                            usuario = usuarioJpaController.findUsuarios(correo);
                            usuario.setContrasenia(Metodos.encripta(contraseniaSinEncriptar));
                            usuarioJpaController.edit(usuario);
                        }
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Error de conexion, porfavor revice si su conexion a internet esta disponible", null));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Usted no se encuentra registrado", null));
                }
            }
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
