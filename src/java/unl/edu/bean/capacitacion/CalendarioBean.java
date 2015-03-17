/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.bean.capacitacion;

import unl.edu.jpa.controllers.CapacitacionCapacitadoJpaController;
import unl.edu.jpa.controllers.CapacitacionCapacitadorJpaController;
import unl.edu.jpa.controllers.CapacitacionJpaController;
import unl.edu.jpa.controllers.CapacitadoJpaController;
import unl.edu.jpa.controllers.CapacitadorJpaController;
import unl.edu.jpa.controllers.FechasFestivasJpaController;
import unl.edu.jpa.controllers.TipoCapacitadoJpaController;
import unl.edu.jpa.controllers.exceptions.NonexistentEntityException;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.CapacitacionCapacitador;
import unl.edu.jpa.entities.CapacitacionCapacitado;
import unl.edu.jpa.entities.Capacitador;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Evaluacion;
import unl.edu.jpa.entities.FechasFestivas;
import unl.edu.jpa.entities.Sector;
import unl.edu.jpa.entities.TipoCapacitado;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DualListModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import unl.edu.bean.loginReg.LoginBean;
import unl.edu.datasource.CapacitadosDataSource;

/**
 * Clase que permite administrar la planificación de las capacitaciones para
 * ello se ha declarado los siquientes atributos ScheduleModel eventModel;
 * ScheduleEvent event = new DefaultScheduleEvent(); Capacitacion capacitacion;
 * CapacitacionJpaController capacitacionJpaController; List<Capacitacion>
 * listaCapacitaciones; Sector sector; List<Capacitador> listaCapacitadores;
 *
 * @author Erika Masache
 * @author Jorge Mejía
 */
@ManagedBean
@SessionScoped
public class CalendarioBean implements Serializable {

    // calendario
    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private Capacitacion capacitacion;
    private final CapacitacionJpaController capacitacionJpaController;
    private List<Capacitacion> listaCapacitaciones;

    //lista capacitadores
    private final CapacitadorJpaController capacitadorJpaController;

    private final CapacitacionCapacitadorJpaController capacitacionCapacitadorJpaController;
    private CapacitacionCapacitador capacitacionCapacitador;
    private List<CapacitacionCapacitador> listCapacitacionCapacitador;

    private DualListModel<Capacitador> listDobleCapacitadores;
    private List<Capacitador> capSourceCapacitadores;
    private List<Capacitador> capTargetCapacitadores;

    //lista capacitados
    private final CapacitacionCapacitadoJpaController capacitacionCapacitadoJpaController;
    private final CapacitacionCapacitado capacitacionCapacitado;
    private final List<CapacitacionCapacitado> listCapacitacionCapacitados;

    private final CapacitadoJpaController capacitadoJpaController;

    private DualListModel<Capacitado> listDobleCapacitados;
    private List<Capacitado> capSourceCapacitados;
    private List<Capacitado> capTargetCapacitados;

    private List<TipoCapacitado> listTipoCapacitado;
    private final TipoCapacitadoJpaController tipoCapacitadoJpaController;
    private TipoCapacitado tipoCapacitado;

    private Sector sec;

    private Evaluacion eval;
    @ManagedProperty("#{evaluacionBean}")
    private EvaluacionBean evaluacionBean;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    //--- fechas
    private Date maxFecha;
    private Date minFecha;
    private Date maxFechaCap;
    private Date minFechaCap;
    private int horaInicio;
    private int horaFin;

    //------------ Fecha festivas
    private FechasFestivas fechasFestivas;
    private FechasFestivasJpaController fechasFestivasJpaController;
    private List<FechasFestivas> listaFechasFestivas;

    /**
     * Constructor que permite
     *
     * @throws NamingException
     */
    public CalendarioBean() throws NamingException {
        capacitacion = new Capacitacion();
        // calendario
        event = new DefaultScheduleEvent();
        capacitacionJpaController = new CapacitacionJpaController();
        listaCapacitaciones = new ArrayList<>();
        // lista capacitadores
        capacitadorJpaController = new CapacitadorJpaController();
        listCapacitacionCapacitador = new ArrayList<>();
        capacitacionCapacitadorJpaController = new CapacitacionCapacitadorJpaController();
        capacitacionCapacitador = new CapacitacionCapacitador();
        capTargetCapacitadores = new ArrayList<>();
        capSourceCapacitadores = new ArrayList<>();
        listDobleCapacitadores = new DualListModel<>(capSourceCapacitadores, capTargetCapacitadores);
        // lista capacitados
        capacitacionCapacitadoJpaController = new CapacitacionCapacitadoJpaController();
        capacitacionCapacitado = new CapacitacionCapacitado();
        listCapacitacionCapacitados = new ArrayList<>();

        capacitadoJpaController = new CapacitadoJpaController();

        capSourceCapacitados = new ArrayList<>();
        capTargetCapacitados = new ArrayList<>();
        listDobleCapacitados = new DualListModel<>(capSourceCapacitados, capTargetCapacitados);

        tipoCapacitadoJpaController = new TipoCapacitadoJpaController();
        listTipoCapacitado = new ArrayList<>();
        tipoCapacitado = new TipoCapacitado();
        sec = new Sector();
        eval = new Evaluacion();

        fechasFestivas = new FechasFestivas();
        fechasFestivasJpaController = new FechasFestivasJpaController();
        listaFechasFestivas = new ArrayList<>();

    }

