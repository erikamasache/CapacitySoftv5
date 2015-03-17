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
import unl.edu.jpa.entities.TipoCapacitado;
import unl.edu.jpa.entities.Sector;
import unl.edu.jpa.entities.Carrera;
import unl.edu.jpa.entities.Capacitador;
import unl.edu.jpa.entities.Registra;
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
import unl.edu.jpa.entities.EvaluacionCapacitado;
import unl.edu.jpa.entities.CapacitacionCapacitado;
import unl.edu.jpa.entities.Capacitado;

/**
 *
 * @author ERIKA
 */
public class CapacitadoJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public CapacitadoJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Capacitado capacitado) throws RollbackFailureException, Exception {
        if (capacitado.getRegistraList() == null) {
            capacitado.setRegistraList(new ArrayList<Registra>());
        }
        if (capacitado.getEvaluacionCapacitadoList() == null) {
            capacitado.setEvaluacionCapacitadoList(new ArrayList<EvaluacionCapacitado>());
        }
        if (capacitado.getCapacitacionCapacitadoList() == null) {
            capacitado.setCapacitacionCapacitadoList(new ArrayList<CapacitacionCapacitado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoCapacitado tipoCapacitadoid = capacitado.getTipoCapacitadoid();
            if (tipoCapacitadoid != null) {
                tipoCapacitadoid = em.getReference(tipoCapacitadoid.getClass(), tipoCapacitadoid.getId());
                capacitado.setTipoCapacitadoid(tipoCapacitadoid);
            }
            Sector sectorId = capacitado.getSectorId();
            if (sectorId != null) {
                sectorId = em.getReference(sectorId.getClass(), sectorId.getId());
                capacitado.setSectorId(sectorId);
            }
            Carrera carreraId = capacitado.getCarreraId();
            if (carreraId != null) {
                carreraId = em.getReference(carreraId.getClass(), carreraId.getId());
                capacitado.setCarreraId(carreraId);
            }
            Capacitador capacitadorId = capacitado.getCapacitadorId();
            if (capacitadorId != null) {
                capacitadorId = em.getReference(capacitadorId.getClass(), capacitadorId.getId());
                capacitado.setCapacitadorId(capacitadorId);
            }
            List<Registra> attachedRegistraList = new ArrayList<Registra>();
            for (Registra registraListRegistraToAttach : capacitado.getRegistraList()) {
                registraListRegistraToAttach = em.getReference(registraListRegistraToAttach.getClass(), registraListRegistraToAttach.getId());
                attachedRegistraList.add(registraListRegistraToAttach);
            }
            capacitado.setRegistraList(attachedRegistraList);
            List<EvaluacionCapacitado> attachedEvaluacionCapacitadoList = new ArrayList<EvaluacionCapacitado>();
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitadoToAttach : capacitado.getEvaluacionCapacitadoList()) {
                evaluacionCapacitadoListEvaluacionCapacitadoToAttach = em.getReference(evaluacionCapacitadoListEvaluacionCapacitadoToAttach.getClass(), evaluacionCapacitadoListEvaluacionCapacitadoToAttach.getId());
                attachedEvaluacionCapacitadoList.add(evaluacionCapacitadoListEvaluacionCapacitadoToAttach);
            }
            capacitado.setEvaluacionCapacitadoList(attachedEvaluacionCapacitadoList);
            List<CapacitacionCapacitado> attachedCapacitacionCapacitadoList = new ArrayList<CapacitacionCapacitado>();
            for (CapacitacionCapacitado capacitacionCapacitadoListCapacitacionCapacitadoToAttach : capacitado.getCapacitacionCapacitadoList()) {
                capacitacionCapacitadoListCapacitacionCapacitadoToAttach = em.getReference(capacitacionCapacitadoListCapacitacionCapacitadoToAttach.getClass(), capacitacionCapacitadoListCapacitacionCapacitadoToAttach.getId());
                attachedCapacitacionCapacitadoList.add(capacitacionCapacitadoListCapacitacionCapacitadoToAttach);
            }
            capacitado.setCapacitacionCapacitadoList(attachedCapacitacionCapacitadoList);
            em.persist(capacitado);
            if (tipoCapacitadoid != null) {
                tipoCapacitadoid.getCapacitadoList().add(capacitado);
                tipoCapacitadoid = em.merge(tipoCapacitadoid);
            }
            if (sectorId != null) {
                sectorId.getCapacitadoList().add(capacitado);
                sectorId = em.merge(sectorId);
            }
            if (carreraId != null) {
                carreraId.getCapacitadoList().add(capacitado);
                carreraId = em.merge(carreraId);
            }
            if (capacitadorId != null) {
                capacitadorId.getCapacitadoList().add(capacitado);
                capacitadorId = em.merge(capacitadorId);
            }
            for (Registra registraListRegistra : capacitado.getRegistraList()) {
                Capacitado oldCapacitadoIdOfRegistraListRegistra = registraListRegistra.getCapacitadoId();
                registraListRegistra.setCapacitadoId(capacitado);
                registraListRegistra = em.merge(registraListRegistra);
                if (oldCapacitadoIdOfRegistraListRegistra != null) {
                    oldCapacitadoIdOfRegistraListRegistra.getRegistraList().remove(registraListRegistra);
                    oldCapacitadoIdOfRegistraListRegistra = em.merge(oldCapacitadoIdOfRegistraListRegistra);
                }
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitado : capacitado.getEvaluacionCapacitadoList()) {
                Capacitado oldCapacitadoIdOfEvaluacionCapacitadoListEvaluacionCapacitado = evaluacionCapacitadoListEvaluacionCapacitado.getCapacitadoId();
                evaluacionCapacitadoListEvaluacionCapacitado.setCapacitadoId(capacitado);
                evaluacionCapacitadoListEvaluacionCapacitado = em.merge(evaluacionCapacitadoListEvaluacionCapacitado);
                if (oldCapacitadoIdOfEvaluacionCapacitadoListEvaluacionCapacitado != null) {
                    oldCapacitadoIdOfEvaluacionCapacitadoListEvaluacionCapacitado.getEvaluacionCapacitadoList().remove(evaluacionCapacitadoListEvaluacionCapacitado);
                    oldCapacitadoIdOfEvaluacionCapacitadoListEvaluacionCapacitado = em.merge(oldCapacitadoIdOfEvaluacionCapacitadoListEvaluacionCapacitado);
                }
            }
            for (CapacitacionCapacitado capacitacionCapacitadoListCapacitacionCapacitado : capacitado.getCapacitacionCapacitadoList()) {
                Capacitado oldCapacitadoIdOfCapacitacionCapacitadoListCapacitacionCapacitado = capacitacionCapacitadoListCapacitacionCapacitado.getCapacitadoId();
                capacitacionCapacitadoListCapacitacionCapacitado.setCapacitadoId(capacitado);
                capacitacionCapacitadoListCapacitacionCapacitado = em.merge(capacitacionCapacitadoListCapacitacionCapacitado);
                if (oldCapacitadoIdOfCapacitacionCapacitadoListCapacitacionCapacitado != null) {
                    oldCapacitadoIdOfCapacitacionCapacitadoListCapacitacionCapacitado.getCapacitacionCapacitadoList().remove(capacitacionCapacitadoListCapacitacionCapacitado);
                    oldCapacitadoIdOfCapacitacionCapacitadoListCapacitacionCapacitado = em.merge(oldCapacitadoIdOfCapacitacionCapacitadoListCapacitacionCapacitado);
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

    public void edit(Capacitado capacitado) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Capacitado persistentCapacitado = em.find(Capacitado.class, capacitado.getId());
            TipoCapacitado tipoCapacitadoidOld = persistentCapacitado.getTipoCapacitadoid();
            TipoCapacitado tipoCapacitadoidNew = capacitado.getTipoCapacitadoid();
            Sector sectorIdOld = persistentCapacitado.getSectorId();
            Sector sectorIdNew = capacitado.getSectorId();
            Carrera carreraIdOld = persistentCapacitado.getCarreraId();
            Carrera carreraIdNew = capacitado.getCarreraId();
            Capacitador capacitadorIdOld = persistentCapacitado.getCapacitadorId();
            Capacitador capacitadorIdNew = capacitado.getCapacitadorId();
            List<Registra> registraListOld = persistentCapacitado.getRegistraList();
            List<Registra> registraListNew = capacitado.getRegistraList();
            List<EvaluacionCapacitado> evaluacionCapacitadoListOld = persistentCapacitado.getEvaluacionCapacitadoList();
            List<EvaluacionCapacitado> evaluacionCapacitadoListNew = capacitado.getEvaluacionCapacitadoList();
            List<CapacitacionCapacitado> capacitacionCapacitadoListOld = persistentCapacitado.getCapacitacionCapacitadoList();
            List<CapacitacionCapacitado> capacitacionCapacitadoListNew = capacitado.getCapacitacionCapacitadoList();
            if (tipoCapacitadoidNew != null) {
                tipoCapacitadoidNew = em.getReference(tipoCapacitadoidNew.getClass(), tipoCapacitadoidNew.getId());
                capacitado.setTipoCapacitadoid(tipoCapacitadoidNew);
            }
            if (sectorIdNew != null) {
                sectorIdNew = em.getReference(sectorIdNew.getClass(), sectorIdNew.getId());
                capacitado.setSectorId(sectorIdNew);
            }
            if (carreraIdNew != null) {
                carreraIdNew = em.getReference(carreraIdNew.getClass(), carreraIdNew.getId());
                capacitado.setCarreraId(carreraIdNew);
            }
            if (capacitadorIdNew != null) {
                capacitadorIdNew = em.getReference(capacitadorIdNew.getClass(), capacitadorIdNew.getId());
                capacitado.setCapacitadorId(capacitadorIdNew);
            }
            List<Registra> attachedRegistraListNew = new ArrayList<Registra>();
            for (Registra registraListNewRegistraToAttach : registraListNew) {
                registraListNewRegistraToAttach = em.getReference(registraListNewRegistraToAttach.getClass(), registraListNewRegistraToAttach.getId());
                attachedRegistraListNew.add(registraListNewRegistraToAttach);
            }
            registraListNew = attachedRegistraListNew;
            capacitado.setRegistraList(registraListNew);
            List<EvaluacionCapacitado> attachedEvaluacionCapacitadoListNew = new ArrayList<EvaluacionCapacitado>();
            for (EvaluacionCapacitado evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach : evaluacionCapacitadoListNew) {
                evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach = em.getReference(evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach.getClass(), evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach.getId());
                attachedEvaluacionCapacitadoListNew.add(evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach);
            }
            evaluacionCapacitadoListNew = attachedEvaluacionCapacitadoListNew;
            capacitado.setEvaluacionCapacitadoList(evaluacionCapacitadoListNew);
            List<CapacitacionCapacitado> attachedCapacitacionCapacitadoListNew = new ArrayList<CapacitacionCapacitado>();
            for (CapacitacionCapacitado capacitacionCapacitadoListNewCapacitacionCapacitadoToAttach : capacitacionCapacitadoListNew) {
                capacitacionCapacitadoListNewCapacitacionCapacitadoToAttach = em.getReference(capacitacionCapacitadoListNewCapacitacionCapacitadoToAttach.getClass(), capacitacionCapacitadoListNewCapacitacionCapacitadoToAttach.getId());
                attachedCapacitacionCapacitadoListNew.add(capacitacionCapacitadoListNewCapacitacionCapacitadoToAttach);
            }
            capacitacionCapacitadoListNew = attachedCapacitacionCapacitadoListNew;
            capacitado.setCapacitacionCapacitadoList(capacitacionCapacitadoListNew);
            capacitado = em.merge(capacitado);
            if (tipoCapacitadoidOld != null && !tipoCapacitadoidOld.equals(tipoCapacitadoidNew)) {
                tipoCapacitadoidOld.getCapacitadoList().remove(capacitado);
                tipoCapacitadoidOld = em.merge(tipoCapacitadoidOld);
            }
            if (tipoCapacitadoidNew != null && !tipoCapacitadoidNew.equals(tipoCapacitadoidOld)) {
                tipoCapacitadoidNew.getCapacitadoList().add(capacitado);
                tipoCapacitadoidNew = em.merge(tipoCapacitadoidNew);
            }
            if (sectorIdOld != null && !sectorIdOld.equals(sectorIdNew)) {
                sectorIdOld.getCapacitadoList().remove(capacitado);
                sectorIdOld = em.merge(sectorIdOld);
            }
            if (sectorIdNew != null && !sectorIdNew.equals(sectorIdOld)) {
                sectorIdNew.getCapacitadoList().add(capacitado);
                sectorIdNew = em.merge(sectorIdNew);
            }
            if (carreraIdOld != null && !carreraIdOld.equals(carreraIdNew)) {
                carreraIdOld.getCapacitadoList().remove(capacitado);
                carreraIdOld = em.merge(carreraIdOld);
            }
            if (carreraIdNew != null && !carreraIdNew.equals(carreraIdOld)) {
                carreraIdNew.getCapacitadoList().add(capacitado);
                carreraIdNew = em.merge(carreraIdNew);
            }
            if (capacitadorIdOld != null && !capacitadorIdOld.equals(capacitadorIdNew)) {
                capacitadorIdOld.getCapacitadoList().remove(capacitado);
                capacitadorIdOld = em.merge(capacitadorIdOld);
            }
            if (capacitadorIdNew != null && !capacitadorIdNew.equals(capacitadorIdOld)) {
                capacitadorIdNew.getCapacitadoList().add(capacitado);
                capacitadorIdNew = em.merge(capacitadorIdNew);
            }
            for (Registra registraListOldRegistra : registraListOld) {
                if (!registraListNew.contains(registraListOldRegistra)) {
                    registraListOldRegistra.setCapacitadoId(null);
                    registraListOldRegistra = em.merge(registraListOldRegistra);
                }
            }
            for (Registra registraListNewRegistra : registraListNew) {
                if (!registraListOld.contains(registraListNewRegistra)) {
                    Capacitado oldCapacitadoIdOfRegistraListNewRegistra = registraListNewRegistra.getCapacitadoId();
                    registraListNewRegistra.setCapacitadoId(capacitado);
                    registraListNewRegistra = em.merge(registraListNewRegistra);
                    if (oldCapacitadoIdOfRegistraListNewRegistra != null && !oldCapacitadoIdOfRegistraListNewRegistra.equals(capacitado)) {
                        oldCapacitadoIdOfRegistraListNewRegistra.getRegistraList().remove(registraListNewRegistra);
                        oldCapacitadoIdOfRegistraListNewRegistra = em.merge(oldCapacitadoIdOfRegistraListNewRegistra);
                    }
                }
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListOldEvaluacionCapacitado : evaluacionCapacitadoListOld) {
                if (!evaluacionCapacitadoListNew.contains(evaluacionCapacitadoListOldEvaluacionCapacitado)) {
                    evaluacionCapacitadoListOldEvaluacionCapacitado.setCapacitadoId(null);
                    evaluacionCapacitadoListOldEvaluacionCapacitado = em.merge(evaluacionCapacitadoListOldEvaluacionCapacitado);
                }
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListNewEvaluacionCapacitado : evaluacionCapacitadoListNew) {
                if (!evaluacionCapacitadoListOld.contains(evaluacionCapacitadoListNewEvaluacionCapacitado)) {
                    Capacitado oldCapacitadoIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado = evaluacionCapacitadoListNewEvaluacionCapacitado.getCapacitadoId();
                    evaluacionCapacitadoListNewEvaluacionCapacitado.setCapacitadoId(capacitado);
                    evaluacionCapacitadoListNewEvaluacionCapacitado = em.merge(evaluacionCapacitadoListNewEvaluacionCapacitado);
                    if (oldCapacitadoIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado != null && !oldCapacitadoIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado.equals(capacitado)) {
                        oldCapacitadoIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado.getEvaluacionCapacitadoList().remove(evaluacionCapacitadoListNewEvaluacionCapacitado);
                        oldCapacitadoIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado = em.merge(oldCapacitadoIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado);
                    }
                }
            }
            for (CapacitacionCapacitado capacitacionCapacitadoListOldCapacitacionCapacitado : capacitacionCapacitadoListOld) {
                if (!capacitacionCapacitadoListNew.contains(capacitacionCapacitadoListOldCapacitacionCapacitado)) {
                    capacitacionCapacitadoListOldCapacitacionCapacitado.setCapacitadoId(null);
                    capacitacionCapacitadoListOldCapacitacionCapacitado = em.merge(capacitacionCapacitadoListOldCapacitacionCapacitado);
                }
            }
            for (CapacitacionCapacitado capacitacionCapacitadoListNewCapacitacionCapacitado : capacitacionCapacitadoListNew) {
                if (!capacitacionCapacitadoListOld.contains(capacitacionCapacitadoListNewCapacitacionCapacitado)) {
                    Capacitado oldCapacitadoIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado = capacitacionCapacitadoListNewCapacitacionCapacitado.getCapacitadoId();
                    capacitacionCapacitadoListNewCapacitacionCapacitado.setCapacitadoId(capacitado);
                    capacitacionCapacitadoListNewCapacitacionCapacitado = em.merge(capacitacionCapacitadoListNewCapacitacionCapacitado);
                    if (oldCapacitadoIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado != null && !oldCapacitadoIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado.equals(capacitado)) {
                        oldCapacitadoIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado.getCapacitacionCapacitadoList().remove(capacitacionCapacitadoListNewCapacitacionCapacitado);
                        oldCapacitadoIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado = em.merge(oldCapacitadoIdOfCapacitacionCapacitadoListNewCapacitacionCapacitado);
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
                Integer id = capacitado.getId();
                if (findCapacitado(id) == null) {
                    throw new NonexistentEntityException("The capacitado with id " + id + " no longer exists.");
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
            Capacitado capacitado;
            try {
                capacitado = em.getReference(Capacitado.class, id);
                capacitado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The capacitado with id " + id + " no longer exists.", enfe);
            }
            TipoCapacitado tipoCapacitadoid = capacitado.getTipoCapacitadoid();
            if (tipoCapacitadoid != null) {
                tipoCapacitadoid.getCapacitadoList().remove(capacitado);
                tipoCapacitadoid = em.merge(tipoCapacitadoid);
            }
            Sector sectorId = capacitado.getSectorId();
            if (sectorId != null) {
                sectorId.getCapacitadoList().remove(capacitado);
                sectorId = em.merge(sectorId);
            }
            Carrera carreraId = capacitado.getCarreraId();
            if (carreraId != null) {
                carreraId.getCapacitadoList().remove(capacitado);
                carreraId = em.merge(carreraId);
            }
            Capacitador capacitadorId = capacitado.getCapacitadorId();
            if (capacitadorId != null) {
                capacitadorId.getCapacitadoList().remove(capacitado);
                capacitadorId = em.merge(capacitadorId);
            }
            List<Registra> registraList = capacitado.getRegistraList();
            for (Registra registraListRegistra : registraList) {
                registraListRegistra.setCapacitadoId(null);
                registraListRegistra = em.merge(registraListRegistra);
            }
            List<EvaluacionCapacitado> evaluacionCapacitadoList = capacitado.getEvaluacionCapacitadoList();
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitado : evaluacionCapacitadoList) {
                evaluacionCapacitadoListEvaluacionCapacitado.setCapacitadoId(null);
                evaluacionCapacitadoListEvaluacionCapacitado = em.merge(evaluacionCapacitadoListEvaluacionCapacitado);
            }
            List<CapacitacionCapacitado> capacitacionCapacitadoList = capacitado.getCapacitacionCapacitadoList();
            for (CapacitacionCapacitado capacitacionCapacitadoListCapacitacionCapacitado : capacitacionCapacitadoList) {
                capacitacionCapacitadoListCapacitacionCapacitado.setCapacitadoId(null);
                capacitacionCapacitadoListCapacitacionCapacitado = em.merge(capacitacionCapacitadoListCapacitacionCapacitado);
            }
            em.remove(capacitado);
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

    public List<Capacitado> findCapacitadoEntities() {
        return findCapacitadoEntities(true, -1, -1);
    }

    public List<Capacitado> findCapacitadoEntities(int maxResults, int firstResult) {
        return findCapacitadoEntities(false, maxResults, firstResult);
    }

    private List<Capacitado> findCapacitadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Capacitado.class));
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

    public Capacitado findCapacitado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Capacitado.class, id);
        } finally {
            em.close();
        }
    }

    public int getCapacitadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Capacitado> rt = cq.from(Capacitado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    /**
     * 
     * @return 
     */
    
    public List<Capacitado> listarCapacitados(){
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitado c ORDER BY c.apellido ASC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Este mÃ©todo permite buscar correos repetidos 
     * @param cap capacitado al que le pertenece el correo a buscar
     * @return lista de capaitados con el mismo correo del capacitado
     * recibido como parÃ¡metro
     */
    public List<Capacitado> correoRepetido(Capacitado cap) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitado c WHERE c.correo = ?1");
            q.setParameter(1, cap.getCorreo());
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Capacitado> correoRepetido1(String correo) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitado c WHERE c.correo = ?1");
            q.setParameter(1, correo);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Este mÃ©todo permite buscar nÃºmeros celular repetidos 
     * @param cap capacitado al que le pertenece el nÃºmero de celular a buscar
     * @return lista de capaitados con el mismo nÃºmero de celular del capacitado 
     * recibido como parÃ¡metro
     */
    public List<Capacitado> celularRepetido(Capacitado cap) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitado c WHERE c.celular = ?1");
            q.setParameter(1, cap.getCelular());
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Este mÃ©todo permite buscar cÃ©dulas repetidas 
     * @param cap capacitado al que le pretenece el nÃºmero de cÃ©dula a buscar
     * @return lista de capaitados con el mismo nÃºmero de cÃ©dula del capacitado 
     * recibido como parÃ¡metro
     */
    public List<Capacitado> cedulaRepetida(Capacitado cap) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitado c WHERE c.cedula = ?1");
            q.setParameter(1, cap.getCedula());
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Capacitado> logueadoCapacitado(String correo, String contrasenia){
        EntityManager em = getEntityManager();
        try {
            Query q = 
            em.createQuery("SELECT a FROM Capacitado a WHERE a.correo = ?1 AND a.contrasenia = ?2");
            q.setParameter(1, correo);
            q.setParameter(2, contrasenia);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
     
    public List<Capacitado> buscarCapacitado(String buscar){
        EntityManager em = getEntityManager();
        try {
            Query q = 
            em.createQuery("SELECT a FROM Capacitado a WHERE a.nombre = ?1 OR a.apellido = ?1 OR a.cedula = ?1");
            q.setParameter(1, buscar);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Capacitado> obtenerCorreo(String correo) {
        EntityManager em = getEntityManager();
        try {
            Query q
                    = em.createQuery("SELECT a FROM Capacitado a WHERE a.correo = ?1");
            q.setParameter(1, correo);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
