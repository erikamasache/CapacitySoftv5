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
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.CapacitacionCapacitado;

/**
 *
 * @author ERIKA
 */
public class CapacitacionCapacitadoJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public CapacitacionCapacitadoJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CapacitacionCapacitado capacitacionCapacitado) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Capacitado capacitadoId = capacitacionCapacitado.getCapacitadoId();
            if (capacitadoId != null) {
                capacitadoId = em.getReference(capacitadoId.getClass(), capacitadoId.getId());
                capacitacionCapacitado.setCapacitadoId(capacitadoId);
            }
            Capacitacion capacitacionId = capacitacionCapacitado.getCapacitacionId();
            if (capacitacionId != null) {
                capacitacionId = em.getReference(capacitacionId.getClass(), capacitacionId.getId());
                capacitacionCapacitado.setCapacitacionId(capacitacionId);
            }
            em.persist(capacitacionCapacitado);
            if (capacitadoId != null) {
                capacitadoId.getCapacitacionCapacitadoList().add(capacitacionCapacitado);
                capacitadoId = em.merge(capacitadoId);
            }
            if (capacitacionId != null) {
                capacitacionId.getCapacitacionCapacitadoList().add(capacitacionCapacitado);
                capacitacionId = em.merge(capacitacionId);
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

    public void edit(CapacitacionCapacitado capacitacionCapacitado) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CapacitacionCapacitado persistentCapacitacionCapacitado = em.find(CapacitacionCapacitado.class, capacitacionCapacitado.getId());
            Capacitado capacitadoIdOld = persistentCapacitacionCapacitado.getCapacitadoId();
            Capacitado capacitadoIdNew = capacitacionCapacitado.getCapacitadoId();
            Capacitacion capacitacionIdOld = persistentCapacitacionCapacitado.getCapacitacionId();
            Capacitacion capacitacionIdNew = capacitacionCapacitado.getCapacitacionId();
            if (capacitadoIdNew != null) {
                capacitadoIdNew = em.getReference(capacitadoIdNew.getClass(), capacitadoIdNew.getId());
                capacitacionCapacitado.setCapacitadoId(capacitadoIdNew);
            }
            if (capacitacionIdNew != null) {
                capacitacionIdNew = em.getReference(capacitacionIdNew.getClass(), capacitacionIdNew.getId());
                capacitacionCapacitado.setCapacitacionId(capacitacionIdNew);
            }
            capacitacionCapacitado = em.merge(capacitacionCapacitado);
            if (capacitadoIdOld != null && !capacitadoIdOld.equals(capacitadoIdNew)) {
                capacitadoIdOld.getCapacitacionCapacitadoList().remove(capacitacionCapacitado);
                capacitadoIdOld = em.merge(capacitadoIdOld);
            }
            if (capacitadoIdNew != null && !capacitadoIdNew.equals(capacitadoIdOld)) {
                capacitadoIdNew.getCapacitacionCapacitadoList().add(capacitacionCapacitado);
                capacitadoIdNew = em.merge(capacitadoIdNew);
            }
            if (capacitacionIdOld != null && !capacitacionIdOld.equals(capacitacionIdNew)) {
                capacitacionIdOld.getCapacitacionCapacitadoList().remove(capacitacionCapacitado);
                capacitacionIdOld = em.merge(capacitacionIdOld);
            }
            if (capacitacionIdNew != null && !capacitacionIdNew.equals(capacitacionIdOld)) {
                capacitacionIdNew.getCapacitacionCapacitadoList().add(capacitacionCapacitado);
                capacitacionIdNew = em.merge(capacitacionIdNew);
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
                Integer id = capacitacionCapacitado.getId();
                if (findCapacitacionCapacitado(id) == null) {
                    throw new NonexistentEntityException("The capacitacionCapacitado with id " + id + " no longer exists.");
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
            CapacitacionCapacitado capacitacionCapacitado;
            try {
                capacitacionCapacitado = em.getReference(CapacitacionCapacitado.class, id);
                capacitacionCapacitado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The capacitacionCapacitado with id " + id + " no longer exists.", enfe);
            }
            Capacitado capacitadoId = capacitacionCapacitado.getCapacitadoId();
            if (capacitadoId != null) {
                capacitadoId.getCapacitacionCapacitadoList().remove(capacitacionCapacitado);
                capacitadoId = em.merge(capacitadoId);
            }
            Capacitacion capacitacionId = capacitacionCapacitado.getCapacitacionId();
            if (capacitacionId != null) {
                capacitacionId.getCapacitacionCapacitadoList().remove(capacitacionCapacitado);
                capacitacionId = em.merge(capacitacionId);
            }
            em.remove(capacitacionCapacitado);
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

    public List<CapacitacionCapacitado> findCapacitacionCapacitadoEntities() {
        return findCapacitacionCapacitadoEntities(true, -1, -1);
    }

    public List<CapacitacionCapacitado> findCapacitacionCapacitadoEntities(int maxResults, int firstResult) {
        return findCapacitacionCapacitadoEntities(false, maxResults, firstResult);
    }

    private List<CapacitacionCapacitado> findCapacitacionCapacitadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CapacitacionCapacitado.class));
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

    public CapacitacionCapacitado findCapacitacionCapacitado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CapacitacionCapacitado.class, id);
        } finally {
            em.close();
        }
    }

    public int getCapacitacionCapacitadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CapacitacionCapacitado> rt = cq.from(CapacitacionCapacitado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