    /**
     * El método init es el que permite especificar las capacitaciones en el
     * calendario
     *
     * ALTER TABLE nombre_tabla AUTO_INCREMENT = 1
     */
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        listaCapacitaciones = capacitacionJpaController.listarCapacitaciones();
        Capacitacion auxCap;
        for (int i = 0; i < listaCapacitaciones.size(); i++) {
            auxCap = listaCapacitaciones.get(i);
            int dia = auxCap.getFechaInicio().getDay();
            int inicio = auxCap.getFechaInicio().getDate();
            int fin = auxCap.getFechaFin().getDate();

            if (inicio == fin) {
                if (auxCap.getFinesSemana()) {
                    DefaultScheduleEvent evento = new DefaultScheduleEvent();
                    evento.setTitle(auxCap.getTema());
                    evento.setDescription(Integer.toString(auxCap.getId()));
                    evento.setStartDate(auxCap.getFechaInicio());
                    evento.setEndDate(auxCap.getFechaFin());
                    evento.setStyleClass("capacitacion");
                    eventModel.addEvent(evento);
                } else {
                    if (dia == 0 || dia == 6) {

                    } else {
                        DefaultScheduleEvent evento = new DefaultScheduleEvent();
                        evento.setTitle(auxCap.getTema());
                        evento.setDescription(Integer.toString(auxCap.getId()));
                        evento.setStartDate(auxCap.getFechaInicio());
                        evento.setEndDate(auxCap.getFechaFin());
                        evento.setStyleClass("capacitacion");
                        eventModel.addEvent(evento);
                    }
                }
            } else {
                for (int j = inicio; j <= fin; j++) {
                    if (auxCap.getFinesSemana()) {
                        DefaultScheduleEvent evento = new DefaultScheduleEvent();
                        evento.setTitle(auxCap.getTema());
                        evento.setDescription(Integer.toString(auxCap.getId()));
                        Date fechaInicio = new Date(auxCap.getFechaInicio().getYear(),
                                auxCap.getFechaInicio().getMonth(),
                                j,
                                auxCap.getFechaInicio().getHours(),
                                auxCap.getFechaInicio().getMinutes(),
                                auxCap.getFechaInicio().getSeconds());
                        evento.setStartDate(fechaInicio);
                        evento.setEndDate(new Date(auxCap.getFechaFin().getYear(),
                                auxCap.getFechaFin().getMonth(),
                                j,
                                auxCap.getFechaFin().getHours(),
                                auxCap.getFechaFin().getMinutes(),
                                auxCap.getFechaFin().getSeconds()));
                        evento.setStyleClass("capacitacion");
                        eventModel.addEvent(evento);
                    } else {
                        if (dia == 7) {
                            dia = 1;
                        } else if (dia == 6) {
                            dia++;
                        } else {
                            dia++;
                            DefaultScheduleEvent evento = new DefaultScheduleEvent();
                            evento.setTitle(auxCap.getTema());
                            evento.setDescription(Integer.toString(auxCap.getId()));
                            Date fechaInicio = new Date(auxCap.getFechaInicio().getYear(),
                                    auxCap.getFechaInicio().getMonth(),
                                    j,
                                    auxCap.getFechaInicio().getHours(),
                                    auxCap.getFechaInicio().getMinutes(),
                                    auxCap.getFechaInicio().getSeconds());
                            evento.setStartDate(fechaInicio);
                            evento.setEndDate(new Date(auxCap.getFechaFin().getYear(),
                                    auxCap.getFechaFin().getMonth(),
                                    j,
                                    auxCap.getFechaFin().getHours(),
                                    auxCap.getFechaFin().getMinutes(),
                                    auxCap.getFechaFin().getSeconds()));
                            evento.setStyleClass("capacitacion");
                            eventModel.addEvent(evento);

                        }
                    }

                }
            }
        }

