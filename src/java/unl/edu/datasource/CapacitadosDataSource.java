/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.datasource;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import unl.edu.jpa.entities.Capacitado;

/**
 *
 * @author ERIKA
 */
public class CapacitadosDataSource implements JRDataSource {

    private List<Capacitado> listaCapacitados = new ArrayList<Capacitado>();
    private int indiceCapacitadoActual = -1;

    @Override
    public boolean next() throws JRException {
        return ++indiceCapacitadoActual < listaCapacitados.size();
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        Object valor = null;

        if ("nombre".equals(jrField.getName())) {
            valor = listaCapacitados.get(indiceCapacitadoActual).getNombre();
        } else if ("apellido".equals(jrField.getName())) {
            valor = listaCapacitados.get(indiceCapacitadoActual).getApellido();
        } else if ("sector".equals(jrField.getName())) {
            valor = listaCapacitados.get(indiceCapacitadoActual).getSectorId().getNombre();
        }
        return valor;
    }

    public void addCapacitado(Capacitado capacitado) {
        this.listaCapacitados.add(capacitado);
    }

    public List<Capacitado> getListaCapacitados() {
        return listaCapacitados;
    }

    public void setListaCapacitados(List<Capacitado> listaCapacitados) {
        this.listaCapacitados = listaCapacitados;
    }
    
}
