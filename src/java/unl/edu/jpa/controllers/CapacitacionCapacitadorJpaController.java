/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unl.edu.jpa.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import unl.edu.jpa.controllers.exceptions.NonexistentEntityException;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.CapacitacionCapacitador;
import unl.edu.jpa.entities.Capacitador;

/**
 *
 * @author ERIKA
 */
public class CapacitacionCapacitadorJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public CapacitacionCapacitadorJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CapacitacionCapacitador capacitacionCapacitador) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Capacitacion capacitacionId = capacitacionCapacitador.getCapacitacionId();
            if (capacitacionId != null) {
                capacitacionId = em.getReference(capacitacionId.getClass(), capacitacionId.getId());
                capacitacionCapacitador.setCapacitacionId(capacitacionId);
            }
            Capacitador capacitadorId = capacitacionCapacitador.getCapacitadorId();
            if (capacitadorId != null) {
                capacitadorId = em.getReference(capacitadorId.getClass(), capacitadorId.getId());
                capacitacionCapacitador.setCapacitadorId(capacitadorId);
            }
            em.persist(capacitacionCapacitador);
            if (capacitacionId != null) {
                capacitacionId.getCapacitacionCapacitadorList().add(capacitacionCapacitador);
                capacitacionId = em.merge(capacitacionId);
            }
            if (capacitadorId != null) {
                capacitadorId.getCapacitacionCapacitadorList().add(capacitacionCapacitador);
                capacitadorId = em.merge(capacitadorId);
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

    public void edit(CapacitacionCapacitador capacitacionCapacitador) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CapacitacionCapacitador persistentCapacitacionCapacitador = em.find(CapacitacionCapacitador.class, capacitacionCapacitador.getId());
            Capacitacion capacitacionIdOld = persistentCapacitacionCapacitador.getCapacitacionId();
            Capacitacion capacitacionIdNew = capacitacionCapacitador.getCapacitacionId();
            Capacitador capacitadorIdOld = persistentCapacitacionCapacitador.getCapacitadorId();
            Capacitador capacitadorIdNew = capacitacionCapacitador.getCapacitadorId();
            if (capacitacionIdNew != null) {
                capacitacionIdNew = em.getReference(capacitacionIdNew.getClass(), capacitacionIdNew.getId());
                capacitacionCapacitador.setCapacitacionId(capacitacionIdNew);
            }
            if (capacitadorIdNew != null) {
                capacitadorIdNew = em.getReference(capacitadorIdNew.getClass(), capacitadorIdNew.getId());
                capacitacionCapacitador.setCapacitadorId(capacitadorIdNew);
            }
            capacitacionCapacitador = em.merge(capacitacionCapacitador);
            if (capacitacionIdOld != null && !capacitacionIdOld.equals(capacitacionIdNew)) {
                capacitacionIdOld.getCapacitacionCapacitadorList().remove(capacitacionCapacitador);
                capacitacionIdOld = em.merge(capacitacionIdOld);
            }
            if (capacitacionIdNew != null && !capacitacionIdNew.equals(capacitacionIdOld)) {
                capacitacionIdNew.getCapacitacionCapacitadorList().add(capacitacionCapacitador);
                capacitacionIdNew = em.merge(capacitacionIdNew);
            }
            if (capacitadorIdOld != null && !capacitadorIdOld.equals(capacitadorIdNew)) {
                capacitadorIdOld.getCapacitacionCapacitadorList().remove(capacitacionCapacitador);
                capacitadorIdOld = em.merge(capacitadorIdOld);
            }
            if (capacitadorIdNew != null && !capacitadorIdNew.equals(capacitadorIdOld)) {
                capacitadorIdNew.getCapacitacionCapacitadorList().add(capacitacionCapacitador);
                capacitadorIdNew = em.merge(capacitadorIdNew);
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
                Integer id = capacitacionCapacitador.getId();
                if (findCapacitacionCapacitador(id) == null) {
                    throw new NonexistentEntityException("The capacitacionCapacitador with id " + id + " no longer exists.");
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
            CapacitacionCapacitador capacitacionCapacitador;
            try {
                capacitacionCapacitador = em.getReference(CapacitacionCapacitador.class, id);
                capacitacionCapacitador.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The capacitacionCapacitador with id " + id + " no longer exists.", enfe);
            }
            Capacitacion capacitacionId = capacitacionCapacitador.getCapacitacionId();
            if (capacitacionId != null) {
                capacitacionId.getCapacitacionCapacitadorList().remove(capacitacionCapacitador);
                capacitacionId = em.merge(capacitacionId);
            }
            Capacitador capacitadorId = capacitacionCapacitador.getCapacitadorId();
            if (capacitadorId != null) {
                capacitadorId.getCapacitacionCapacitadorList().remove(capacitacionCapacitador);
                capacitadorId = em.merge(capacitadorId);
            }
            em.remove(capacitacionCapacitador);
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

    public List<CapacitacionCapacitador> findCapacitacionCapacitadorEntities() {
        return findCapacitacionCapacitadorEntities(true, -1, -1);
    }

    public List<CapacitacionCapacitador> findCapacitacionCapacitadorEntities(int maxResults, int firstResult) {
        return findCapacitacionCapacitadorEntities(false, maxResults, firstResult);
    }

    private List<CapacitacionCapacitador> findCapacitacionCapacitadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CapacitacionCapacitador.class));
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

    public CapacitacionCapacitador findCapacitacionCapacitador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CapacitacionCapacitador.class, id);
        } finally {
            em.close();
        }
    }

    public int getCapacitacionCapacitadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CapacitacionCapacitador> rt = cq.from(CapacitacionCapacitador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
