/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.face.converters;

import unl.edu.jpa.controllers.SectorJpaController;
import unl.edu.jpa.entities.Sector;
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
@FacesConverter(value = "sectorConverter")
public class SectorConverter implements Converter {

    private SectorJpaController sectorJpaController;
    private List<Sector> listaSectores;

    public SectorConverter() throws NamingException {
        sectorJpaController = new SectorJpaController();
        listaSectores = new ArrayList<>();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Sector sector = null;

        if (value != null && value.trim().length() > 0) {
            try {
                int id_sector = Integer.parseInt(value);
                listaSectores = sectorJpaController.findSectorEntities();
                for (int i = 0; i < listaSectores.size(); i++) {
                    if (listaSectores.get(i).getId() == id_sector) {
                        sector = listaSectores.get(i);
                        return sector;
                    }
                }
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return sector;
        }
        return sector;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String str = "";
        if (value instanceof Sector) {
            str = "" + ((Sector) value).getId();
        }
        return str;
    }
}
