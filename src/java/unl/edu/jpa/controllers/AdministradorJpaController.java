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
import unl.edu.jpa.entities.Capacitador;
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
import unl.edu.jpa.entities.Administrador;

/**
 *
 * @author ERIKA
 */
public class AdministradorJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public AdministradorJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Administrador administrador) throws RollbackFailureException, Exception {
        if (administrador.getCapacitadorList() == null) {
            administrador.setCapacitadorList(new ArrayList<Capacitador>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Capacitador> attachedCapacitadorList = new ArrayList<Capacitador>();
            for (Capacitador capacitadorListCapacitadorToAttach : administrador.getCapacitadorList()) {
                capacitadorListCapacitadorToAttach = em.getReference(capacitadorListCapacitadorToAttach.getClass(), capacitadorListCapacitadorToAttach.getId());
                attachedCapacitadorList.add(capacitadorListCapacitadorToAttach);
            }
            administrador.setCapacitadorList(attachedCapacitadorList);
            em.persist(administrador);
            for (Capacitador capacitadorListCapacitador : administrador.getCapacitadorList()) {
                Administrador oldAdministradorIdOfCapacitadorListCapacitador = capacitadorListCapacitador.getAdministradorId();
                capacitadorListCapacitador.setAdministradorId(administrador);
                capacitadorListCapacitador = em.merge(capacitadorListCapacitador);
                if (oldAdministradorIdOfCapacitadorListCapacitador != null) {
                    oldAdministradorIdOfCapacitadorListCapacitador.getCapacitadorList().remove(capacitadorListCapacitador);
                    oldAdministradorIdOfCapacitadorListCapacitador = em.merge(oldAdministradorIdOfCapacitadorListCapacitador);
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

    public void edit(Administrador administrador) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Administrador persistentAdministrador = em.find(Administrador.class, administrador.getId());
            List<Capacitador> capacitadorListOld = persistentAdministrador.getCapacitadorList();
            List<Capacitador> capacitadorListNew = administrador.getCapacitadorList();
            List<Capacitador> attachedCapacitadorListNew = new ArrayList<Capacitador>();
            for (Capacitador capacitadorListNewCapacitadorToAttach : capacitadorListNew) {
                capacitadorListNewCapacitadorToAttach = em.getReference(capacitadorListNewCapacitadorToAttach.getClass(), capacitadorListNewCapacitadorToAttach.getId());
                attachedCapacitadorListNew.add(capacitadorListNewCapacitadorToAttach);
            }
            capacitadorListNew = attachedCapacitadorListNew;
            administrador.setCapacitadorList(capacitadorListNew);
            administrador = em.merge(administrador);
            for (Capacitador capacitadorListOldCapacitador : capacitadorListOld) {
                if (!capacitadorListNew.contains(capacitadorListOldCapacitador)) {
                    capacitadorListOldCapacitador.setAdministradorId(null);
                    capacitadorListOldCapacitador = em.merge(capacitadorListOldCapacitador);
                }
            }
            for (Capacitador capacitadorListNewCapacitador : capacitadorListNew) {
                if (!capacitadorListOld.contains(capacitadorListNewCapacitador)) {
                    Administrador oldAdministradorIdOfCapacitadorListNewCapacitador = capacitadorListNewCapacitador.getAdministradorId();
                    capacitadorListNewCapacitador.setAdministradorId(administrador);
                    capacitadorListNewCapacitador = em.merge(capacitadorListNewCapacitador);
                    if (oldAdministradorIdOfCapacitadorListNewCapacitador != null && !oldAdministradorIdOfCapacitadorListNewCapacitador.equals(administrador)) {
                        oldAdministradorIdOfCapacitadorListNewCapacitador.getCapacitadorList().remove(capacitadorListNewCapacitador);
                        oldAdministradorIdOfCapacitadorListNewCapacitador = em.merge(oldAdministradorIdOfCapacitadorListNewCapacitador);
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
                Integer id = administrador.getId();
                if (findAdministrador(id) == null) {
                    throw new NonexistentEntityException("The administrador with id " + id + " no longer exists.");
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
            Administrador administrador;
            try {
                administrador = em.getReference(Administrador.class, id);
                administrador.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The administrador with id " + id + " no longer exists.", enfe);
            }
            List<Capacitador> capacitadorList = administrador.getCapacitadorList();
            for (Capacitador capacitadorListCapacitador : capacitadorList) {
                capacitadorListCapacitador.setAdministradorId(null);
                capacitadorListCapacitador = em.merge(capacitadorListCapacitador);
            }
            em.remove(administrador);
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

    public List<Administrador> findAdministradorEntities() {
        return findAdministradorEntities(true, -1, -1);
    }

    public List<Administrador> findAdministradorEntities(int maxResults, int firstResult) {
        return findAdministradorEntities(false, maxResults, firstResult);
    }

    private List<Administrador> findAdministradorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Administrador.class));
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

    public Administrador findAdministrador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Administrador.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdministradorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Administrador> rt = cq.from(Administrador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Administrador> correoRepetido(Administrador adm) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Administrador c WHERE c.correo = ?1");
            q.setParameter(1, adm.getCorreo());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Este mÃ©todo permite buscar nÃºmeros celular repetidos
     *
     * @param adm capacitador al que le pertenece el nÃºmero de celular a buscar
     * @return lista de capaitadores con el mismo nÃºmero de celular del
     * capacitador recibido como parÃ¡metro
     */
    public List<Administrador> celularRepetido(Administrador adm) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Administrador c WHERE c.celular = ?1");
            q.setParameter(1, adm.getCelular());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Este mÃ©todo permite buscar cÃ©dulas repetidas
     *
     * @param adm capacitador al que le pretenece el nÃºmero de cÃ©dula a buscar
     * @return lista de capaitadores con el mismo nÃºmero de cÃ©dula del
     * capacitador recibido como parÃ¡metro
     */
    public List<Administrador> cedulaRepetida(Administrador adm) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Administrador c WHERE c.cedula = ?1");
            q.setParameter(1, adm.getCedula());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Administrador> logueado(String correo, String contrasenia) {
        EntityManager em = getEntityManager();
        try {
            Query q
                    = em.createQuery("SELECT a FROM Administrador a WHERE a.correo = ?1 AND a.contrasenia = ?2");
            q.setParameter(1, correo);
            q.setParameter(2, contrasenia);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Administrador> obtenerCorreo(String correo) {
        EntityManager em = getEntityManager();
        try {
            Query q
                    = em.createQuery("SELECT a FROM Administrador a WHERE a.correo = ?1");
            q.setParameter(1, correo);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

}
