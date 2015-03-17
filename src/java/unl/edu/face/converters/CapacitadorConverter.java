/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.face.converters;

import unl.edu.jpa.controllers.CapacitadorJpaController;
import unl.edu.jpa.entities.Capacitador;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.naming.NamingException;

/**
 *
 * @author ERIKA
 */
@FacesConverter(value = "capacitadorConverter")
public class CapacitadorConverter {

//    private CapacitadorJpaController capacitadorJpaController;
//    private List<Capacitador> listaCapacitadores;
//
//    public CapacitadorConverter() throws NamingException {
//        capacitadorJpaController = new CapacitadorJpaController();
//        listaCapacitadores = new ArrayList<>();
//    }
//
//    @Override
//    public Object getAsObject(FacesContext context, UIComponent component, String value) {
//        Capacitador capacitador = null;
//        if (value != null && value.trim().length() > 0) {
//            try {
//                int id_capacitador = Integer.parseInt(value);
//                listaCapacitadores = capacitadorJpaController.findCapacitadorEntities();
//                for (int i = 0; i < listaCapacitadores.size(); i++) {
//                    capacitador = listaCapacitadores.get(i);
//                    if (capacitador.getId() == id_capacitador) {
//                        capacitador = listaCapacitadores.get(i);
//                        break;
//                    }
//                }
//                return capacitador;
//            } catch (NumberFormatException e) {
//                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
//            }
//        } else {
//            return capacitador;
//        }
//    }
//
//    @Override
//    public String getAsString(FacesContext context, UIComponent component, Object value) {
//        String str = "";
//        if (value instanceof Capacitador) {
//            str = "" + ((Capacitador) value).getId();
//        }
//        return str;
//    }

}