        listaFechasFestivas = fechasFestivasJpaController.findFechasFestivasEntities();
        FechasFestivas fechaFestiva;
//        int dia = 0;
        for (int i = 0; i < listaFechasFestivas.size(); i++) {
            fechaFestiva = listaFechasFestivas.get(i);
            int dia = fechaFestiva.getFechaInicio().getDay();
            int inicio = fechaFestiva.getFechaInicio().getDate();
            int fin = fechaFestiva.getFechaFin().getDate();

            if (inicio == fin) {
                DefaultScheduleEvent evento = new DefaultScheduleEvent();
                evento.setTitle(fechaFestiva.getAcontecimiento());
                evento.setDescription(Integer.toString(fechaFestiva.getId()));
                evento.setStartDate(fechaFestiva.getFechaInicio());
                evento.setEndDate(fechaFestiva.getFechaFin());
                evento.setStyleClass("feriado");
                eventModel.addEvent(evento);
            } else {
                for (int j = inicio; j <= fin; j++) {

                    DefaultScheduleEvent evento = new DefaultScheduleEvent();
                    evento.setTitle(fechaFestiva.getAcontecimiento());
                    evento.setDescription(Integer.toString(fechaFestiva.getId()));
                    Date fechaInicio = new Date(fechaFestiva.getFechaInicio().getYear(),
                            fechaFestiva.getFechaInicio().getMonth(),
                            j,
                            fechaFestiva.getFechaInicio().getHours(),
                            fechaFestiva.getFechaInicio().getMinutes(),
                            fechaFestiva.getFechaInicio().getSeconds());
                    evento.setStartDate(fechaInicio);
                    evento.setEndDate(new Date(fechaFestiva.getFechaFin().getYear(),
                            fechaFestiva.getFechaFin().getMonth(),
                            j,
                            fechaFestiva.getFechaFin().getHours(),
                            fechaFestiva.getFechaFin().getMinutes(),
                            fechaFestiva.getFechaFin().getSeconds()));
                    evento.setStyleClass("feriado");
                    eventModel.addEvent(evento);

                }
            }
        }
    }

    /**
     * Este método permite inicializar la fecha del calendario
     *
     * @return Date
     */
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(
                Calendar.YEAR),
                Calendar.FEBRUARY,
                calendar.get(Calendar.DATE), 0, 0, 0);
        return calendar.getTime();
    }

    public String update() {
        String update;
        if (event.getStyleClass().equals("capacitacion")) {
            update = ":detalles";
        } else {
            update = ":detallesFechaFestiva";
        }
        return update;
    }

    /**
     * Método que permite saber que capacitacion ha sido seleccionada del
     * calendario
     *
     * @param selectEvent
     */
    public void onEventSelect(SelectEvent selectEvent) {
        RequestContext context = RequestContext.getCurrentInstance();
        event = (ScheduleEvent) selectEvent.getObject();

        if (event.getStyleClass().equals("capacitacion")) {
            capacitacion = capacitacionJpaController.findCapacitacion(Integer.parseInt(event.getDescription() + ""));
            System.out.println("capacitacion " + capacitacion.getFinesSemana() + "  fecha  " + capacitacion.getFechaInicio().getDay());
            context.update(":detalles");
            context.execute("PF('CrearCapacitacion').show();");
            // fechas
            maxFechaCap = capacitacion.getFechaFin();
            minFechaCap = capacitacion.getFechaInicio();
            horaInicio = capacitacion.getHoraInicio().getHours();
            horaFin = capacitacion.getHoraFin().getHours();

            //-------------// Capacitadores //-------------//
            capTargetCapacitadores = new ArrayList<>();
            capSourceCapacitadores = new ArrayList<>();
            List<Capacitador> listaCdores;
            List<CapacitacionCapacitador> listaCapnCdores;
            listaCapnCdores = capacitacion.getCapacitacionCapacitadorList();
            if (!listaCapnCdores.isEmpty()) {
                for (int i = 0; i < listaCapnCdores.size(); i++) {
                    capTargetCapacitadores.add(listaCapnCdores.get(i).getCapacitadorId());
                }
            }
            listaCdores = capacitadorJpaController.findCapacitadorEntities();
            for (int i = 0; i < listaCdores.size(); i++) {
                if (!capTargetCapacitadores.contains(listaCdores.get(i))) {
                    capSourceCapacitadores.add(listaCdores.get(i));
                }
            }
            listDobleCapacitadores.setTarget(capTargetCapacitadores);
            listDobleCapacitadores.setSource(capSourceCapacitadores);
            //----------// Capacitados //---------//
            tipoCapacitado = new TipoCapacitado();
            sec = new Sector();
            sec = capacitacion.getSectorId();
            capSourceCapacitados = new ArrayList<>();
            capTargetCapacitados = new ArrayList<>();
            List<CapacitacionCapacitado> listaCapnCdos;
            listaCapnCdos = capacitacion.getCapacitacionCapacitadoList();
            if (!listaCapnCdos.isEmpty()) {
                for (int i = 0; i < listaCapnCdos.size(); i++) {
                    capTargetCapacitados.add(listaCapnCdos.get(i).getCapacitadoId());
                }
            }
            listDobleCapacitados.setTarget(capTargetCapacitados);
            listDobleCapacitados.setSource(capSourceCapacitados);
        }
        if (event.getStyleClass().equals("feriado")) {
            fechasFestivas = fechasFestivasJpaController.findFechasFestivas(Integer.parseInt(event.getDescription() + ""));
            context.update(":detallesFechaFestiva");
            context.execute("PF('VerFeriado').show();");
        }
    }

    public void cargarCapacitados() {
        sec = capacitacion.getSectorId();
        capSourceCapacitados = new ArrayList<>();
        capTargetCapacitados = new ArrayList<>();
        capTargetCapacitados = listDobleCapacitados.getTarget();
        List<Capacitado> listaCdos;
        listaCdos = capacitadoJpaController.findCapacitadoEntities();
        if (capTargetCapacitados.isEmpty()) {
            if (tipoCapacitado != null) {
                for (int u = 0; u < listaCdos.size(); u++) {
                    if (listaCdos.get(u).getSectorId().equals(sec)
                            && listaCdos.get(u).getTipoCapacitadoid().getId()
                            == tipoCapacitado.getId()) {
                        capSourceCapacitados.add(listaCdos.get(u));
                    }
                }
            } else {
                for (int u = 0; u < listaCdos.size(); u++) {
                    if (listaCdos.get(u).getSectorId().equals(sec)) {
                        capSourceCapacitados.add(listaCdos.get(u));
                    }
                }
            }
        } else {
            if (tipoCapacitado != null) {
                for (int i = 0; i < listaCdos.size(); i++) {
                    if (!capTargetCapacitados.contains(listaCdos.get(i))
                            && listaCdos.get(i).getTipoCapacitadoid().getId()
                            == tipoCapacitado.getId()
                            && listaCdos.get(i).getSectorId().equals(sec)) {
                        capSourceCapacitados.add(listaCdos.get(i));
                    }
                }
            } else {
                for (int i = 0; i < listaCdos.size(); i++) {
                    if (!capTargetCapacitados.contains(listaCdos.get(i))
                            && listaCdos.get(i).getSectorId().equals(sec)) {
                        capSourceCapacitados.add(listaCdos.get(i));
                    }
                }
            }

        }
        listDobleCapacitados.setTarget(capTargetCapacitados);
        listDobleCapacitados.setSource(capSourceCapacitados);
    }

    /**
     * Método que permite agregar o editar una capacitación
     *
     * @param actionEvent
     * @throws java.lang.Exception
     */
    public void addEvent(ActionEvent actionEvent) throws Exception {
        Evaluacion ev;
        List<CapacitacionCapacitador> listaCapacitacionCapacitadoresAddEvent;
        List<CapacitacionCapacitado> listaCapacitacionCapacitadosAddEvent;
        estadoCapacitacion();
        if (event.getId() == null) {
            if (capacitacion.getTema() != null) {
                if (capacitacionJpaController.nombreRepetido(capacitacion).isEmpty()) {
                    capacitacionJpaController.create(capacitacion);
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "La capacitación " + capacitacion.getTema()
                                    + " ha sido creada", null));
                    listaCapacitacionCapacitadoresAddEvent = guardarCapacitadores();
                    listaCapacitacionCapacitadosAddEvent = guardarCapacitados();
                    capacitacion.setCapacitacionCapacitadorList(listaCapacitacionCapacitadoresAddEvent);
                    capacitacion.setCapacitacionCapacitadoList(listaCapacitacionCapacitadosAddEvent);
                    capacitacionJpaController.edit(capacitacion);
                    eventModel.addEvent(event);
                    init();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "La capacitación " + capacitacion.getTema()
                                    + " ya existe", null));
                    init();
                }
            }

            init();
        } else {
            ev = capacitacion.getEvaluacionId();
            if (ev != null) {
                if (capacitacion.getFechaFin().getDate()
                        <= ev.getFechaInicio().getDate()) {
                    if (!capacitacion.getTema()
                            .equals(capacitacionJpaController
                                    .findCapacitacion(capacitacion.getId()).getTema())) {
                        ev.setTema(capacitacion.getTema());
                        evaluacionBean.editarEvaluacionCap(capacitacion, ev);
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Los cambios no pueden ser guardados " + capacitacion.getTema()
                                    + " existen conflictos con las fechas en evaluación", null));
                    System.out.println("Los cambios no pueden ser guardados");
                }
            }
            listaCapacitacionCapacitadoresAddEvent = guardarCapacitadores();
            listaCapacitacionCapacitadosAddEvent = guardarCapacitados();
            capacitacion.setCapacitacionCapacitadoList(listaCapacitacionCapacitadosAddEvent);
            capacitacion.setCapacitacionCapacitadorList(listaCapacitacionCapacitadoresAddEvent);
            capacitacionJpaController.edit(capacitacion);
            eventModel.updateEvent(event);
            init();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "La capacitación " + capacitacion.getTema()
                            + " ha sido modificada", null));
            System.out.println("modificada");
            init();
