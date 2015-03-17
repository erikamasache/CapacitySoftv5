/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unl.edu.jpa.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import unl.edu.jpa.entities.Sector;
import unl.edu.jpa.entities.Evaluacion;
import unl.edu.jpa.entities.CapacitacionCapacitador;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import unl.edu.jpa.controllers.exceptions.NonexistentEntityException;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.Registra;
import unl.edu.jpa.entities.Recurso;
import unl.edu.jpa.entities.CapacitacionCapacitado;

/**
 *
 * @author ERIKA
 */
public class CapacitacionJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public CapacitacionJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Capacitacion capacitacion) throws RollbackFailureException, Exception {
        if (capacitacion.getCapacitacionCapacitadorList() == null) {
            capacitacion.setCapacitacionCapacitadorList(new ArrayList<CapacitacionCapacitador>());
        }
        if (capacitacion.getRegistraList() == null) {
            capacitacion.setRegistraList(new ArrayList<Registra>());
        }
        if (capacitacion.getRecursoList() == null) {
            capacitacion.setRecursoList(new ArrayList<Recurso>());
        }
        if (capacitacion.getCapacitacionCapacitadoList() == null) {
            capacitacion.setCapacitacionCapacitadoList(new ArrayList<CapacitacionCapacitado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Sector sectorId = capacitacion.getSectorId();
            if (sectorId != null) {
                sectorId = em.getReference(sectorId.getClass(), sectorId.getId());
                capacitacion.setSectorId(sectorId);
            }
            Evaluacion evaluacionId = capacitacion.getEvaluacionId();
            if (evaluacionId != null) {
                evaluacionId = em.getReference(evaluacionId.getClass(), evaluacionId.getId());
                capacitacion.setEvaluacionId(evaluacionId);
            }
            List<CapacitacionCapacitador> attachedCapacitacionCapacitadorList = new ArrayList<CapacitacionCapacitador>();
            for (CapacitacionCapacitador capacitacionCapacitadorListCapacitacionCapacitadorToAttach : capacitacion.getCapacitacionCapacitadorList()) {
                capacitacionCapacitadorListCapacitacionCapacitadorToAttach = em.getReference(capacitacionCapacitadorListCapacitacionCapacitadorToAttach.getClass(), capacitacionCapacitadorListCapacitacionCapacitadorToAttach.getId());
                attachedCapacitacionCapacitadorList.add(capacitacionCapacitadorListCapacitacionCapacitadorToAttach);
            }
            capacitacion.setCapacitacionCapacitadorList(attachedCapacitacionCapacitadorList);
            List<Registra> attachedRegistraList = new ArrayList<Registra>();
            for (Registra registraListRegistraToAttach : capacitacion.getRegistraList()) {
                registraListRegistraToAttach = em.getReference(registraListRegistraToAttach.getClass(), registraListRegistraToAttach.getId());
                attachedRegistraList.add(registraListRegistraToAttach);
            }
            capacitacion.setRegistraList(attachedRegistraList);
            List<Recurso> attachedRecursoList = new ArrayList<Recurso>();
            for (Recurso recursoListRecursoToAttach : capacitacion.getRecursoList()) {
                recursoListRecursoToAttach = em.getReference(recursoListRecursoToAttach.getClass(), recursoListRecursoToAttach.getId());
                attachedRecursoList.add(recursoListRecursoToAttach);
            }
            capacitacion.setRecursoList(attachedRecursoList);
            List<CapacitacionCapacitado> attachedCapacitacionCapacitadoList = new ArrayList<CapacitacionCapacitado>();
            for (CapacitacionCapacitado capacitacionCapacitadoListCapacitacionCapacitadoToAttach : capacitacion.getCapacitacionCapacitadoList()) {
                capacitacionCapacitadoListCapacitacionCapacitadoToAttach = em.getReference(capacitacionCapacitadoListCapacitacionCapacitadoToAttach.getClass(), capacitacionCapacitadoListCapacitacionCapacitadoToAttach.getId());
                attachedCapacitacionCapacitadoList.add(capacitacionCapacitadoListCapacitacionCapacitadoToAttach);
            }
            capacitacion.setCapacitacionCapacitadoList(attachedCapacitacionCapacitadoList);
            em.persist(capacitacion);
            if (sectorId != null) {
                sectorId.getCapacitacionList().add(capacitacion);
                sectorId = em.merge(sectorId);
            }
            if (evaluacionId != null) {
                evaluacionId.getCapacitacionList().add(capacitacion);
                evaluacionId = em.merge(evaluacionId);
            }
            for (CapacitacionCapacitador capacitacionCapacitadorListCapacitacionCapacitador : capacitacion.getCapacitacionCapacitadorList()) {
                Capacitacion oldCapacitacionIdOfCapacitacionCapacitadorListCapacitacionCapacitador = capacitacionCapacitadorListCapacitacionCapacitador.getCapacitacionId();
                capacitacionCapacitadorListCapacitacionCapacitador.setCapacitacionId(capacitacion);
                capacitacionCapacitadorListCapacitacionCapacitador = em.merge(capacitacionCapacitadorListCapacitacionCapacitador);
                if (oldCapacitacionIdOfCapacitacionCapacitadorListCapacitacionCapacitador != null) {
                    oldCapacitacionIdOfCapacitacionCapacitadorListCapacitacionCapacitador.getCapacitacionCapacitadorList().remove(capacitacionCapacitadorListCapacitacionCapacitador);
                    oldCapacitacionIdOfCapacitacionCapacitadorListCapacitacionCapacitador = em.merge(oldCapacitacionIdOfCapacitacionCapacitadorListCapacitacionCapacitador);
                }
            }
            for (Registra registraListRegistra : capacitacion.getRegistraList()) {
                Capacitacion oldCapacitacionIdOfRegistraListRegistra = registraListRegistra.getCapacitacionId();
                registraListRegistra.setCapacitacionId(capacitacion);
                registraListRegistra = em.merge(registraListRegistra);
                if (oldCapacitacionIdOfRegistraListRegistra != null) {
                    oldCapacitacionIdOfRegistraListRegistra.getRegistraList().remove(registraListRegistra);
                    oldCapacitacionIdOfRegistraListRegistra = em.merge(oldCapacitacionIdOfRegistraListRegistra);
                }
            }
            for (Recurso recursoListRecurso : capacitacion.getRecursoList()) {
                Capacitacion oldCapacitacionIdOfRecursoListRecurso = recursoListRecurso.getCapacitacionId();
                recursoListRecurso.setCapacitacionId(capacitacion);
                recursoListRecurso = em.merge(recursoListRecurso);
                if (oldCapacitacionIdOfRecursoListRecurso != null) {
                    oldCapacitacionIdOfRecursoListRecurso.getRecursoList().remove(recursoListRecurso);
                    oldCapacitacionIdOfRecursoListRecurso = em.merge(oldCapacitacionIdOfRecursoListRecurso);
                }
            }
            for (CapacitacionCapacitado capacitacionCapacitadoListCapacitacionCapacitado : capacitacion.getCapacitacionCapacitadoList()) {
                Capacitacion oldCapacitacionIdOfCapacitacionCapacitadoListCapacitacionCapacitado = capacitacionCapacitadoListCapacitacionCapacitado.getCapacitacionId();
                capacitacionCapacitadoListCapacitacionCapacitado.setCapacitacionId(capacitacion);
                capacitacionCapacitadoListCapacitacionCapacitado = em.merge(capacitacionCapacitadoListCapacitacionCapacitado);
                if (oldCapacitacionIdOfCapacitacionCapacitadoListCapacitacionCapacitado != null) {
                    oldCapacitacionIdOfCapacitacionCapacitadoListCapacitacionCapacitado.getCapacitacionCapacitadoList().remove(capacitacionCapacitadoListCapacitacionCapacitado);
                    oldCapacitacionIdOfCapacitacionCapacitadoListCapacitacionCapacitado = em.merge(oldCapacitacionIdOfCapacitacionCapacitadoListCapacitacionCapacitado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Capacitacion capacitacion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Capacitacion persistentCapacitacion = em.find(Capacitacion.class, capacitacion.getId());
            Sector sectorIdOld = persistentCapacitacion.getSectorId();
            Sector sectorIdNew = capacitacion.getSectorId();
            Evaluacion evaluacionIdOld = persistentCapacitacion.getEvaluacionId();
            Evaluacion evaluacionIdNew = capacitacion.getEvaluacionId();
            List<CapacitacionCapacitador> capacitacionCapacitadorListOld = persistentCapacitacion.getCapacitacionCapacitadorList();
            List<CapacitacionCapacitador> capacitacionCapacitadorListNew = capacitacion.getCapacitacionCapacitadorList();
            List<Registra> registraListOld = persistentCapacitacion.getRegistraList();
            List<Registra> registraListNew = capacitacion.getRegistraList();
            List<Recurso> recursoListOld = persistentCapacitacion.getRecursoList();
            List<Recurso> recursoListNew = capacitacion.getRecursoList();
            List<CapacitacionCapacitado> capacitacionCapacitadoListOld = persistentCapacitacion.getCapacitacionCapacitadoList();
            List<CapacitacionCapacitado> capacitacionCapacitadoListNew = capacitacion.getCapacitacionCapacitadoList();
            if (sectorIdNew != null) {
                sectorIdNew = em.getReference(sectorIdNew.getClass(), sectorIdNew.getId());
                capacitacion.setSectorId(sectorIdNew);
            }
            if (evaluacionIdNew != null) {
                evaluacionIdNew = em.getReference(evaluacionIdNew.getClass(), evaluacionIdNew.getId());
                capacitacion.setEvaluacionId(evaluacionIdNew);
            }
            List<CapacitacionCapacitador> attachedCapacitacionCapacitadorListNew = new ArrayList<CapacitacionCapacitador>();
            for (CapacitacionCapacitador capacitacionCapacitadorListNewCapacitacionCapacitadorToAttach : capacitacionCapacitadorListNew) {
                capacitacionCapacitadorListNewCapacitacionCapacitadorToAttach = em.getReference(capacitacionCapacitadorListNewCapacitacionCapacitadorToAttach.getClass(), capacitacionCapacitadorListNewCapacitacionCapacitadorToAttach.getId());
                attachedCapacitacionCapacitadorListNew.add(capacitacionCapacitadorListNewCapacitacionCapacitadorToAttach);
            }
            capacitacionCapacitadorListNew = attachedCapacitacionCapacitadorListNew;
            capacitacion.setCapacitacionCapacitadorList(capacitacionCapacitadorListNew);
            List<Registra> attachedRegistraListNew = new ArrayList<Registra>();
            for (Registra registraListNewRegistraToAttach : registraListNew) {
                registraListNewRegistraToAttach = em.getReference(registraListNewRegistraToAttach.getClass(), registraListNewRegistraToAttach.getId());
                attachedRegistraListNew.add(registraListNewRegistraToAttach);
            }
            registraListNew = attachedRegistraListNew;
            capacitacion.setRegistraList(registraListNew);
            List<Recurso> attachedRecursoListNew = new ArrayList<Recurso>();
            for (Recurso recursoListNewRecursoToAttach : recursoListNew) {
                recursoListNewRecursoToAttach = em.getReference(recursoListNewRecursoToAttach.getClass(), recursoListNewRecursoToAttach.getId());
                attachedRecursoListNew.add(recursoListNewRecursoToAttach);
            }
            recursoListNew = attachedRecursoListNew;
            capacitacion.setRecursoList(recursoListNew);
            List<CapacitacionCapacitado> attachedCapacitacionCapacitadoListNew = new ArrayList<CapacitacionCapacitado>();
            for (CapacitacionCapacitado capacitacionCapacitadoListNewCapacitacionCapacitadoToAttach : capacitacionCapacitadoListNew) {
                capacitacionCapacitadoListNewCapacitacionCapacitadoToAttach = em.getReference(capacitacionCapacitadoListNewCapacitacionCapacitadoToAttach.getClass(), capacitacionCapacitadoListNewCapacitacionCapacitadoToAttach.getId());
                attachedCapacitacionCapacitadoListNew.add(capacitacionCapacitadoListNewCapacitacionCapacitadoToAttach);
            }
            capacitacionCapacitadoListNew = attachedCapacitacionCapacitadoListNew;
            capacitacion.setCapacitacionCapacitadoList(capacitacionCapacitadoListNew);
            capacitacion = em.merge(capacitacion);
            if (sectorIdOld != null && !sectorIdOld.equals(sectorIdNew)) {
                sectorIdOld.getCapacitacionList().remove(capacitacion);
                sectorIdOld = em.merge(sectorIdOld);
            }
            if (sectorIdNew != null && !sectorIdNew.equals(sectorIdOld)) {
                sectorIdNew.getCapacitacionList().add(capacitacion);
                sectorIdNew = em.merge(sectorIdNew);
            }
            if (evaluacionIdOld != null && !evaluacionIdOld.equals(evaluacionIdNew)) {
                evaluacionIdOld.getCapacitacionList().remove(capacitacion);
                evaluacionIdOld = em.merge(evaluacionIdOld);
            }
            if (evaluacionIdNew != null && !evaluacionIdNew.equals(evaluacionIdOld)) {
                evaluacionIdNew.getCapacitacionList().add(capacitacion);
                evaluacionIdNew = em.merge(evaluacionIdNew);
            }
            for (CapacitacionCapacitador capacitacionCapacitadorListOldCapacitacionCapacitador : capacitacionCapacitadorListOld) {
                if (!capacitacionCapacitadorListNew.contains(capacitacionCapacitadorListOldCapacitacionCapacitador)) {
                    capacitacionCapacitadorListOldCapacitacionCapacitador.setCapacitacionId(null);
                    capacitacionCapacitadorListOldCapacitacionCapacitador = em.merge(capacitacionCapacitadorListOldCapacitacionCapacitador);
                }
            }
            for (CapacitacionCapacitador capacitacionCapacitadorListNewCapacitacionCapacitador : capacitacionCapacitadorListNew) {
                if (!capacitacionCapacitadorListOld.contains(capacitacionCapacitadorListNewCapacitacionCapacitador)) {
                    Capacitacion oldCapacitacionIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador = capacitacionCapacitadorListNewCapacitacionCapacitador.getCapacitacionId();
                    capacitacionCapacitadorListNewCapacitacionCapacitador.setCapacitacionId(capacitacion);
                    capacitacionCapacitadorListNewCapacitacionCapacitador = em.merge(capacitacionCapacitadorListNewCapacitacionCapacitador);
                    if (oldCapacitacionIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador != null && !oldCapacitacionIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador.equals(capacitacion)) {
                        oldCapacitacionIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador.getCapacitacionCapacitadorList().remove(capacitacionCapacitadorListNewCapacitacionCapacitador);
                        oldCapacitacionIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador = em.merge(oldCapacitacionIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador);
                    }
                }
            }
            for (Registra registraListOldRegistra : registraListOld) {
                if (!registraListNew.contains(registraListOldRegistra)) {
                    registraListOldRegistra.setCapacitacionId(null);
                    registraListOldRegistra = em.merge(registraListOldRegistra);
                }
            }
            for (Registra registraListNewRegistra : registraListNew) {
                if (!registraListOld.contains(registraListNewRegistra)) {
                    Capacitacion oldCapacitacionIdOfRegistraListNewRegistra = registraListNewRegistra.getCapacitacionId();
                    registraListNewRegistra.setCapacitacionId(capacitacion);
                    registraListNewRegistra = em.merge(registraListNewRegistra);
                    if (oldCapacitacionIdOfRegistraListNewRegistra != null && !oldCapacitacionIdOfRegistraListNewRegistra.equals(capacitacion)) {
                        oldCapacitacionIdOfRegistraListNewRegistra.getRegistraList().remove(registraListNewRegistra);
                        oldCapacitacionIdOfRegistraListNewRegistra = em.merge(oldCapacitacionIdOfRegistraListNewRegistra);
                    }
                }
            }
            for (Recurso recursoListOldRecurso : recursoListOld) {
                if (!recursoListNew.contains(recursoListOldRecurso)) {
                    recursoListOldRecurso.setCapacitacionId(null);
                    recursoListOldRecurso = em.merge(recursoListOldRecurso);
                }
            }
            for (Recurso recursoListNewRecurso : recursoListNew) {
                if (!recursoListOld.contains(recursoListNewRecurso)) {
                    Capacitacion oldCapacitacionIdOfRecursoListNewRecurso = recursoListNewRecurso.getCapacitacionId();
                    recursoListNewRecurso.setCapacitacionId(capacitacion);
                    recursoListNewRecurso = em.merge(recursoListNewRecurso);
                    if (oldCapacitacionIdOfRecursoListNewRecurso != null && !oldCapacitacionIdOfRecursoListNewRecurso.equals(capacitacion)) {
                        oldCapacitacionIdOfRecursoListNewRecurso.getRecursoList().remove(recursoListNewRecurso);
                        oldCapacitacionIdOfRecursoListNewRecurso = em.merge(oldCapacitacionIdOfRecursoListNewRecurso);
                    }
                }
            }
            for (CapacitacionCapacitado capacitacionCapacitadoListOldCapacitacionCapacitado : capacitacionCapacitadoListOld) {
                if (!capacitacionCapacitadoListNew.contains(capacitacionCapacitadoListOldCapacitacionCapacitado)) {
                    capacitacionCapacitadoListOldCapacitacionCapacitado.setCapacitacionId(null);
                    capacitacionCapacitadoListOldCapacitacionCapacitado = em.merge(capacitacionCapacitadoListOldCapacitacionCapacitado);
                }
            }
            for (CapacitacionCapacitado capacitacionCapacitadoListNewCapacitacionCapacitado : capacitacionCapacitadoListNew) {
                if (!capacitacionCapacitadoListOld.contains(capacitacionCapacitadoListNewCapacitacionCapacitado)) {
                    Capacitacion oldCapacitacionIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado = capacitacionCapacitadoListNewCapacitacionCapacitado.getCapacitacionId();
                    capacitacionCapacitadoListNewCapacitacionCapacitado.setCapacitacionId(capacitacion);
                    capacitacionCapacitadoListNewCapacitacionCapacitado = em.merge(capacitacionCapacitadoListNewCapacitacionCapacitado);
                    if (oldCapacitacionIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado != null && !oldCapacitacionIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado.equals(capacitacion)) {
                        oldCapacitacionIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado.getCapacitacionCapacitadoList().remove(capacitacionCapacitadoListNewCapacitacionCapacitado);
                        oldCapacitacionIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado = em.merge(oldCapacitacionIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = capacitacion.getId();
                if (findCapacitacion(id) == null) {
                    throw new NonexistentEntityException("The capacitacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Capacitacion capacitacion;
            try {
                capacitacion = em.getReference(Capacitacion.class, id);
                capacitacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The capacitacion with id " + id + " no longer exists.", enfe);
            }
            Sector sectorId = capacitacion.getSectorId();
            if (sectorId != null) {
                sectorId.getCapacitacionList().remove(capacitacion);
                sectorId = em.merge(sectorId);
            }
            Evaluacion evaluacionId = capacitacion.getEvaluacionId();
            if (evaluacionId != null) {
                evaluacionId.getCapacitacionList().remove(capacitacion);
                evaluacionId = em.merge(evaluacionId);
            }
            List<CapacitacionCapacitador> capacitacionCapacitadorList = capacitacion.getCapacitacionCapacitadorList();
            for (CapacitacionCapacitador capacitacionCapacitadorListCapacitacionCapacitador : capacitacionCapacitadorList) {
                capacitacionCapacitadorListCapacitacionCapacitador.setCapacitacionId(null);
                capacitacionCapacitadorListCapacitacionCapacitador = em.merge(capacitacionCapacitadorListCapacitacionCapacitador);
            }
            List<Registra> registraList = capacitacion.getRegistraList();
            for (Registra registraListRegistra : registraList) {
                registraListRegistra.setCapacitacionId(null);
                registraListRegistra = em.merge(registraListRegistra);
            }
            List<Recurso> recursoList = capacitacion.getRecursoList();
            for (Recurso recursoListRecurso : recursoList) {
                recursoListRecurso.setCapacitacionId(null);
                recursoListRecurso = em.merge(recursoListRecurso);
            }
            List<CapacitacionCapacitado> capacitacionCapacitadoList = capacitacion.getCapacitacionCapacitadoList();
            for (CapacitacionCapacitado capacitacionCapacitadoListCapacitacionCapacitado : capacitacionCapacitadoList) {
                capacitacionCapacitadoListCapacitacionCapacitado.setCapacitacionId(null);
                capacitacionCapacitadoListCapacitacionCapacitado = em.merge(capacitacionCapacitadoListCapacitacionCapacitado);
            }
            em.remove(capacitacion);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Capacitacion> findCapacitacionEntities() {
        return findCapacitacionEntities(true, -1, -1);
    }

    public List<Capacitacion> findCapacitacionEntities(int maxResults, int firstResult) {
        return findCapacitacionEntities(false, maxResults, firstResult);
    }

    private List<Capacitacion> findCapacitacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Capacitacion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Capacitacion findCapacitacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Capacitacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getCapacitacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Capacitacion> rt = cq.from(Capacitacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Este mÃ©todo permite buscar capacitaciones repetidas
     *
     * @param capacitacion a la que le pretenece el tema a buscar
     * @return lista de capacitaciones con el mismo tema de la capacitacion
     * recibida como parÃ¡metro
     */
    public List<Capacitacion> nombreRepetido(Capacitacion capacitacion) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitacion c WHERE c.tema = ?1");
            q.setParameter(1, capacitacion.getTema());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Capacitacion> listarCapacitaciones() {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitacion c ORDER BY c.fechaInicio DESC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Capacitacion> obtenerCapacitacionesActivas() {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitacion c WHERE c.activo = true");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
