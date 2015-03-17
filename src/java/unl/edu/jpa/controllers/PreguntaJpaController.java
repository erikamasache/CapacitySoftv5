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
import unl.edu.jpa.entities.Evaluacion;
import unl.edu.jpa.entities.Respuesta;
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
import unl.edu.jpa.entities.EvaluacionCapacitado;
import unl.edu.jpa.entities.Pregunta;

/**
 *
 * @author ERIKA
 */
public class PreguntaJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public PreguntaJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pregunta pregunta) throws RollbackFailureException, Exception {
        if (pregunta.getRespuestaList() == null) {
            pregunta.setRespuestaList(new ArrayList<Respuesta>());
        }
        if (pregunta.getEvaluacionCapacitadoList() == null) {
            pregunta.setEvaluacionCapacitadoList(new ArrayList<EvaluacionCapacitado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Evaluacion evaluacionId = pregunta.getEvaluacionId();
            if (evaluacionId != null) {
                evaluacionId = em.getReference(evaluacionId.getClass(), evaluacionId.getId());
                pregunta.setEvaluacionId(evaluacionId);
            }
            List<Respuesta> attachedRespuestaList = new ArrayList<Respuesta>();
            for (Respuesta respuestaListRespuestaToAttach : pregunta.getRespuestaList()) {
                respuestaListRespuestaToAttach = em.getReference(respuestaListRespuestaToAttach.getClass(), respuestaListRespuestaToAttach.getId());
                attachedRespuestaList.add(respuestaListRespuestaToAttach);
            }
            pregunta.setRespuestaList(attachedRespuestaList);
            List<EvaluacionCapacitado> attachedEvaluacionCapacitadoList = new ArrayList<EvaluacionCapacitado>();
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitadoToAttach : pregunta.getEvaluacionCapacitadoList()) {
                evaluacionCapacitadoListEvaluacionCapacitadoToAttach = em.getReference(evaluacionCapacitadoListEvaluacionCapacitadoToAttach.getClass(), evaluacionCapacitadoListEvaluacionCapacitadoToAttach.getId());
                attachedEvaluacionCapacitadoList.add(evaluacionCapacitadoListEvaluacionCapacitadoToAttach);
            }
            pregunta.setEvaluacionCapacitadoList(attachedEvaluacionCapacitadoList);
            em.persist(pregunta);
            if (evaluacionId != null) {
                evaluacionId.getPreguntaList().add(pregunta);
                evaluacionId = em.merge(evaluacionId);
            }
            for (Respuesta respuestaListRespuesta : pregunta.getRespuestaList()) {
                Pregunta oldPreguntaIdOfRespuestaListRespuesta = respuestaListRespuesta.getPreguntaId();
                respuestaListRespuesta.setPreguntaId(pregunta);
                respuestaListRespuesta = em.merge(respuestaListRespuesta);
                if (oldPreguntaIdOfRespuestaListRespuesta != null) {
                    oldPreguntaIdOfRespuestaListRespuesta.getRespuestaList().remove(respuestaListRespuesta);
                    oldPreguntaIdOfRespuestaListRespuesta = em.merge(oldPreguntaIdOfRespuestaListRespuesta);
                }
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitado : pregunta.getEvaluacionCapacitadoList()) {
                Pregunta oldPreguntaIdOfEvaluacionCapacitadoListEvaluacionCapacitado = evaluacionCapacitadoListEvaluacionCapacitado.getPreguntaId();
                evaluacionCapacitadoListEvaluacionCapacitado.setPreguntaId(pregunta);
                evaluacionCapacitadoListEvaluacionCapacitado = em.merge(evaluacionCapacitadoListEvaluacionCapacitado);
                if (oldPreguntaIdOfEvaluacionCapacitadoListEvaluacionCapacitado != null) {
                    oldPreguntaIdOfEvaluacionCapacitadoListEvaluacionCapacitado.getEvaluacionCapacitadoList().remove(evaluacionCapacitadoListEvaluacionCapacitado);
                    oldPreguntaIdOfEvaluacionCapacitadoListEvaluacionCapacitado = em.merge(oldPreguntaIdOfEvaluacionCapacitadoListEvaluacionCapacitado);
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

    public void edit(Pregunta pregunta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pregunta persistentPregunta = em.find(Pregunta.class, pregunta.getId());
            Evaluacion evaluacionIdOld = persistentPregunta.getEvaluacionId();
            Evaluacion evaluacionIdNew = pregunta.getEvaluacionId();
            List<Respuesta> respuestaListOld = persistentPregunta.getRespuestaList();
            List<Respuesta> respuestaListNew = pregunta.getRespuestaList();
            List<EvaluacionCapacitado> evaluacionCapacitadoListOld = persistentPregunta.getEvaluacionCapacitadoList();
            List<EvaluacionCapacitado> evaluacionCapacitadoListNew = pregunta.getEvaluacionCapacitadoList();
            if (evaluacionIdNew != null) {
                evaluacionIdNew = em.getReference(evaluacionIdNew.getClass(), evaluacionIdNew.getId());
                pregunta.setEvaluacionId(evaluacionIdNew);
            }
            List<Respuesta> attachedRespuestaListNew = new ArrayList<Respuesta>();
            for (Respuesta respuestaListNewRespuestaToAttach : respuestaListNew) {
                respuestaListNewRespuestaToAttach = em.getReference(respuestaListNewRespuestaToAttach.getClass(), respuestaListNewRespuestaToAttach.getId());
                attachedRespuestaListNew.add(respuestaListNewRespuestaToAttach);
            }
            respuestaListNew = attachedRespuestaListNew;
            pregunta.setRespuestaList(respuestaListNew);
            List<EvaluacionCapacitado> attachedEvaluacionCapacitadoListNew = new ArrayList<EvaluacionCapacitado>();
            for (EvaluacionCapacitado evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach : evaluacionCapacitadoListNew) {
                evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach = em.getReference(evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach.getClass(), evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach.getId());
                attachedEvaluacionCapacitadoListNew.add(evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach);
            }
            evaluacionCapacitadoListNew = attachedEvaluacionCapacitadoListNew;
            pregunta.setEvaluacionCapacitadoList(evaluacionCapacitadoListNew);
            pregunta = em.merge(pregunta);
            if (evaluacionIdOld != null && !evaluacionIdOld.equals(evaluacionIdNew)) {
                evaluacionIdOld.getPreguntaList().remove(pregunta);
                evaluacionIdOld = em.merge(evaluacionIdOld);
            }
            if (evaluacionIdNew != null && !evaluacionIdNew.equals(evaluacionIdOld)) {
                evaluacionIdNew.getPreguntaList().add(pregunta);
                evaluacionIdNew = em.merge(evaluacionIdNew);
            }
            for (Respuesta respuestaListOldRespuesta : respuestaListOld) {
                if (!respuestaListNew.contains(respuestaListOldRespuesta)) {
                    respuestaListOldRespuesta.setPreguntaId(null);
                    respuestaListOldRespuesta = em.merge(respuestaListOldRespuesta);
                }
            }
            for (Respuesta respuestaListNewRespuesta : respuestaListNew) {
                if (!respuestaListOld.contains(respuestaListNewRespuesta)) {
                    Pregunta oldPreguntaIdOfRespuestaListNewRespuesta = respuestaListNewRespuesta.getPreguntaId();
                    respuestaListNewRespuesta.setPreguntaId(pregunta);
                    respuestaListNewRespuesta = em.merge(respuestaListNewRespuesta);
                    if (oldPreguntaIdOfRespuestaListNewRespuesta != null && !oldPreguntaIdOfRespuestaListNewRespuesta.equals(pregunta)) {
                        oldPreguntaIdOfRespuestaListNewRespuesta.getRespuestaList().remove(respuestaListNewRespuesta);
                        oldPreguntaIdOfRespuestaListNewRespuesta = em.merge(oldPreguntaIdOfRespuestaListNewRespuesta);
                    }
                }
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListOldEvaluacionCapacitado : evaluacionCapacitadoListOld) {
                if (!evaluacionCapacitadoListNew.contains(evaluacionCapacitadoListOldEvaluacionCapacitado)) {
                    evaluacionCapacitadoListOldEvaluacionCapacitado.setPreguntaId(null);
                    evaluacionCapacitadoListOldEvaluacionCapacitado = em.merge(evaluacionCapacitadoListOldEvaluacionCapacitado);
                }
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListNewEvaluacionCapacitado : evaluacionCapacitadoListNew) {
                if (!evaluacionCapacitadoListOld.contains(evaluacionCapacitadoListNewEvaluacionCapacitado)) {
                    Pregunta oldPreguntaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado = evaluacionCapacitadoListNewEvaluacionCapacitado.getPreguntaId();
                    evaluacionCapacitadoListNewEvaluacionCapacitado.setPreguntaId(pregunta);
                    evaluacionCapacitadoListNewEvaluacionCapacitado = em.merge(evaluacionCapacitadoListNewEvaluacionCapacitado);
                    if (oldPreguntaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado != null && !oldPreguntaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado.equals(pregunta)) {
                        oldPreguntaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado.getEvaluacionCapacitadoList().remove(evaluacionCapacitadoListNewEvaluacionCapacitado);
                        oldPreguntaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado = em.merge(oldPreguntaIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado);
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
                Integer id = pregunta.getId();
                if (findPregunta(id) == null) {
                    throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.");
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
            Pregunta pregunta;
            try {
                pregunta = em.getReference(Pregunta.class, id);
                pregunta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.", enfe);
            }
            Evaluacion evaluacionId = pregunta.getEvaluacionId();
            if (evaluacionId != null) {
                evaluacionId.getPreguntaList().remove(pregunta);
                evaluacionId = em.merge(evaluacionId);
            }
            List<Respuesta> respuestaList = pregunta.getRespuestaList();
            for (Respuesta respuestaListRespuesta : respuestaList) {
                respuestaListRespuesta.setPreguntaId(null);
                respuestaListRespuesta = em.merge(respuestaListRespuesta);
            }
            List<EvaluacionCapacitado> evaluacionCapacitadoList = pregunta.getEvaluacionCapacitadoList();
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitado : evaluacionCapacitadoList) {
                evaluacionCapacitadoListEvaluacionCapacitado.setPreguntaId(null);
                evaluacionCapacitadoListEvaluacionCapacitado = em.merge(evaluacionCapacitadoListEvaluacionCapacitado);
            }
            em.remove(pregunta);
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

    public List<Pregunta> findPreguntaEntities() {
        return findPreguntaEntities(true, -1, -1);
    }

    public List<Pregunta> findPreguntaEntities(int maxResults, int firstResult) {
        return findPreguntaEntities(false, maxResults, firstResult);
    }

    private List<Pregunta> findPreguntaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pregunta.class));
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

    public Pregunta findPregunta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pregunta.class, id);
        } finally {
            em.close();
        }
    }

    public int getPreguntaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pregunta> rt = cq.from(Pregunta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
