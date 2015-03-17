/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.face.converters;

import unl.edu.jpa.controllers.TipoCapacitadoJpaController;
import unl.edu.jpa.entities.TipoCapacitado;
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
@FacesConverter(value = "tipoConverter")
public class TipoConverter implements Converter {

    private TipoCapacitadoJpaController tipoCapacitadoJpaController;
    private List<TipoCapacitado> listaTiposCapacitados;

    public TipoConverter() throws NamingException {
        tipoCapacitadoJpaController = new TipoCapacitadoJpaController();
        listaTiposCapacitados = new ArrayList<>();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TipoCapacitado tipoCapacitado = null;

        if (value != null && value.trim().length() > 0) {
            try {
                int id_sector = Integer.parseInt(value);
                listaTiposCapacitados = tipoCapacitadoJpaController.findTipoCapacitadoEntities();
                for (int i = 0; i < listaTiposCapacitados.size(); i++) {
                    if (listaTiposCapacitados.get(i).getId() == id_sector) {
                        tipoCapacitado = listaTiposCapacitados.get(i);
                        return tipoCapacitado;
                    }
                }
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return tipoCapacitado;
        }
        return tipoCapacitado;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String str = "";
        if (value instanceof TipoCapacitado) {
            str = "" + ((TipoCapacitado) value).getId();
        }
        return str;
    }
}
