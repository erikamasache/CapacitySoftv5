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
import unl.edu.jpa.entities.Administrador;
import unl.edu.jpa.entities.CapacitacionCapacitador;
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
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.Capacitador;

/**
 *
 * @author ERIKA
 */
public class CapacitadorJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public CapacitadorJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Capacitador capacitador) throws RollbackFailureException, Exception {
        if (capacitador.getCapacitacionCapacitadorList() == null) {
            capacitador.setCapacitacionCapacitadorList(new ArrayList<CapacitacionCapacitador>());
        }
        if (capacitador.getCapacitadoList() == null) {
            capacitador.setCapacitadoList(new ArrayList<Capacitado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Administrador administradorId = capacitador.getAdministradorId();
            if (administradorId != null) {
                administradorId = em.getReference(administradorId.getClass(), administradorId.getId());
                capacitador.setAdministradorId(administradorId);
            }
            List<CapacitacionCapacitador> attachedCapacitacionCapacitadorList = new ArrayList<CapacitacionCapacitador>();
            for (CapacitacionCapacitador capacitacionCapacitadorListCapacitacionCapacitadorToAttach : capacitador.getCapacitacionCapacitadorList()) {
                capacitacionCapacitadorListCapacitacionCapacitadorToAttach = em.getReference(capacitacionCapacitadorListCapacitacionCapacitadorToAttach.getClass(), capacitacionCapacitadorListCapacitacionCapacitadorToAttach.getId());
                attachedCapacitacionCapacitadorList.add(capacitacionCapacitadorListCapacitacionCapacitadorToAttach);
            }
            capacitador.setCapacitacionCapacitadorList(attachedCapacitacionCapacitadorList);
            List<Capacitado> attachedCapacitadoList = new ArrayList<Capacitado>();
            for (Capacitado capacitadoListCapacitadoToAttach : capacitador.getCapacitadoList()) {
                capacitadoListCapacitadoToAttach = em.getReference(capacitadoListCapacitadoToAttach.getClass(), capacitadoListCapacitadoToAttach.getId());
                attachedCapacitadoList.add(capacitadoListCapacitadoToAttach);
            }
            capacitador.setCapacitadoList(attachedCapacitadoList);
            em.persist(capacitador);
            if (administradorId != null) {
                administradorId.getCapacitadorList().add(capacitador);
                administradorId = em.merge(administradorId);
            }
            for (CapacitacionCapacitador capacitacionCapacitadorListCapacitacionCapacitador : capacitador.getCapacitacionCapacitadorList()) {
                Capacitador oldCapacitadorIdOfCapacitacionCapacitadorListCapacitacionCapacitador = capacitacionCapacitadorListCapacitacionCapacitador.getCapacitadorId();
                capacitacionCapacitadorListCapacitacionCapacitador.setCapacitadorId(capacitador);
                capacitacionCapacitadorListCapacitacionCapacitador = em.merge(capacitacionCapacitadorListCapacitacionCapacitador);
                if (oldCapacitadorIdOfCapacitacionCapacitadorListCapacitacionCapacitador != null) {
                    oldCapacitadorIdOfCapacitacionCapacitadorListCapacitacionCapacitador.getCapacitacionCapacitadorList().remove(capacitacionCapacitadorListCapacitacionCapacitador);
                    oldCapacitadorIdOfCapacitacionCapacitadorListCapacitacionCapacitador = em.merge(oldCapacitadorIdOfCapacitacionCapacitadorListCapacitacionCapacitador);
                }
            }
            for (Capacitado capacitadoListCapacitado : capacitador.getCapacitadoList()) {
                Capacitador oldCapacitadorIdOfCapacitadoListCapacitado = capacitadoListCapacitado.getCapacitadorId();
                capacitadoListCapacitado.setCapacitadorId(capacitador);
                capacitadoListCapacitado = em.merge(capacitadoListCapacitado);
                if (oldCapacitadorIdOfCapacitadoListCapacitado != null) {
                    oldCapacitadorIdOfCapacitadoListCapacitado.getCapacitadoList().remove(capacitadoListCapacitado);
                    oldCapacitadorIdOfCapacitadoListCapacitado = em.merge(oldCapacitadorIdOfCapacitadoListCapacitado);
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

    public void edit(Capacitador capacitador) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Capacitador persistentCapacitador = em.find(Capacitador.class, capacitador.getId());
            Administrador administradorIdOld = persistentCapacitador.getAdministradorId();
            Administrador administradorIdNew = capacitador.getAdministradorId();
            List<CapacitacionCapacitador> capacitacionCapacitadorListOld = persistentCapacitador.getCapacitacionCapacitadorList();
            List<CapacitacionCapacitador> capacitacionCapacitadorListNew = capacitador.getCapacitacionCapacitadorList();
            List<Capacitado> capacitadoListOld = persistentCapacitador.getCapacitadoList();
            List<Capacitado> capacitadoListNew = capacitador.getCapacitadoList();
            if (administradorIdNew != null) {
                administradorIdNew = em.getReference(administradorIdNew.getClass(), administradorIdNew.getId());
                capacitador.setAdministradorId(administradorIdNew);
            }
            List<CapacitacionCapacitador> attachedCapacitacionCapacitadorListNew = new ArrayList<CapacitacionCapacitador>();
            for (CapacitacionCapacitador capacitacionCapacitadorListNewCapacitacionCapacitadorToAttach : capacitacionCapacitadorListNew) {
                capacitacionCapacitadorListNewCapacitacionCapacitadorToAttach = em.getReference(capacitacionCapacitadorListNewCapacitacionCapacitadorToAttach.getClass(), capacitacionCapacitadorListNewCapacitacionCapacitadorToAttach.getId());
                attachedCapacitacionCapacitadorListNew.add(capacitacionCapacitadorListNewCapacitacionCapacitadorToAttach);
            }
            capacitacionCapacitadorListNew = attachedCapacitacionCapacitadorListNew;
            capacitador.setCapacitacionCapacitadorList(capacitacionCapacitadorListNew);
            List<Capacitado> attachedCapacitadoListNew = new ArrayList<Capacitado>();
            for (Capacitado capacitadoListNewCapacitadoToAttach : capacitadoListNew) {
                capacitadoListNewCapacitadoToAttach = em.getReference(capacitadoListNewCapacitadoToAttach.getClass(), capacitadoListNewCapacitadoToAttach.getId());
                attachedCapacitadoListNew.add(capacitadoListNewCapacitadoToAttach);
            }
            capacitadoListNew = attachedCapacitadoListNew;
            capacitador.setCapacitadoList(capacitadoListNew);
            capacitador = em.merge(capacitador);
            if (administradorIdOld != null && !administradorIdOld.equals(administradorIdNew)) {
                administradorIdOld.getCapacitadorList().remove(capacitador);
                administradorIdOld = em.merge(administradorIdOld);
            }
            if (administradorIdNew != null && !administradorIdNew.equals(administradorIdOld)) {
                administradorIdNew.getCapacitadorList().add(capacitador);
                administradorIdNew = em.merge(administradorIdNew);
            }
            for (CapacitacionCapacitador capacitacionCapacitadorListOldCapacitacionCapacitador : capacitacionCapacitadorListOld) {
                if (!capacitacionCapacitadorListNew.contains(capacitacionCapacitadorListOldCapacitacionCapacitador)) {
                    capacitacionCapacitadorListOldCapacitacionCapacitador.setCapacitadorId(null);
                    capacitacionCapacitadorListOldCapacitacionCapacitador = em.merge(capacitacionCapacitadorListOldCapacitacionCapacitador);
                }
            }
            for (CapacitacionCapacitador capacitacionCapacitadorListNewCapacitacionCapacitador : capacitacionCapacitadorListNew) {
                if (!capacitacionCapacitadorListOld.contains(capacitacionCapacitadorListNewCapacitacionCapacitador)) {
                    Capacitador oldCapacitadorIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador = capacitacionCapacitadorListNewCapacitacionCapacitador.getCapacitadorId();
                    capacitacionCapacitadorListNewCapacitacionCapacitador.setCapacitadorId(capacitador);
                    capacitacionCapacitadorListNewCapacitacionCapacitador = em.merge(capacitacionCapacitadorListNewCapacitacionCapacitador);
                    if (oldCapacitadorIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador != null && !oldCapacitadorIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador.equals(capacitador)) {
                        oldCapacitadorIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador.getCapacitacionCapacitadorList().remove(capacitacionCapacitadorListNewCapacitacionCapacitador);
                        oldCapacitadorIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador = em.merge(oldCapacitadorIdOfCapacitacionCapacitadorListNewCapacitacionCapacitador);
                    }
                }
            }
            for (Capacitado capacitadoListOldCapacitado : capacitadoListOld) {
                if (!capacitadoListNew.contains(capacitadoListOldCapacitado)) {
                    capacitadoListOldCapacitado.setCapacitadorId(null);
                    capacitadoListOldCapacitado = em.merge(capacitadoListOldCapacitado);
                }
            }
            for (Capacitado capacitadoListNewCapacitado : capacitadoListNew) {
                if (!capacitadoListOld.contains(capacitadoListNewCapacitado)) {
                    Capacitador oldCapacitadorIdOfCapacitadoListNewCapacitado = capacitadoListNewCapacitado.getCapacitadorId();
                    capacitadoListNewCapacitado.setCapacitadorId(capacitador);
                    capacitadoListNewCapacitado = em.merge(capacitadoListNewCapacitado);
                    if (oldCapacitadorIdOfCapacitadoListNewCapacitado != null && !oldCapacitadorIdOfCapacitadoListNewCapacitado.equals(capacitador)) {
                        oldCapacitadorIdOfCapacitadoListNewCapacitado.getCapacitadoList().remove(capacitadoListNewCapacitado);
                        oldCapacitadorIdOfCapacitadoListNewCapacitado = em.merge(oldCapacitadorIdOfCapacitadoListNewCapacitado);
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
                Integer id = capacitador.getId();
                if (findCapacitador(id) == null) {
                    throw new NonexistentEntityException("The capacitador with id " + id + " no longer exists.");
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
            Capacitador capacitador;
            try {
                capacitador = em.getReference(Capacitador.class, id);
                capacitador.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The capacitador with id " + id + " no longer exists.", enfe);
            }
            Administrador administradorId = capacitador.getAdministradorId();
            if (administradorId != null) {
                administradorId.getCapacitadorList().remove(capacitador);
                administradorId = em.merge(administradorId);
            }
            List<CapacitacionCapacitador> capacitacionCapacitadorList = capacitador.getCapacitacionCapacitadorList();
            for (CapacitacionCapacitador capacitacionCapacitadorListCapacitacionCapacitador : capacitacionCapacitadorList) {
                capacitacionCapacitadorListCapacitacionCapacitador.setCapacitadorId(null);
                capacitacionCapacitadorListCapacitacionCapacitador = em.merge(capacitacionCapacitadorListCapacitacionCapacitador);
            }
            List<Capacitado> capacitadoList = capacitador.getCapacitadoList();
            for (Capacitado capacitadoListCapacitado : capacitadoList) {
                capacitadoListCapacitado.setCapacitadorId(null);
                capacitadoListCapacitado = em.merge(capacitadoListCapacitado);
            }
            em.remove(capacitador);
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

    public List<Capacitador> findCapacitadorEntities() {
        return findCapacitadorEntities(true, -1, -1);
    }

    public List<Capacitador> findCapacitadorEntities(int maxResults, int firstResult) {
        return findCapacitadorEntities(false, maxResults, firstResult);
    }

    private List<Capacitador> findCapacitadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Capacitador.class));
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

    public Capacitador findCapacitador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Capacitador.class, id);
        } finally {
            em.close();
        }
    }

    public int getCapacitadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Capacitador> rt = cq.from(Capacitador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    /**
     * Este mÃ©todo permite buscar correos repetidos 
     * @param cap capacitador al que le pertenece el correo a buscar
     * @return lista de capaitadores con el mismo correo del capacitador 
     * recibido como parÃ¡metro
     */
    public List<Capacitador> correoRepetido(Capacitador cap) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitador c WHERE c.correo = ?1");
            q.setParameter(1, cap.getCorreo());
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Este mÃ©todo permite buscar nÃºmeros celular repetidos 
     * @param cap capacitador al que le pertenece el nÃºmero de celular a buscar
     * @return lista de capaitadores con el mismo nÃºmero de celular del capacitador 
     * recibido como parÃ¡metro
     */
    public List<Capacitador> celularRepetido(Capacitador cap) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitador c WHERE c.celular = ?1");
            q.setParameter(1, cap.getCelular());
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Este mÃ©todo permite buscar cÃ©dulas repetidas 
     * @param cap capacitador al que le pretenece el nÃºmero de cÃ©dula a buscar
     * @return lista de capaitadores con el mismo nÃºmero de cÃ©dula del capacitador 
     * recibido como parÃ¡metro
     */
    public List<Capacitador> cedulaRepetida(Capacitador cap) {
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitador c WHERE c.cedula = ?1");
            q.setParameter(1, cap.getCedula());
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
     public List<Capacitador> logueadoCapacitador(String correo, String contrasenia){
        EntityManager em = getEntityManager();
        try {
            Query q = 
            em.createQuery("SELECT a FROM Capacitador a WHERE a.correo = ?1 AND a.contrasenia = ?2");
            q.setParameter(1, correo);
            q.setParameter(2, contrasenia);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

     public List<Capacitador> listarCapacitadores(){
        EntityManager em = getEntityManager();
        try {
            Query q = em
                    .createQuery("SELECT c FROM Capacitador c ORDER BY c.apellido ASC");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
     
     public List<Capacitador> obtenerCorreo(String correo) {
        EntityManager em = getEntityManager();
        try {
            Query q
                    = em.createQuery("SELECT a FROM Capacitador a WHERE a.correo = ?1");
            q.setParameter(1, correo);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
