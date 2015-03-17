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
import unl.edu.jpa.entities.Capacitado;
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
import unl.edu.jpa.entities.Carrera;

/**
 *
 * @author ERIKA
 */
public class CarreraJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public CarreraJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carrera carrera) throws RollbackFailureException, Exception {
        if (carrera.getCapacitadoList() == null) {
            carrera.setCapacitadoList(new ArrayList<Capacitado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Sector sectorId = carrera.getSectorId();
            if (sectorId != null) {
                sectorId = em.getReference(sectorId.getClass(), sectorId.getId());
                carrera.setSectorId(sectorId);
            }
            List<Capacitado> attachedCapacitadoList = new ArrayList<Capacitado>();
            for (Capacitado capacitadoListCapacitadoToAttach : carrera.getCapacitadoList()) {
                capacitadoListCapacitadoToAttach = em.getReference(capacitadoListCapacitadoToAttach.getClass(), capacitadoListCapacitadoToAttach.getId());
                attachedCapacitadoList.add(capacitadoListCapacitadoToAttach);
            }
            carrera.setCapacitadoList(attachedCapacitadoList);
            em.persist(carrera);
            if (sectorId != null) {
                sectorId.getCarreraList().add(carrera);
                sectorId = em.merge(sectorId);
            }
            for (Capacitado capacitadoListCapacitado : carrera.getCapacitadoList()) {
                Carrera oldCarreraIdOfCapacitadoListCapacitado = capacitadoListCapacitado.getCarreraId();
                capacitadoListCapacitado.setCarreraId(carrera);
                capacitadoListCapacitado = em.merge(capacitadoListCapacitado);
                if (oldCarreraIdOfCapacitadoListCapacitado != null) {
                    oldCarreraIdOfCapacitadoListCapacitado.getCapacitadoList().remove(capacitadoListCapacitado);
                    oldCarreraIdOfCapacitadoListCapacitado = em.merge(oldCarreraIdOfCapacitadoListCapacitado);
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

    public void edit(Carrera carrera) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Carrera persistentCarrera = em.find(Carrera.class, carrera.getId());
            Sector sectorIdOld = persistentCarrera.getSectorId();
            Sector sectorIdNew = carrera.getSectorId();
            List<Capacitado> capacitadoListOld = persistentCarrera.getCapacitadoList();
            List<Capacitado> capacitadoListNew = carrera.getCapacitadoList();
            if (sectorIdNew != null) {
                sectorIdNew = em.getReference(sectorIdNew.getClass(), sectorIdNew.getId());
                carrera.setSectorId(sectorIdNew);
            }
            List<Capacitado> attachedCapacitadoListNew = new ArrayList<Capacitado>();
            for (Capacitado capacitadoListNewCapacitadoToAttach : capacitadoListNew) {
                capacitadoListNewCapacitadoToAttach = em.getReference(capacitadoListNewCapacitadoToAttach.getClass(), capacitadoListNewCapacitadoToAttach.getId());
                attachedCapacitadoListNew.add(capacitadoListNewCapacitadoToAttach);
            }
            capacitadoListNew = attachedCapacitadoListNew;
            carrera.setCapacitadoList(capacitadoListNew);
            carrera = em.merge(carrera);
            if (sectorIdOld != null && !sectorIdOld.equals(sectorIdNew)) {
                sectorIdOld.getCarreraList().remove(carrera);
                sectorIdOld = em.merge(sectorIdOld);
            }
            if (sectorIdNew != null && !sectorIdNew.equals(sectorIdOld)) {
                sectorIdNew.getCarreraList().add(carrera);
                sectorIdNew = em.merge(sectorIdNew);
            }
            for (Capacitado capacitadoListOldCapacitado : capacitadoListOld) {
                if (!capacitadoListNew.contains(capacitadoListOldCapacitado)) {
                    capacitadoListOldCapacitado.setCarreraId(null);
                    capacitadoListOldCapacitado = em.merge(capacitadoListOldCapacitado);
                }
            }
            for (Capacitado capacitadoListNewCapacitado : capacitadoListNew) {
                if (!capacitadoListOld.contains(capacitadoListNewCapacitado)) {
                    Carrera oldCarreraIdOfCapacitadoListNewCapacitado = capacitadoListNewCapacitado.getCarreraId();
                    capacitadoListNewCapacitado.setCarreraId(carrera);
                    capacitadoListNewCapacitado = em.merge(capacitadoListNewCapacitado);
                    if (oldCarreraIdOfCapacitadoListNewCapacitado != null && !oldCarreraIdOfCapacitadoListNewCapacitado.equals(carrera)) {
                        oldCarreraIdOfCapacitadoListNewCapacitado.getCapacitadoList().remove(capacitadoListNewCapacitado);
                        oldCarreraIdOfCapacitadoListNewCapacitado = em.merge(oldCarreraIdOfCapacitadoListNewCapacitado);
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
                Integer id = carrera.getId();
                if (findCarrera(id) == null) {
                    throw new NonexistentEntityException("The carrera with id " + id + " no longer exists.");
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
            Carrera carrera;
            try {
                carrera = em.getReference(Carrera.class, id);
                carrera.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carrera with id " + id + " no longer exists.", enfe);
            }
            Sector sectorId = carrera.getSectorId();
            if (sectorId != null) {
                sectorId.getCarreraList().remove(carrera);
                sectorId = em.merge(sectorId);
            }
            List<Capacitado> capacitadoList = carrera.getCapacitadoList();
            for (Capacitado capacitadoListCapacitado : capacitadoList) {
                capacitadoListCapacitado.setCarreraId(null);
                capacitadoListCapacitado = em.merge(capacitadoListCapacitado);
            }
            em.remove(carrera);
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

    public List<Carrera> findCarreraEntities() {
        return findCarreraEntities(true, -1, -1);
    }

    public List<Carrera> findCarreraEntities(int maxResults, int firstResult) {
        return findCarreraEntities(false, maxResults, firstResult);
    }

    private List<Carrera> findCarreraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carrera.class));
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

    public Carrera findCarrera(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carrera.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarreraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carrera> rt = cq.from(Carrera.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    /**
     * Este mÃ©todo permite buscar nombres repetidos 
     * @param carrera a la que le pretenece el nombre a buscar
     * @return lista de carreras con el mismo nombre de la carrera
     * recibida como parÃ¡metro
     */
    public List<Carrera> nombreRepetido(Carrera carrera) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Carrera c WHERE c.nombre = ?1");
            q.setParameter(1, carrera.getNombre());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

}