//            
        }
        event = new DefaultScheduleEvent();  //reset dialog form
        init();
        capacitacion = new Capacitacion();
    }

    public void crear() throws Exception {
        Evaluacion ev;
        List<CapacitacionCapacitador> listaCapacitacionCapacitadoresAddEvent;
        List<CapacitacionCapacitado> listaCapacitacionCapacitadosAddEvent;
        estadoCapacitacion();
        if (capacitacion.getId() == null) {
            if (capacitacion.getTema() != null) {
                if (capacitacionJpaController.nombreRepetido(capacitacion).isEmpty()) {
                    capacitacionJpaController.create(capacitacion);
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "La capacitación " + capacitacion.getTema()
                                    + " ha sido creada", null));
                    listaCapacitacionCapacitadoresAddEvent = guardarCapacitadores();
                    listaCapacitacionCapacitadosAddEvent = guardarCapacitados();
                    capacitacion.setCapacitacionCapacitadorList(listaCapacitacionCapacitadoresAddEvent);
                    capacitacion.setCapacitacionCapacitadoList(listaCapacitacionCapacitadosAddEvent);
                    capacitacionJpaController.edit(capacitacion);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "La capacitación " + capacitacion.getTema()
                                    + " ya existe", null));
                }
            }
        } else {
            ev = capacitacion.getEvaluacionId();
            if (ev != null) {
                if (capacitacion.getFechaFin().getDate()
                        <= ev.getFechaInicio().getDate()) {
                    if (!capacitacion.getTema()
                            .equals(capacitacionJpaController
                                    .findCapacitacion(capacitacion.getId()).getTema())) {
                        ev.setTema(capacitacion.getTema());
                        evaluacionBean.editarEvaluacionCap(capacitacion, ev);
                        System.out.println("Se modifico el tema de la evaluacion");
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Los cambios no pueden ser guardados " + capacitacion.getTema()
                                    + " existen conflictos con las fechas en evaluación", null));
                    System.out.println("Los cambios no pueden ser guardados");
                }
            }
            listaCapacitacionCapacitadoresAddEvent = guardarCapacitadores();
            listaCapacitacionCapacitadosAddEvent = guardarCapacitados();
            capacitacion.setCapacitacionCapacitadoList(listaCapacitacionCapacitadosAddEvent);
            capacitacion.setCapacitacionCapacitadorList(listaCapacitacionCapacitadoresAddEvent);
            capacitacionJpaController.edit(capacitacion);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "La capacitación " + capacitacion.getTema()
                            + " ha sido modificada", null));
            System.out.println("modificada");
        }
        capacitacion = new Capacitacion();
    }

    /**
     * Método que permite saber que fecha se ha seleccionado, para poder crear
     * una capacitación
     *
     * @param selectEvent
     */
    public void onDateSelect(SelectEvent selectEvent) {
        horaFin = 19;
        horaInicio = 7;
        minFechaCap = (Date) selectEvent.getObject();
        maxFechaCap = null;
        capTargetCapacitados = new ArrayList<>();
        capSourceCapacitados = new ArrayList<>();
        capTargetCapacitadores = new ArrayList<>();
        capSourceCapacitadores = new ArrayList<>();
        tipoCapacitado = new TipoCapacitado();
        capacitacion = new Capacitacion();
        capacitacion.setTema("");
        capacitacion.setFechaInicio((Date) selectEvent.getObject());
        capacitacion.setFechaFin((Date) selectEvent.getObject());
        capacitacion.setSectorId(null);
        capacitacion.setNumMaxCapacitados(15);
        capacitacion.setFinesSemana(false);
        capacitacion.setCapacitacionCapacitadorList(listCapacitacionCapacitador);
        List<Capacitador> listaCdores;
        listaCdores = capacitadorJpaController.findCapacitadorEntities();
        for (int i = 0; i < listaCdores.size(); i++) {
            capSourceCapacitadores.add(listaCdores.get(i));
        }
        listDobleCapacitadores.setSource(capSourceCapacitadores);
        listDobleCapacitadores.setTarget(capTargetCapacitadores);
        capacitacion.setCapacitacionCapacitadoList(listCapacitacionCapacitados);
        listDobleCapacitados.setTarget(capTargetCapacitados);
        listDobleCapacitados.setSource(capSourceCapacitados);
        event = new DefaultScheduleEvent(capacitacion.getTema(),
                capacitacion.getFechaInicio(), capacitacion.getFechaFin());
    }

    /**
     * Método que permite eliminar una capacitación
     *
     * @param actionEvent
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void eliminarCapacitacion(ActionEvent actionEvent)
            throws NonexistentEntityException, RollbackFailureException, Exception {
        eventModel.deleteEvent(event);
        capacitacionJpaController.destroy(capacitacion.getId());
        init();
    }

    public void eliminarFechaFestiva(ActionEvent actionEvent)
            throws NonexistentEntityException, RollbackFailureException, Exception {
        eventModel.deleteEvent(event);
        fechasFestivasJpaController.destroy(fechasFestivas.getId());
        init();
    }

    /// lista capacitadores
    public List<CapacitacionCapacitador> guardarCapacitadores() throws Exception {
        List<CapacitacionCapacitador> listaRetornarCapacitacionCapacitadores
                = new ArrayList<>();
        List<Capacitador> listaTargetCapacitador = listDobleCapacitadores.getTarget();
        List<CapacitacionCapacitador> listaCapacitacionCapacitadorDeCapacitacion
                = capacitacion.getCapacitacionCapacitadorList();

        if (!listaTargetCapacitador.isEmpty()) {
            for (int i = 0; i < listaTargetCapacitador.size(); i++) {
                CapacitacionCapacitador capnCapr = new CapacitacionCapacitador();
                capnCapr.setCapacitacionId(capacitacion);
                capnCapr.setCapacitadorId(listaTargetCapacitador.get(i));
                if (listaCapacitacionCapacitadorDeCapacitacion == null
                        || listaCapacitacionCapacitadorDeCapacitacion.isEmpty()) {
                    listaRetornarCapacitacionCapacitadores.add(capnCapr);
                    capacitacionCapacitadorJpaController.create(capnCapr);
                } else {
                    for (int j = 0; j < listaCapacitacionCapacitadorDeCapacitacion.size(); j++) {
                        if (listaCapacitacionCapacitadorDeCapacitacion.get(j)
                                .getCapacitadorId().equals(capnCapr.getCapacitadorId())
                                && listaCapacitacionCapacitadorDeCapacitacion
                                .get(j).getCapacitacionId().equals(capacitacion)) {
                            if (!listaRetornarCapacitacionCapacitadores
                                    .contains(listaCapacitacionCapacitadorDeCapacitacion.get(j))) {
                                listaRetornarCapacitacionCapacitadores
                                        .add(listaCapacitacionCapacitadorDeCapacitacion.get(j));
                            }
                            break;
                        } else {
                            if (!listaCapacitacionCapacitadorDeCapacitacion.get(j)
                                    .getCapacitadorId().equals(capnCapr.getCapacitadorId())
                                    && !listaRetornarCapacitacionCapacitadores.contains(capnCapr)) {
                                if (!listaRetornarCapacitacionCapacitadores.contains(capnCapr)) {
                                    listaRetornarCapacitacionCapacitadores.add(capnCapr);
                                    capacitacionCapacitadorJpaController.create(capnCapr);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Capacitadores Agregados a la capacitación ", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "No hay capacitadores seleccionados ", null));
        }
        for (int y = 0; y < listaRetornarCapacitacionCapacitadores.size(); y++) {
            for (int x = 0; x < listaRetornarCapacitacionCapacitadores.size(); x++) {
                if (listaRetornarCapacitacionCapacitadores.get(y)
                        .equals(listaRetornarCapacitacionCapacitadores.get(x)) && x != y) {
                    listaRetornarCapacitacionCapacitadores
                            .remove(listaRetornarCapacitacionCapacitadores.get(x));
                }
            }
        }
        for (int z = 0; z < listaCapacitacionCapacitadorDeCapacitacion.size(); z++) {
            if (!listaRetornarCapacitacionCapacitadores
                    .contains(listaCapacitacionCapacitadorDeCapacitacion.get(z))) {
                capacitacionCapacitadorJpaController
                        .destroy(listaCapacitacionCapacitadorDeCapacitacion.get(z).getId());
            }
        }
        return listaRetornarCapacitacionCapacitadores;
    }

    public List<CapacitacionCapacitado> guardarCapacitados() throws Exception {
        List<Capacitado> listaTargetCapacitado = listDobleCapacitados.getTarget();
        List<CapacitacionCapacitado> listaRetornarCapacitacionCapacitados = new ArrayList<>();
        List<CapacitacionCapacitado> listaCapacitacionCapacitadoDeCapacitacion;
        listaCapacitacionCapacitadoDeCapacitacion = capacitacion.getCapacitacionCapacitadoList();
        if (!listaTargetCapacitado.isEmpty()) {
            for (int i = 0; i < listaTargetCapacitado.size(); i++) {
                CapacitacionCapacitado capacitacionCapacitadoNuevo = new CapacitacionCapacitado();
                capacitacionCapacitadoNuevo.setCapacitacionId(capacitacion);
                capacitacionCapacitadoNuevo.setCapacitadoId(listaTargetCapacitado.get(i));
                if (listaCapacitacionCapacitadoDeCapacitacion == null
                        || listaCapacitacionCapacitadoDeCapacitacion.isEmpty()) {
                    listaRetornarCapacitacionCapacitados.add(capacitacionCapacitadoNuevo);
                    capacitacionCapacitadoJpaController.create(capacitacionCapacitadoNuevo);
                } else {
                    for (int j = 0; j < listaCapacitacionCapacitadoDeCapacitacion.size(); j++) {
                        if (listaCapacitacionCapacitadoDeCapacitacion.get(j)
                                .getCapacitadoId().equals(capacitacionCapacitadoNuevo.getCapacitadoId())
                                && listaCapacitacionCapacitadoDeCapacitacion
                                .get(j).getCapacitacionId().equals(capacitacion)) {
                            if (!listaRetornarCapacitacionCapacitados
                                    .contains(listaCapacitacionCapacitadoDeCapacitacion.get(j))) {
                                listaRetornarCapacitacionCapacitados
                                        .add(listaCapacitacionCapacitadoDeCapacitacion.get(j));
                            }
                            break;
                        } else {
                            if (!listaCapacitacionCapacitadoDeCapacitacion.get(j)
                                    .getCapacitadoId().equals(capacitacionCapacitadoNuevo.getCapacitadoId())
                                    && !listaRetornarCapacitacionCapacitados
                                    .contains(capacitacionCapacitadoNuevo)) {
                                if (!listaRetornarCapacitacionCapacitados
                                        .contains(capacitacionCapacitadoNuevo)) {
                                    listaRetornarCapacitacionCapacitados
                                            .add(capacitacionCapacitadoNuevo);
                                    capacitacionCapacitadoJpaController
                                            .create(capacitacionCapacitadoNuevo);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Capacitados Agregados a la capacitación ", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "No hay capacitados seleccionados ", null));
        }
        for (int z = 0; z < listaCapacitacionCapacitadoDeCapacitacion.size(); z++) {
            if (!listaRetornarCapacitacionCapacitados
                    .contains(listaCapacitacionCapacitadoDeCapacitacion.get(z))) {
                capacitacionCapacitadoJpaController
                        .destroy(listaCapacitacionCapacitadoDeCapacitacion.get(z).getId());
            }
        }
        for (int y = 0; y < listaRetornarCapacitacionCapacitados.size(); y++) {
            for (int x = 0; x < listaRetornarCapacitacionCapacitados.size(); x++) {
                if (listaRetornarCapacitacionCapacitados.get(y)
                        .equals(listaRetornarCapacitacionCapacitados.get(x)) && x != y) {
                    listaRetornarCapacitacionCapacitados
                            .remove(listaRetornarCapacitacionCapacitados.get(x));
                }
            }
        }
        return listaRetornarCapacitacionCapacitados;
    }

    public void estadoCapcitaciones() {
        Date fechaActual = new Date();
        Capacitacion capacitacionActiva = new Capacitacion();
        List<Capacitacion> listaCapacitacionesActivas = capacitacionJpaController.obtenerCapacitacionesActivas();
        for (int i = 0; i < listaCapacitacionesActivas.size(); i++) {
            capacitacionActiva = listaCapacitacionesActivas.get(i);
            if (capacitacionActiva.getFechaInicio().after(fechaActual) || capacitacionActiva.getFechaInicio().equals(fechaActual)) {
                if (capacitacionActiva.getFechaInicio().equals(fechaActual)) {
                    if (capacitacionActiva.getHoraInicio().after(fechaActual) || capacitacionActiva.getHoraInicio().equals(fechaActual)) {
                        capacitacionActiva.setActivo(true);
                    } else {
                        capacitacionActiva.setActivo(false);
                    }
                } else {
                    capacitacionActiva.setActivo(true);
                }
            } else {
                capacitacionActiva.setActivo(false);
            }
            System.out.println(capacitacionActiva.getTema());
        }

    }

    public void estadoCapacitacion() {
        Date fechaActual = new Date();
        if (capacitacion.getFechaInicio().after(fechaActual) || capacitacion.getFechaInicio().equals(fechaActual)) {
            if (capacitacion.getFechaInicio().equals(fechaActual)) {
                if (capacitacion.getHoraInicio().after(fechaActual) || capacitacion.getHoraInicio().equals(fechaActual)) {
                    capacitacion.setActivo(true);
                } else {
                    capacitacion.setActivo(false);
                }
            } else {
                capacitacion.setActivo(true);
            }
        } else {
            capacitacion.setActivo(false);
        }
    }

    public void guardarEvaluacion(ActionEvent actionEvent) throws Exception {
        if (capacitacion.getEvaluacionId() == null) {
            eval.setTema(capacitacion.getTema());
            evaluacionBean.getEvaluacionJpaController().create(eval);
            capacitacion.setEvaluacionId(eval);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "La evaluación " + eval.getTema()
                            + " ha sido creado", null));
        } else {
            eval.setTema(capacitacion.getTema());
            evaluacionBean.editarEvaluacionCap(capacitacion, eval);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "La evaluación " + eval.getTema()
                            + " ha sido modificada", null));
        }
    }

    public void obtenerEvaluacion() {
        if (capacitacion.getEvaluacionId() != null) {
            eval = capacitacion.getEvaluacionId();
        } else {
            eval = new Evaluacion();
        }
    }

    public void obtenerMaxFechaEval(SelectEvent event) {
        maxFecha = (Date) event.getObject();
    }

    public void obtenerMinFechaEval(SelectEvent event) {
        minFecha = (Date) event.getObject();
    }

    public void obtenerMaxFechaCap(SelectEvent event) {
        maxFechaCap = (Date) event.getObject();
    }

    public void obtenerMinFechaCap(SelectEvent event) {
        minFechaCap = (Date) event.getObject();
    }

    public void obtenerHoraInicio(SelectEvent event) {
        horaInicio = ((Date) event.getObject()).getHours();
    }

    public void obtenerHoraFin(SelectEvent event) {
        horaFin = ((Date) event.getObject()).getHours();
    }

    public void exportarPDF(ActionEvent actionEvent) throws JRException, IOException {
        CapacitadosDataSource datasource = new CapacitadosDataSource();
        datasource.setListaCapacitados(this.getCapTargetCapacitados());

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("txtRol", loginBean.getNombre());
        parametros.put("txtTemaCap", capacitacion.getTema());
        String fechaInicio = capacitacion.getFechaInicio().getDate() + "/"
                + (capacitacion.getFechaInicio().getMonth() + 1) + "/"
                + (capacitacion.getFechaInicio().getYear() + 1900);
        parametros.put("txtFechaInicio", fechaInicio);
        String fechaFin = capacitacion.getFechaFin().getDate() + "/"
                + (capacitacion.getFechaFin().getMonth() + 1) + "/"
                + (capacitacion.getFechaFin().getYear() + 1900);
        parametros.put("txtFechaFin", fechaFin);
        String horaInicioPDF = capacitacion.getHoraInicio().getHours() + ":"
                + capacitacion.getHoraInicio().getMinutes();
        parametros.put("txtHoraInicio", horaInicioPDF);
        String horaFinPDF = capacitacion.getHoraFin().getHours() + ":"
                + capacitacion.getHoraFin().getMinutes();
        parametros.put("txtHoraFin", horaFinPDF);

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Jasper/Cap.jasper"));
        JasperPrint jasperPrint;

        jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, datasource);

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=Capacitacion.pdf");
        ServletOutputStream stream = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public boolean asignarRecursos() {
        if (capacitacion.getId() != null) {
            return true;
        } else {
            return false;
        }
    }

    //setters y getters
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public List<Capacitacion> getListaCapacitaciones() {

        return listaCapacitaciones;
    }

    public void setListaCapacitaciones(List<Capacitacion> listaCapacitaciones) {
        this.listaCapacitaciones = listaCapacitaciones;
    }

    // setters y getters de lista capacitadores
    public List<Capacitador> getCapSourceCapacitadores() {
        return capSourceCapacitadores;
    }

    public void setCapSourceCapacitadores(List<Capacitador> capSourceCapacitadores) {
        this.capSourceCapacitadores = capSourceCapacitadores;
    }

    public List<Capacitador> getCapTargetCapacitadores() {
        return capTargetCapacitadores;
    }

    public void setCapTargetCapacitadores(List<Capacitador> capTargetCapacitadores) {
        this.capTargetCapacitadores = capTargetCapacitadores;
    }

    public CapacitacionCapacitador getCapacitacionCapacitador() {
        return capacitacionCapacitador;
    }

    public void setCapacitacionCapacitador(CapacitacionCapacitador capacitacionCapacitador) {
        this.capacitacionCapacitador = capacitacionCapacitador;
    }

    public List<CapacitacionCapacitador> getListCapacitacionCapacitador() {
        return listCapacitacionCapacitador;
    }

    public void setListCapacitacionCapacitador(List<CapacitacionCapacitador> listCapacitacionCapacitador) {
        this.listCapacitacionCapacitador = listCapacitacionCapacitador;
    }

    public DualListModel<Capacitador> getListDobleCapacitadores() {
        return listDobleCapacitadores;
    }

    public void setListDobleCapacitadores(DualListModel<Capacitador> listDobleCapacitadores) {
        this.listDobleCapacitadores = listDobleCapacitadores;
    }

// capacitados
    public DualListModel<Capacitado> getListDobleCapacitados() {
        return listDobleCapacitados;
    }

    public void setListDobleCapacitados(DualListModel<Capacitado> listDobleCapacitados) {
        this.listDobleCapacitados = listDobleCapacitados;
    }

    public List<TipoCapacitado> getListTipoCapacitado() {
        listTipoCapacitado = tipoCapacitadoJpaController.findTipoCapacitadoEntities();
        return listTipoCapacitado;
    }

    public void setListTipoCapacitado(List<TipoCapacitado> listTipoCapacitado) {
        this.listTipoCapacitado = listTipoCapacitado;
    }

    public TipoCapacitado getTipoCapacitado() {
        return tipoCapacitado;
    }

    public void setTipoCapacitado(TipoCapacitado tipoCapacitado) {
        this.tipoCapacitado = tipoCapacitado;
    }

    public Sector getSec() {
        return sec;
    }

    public void setSec(Sector sec) {
        this.sec = sec;
    }

    public Evaluacion getEval() {
        return eval;
    }

    public void setEval(Evaluacion eval) {
        this.eval = eval;
    }

    public EvaluacionBean getEvaluacionBean() {
        return evaluacionBean;
    }

    public void setEvaluacionBean(EvaluacionBean evaluacionBean) {
        this.evaluacionBean = evaluacionBean;
    }

    //--- fechas
    public Date getMaxFechaCap() {
        return maxFechaCap;
    }

    public void setMaxFechaCap(Date maxFechaCap) {
        this.maxFechaCap = maxFechaCap;
    }

    public Date getMinFechaCap() {
        return minFechaCap;
    }

    public void setMinFechaCap(Date minFechaCap) {
        this.minFechaCap = minFechaCap;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public Date getMaxFecha() {
        return maxFecha;
    }

    public void setMaxFecha(Date maxFecha) {
        this.maxFecha = maxFecha;
    }

    public Date getMinFecha() {
        return minFecha;
    }

    public void setMinFecha(Date minFecha) {
        this.minFecha = minFecha;
    }

    public FechasFestivas getFechasFestivas() {
        return fechasFestivas;
    }

    public void setFechasFestivas(FechasFestivas fechasFestivas) {
        this.fechasFestivas = fechasFestivas;
    }

    public List<Capacitado> getCapSourceCapacitados() {
        return capSourceCapacitados;
    }

    public void setCapSourceCapacitados(List<Capacitado> capSourceCapacitados) {
        this.capSourceCapacitados = capSourceCapacitados;
    }

    public List<Capacitado> getCapTargetCapacitados() {
        return capTargetCapacitados;
    }

    public void setCapTargetCapacitados(List<Capacitado> capTargetCapacitados) {
        this.capTargetCapacitados = capTargetCapacitados;
    }

    public List<FechasFestivas> getListaFechasFestivas() {
        return listaFechasFestivas;
    }

    public void setListaFechasFestivas(List<FechasFestivas> listaFechasFestivas) {
        this.listaFechasFestivas = listaFechasFestivas;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

}
