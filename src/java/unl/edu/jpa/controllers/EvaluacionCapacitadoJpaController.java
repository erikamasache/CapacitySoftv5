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
import unl.edu.jpa.entities.Evaluacion;
import unl.edu.jpa.entities.Respuesta;
import unl.edu.jpa.entities.Pregunta;
import unl.edu.jpa.entities.Capacitado;
import unl.edu.jpa.entities.EvaluacionCapacitado;

/**
 *
 * @author ERIKA
 */
public class EvaluacionCapacitadoJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public EvaluacionCapacitadoJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EvaluacionCapacitado evaluacionCapacitado) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Evaluacion evaluacionId = evaluacionCapacitado.getEvaluacionId();
            if (evaluacionId != null) {
                evaluacionId = em.getReference(evaluacionId.getClass(), evaluacionId.getId());
                evaluacionCapacitado.setEvaluacionId(evaluacionId);
            }
            Respuesta respuestaId = evaluacionCapacitado.getRespuestaId();
            if (respuestaId != null) {
                respuestaId = em.getReference(respuestaId.getClass(), respuestaId.getId());
                evaluacionCapacitado.setRespuestaId(respuestaId);
            }
            Pregunta preguntaId = evaluacionCapacitado.getPreguntaId();
            if (preguntaId != null) {
                preguntaId = em.getReference(preguntaId.getClass(), preguntaId.getId());
                evaluacionCapacitado.setPreguntaId(preguntaId);
            }
            Capacitado capacitadoId = evaluacionCapacitado.getCapacitadoId();
            if (capacitadoId != null) {
                capacitadoId = em.getReference(capacitadoId.getClass(), capacitadoId.getId());
                evaluacionCapacitado.setCapacitadoId(capacitadoId);
            }
            em.persist(evaluacionCapacitado);
            if (evaluacionId != null) {
                evaluacionId.getEvaluacionCapacitadoList().add(evaluacionCapacitado);
                evaluacionId = em.merge(evaluacionId);
            }
            if (respuestaId != null) {
                respuestaId.getEvaluacionCapacitadoList().add(evaluacionCapacitado);
                respuestaId = em.merge(respuestaId);
            }
            if (preguntaId != null) {
                preguntaId.getEvaluacionCapacitadoList().add(evaluacionCapacitado);
                preguntaId = em.merge(preguntaId);
            }
            if (capacitadoId != null) {
                capacitadoId.getEvaluacionCapacitadoList().add(evaluacionCapacitado);
                capacitadoId = em.merge(capacitadoId);
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

    public void edit(EvaluacionCapacitado evaluacionCapacitado) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EvaluacionCapacitado persistentEvaluacionCapacitado = em.find(EvaluacionCapacitado.class, evaluacionCapacitado.getId());
            Evaluacion evaluacionIdOld = persistentEvaluacionCapacitado.getEvaluacionId();
            Evaluacion evaluacionIdNew = evaluacionCapacitado.getEvaluacionId();
            Respuesta respuestaIdOld = persistentEvaluacionCapacitado.getRespuestaId();
            Respuesta respuestaIdNew = evaluacionCapacitado.getRespuestaId();
            Pregunta preguntaIdOld = persistentEvaluacionCapacitado.getPreguntaId();
            Pregunta preguntaIdNew = evaluacionCapacitado.getPreguntaId();
            Capacitado capacitadoIdOld = persistentEvaluacionCapacitado.getCapacitadoId();
            Capacitado capacitadoIdNew = evaluacionCapacitado.getCapacitadoId();
            if (evaluacionIdNew != null) {
                evaluacionIdNew = em.getReference(evaluacionIdNew.getClass(), evaluacionIdNew.getId());
                evaluacionCapacitado.setEvaluacionId(evaluacionIdNew);
            }
            if (respuestaIdNew != null) {
                respuestaIdNew = em.getReference(respuestaIdNew.getClass(), respuestaIdNew.getId());
                evaluacionCapacitado.setRespuestaId(respuestaIdNew);
            }
            if (preguntaIdNew != null) {
                preguntaIdNew = em.getReference(preguntaIdNew.getClass(), preguntaIdNew.getId());
                evaluacionCapacitado.setPreguntaId(preguntaIdNew);
            }
            if (capacitadoIdNew != null) {
                capacitadoIdNew = em.getReference(capacitadoIdNew.getClass(), capacitadoIdNew.getId());
                evaluacionCapacitado.setCapacitadoId(capacitadoIdNew);
            }
            evaluacionCapacitado = em.merge(evaluacionCapacitado);
            if (evaluacionIdOld != null && !evaluacionIdOld.equals(evaluacionIdNew)) {
                evaluacionIdOld.getEvaluacionCapacitadoList().remove(evaluacionCapacitado);
                evaluacionIdOld = em.merge(evaluacionIdOld);
            }
            if (evaluacionIdNew != null && !evaluacionIdNew.equals(evaluacionIdOld)) {
                evaluacionIdNew.getEvaluacionCapacitadoList().add(evaluacionCapacitado);
                evaluacionIdNew = em.merge(evaluacionIdNew);
            }
            if (respuestaIdOld != null && !respuestaIdOld.equals(respuestaIdNew)) {
                respuestaIdOld.getEvaluacionCapacitadoList().remove(evaluacionCapacitado);
                respuestaIdOld = em.merge(respuestaIdOld);
            }
            if (respuestaIdNew != null && !respuestaIdNew.equals(respuestaIdOld)) {
                respuestaIdNew.getEvaluacionCapacitadoList().add(evaluacionCapacitado);
                respuestaIdNew = em.merge(respuestaIdNew);
            }
            if (preguntaIdOld != null && !preguntaIdOld.equals(preguntaIdNew)) {
                preguntaIdOld.getEvaluacionCapacitadoList().remove(evaluacionCapacitado);
                preguntaIdOld = em.merge(preguntaIdOld);
            }
            if (preguntaIdNew != null && !preguntaIdNew.equals(preguntaIdOld)) {
                preguntaIdNew.getEvaluacionCapacitadoList().add(evaluacionCapacitado);
                preguntaIdNew = em.merge(preguntaIdNew);
            }
            if (capacitadoIdOld != null && !capacitadoIdOld.equals(capacitadoIdNew)) {
                capacitadoIdOld.getEvaluacionCapacitadoList().remove(evaluacionCapacitado);
                capacitadoIdOld = em.merge(capacitadoIdOld);
            }
            if (capacitadoIdNew != null && !capacitadoIdNew.equals(capacitadoIdOld)) {
                capacitadoIdNew.getEvaluacionCapacitadoList().add(evaluacionCapacitado);
                capacitadoIdNew = em.merge(capacitadoIdNew);
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
                Integer id = evaluacionCapacitado.getId();
                if (findEvaluacionCapacitado(id) == null) {
                    throw new NonexistentEntityException("The evaluacionCapacitado with id " + id + " no longer exists.");
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
            EvaluacionCapacitado evaluacionCapacitado;
            try {
                evaluacionCapacitado = em.getReference(EvaluacionCapacitado.class, id);
                evaluacionCapacitado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The evaluacionCapacitado with id " + id + " no longer exists.", enfe);
            }
            Evaluacion evaluacionId = evaluacionCapacitado.getEvaluacionId();
            if (evaluacionId != null) {
                evaluacionId.getEvaluacionCapacitadoList().remove(evaluacionCapacitado);
                evaluacionId = em.merge(evaluacionId);
            }
            Respuesta respuestaId = evaluacionCapacitado.getRespuestaId();
            if (respuestaId != null) {
                respuestaId.getEvaluacionCapacitadoList().remove(evaluacionCapacitado);
                respuestaId = em.merge(respuestaId);
            }
            Pregunta preguntaId = evaluacionCapacitado.getPreguntaId();
            if (preguntaId != null) {
                preguntaId.getEvaluacionCapacitadoList().remove(evaluacionCapacitado);
                preguntaId = em.merge(preguntaId);
            }
            Capacitado capacitadoId = evaluacionCapacitado.getCapacitadoId();
            if (capacitadoId != null) {
                capacitadoId.getEvaluacionCapacitadoList().remove(evaluacionCapacitado);
                capacitadoId = em.merge(capacitadoId);
            }
            em.remove(evaluacionCapacitado);
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

    public List<EvaluacionCapacitado> findEvaluacionCapacitadoEntities() {
        return findEvaluacionCapacitadoEntities(true, -1, -1);
    }

    public List<EvaluacionCapacitado> findEvaluacionCapacitadoEntities(int maxResults, int firstResult) {
        return findEvaluacionCapacitadoEntities(false, maxResults, firstResult);
    }

    private List<EvaluacionCapacitado> findEvaluacionCapacitadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EvaluacionCapacitado.class));
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

    public EvaluacionCapacitado findEvaluacionCapacitado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EvaluacionCapacitado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEvaluacionCapacitadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EvaluacionCapacitado> rt = cq.from(EvaluacionCapacitado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
