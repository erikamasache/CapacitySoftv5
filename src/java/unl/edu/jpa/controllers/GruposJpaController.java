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
import unl.edu.jpa.entities.Grupos;
import unl.edu.jpa.entities.Usuarios;

/**
 *
 * @author ERIKA
 */
public class GruposJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public  GruposJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupos grupos) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios correo = grupos.getCorreo();
            if (correo != null) {
                correo = em.getReference(correo.getClass(), correo.getCorreo());
                grupos.setCorreo(correo);
            }
            em.persist(grupos);
            if (correo != null) {
                correo.getGruposList().add(grupos);
                correo = em.merge(correo);
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

    public void edit(Grupos grupos) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Grupos persistentGrupos = em.find(Grupos.class, grupos.getId());
            Usuarios correoOld = persistentGrupos.getCorreo();
            Usuarios correoNew = grupos.getCorreo();
            if (correoNew != null) {
                correoNew = em.getReference(correoNew.getClass(), correoNew.getCorreo());
                grupos.setCorreo(correoNew);
            }
            grupos = em.merge(grupos);
            if (correoOld != null && !correoOld.equals(correoNew)) {
                correoOld.getGruposList().remove(grupos);
                correoOld = em.merge(correoOld);
            }
            if (correoNew != null && !correoNew.equals(correoOld)) {
                correoNew.getGruposList().add(grupos);
                correoNew = em.merge(correoNew);
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
                Integer id = grupos.getId();
                if (findGrupos(id) == null) {
                    throw new NonexistentEntityException("The grupos with id " + id + " no longer exists.");
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
            Grupos grupos;
            try {
                grupos = em.getReference(Grupos.class, id);
                grupos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupos with id " + id + " no longer exists.", enfe);
            }
            Usuarios correo = grupos.getCorreo();
            if (correo != null) {
                correo.getGruposList().remove(grupos);
                correo = em.merge(correo);
            }
            em.remove(grupos);
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

    public List<Grupos> findGruposEntities() {
        return findGruposEntities(true, -1, -1);
    }

    public List<Grupos> findGruposEntities(int maxResults, int firstResult) {
        return findGruposEntities(false, maxResults, firstResult);
    }

    private List<Grupos> findGruposEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupos.class));
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

    public Grupos findGrupos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupos.class, id);
        } finally {
            em.close();
        }
    }

    public int getGruposCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupos> rt = cq.from(Grupos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
