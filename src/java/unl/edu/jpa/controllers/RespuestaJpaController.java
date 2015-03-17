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
import unl.edu.jpa.entities.Pregunta;
import unl.edu.jpa.entities.EvaluacionCapacitado;
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
import unl.edu.jpa.entities.Respuesta;

/**
 *
 * @author ERIKA
 */
public class RespuestaJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public RespuestaJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Respuesta respuesta) throws RollbackFailureException, Exception {
        if (respuesta.getEvaluacionCapacitadoList() == null) {
            respuesta.setEvaluacionCapacitadoList(new ArrayList<EvaluacionCapacitado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pregunta preguntaId = respuesta.getPreguntaId();
            if (preguntaId != null) {
                preguntaId = em.getReference(preguntaId.getClass(), preguntaId.getId());
                respuesta.setPreguntaId(preguntaId);
            }
            List<EvaluacionCapacitado> attachedEvaluacionCapacitadoList = new ArrayList<EvaluacionCapacitado>();
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitadoToAttach : respuesta.getEvaluacionCapacitadoList()) {
                evaluacionCapacitadoListEvaluacionCapacitadoToAttach = em.getReference(evaluacionCapacitadoListEvaluacionCapacitadoToAttach.getClass(), evaluacionCapacitadoListEvaluacionCapacitadoToAttach.getId());
                attachedEvaluacionCapacitadoList.add(evaluacionCapacitadoListEvaluacionCapacitadoToAttach);
            }
            respuesta.setEvaluacionCapacitadoList(attachedEvaluacionCapacitadoList);
            em.persist(respuesta);
            if (preguntaId != null) {
                preguntaId.getRespuestaList().add(respuesta);
                preguntaId = em.merge(preguntaId);
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitado : respuesta.getEvaluacionCapacitadoList()) {
                Respuesta oldRespuestaIdOfEvaluacionCapacitadoListEvaluacionCapacitado = evaluacionCapacitadoListEvaluacionCapacitado.getRespuestaId();
                evaluacionCapacitadoListEvaluacionCapacitado.setRespuestaId(respuesta);
                evaluacionCapacitadoListEvaluacionCapacitado = em.merge(evaluacionCapacitadoListEvaluacionCapacitado);
                if (oldRespuestaIdOfEvaluacionCapacitadoListEvaluacionCapacitado != null) {
                    oldRespuestaIdOfEvaluacionCapacitadoListEvaluacionCapacitado.getEvaluacionCapacitadoList().remove(evaluacionCapacitadoListEvaluacionCapacitado);
                    oldRespuestaIdOfEvaluacionCapacitadoListEvaluacionCapacitado = em.merge(oldRespuestaIdOfEvaluacionCapacitadoListEvaluacionCapacitado);
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

    public void edit(Respuesta respuesta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Respuesta persistentRespuesta = em.find(Respuesta.class, respuesta.getId());
            Pregunta preguntaIdOld = persistentRespuesta.getPreguntaId();
            Pregunta preguntaIdNew = respuesta.getPreguntaId();
            List<EvaluacionCapacitado> evaluacionCapacitadoListOld = persistentRespuesta.getEvaluacionCapacitadoList();
            List<EvaluacionCapacitado> evaluacionCapacitadoListNew = respuesta.getEvaluacionCapacitadoList();
            if (preguntaIdNew != null) {
                preguntaIdNew = em.getReference(preguntaIdNew.getClass(), preguntaIdNew.getId());
                respuesta.setPreguntaId(preguntaIdNew);
            }
            List<EvaluacionCapacitado> attachedEvaluacionCapacitadoListNew = new ArrayList<EvaluacionCapacitado>();
            for (EvaluacionCapacitado evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach : evaluacionCapacitadoListNew) {
                evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach = em.getReference(evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach.getClass(), evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach.getId());
                attachedEvaluacionCapacitadoListNew.add(evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach);
            }
            evaluacionCapacitadoListNew = attachedEvaluacionCapacitadoListNew;
            respuesta.setEvaluacionCapacitadoList(evaluacionCapacitadoListNew);
            respuesta = em.merge(respuesta);
            if (preguntaIdOld != null && !preguntaIdOld.equals(preguntaIdNew)) {
                preguntaIdOld.getRespuestaList().remove(respuesta);
                preguntaIdOld = em.merge(preguntaIdOld);
            }
            if (preguntaIdNew != null && !preguntaIdNew.equals(preguntaIdOld)) {
                preguntaIdNew.getRespuestaList().add(respuesta);
                preguntaIdNew = em.merge(preguntaIdNew);
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListOldEvaluacionCapacitado : evaluacionCapacitadoListOld) {
                if (!evaluacionCapacitadoListNew.contains(evaluacionCapacitadoListOldEvaluacionCapacitado)) {
                    evaluacionCapacitadoListOldEvaluacionCapacitado.setRespuestaId(null);
                    evaluacionCapacitadoListOldEvaluacionCapacitado = em.merge(evaluacionCapacitadoListOldEvaluacionCapacitado);
                }
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListNewEvaluacionCapacitado : evaluacionCapacitadoListNew) {
                if (!evaluacionCapacitadoListOld.contains(evaluacionCapacitadoListNewEvaluacionCapacitado)) {
                    Respuesta oldRespuestaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado = evaluacionCapacitadoListNewEvaluacionCapacitado.getRespuestaId();
                    evaluacionCapacitadoListNewEvaluacionCapacitado.setRespuestaId(respuesta);
                    evaluacionCapacitadoListNewEvaluacionCapacitado = em.merge(evaluacionCapacitadoListNewEvaluacionCapacitado);
                    if (oldRespuestaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado != null && !oldRespuestaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado.equals(respuesta)) {
                        oldRespuestaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado.getEvaluacionCapacitadoList().remove(evaluacionCapacitadoListNewEvaluacionCapacitado);
                        oldRespuestaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado = em.merge(oldRespuestaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado);
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
                Integer id = respuesta.getId();
                if (findRespuesta(id) == null) {
                    throw new NonexistentEntityException("The respuesta with id " + id + " no longer exists.");
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
            Respuesta respuesta;
            try {
                respuesta = em.getReference(Respuesta.class, id);
                respuesta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The respuesta with id " + id + " no longer exists.", enfe);
            }
            Pregunta preguntaId = respuesta.getPreguntaId();
            if (preguntaId != null) {
                preguntaId.getRespuestaList().remove(respuesta);
                preguntaId = em.merge(preguntaId);
            }
            List<EvaluacionCapacitado> evaluacionCapacitadoList = respuesta.getEvaluacionCapacitadoList();
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitado : evaluacionCapacitadoList) {
                evaluacionCapacitadoListEvaluacionCapacitado.setRespuestaId(null);
                evaluacionCapacitadoListEvaluacionCapacitado = em.merge(evaluacionCapacitadoListEvaluacionCapacitado);
            }
            em.remove(respuesta);
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

    public List<Respuesta> findRespuestaEntities() {
        return findRespuestaEntities(true, -1, -1);
    }

    public List<Respuesta> findRespuestaEntities(int maxResults, int firstResult) {
        return findRespuestaEntities(false, maxResults, firstResult);
    }

    private List<Respuesta> findRespuestaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Respuesta.class));
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

    public Respuesta findRespuesta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Respuesta.class, id);
        } finally {
            em.close();
        }
    }

    public int getRespuestaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Respuesta> rt = cq.from(Respuesta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
