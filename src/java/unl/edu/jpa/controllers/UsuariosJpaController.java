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
import unl.edu.jpa.entities.Grupos;
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
import unl.edu.jpa.controllers.exceptions.PreexistingEntityException;
import unl.edu.jpa.controllers.exceptions.RollbackFailureException;
import unl.edu.jpa.entities.Usuarios;

/**
 *
 * @author ERIKA
 */
public class UsuariosJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public UsuariosJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (usuarios.getGruposList() == null) {
            usuarios.setGruposList(new ArrayList<Grupos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Grupos> attachedGruposList = new ArrayList<Grupos>();
            for (Grupos gruposListGruposToAttach : usuarios.getGruposList()) {
                gruposListGruposToAttach = em.getReference(gruposListGruposToAttach.getClass(), gruposListGruposToAttach.getId());
                attachedGruposList.add(gruposListGruposToAttach);
            }
            usuarios.setGruposList(attachedGruposList);
            em.persist(usuarios);
            for (Grupos gruposListGrupos : usuarios.getGruposList()) {
                Usuarios oldCorreoOfGruposListGrupos = gruposListGrupos.getCorreo();
                gruposListGrupos.setCorreo(usuarios);
                gruposListGrupos = em.merge(gruposListGrupos);
                if (oldCorreoOfGruposListGrupos != null) {
                    oldCorreoOfGruposListGrupos.getGruposList().remove(gruposListGrupos);
                    oldCorreoOfGruposListGrupos = em.merge(oldCorreoOfGruposListGrupos);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsuarios(usuarios.getCorreo()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getCorreo());
            List<Grupos> gruposListOld = persistentUsuarios.getGruposList();
            List<Grupos> gruposListNew = usuarios.getGruposList();
            List<Grupos> attachedGruposListNew = new ArrayList<Grupos>();
            for (Grupos gruposListNewGruposToAttach : gruposListNew) {
                gruposListNewGruposToAttach = em.getReference(gruposListNewGruposToAttach.getClass(), gruposListNewGruposToAttach.getId());
                attachedGruposListNew.add(gruposListNewGruposToAttach);
            }
            gruposListNew = attachedGruposListNew;
            usuarios.setGruposList(gruposListNew);
            usuarios = em.merge(usuarios);
            for (Grupos gruposListOldGrupos : gruposListOld) {
                if (!gruposListNew.contains(gruposListOldGrupos)) {
                    gruposListOldGrupos.setCorreo(null);
                    gruposListOldGrupos = em.merge(gruposListOldGrupos);
                }
            }
            for (Grupos gruposListNewGrupos : gruposListNew) {
                if (!gruposListOld.contains(gruposListNewGrupos)) {
                    Usuarios oldCorreoOfGruposListNewGrupos = gruposListNewGrupos.getCorreo();
                    gruposListNewGrupos.setCorreo(usuarios);
                    gruposListNewGrupos = em.merge(gruposListNewGrupos);
                    if (oldCorreoOfGruposListNewGrupos != null && !oldCorreoOfGruposListNewGrupos.equals(usuarios)) {
                        oldCorreoOfGruposListNewGrupos.getGruposList().remove(gruposListNewGrupos);
                        oldCorreoOfGruposListNewGrupos = em.merge(oldCorreoOfGruposListNewGrupos);
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
                String id = usuarios.getCorreo();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getCorreo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<Grupos> gruposList = usuarios.getGruposList();
            for (Grupos gruposListGrupos : gruposList) {
                gruposListGrupos.setCorreo(null);
                gruposListGrupos = em.merge(gruposListGrupos);
            }
            em.remove(usuarios);
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

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
