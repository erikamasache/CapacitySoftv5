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
import unl.edu.jpa.entities.Administrador;
import unl.edu.jpa.entities.Capacitacion;
import unl.edu.jpa.entities.Recurso;

/**
 *
 * @author ERIKA
 */
public class RecursoJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public RecursoJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recurso recurso) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Capacitacion capacitacionId = recurso.getCapacitacionId();
            if (capacitacionId != null) {
                capacitacionId = em.getReference(capacitacionId.getClass(), capacitacionId.getId());
                recurso.setCapacitacionId(capacitacionId);
            }
            em.persist(recurso);
            if (capacitacionId != null) {
                capacitacionId.getRecursoList().add(recurso);
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

    public void edit(Recurso recurso) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Recurso persistentRecurso = em.find(Recurso.class, recurso.getId());
            Capacitacion capacitacionIdOld = persistentRecurso.getCapacitacionId();
            Capacitacion capacitacionIdNew = recurso.getCapacitacionId();
            if (capacitacionIdNew != null) {
                capacitacionIdNew = em.getReference(capacitacionIdNew.getClass(), capacitacionIdNew.getId());
                recurso.setCapacitacionId(capacitacionIdNew);
            }
            recurso = em.merge(recurso);
            if (capacitacionIdOld != null && !capacitacionIdOld.equals(capacitacionIdNew)) {
                capacitacionIdOld.getRecursoList().remove(recurso);
                capacitacionIdOld = em.merge(capacitacionIdOld);
            }
            if (capacitacionIdNew != null && !capacitacionIdNew.equals(capacitacionIdOld)) {
                capacitacionIdNew.getRecursoList().add(recurso);
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
                Integer id = recurso.getId();
                if (findRecurso(id) == null) {
                    throw new NonexistentEntityException("The recurso with id " + id + " no longer exists.");
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
            Recurso recurso;
            try {
                recurso = em.getReference(Recurso.class, id);
                recurso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recurso with id " + id + " no longer exists.", enfe);
            }
            Capacitacion capacitacionId = recurso.getCapacitacionId();
            if (capacitacionId != null) {
                capacitacionId.getRecursoList().remove(recurso);
                capacitacionId = em.merge(capacitacionId);
            }
            em.remove(recurso);
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

    public List<Recurso> findRecursoEntities() {
        return findRecursoEntities(true, -1, -1);
    }

    public List<Recurso> findRecursoEntities(int maxResults, int firstResult) {
        return findRecursoEntities(false, maxResults, firstResult);
    }

    private List<Recurso> findRecursoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recurso.class));
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

    public Recurso findRecurso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recurso.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecursoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recurso> rt = cq.from(Recurso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Recurso> videoRepetido(String enlace, Capacitacion cap) {
        EntityManager em = getEntityManager();
        try {
            Query q
                    = em.createQuery("SELECT a FROM Recurso a WHERE a.pathVideo = ?1 AND a.capacitacionId = ?2");
            q.setParameter(1, enlace);
            q.setParameter(2, cap);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
