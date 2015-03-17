package unl.edu.bean.capacitado;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import unl.edu.bean.loginReg.LoginBean;
import unl.edu.jpa.controllers.CapacitacionJpaController;
import unl.edu.jpa.controllers.RegistraJpaController;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Registra;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@RequestScoped
public class CapacitacionCapacitadoBean {

    private Capacitacion capacitacion;
    private CapacitacionJpaController capacitacionJpaController;
    private List<Capacitacion> listaCapacitacionesPasadas;
    private List<Capacitacion> listaCapacitacionesPenientes;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;
    Capacitado capacitado;

    private Registra registra;
    private RegistraJpaController registraJpaController;
    private List<Registra> listaRegistrados;

    public CapacitacionCapacitadoBean() throws NamingException {
        capacitacion = new Capacitacion();
        capacitacionJpaController = new CapacitacionJpaController();
        listaCapacitacionesPasadas = new ArrayList<>();
        listaCapacitacionesPenientes = new ArrayList<>();
        registra = new Registra();
        registraJpaController = new RegistraJpaController();
        listaRegistrados = new ArrayList<>();
    }

    public String obtenerCapacitacion(Capacitacion capacitacion1) throws Exception {
        capacitacion = capacitacion1;
        return "recursoCapacitado?faces-redirect=true";
    }

    public boolean textoRegistrarse(Capacitacion cap) {
        System.out.println("capacitacion " + cap + " capacitado " + loginBean.getCapacitado());
        if (registraJpaController.registradoRepetido(cap, loginBean.getCapacitado()).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void registrarse() throws Exception {
        Date fecha = new Date();
        listaRegistrados = new ArrayList<>();
        capacitado = loginBean.getCapacitado();
        registra.setCapacitacionId(capacitacion);
        registra.setCapacitadoId(capacitado);
        registra.setFecha(fecha);
        registra.setHora(fecha);
        registra.setAsistencia(false);
        listaRegistrados = registraJpaController.numeroRegistrados(capacitacion);
        if (listaRegistrados.size() < capacitacion.getNumMaxCapacitados()) {
            if (registraJpaController.registradoRepetido(capacitacion, capacitado).isEmpty()) {
                registraJpaController.create(registra);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Registrado en la capacitación" + capacitacion.getTema(), null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Ya se encuentra registrado en esta capacitación" + capacitacion.getTema(), null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Lamentamos informarle que en la capacitación "
                    + capacitacion.getTema()
                    + " el cupo ha excedido reportelo al administrador ", null));
        }
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public List<Capacitacion> getListaCapacitacionesPasadas() {
        listaCapacitacionesPasadas = new ArrayList<>();
        List<Capacitacion> listaCapacitaciones = capacitacionJpaController.findCapacitacionEntities();
        Date hoy = new Date();
        for (int i = 0; i < listaCapacitaciones.size(); i++) {
            if (listaCapacitaciones.get(i).getFechaInicio().getYear() < hoy.getYear()) {
//                System.out.println("a;o menor " + listaCapacitaciones.get(i).getFechaInicio().getYear());
                listaCapacitacionesPasadas.add(listaCapacitaciones.get(i));
            } else {
                if (listaCapacitaciones.get(i).getFechaInicio().getYear() == hoy.getYear()) {
                    if (listaCapacitaciones.get(i).getFechaInicio().getMonth() < hoy.getMonth()) {
//                        System.out.println("mismo-mes menor" + listaCapacitaciones.get(i).getFechaInicio().getMonth());
                        listaCapacitacionesPasadas.add(listaCapacitaciones.get(i));
                    }
                    if (listaCapacitaciones.get(i).getFechaInicio().getMonth() == hoy.getMonth()) {
                        if (listaCapacitaciones.get(i).getFechaInicio().getDate() < hoy.getDate()) {
//                            System.out.println("mismo-dia menor" + listaCapacitaciones.get(i).getFechaInicio().getDate());
                            listaCapacitacionesPasadas.add(listaCapacitaciones.get(i));
                        }
                    }
                }
            }
        }
        return listaCapacitacionesPasadas;
    }

    public void setListaCapacitacionesPasadas(List<Capacitacion> listaCapacitacionesPasadas) {
        this.listaCapacitacionesPasadas = listaCapacitacionesPasadas;
    }

    public List<Capacitacion> getListaCapacitacionesPenientes() {
        listaCapacitacionesPenientes = new ArrayList<>();
        List<Capacitacion> listaCapacitaciones = new ArrayList<>();
        List<Capacitacion> lista = new ArrayList<>();
        lista = capacitacionJpaController.findCapacitacionEntities();
        capacitado = loginBean.getCapacitado();
        int id = capacitado.getId();
        for (int i = 0; i < lista.size(); i++) {
            for (int j = 0; j < lista.get(i).getCapacitacionCapacitadoList().size(); j++) {
                if (lista.get(i).getCapacitacionCapacitadoList().get(j).getCapacitadoId().getId() == id) {
                    listaCapacitaciones.add(lista.get(i));
                }
            }
        }

        Date hoy = new Date();

        for (int i = 0; i < listaCapacitaciones.size(); i++) {
            if (listaCapacitaciones.get(i).getFechaInicio().getYear() > hoy.getYear()) {
                listaCapacitacionesPenientes.add(listaCapacitaciones.get(i));
            }
            if (listaCapacitaciones.get(i).getFechaInicio().getYear() == hoy.getYear()) {
                if (listaCapacitaciones.get(i).getFechaInicio().getMonth() > hoy.getMonth()) {
                    listaCapacitacionesPenientes.add(listaCapacitaciones.get(i));
                }
                if (listaCapacitaciones.get(i).getFechaInicio().getMonth() == hoy.getMonth()) {
                    if (listaCapacitaciones.get(i).getFechaInicio().getDate() >= hoy.getDate()) {
                        listaCapacitacionesPenientes.add(listaCapacitaciones.get(i));
                    }
                }
            }
        }

        return listaCapacitacionesPenientes;
    }

    public void setListaCapacitacionesPenientes(List<Capacitacion> listaCapacitacionesPenientes) {
        this.listaCapacitacionesPenientes = listaCapacitacionesPenientes;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

}
