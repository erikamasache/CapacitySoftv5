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
import unl.edu.jpa.entities.Responsable;
import unl.edu.jpa.entities.Sector;

/**
 *
 * @author ERIKA
 */
public class ResponsableJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public ResponsableJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Responsable responsable) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Sector sectorId = responsable.getSectorId();
            if (sectorId != null) {
                sectorId = em.getReference(sectorId.getClass(), sectorId.getId());
                responsable.setSectorId(sectorId);
            }
            em.persist(responsable);
            if (sectorId != null) {
                sectorId.getResponsableList().add(responsable);
                sectorId = em.merge(sectorId);
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

    public void edit(Responsable responsable) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Responsable persistentResponsable = em.find(Responsable.class, responsable.getId());
            Sector sectorIdOld = persistentResponsable.getSectorId();
            Sector sectorIdNew = responsable.getSectorId();
            if (sectorIdNew != null) {
                sectorIdNew = em.getReference(sectorIdNew.getClass(), sectorIdNew.getId());
                responsable.setSectorId(sectorIdNew);
            }
            responsable = em.merge(responsable);
            if (sectorIdOld != null && !sectorIdOld.equals(sectorIdNew)) {
                sectorIdOld.getResponsableList().remove(responsable);
                sectorIdOld = em.merge(sectorIdOld);
            }
            if (sectorIdNew != null && !sectorIdNew.equals(sectorIdOld)) {
                sectorIdNew.getResponsableList().add(responsable);
                sectorIdNew = em.merge(sectorIdNew);
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
                Integer id = responsable.getId();
                if (findResponsable(id) == null) {
                    throw new NonexistentEntityException("The responsable with id " + id + " no longer exists.");
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
            Responsable responsable;
            try {
                responsable = em.getReference(Responsable.class, id);
                responsable.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The responsable with id " + id + " no longer exists.", enfe);
            }
            Sector sectorId = responsable.getSectorId();
            if (sectorId != null) {
                sectorId.getResponsableList().remove(responsable);
                sectorId = em.merge(sectorId);
            }
            em.remove(responsable);
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

    public List<Responsable> findResponsableEntities() {
        return findResponsableEntities(true, -1, -1);
    }

    public List<Responsable> findResponsableEntities(int maxResults, int firstResult) {
        return findResponsableEntities(false, maxResults, firstResult);
    }

    private List<Responsable> findResponsableEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Responsable.class));
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

    public Responsable findResponsable(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Responsable.class, id);
        } finally {
            em.close();
        }
    }

    public int getResponsableCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Responsable> rt = cq.from(Responsable.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Este mÃ©todo permite buscar correos repetidos
     *
     * @param responsable al que le pertenece el correo a buscar
     * @return lista de responsables con el mismo correo del responsable
     * recibido como parÃ¡metro
     */
    public List<Responsable> correoRepetido(Responsable responsable) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Responsable c WHERE c.correo = ?1");
            q.setParameter(1, responsable.getCorreo());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Este mÃ©todo permite buscar nÃºmeros celular repetidos
     *
     * @param responsable al que le pertenece el nÃºmero de celular a buscar
     * @return lista de responsable con el mismo nÃºmero de celular del
     * responsable recibido como parÃ¡metro
     */
    public List<Responsable> celularRepetido(Responsable responsable) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Responsable c WHERE c.celular = ?1");
            q.setParameter(1, responsable.getCelular());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Este mÃ©todo permite buscar cÃ©dulas repetidas
     *
     * @param responsable al que le pretenece el nÃºmero de cÃ©dula a buscar
     * @return lista de responsables con el mismo nÃºmero de cÃ©dula del
     * responsable recibido como parÃ¡metro
     */
    public List<Responsable> cedulaRepetida(Responsable responsable) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Responsable c WHERE c.cedula = ?1");
            q.setParameter(1, responsable.getCedula());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

}
