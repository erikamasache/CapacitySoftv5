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
import unl.edu.jpa.entities.Registra;

/**
 *
 * @author ERIKA
 */
public class RegistraJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public RegistraJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Registra registra) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Capacitado capacitadoId = registra.getCapacitadoId();
            if (capacitadoId != null) {
                capacitadoId = em.getReference(capacitadoId.getClass(), capacitadoId.getId());
                registra.setCapacitadoId(capacitadoId);
            }
            Capacitacion capacitacionId = registra.getCapacitacionId();
            if (capacitacionId != null) {
                capacitacionId = em.getReference(capacitacionId.getClass(), capacitacionId.getId());
                registra.setCapacitacionId(capacitacionId);
            }
            em.persist(registra);
            if (capacitadoId != null) {
                capacitadoId.getRegistraList().add(registra);
                capacitadoId = em.merge(capacitadoId);
            }
            if (capacitacionId != null) {
                capacitacionId.getRegistraList().add(registra);
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

    public void edit(Registra registra) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Registra persistentRegistra = em.find(Registra.class, registra.getId());
            Capacitado capacitadoIdOld = persistentRegistra.getCapacitadoId();
            Capacitado capacitadoIdNew = registra.getCapacitadoId();
            Capacitacion capacitacionIdOld = persistentRegistra.getCapacitacionId();
            Capacitacion capacitacionIdNew = registra.getCapacitacionId();
            if (capacitadoIdNew != null) {
                capacitadoIdNew = em.getReference(capacitadoIdNew.getClass(), capacitadoIdNew.getId());
                registra.setCapacitadoId(capacitadoIdNew);
            }
            if (capacitacionIdNew != null) {
                capacitacionIdNew = em.getReference(capacitacionIdNew.getClass(), capacitacionIdNew.getId());
                registra.setCapacitacionId(capacitacionIdNew);
            }
            registra = em.merge(registra);
            if (capacitadoIdOld != null && !capacitadoIdOld.equals(capacitadoIdNew)) {
                capacitadoIdOld.getRegistraList().remove(registra);
                capacitadoIdOld = em.merge(capacitadoIdOld);
            }
            if (capacitadoIdNew != null && !capacitadoIdNew.equals(capacitadoIdOld)) {
                capacitadoIdNew.getRegistraList().add(registra);
                capacitadoIdNew = em.merge(capacitadoIdNew);
            }
            if (capacitacionIdOld != null && !capacitacionIdOld.equals(capacitacionIdNew)) {
                capacitacionIdOld.getRegistraList().remove(registra);
                capacitacionIdOld = em.merge(capacitacionIdOld);
            }
            if (capacitacionIdNew != null && !capacitacionIdNew.equals(capacitacionIdOld)) {
                capacitacionIdNew.getRegistraList().add(registra);
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
                Integer id = registra.getId();
                if (findRegistra(id) == null) {
                    throw new NonexistentEntityException("The registra with id " + id + " no longer exists.");
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
            Registra registra;
            try {
                registra = em.getReference(Registra.class, id);
                registra.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The registra with id " + id + " no longer exists.", enfe);
            }
            Capacitado capacitadoId = registra.getCapacitadoId();
            if (capacitadoId != null) {
                capacitadoId.getRegistraList().remove(registra);
                capacitadoId = em.merge(capacitadoId);
            }
            Capacitacion capacitacionId = registra.getCapacitacionId();
            if (capacitacionId != null) {
                capacitacionId.getRegistraList().remove(registra);
                capacitacionId = em.merge(capacitacionId);
            }
            em.remove(registra);
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

    public List<Registra> findRegistraEntities() {
        return findRegistraEntities(true, -1, -1);
    }

    public List<Registra> findRegistraEntities(int maxResults, int firstResult) {
        return findRegistraEntities(false, maxResults, firstResult);
    }

    private List<Registra> findRegistraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Registra.class));
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

    public Registra findRegistra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Registra.class, id);
        } finally {
            em.close();
        }
    }

    public int getRegistraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Registra> rt = cq.from(Registra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Registra> numeroRegistrados(Capacitacion cap) {
        EntityManager em = getEntityManager();
        try {
            Query q
                    = em.createQuery("SELECT r FROM Registra r WHERE r.capacitacionId = ?1");
            q.setParameter(1, cap);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Registra> registradoRepetido(Capacitacion capacitacion, Capacitado capacitado) {
        EntityManager em = getEntityManager();
        try {
            Query q
                    = em.createQuery("SELECT r FROM Registra r WHERE r.capacitacionId = ?1 AND r.capacitadoId = ?2");
            q.setParameter(1, capacitacion);
            q.setParameter(2, capacitado);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Registra> registradoLista(Capacitado capacitado) {
        EntityManager em = getEntityManager();
        try {
            Query q
                    = em.createQuery("SELECT r FROM Registra r WHERE r.capacitadoId = ?1");
            q.setParameter(1, capacitado);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

}
