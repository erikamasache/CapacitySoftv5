/**
 * Clase CapacitacionBean esta clase sirve para acceder al conrolador y contiene
 * los metodos Guardar, Editar, Eliminar
 */
package unl.edu.bean.capacitacion;

import unl.edu.bean.capacitador.CapacitadorBean;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.naming.NamingException;

/**
 *
 * @author ERIKA
 */
@ManagedBean
@RequestScoped
public class CapacitacionBean implements Serializable {

    @ManagedProperty("#{capacitadorBean}")
    private CapacitadorBean capacitadorBean;
    @ManagedProperty("#{calendarioBean}")
    private CalendarioBean calendarioBean;
    

    public CapacitacionBean() throws NamingException {
    }

    public void crearCapacitacion(ActionEvent actionEvent) throws Exception {
        calendarioBean.addEvent(actionEvent);
    }
    
    public void crearCapacitacion() throws Exception{
        calendarioBean.crear();
    }

///////////////setters y getters

    public CapacitadorBean getCapacitadorBean() {
        return capacitadorBean;
    }

    public void setCapacitadorBean(CapacitadorBean capacitadorBean) {
        this.capacitadorBean = capacitadorBean;
    }

    public CalendarioBean getCalendarioBean() {
        return calendarioBean;
    }

    public void setCalendarioBean(CalendarioBean calendarioBean) {
        this.calendarioBean = calendarioBean;
    }


}
