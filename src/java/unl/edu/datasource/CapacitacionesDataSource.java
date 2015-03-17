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
import unl.edu.jpa.entities.Capacitacion;

/**
 *
 * @author ERIKA
 */
public class CapacitacionesDataSource implements JRDataSource {

    private List<Capacitacion> listaCapacitaciones = new ArrayList<Capacitacion>();
    private int indiceCapacitadoActual = -1;

    @Override
    public boolean next() throws JRException {
        return ++indiceCapacitadoActual < listaCapacitaciones.size();
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        Object valor = null;

        if ("tema".equals(jrField.getName())) {
            valor = listaCapacitaciones.get(indiceCapacitadoActual).getTema();
        } else if ("lugar".equals(jrField.getName())) {
            valor = listaCapacitaciones.get(indiceCapacitadoActual).getLugar();
        } else if ("fecha_inicio".equals(jrField.getName())) {
            String dia = "";
            String mes = "";
            String anio = "";
            if (listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getDate() < 10) {
                dia = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getDate();
                if (listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getMonth() < 10) {
                    mes = "0" + (listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getMonth() + 1);
                } else {
                    mes = "" + (listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getMonth() + 1);
                }
            } else {
                dia = "" + listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getDate();
                if (listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getMonth() < 10) {
                    mes = "0" + (listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getMonth() + 1);
                } else {
                    mes = "" + (listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getMonth() + 1);
                }
            }
            anio = (listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getYear() + 1900) + "";
            valor = dia + "-" + mes + "-" + anio;
        } else if ("fecha_fin".equals(jrField.getName())) {
            String dia = "";
            String mes = "";
            String anio = "";
            if (listaCapacitaciones.get(indiceCapacitadoActual).getFechaFin().getDate() < 10) {
                dia = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getFechaFin().getDate();
                if (listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getMonth() < 10) {
                    mes = "0" + (listaCapacitaciones.get(indiceCapacitadoActual).getFechaFin().getMonth() + 1);
                } else {
                    mes = "" + (listaCapacitaciones.get(indiceCapacitadoActual).getFechaFin().getMonth() + 1);
                }
            } else {
                dia = "" + listaCapacitaciones.get(indiceCapacitadoActual).getFechaFin().getDate();
                if (listaCapacitaciones.get(indiceCapacitadoActual).getFechaInicio().getMonth() < 10) {
                    mes = "0" + (listaCapacitaciones.get(indiceCapacitadoActual).getFechaFin().getMonth() + 1);
                } else {
                    mes = "" + (listaCapacitaciones.get(indiceCapacitadoActual).getFechaFin().getMonth() + 1);
                }
            }
            anio = (listaCapacitaciones.get(indiceCapacitadoActual).getFechaFin().getYear() + 1900) + "";
            valor = dia + "-" + mes + "-" + anio;

        } else if ("horario".equals(jrField.getName())) {
            String hora1 = "";
            String minuto1 = "";
            String hora2 = "";
            String minuto2 = "";
            if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraInicio().getHours() < 10) {
                hora1 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraInicio().getHours();
                if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraInicio().getMinutes() < 10) {
                    minuto1 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraInicio().getMinutes();
                    if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours() < 10) {
                        hora2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours();
                        if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes() < 10) {
                            minuto2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        } else {
                            minuto2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        }
                    } else {
                        hora2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours();
                        if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes() < 10) {
                            minuto2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        } else {
                            minuto2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        }
                    }
                } else {
                    minuto1 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraInicio().getMinutes();
                    if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours() < 10) {
                        hora2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours();
                        if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes() < 10) {
                            minuto2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        } else {
                            minuto2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        }
                    } else {
                        hora2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours();
                        if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes() < 10) {
                            minuto2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        } else {
                            minuto2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        }
                    }
                }
            } else {
                hora1 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraInicio().getHours();
                if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraInicio().getMinutes() < 10) {
                    minuto1 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraInicio().getMinutes();
                    if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours() < 10) {
                        hora2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours();
                        if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes() < 10) {
                            minuto2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        } else {
                            minuto2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        }
                    } else {
                        hora2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours();
                        if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes() < 10) {
                            minuto2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        } else {
                            minuto2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        }
                    }
                } else {
                    minuto1 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraInicio().getMinutes();
                    if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours() < 10) {
                        hora2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours();
                        if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes() < 10) {
                            minuto2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        } else {
                            minuto2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        }
                    } else {
                        hora2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getHours();
                        if (listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes() < 10) {
                            minuto2 = "0" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        } else {
                            minuto2 = "" + listaCapacitaciones.get(indiceCapacitadoActual).getHoraFin().getMinutes();
                        }
                    }
                }
            }
            valor = hora1 + ":" + minuto1 + "-" + hora2 + ":" + minuto2;
        }
        return valor;
    }

    public void addCapacitacion(Capacitacion capacitacion) {
        this.listaCapacitaciones.add(capacitacion);
    }

    public List<Capacitacion> getListaCapacitaciones() {
        return listaCapacitaciones;
    }

    public void setListaCapacitaciones(List<Capacitacion> listaCapacitaciones) {
        this.listaCapacitaciones = listaCapacitaciones;
    }
}
