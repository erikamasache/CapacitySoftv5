package unl.edu.bean.rol;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.NamingException;
import unl.edu.jpa.controllers.GruposJpaController;
import unl.edu.jpa.controllers.UsuariosJpaController;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Grupos;
import unl.edu.jpa.entities.Usuarios;

/**
 *
 * @author ERIKA
 */

@ManagedBean
@SessionScoped
public class RolBean implements Serializable {

    private Usuarios usuarios;
    private UsuariosJpaController usuariosJpaController;
    private Grupos grupos;
    private GruposJpaController gruposJpaController;

    public RolBean() throws NamingException {
        usuarios = new Usuarios();
        usuariosJpaController = new UsuariosJpaController();
        grupos = new Grupos();
        gruposJpaController = new GruposJpaController();
    }

    public void nuevoUsuario(String correo, String contrasenia,String grupo) throws RollbackFailureException, Exception {
        usuarios.setCorreo(correo);
        usuarios.setContrasenia(contrasenia);
        usuariosJpaController.create(usuarios);
        grupos.setCorreo(usuarios);
        grupos.setRol(grupo);
        gruposJpaController.create(grupos);
        usuarios = new Usuarios();
    }

    public void editarUsuario(String correo, String contrasenia, String correoAnterior, String grupo) throws RollbackFailureException, Exception {
        usuariosJpaController.destroy(correoAnterior);
        usuarios.setCorreo(correo);
        usuarios.setContrasenia(contrasenia);
        usuariosJpaController.create(usuarios);
        grupos.setCorreo(usuarios);
        grupos.setRol(grupo);
        gruposJpaController.create(grupos);
        usuarios = new Usuarios();
    }
    
    public void eliminarUsuario(String correo) throws RollbackFailureException, Exception{
        usuariosJpaController.destroy(correo);
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    public Grupos getGrupos() {
        return grupos;
    }

    public void setGrupos(Grupos grupos) {
        this.grupos = grupos;
    }

}
