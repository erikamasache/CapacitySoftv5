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
import unl.edu.jpa.entities.TipoCapacitado;

/**
 *
 * @author ERIKA
 */
public class TipoCapacitadoJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public TipoCapacitadoJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoCapacitado tipoCapacitado) throws RollbackFailureException, Exception {
        if (tipoCapacitado.getCapacitadoList() == null) {
            tipoCapacitado.setCapacitadoList(new ArrayList<Capacitado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Capacitado> attachedCapacitadoList = new ArrayList<Capacitado>();
            for (Capacitado capacitadoListCapacitadoToAttach : tipoCapacitado.getCapacitadoList()) {
                capacitadoListCapacitadoToAttach = em.getReference(capacitadoListCapacitadoToAttach.getClass(), capacitadoListCapacitadoToAttach.getId());
                attachedCapacitadoList.add(capacitadoListCapacitadoToAttach);
            }
            tipoCapacitado.setCapacitadoList(attachedCapacitadoList);
            em.persist(tipoCapacitado);
            for (Capacitado capacitadoListCapacitado : tipoCapacitado.getCapacitadoList()) {
                TipoCapacitado oldTipoCapacitadoidOfCapacitadoListCapacitado = capacitadoListCapacitado.getTipoCapacitadoid();
                capacitadoListCapacitado.setTipoCapacitadoid(tipoCapacitado);
                capacitadoListCapacitado = em.merge(capacitadoListCapacitado);
                if (oldTipoCapacitadoidOfCapacitadoListCapacitado != null) {
                    oldTipoCapacitadoidOfCapacitadoListCapacitado.getCapacitadoList().remove(capacitadoListCapacitado);
                    oldTipoCapacitadoidOfCapacitadoListCapacitado = em.merge(oldTipoCapacitadoidOfCapacitadoListCapacitado);
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

    public void edit(TipoCapacitado tipoCapacitado) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoCapacitado persistentTipoCapacitado = em.find(TipoCapacitado.class, tipoCapacitado.getId());
            List<Capacitado> capacitadoListOld = persistentTipoCapacitado.getCapacitadoList();
            List<Capacitado> capacitadoListNew = tipoCapacitado.getCapacitadoList();
            List<Capacitado> attachedCapacitadoListNew = new ArrayList<Capacitado>();
            for (Capacitado capacitadoListNewCapacitadoToAttach : capacitadoListNew) {
                capacitadoListNewCapacitadoToAttach = em.getReference(capacitadoListNewCapacitadoToAttach.getClass(), capacitadoListNewCapacitadoToAttach.getId());
                attachedCapacitadoListNew.add(capacitadoListNewCapacitadoToAttach);
            }
            capacitadoListNew = attachedCapacitadoListNew;
            tipoCapacitado.setCapacitadoList(capacitadoListNew);
            tipoCapacitado = em.merge(tipoCapacitado);
            for (Capacitado capacitadoListOldCapacitado : capacitadoListOld) {
                if (!capacitadoListNew.contains(capacitadoListOldCapacitado)) {
                    capacitadoListOldCapacitado.setTipoCapacitadoid(null);
                    capacitadoListOldCapacitado = em.merge(capacitadoListOldCapacitado);
                }
            }
            for (Capacitado capacitadoListNewCapacitado : capacitadoListNew) {
                if (!capacitadoListOld.contains(capacitadoListNewCapacitado)) {
                    TipoCapacitado oldTipoCapacitadoidOfCapacitadoListNewCapacitado = capacitadoListNewCapacitado.getTipoCapacitadoid();
                    capacitadoListNewCapacitado.setTipoCapacitadoid(tipoCapacitado);
                    capacitadoListNewCapacitado = em.merge(capacitadoListNewCapacitado);
                    if (oldTipoCapacitadoidOfCapacitadoListNewCapacitado != null && !oldTipoCapacitadoidOfCapacitadoListNewCapacitado.equals(tipoCapacitado)) {
                        oldTipoCapacitadoidOfCapacitadoListNewCapacitado.getCapacitadoList().remove(capacitadoListNewCapacitado);
                        oldTipoCapacitadoidOfCapacitadoListNewCapacitado = em.merge(oldTipoCapacitadoidOfCapacitadoListNewCapacitado);
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
                Integer id = tipoCapacitado.getId();
                if (findTipoCapacitado(id) == null) {
                    throw new NonexistentEntityException("The tipoCapacitado with id " + id + " no longer exists.");
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
            TipoCapacitado tipoCapacitado;
            try {
                tipoCapacitado = em.getReference(TipoCapacitado.class, id);
                tipoCapacitado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoCapacitado with id " + id + " no longer exists.", enfe);
            }
            List<Capacitado> capacitadoList = tipoCapacitado.getCapacitadoList();
            for (Capacitado capacitadoListCapacitado : capacitadoList) {
                capacitadoListCapacitado.setTipoCapacitadoid(null);
                capacitadoListCapacitado = em.merge(capacitadoListCapacitado);
            }
            em.remove(tipoCapacitado);
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

    public List<TipoCapacitado> findTipoCapacitadoEntities() {
        return findTipoCapacitadoEntities(true, -1, -1);
    }

    public List<TipoCapacitado> findTipoCapacitadoEntities(int maxResults, int firstResult) {
        return findTipoCapacitadoEntities(false, maxResults, firstResult);
    }

    private List<TipoCapacitado> findTipoCapacitadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoCapacitado.class));
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

    public TipoCapacitado findTipoCapacitado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoCapacitado.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoCapacitadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoCapacitado> rt = cq.from(TipoCapacitado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<TipoCapacitado> obtenerTipoCapacitado(TipoCapacitado tipoCapacitado) {
        EntityManager em = getEntityManager();
        try {
            Query q
                    = em.createQuery("SELECT a.tipoCapacitadoid.id FROM Capacitado a WHERE a.tipoCapacitadoid.id = ?1");
            q.setParameter(1, tipoCapacitado.getId());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

}
