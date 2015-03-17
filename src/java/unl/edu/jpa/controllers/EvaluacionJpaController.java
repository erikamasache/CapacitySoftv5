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
import unl.edu.jpa.entities.Capacitacion;
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
import unl.edu.jpa.entities.Evaluacion;
import unl.edu.jpa.entities.Pregunta;
import unl.edu.jpa.entities.EvaluacionCapacitado;

/**
 *
 * @author ERIKA
 */
public class EvaluacionJpaController implements Serializable {

    // Modificar Constructor
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "CapacitySoftv5PU")
    private EntityManagerFactory emf = null;
    InitialContext ic = new InitialContext();

    public EvaluacionJpaController() throws NamingException {
        utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
        emf = Persistence
                .createEntityManagerFactory("CapacitySoftv5PU");
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Evaluacion evaluacion) throws RollbackFailureException, Exception {
        if (evaluacion.getCapacitacionList() == null) {
            evaluacion.setCapacitacionList(new ArrayList<Capacitacion>());
        }
        if (evaluacion.getPreguntaList() == null) {
            evaluacion.setPreguntaList(new ArrayList<Pregunta>());
        }
        if (evaluacion.getEvaluacionCapacitadoList() == null) {
            evaluacion.setEvaluacionCapacitadoList(new ArrayList<EvaluacionCapacitado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Capacitacion> attachedCapacitacionList = new ArrayList<Capacitacion>();
            for (Capacitacion capacitacionListCapacitacionToAttach : evaluacion.getCapacitacionList()) {
                capacitacionListCapacitacionToAttach = em.getReference(capacitacionListCapacitacionToAttach.getClass(), capacitacionListCapacitacionToAttach.getId());
                attachedCapacitacionList.add(capacitacionListCapacitacionToAttach);
            }
            evaluacion.setCapacitacionList(attachedCapacitacionList);
            List<Pregunta> attachedPreguntaList = new ArrayList<Pregunta>();
            for (Pregunta preguntaListPreguntaToAttach : evaluacion.getPreguntaList()) {
                preguntaListPreguntaToAttach = em.getReference(preguntaListPreguntaToAttach.getClass(), preguntaListPreguntaToAttach.getId());
                attachedPreguntaList.add(preguntaListPreguntaToAttach);
            }
            evaluacion.setPreguntaList(attachedPreguntaList);
            List<EvaluacionCapacitado> attachedEvaluacionCapacitadoList = new ArrayList<EvaluacionCapacitado>();
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitadoToAttach : evaluacion.getEvaluacionCapacitadoList()) {
                evaluacionCapacitadoListEvaluacionCapacitadoToAttach = em.getReference(evaluacionCapacitadoListEvaluacionCapacitadoToAttach.getClass(), evaluacionCapacitadoListEvaluacionCapacitadoToAttach.getId());
                attachedEvaluacionCapacitadoList.add(evaluacionCapacitadoListEvaluacionCapacitadoToAttach);
            }
            evaluacion.setEvaluacionCapacitadoList(attachedEvaluacionCapacitadoList);
            em.persist(evaluacion);
            for (Capacitacion capacitacionListCapacitacion : evaluacion.getCapacitacionList()) {
                Evaluacion oldEvaluacionIdOfCapacitacionListCapacitacion = capacitacionListCapacitacion.getEvaluacionId();
                capacitacionListCapacitacion.setEvaluacionId(evaluacion);
                capacitacionListCapacitacion = em.merge(capacitacionListCapacitacion);
                if (oldEvaluacionIdOfCapacitacionListCapacitacion != null) {
                    oldEvaluacionIdOfCapacitacionListCapacitacion.getCapacitacionList().remove(capacitacionListCapacitacion);
                    oldEvaluacionIdOfCapacitacionListCapacitacion = em.merge(oldEvaluacionIdOfCapacitacionListCapacitacion);
                }
            }
            for (Pregunta preguntaListPregunta : evaluacion.getPreguntaList()) {
                Evaluacion oldEvaluacionIdOfPreguntaListPregunta = preguntaListPregunta.getEvaluacionId();
                preguntaListPregunta.setEvaluacionId(evaluacion);
                preguntaListPregunta = em.merge(preguntaListPregunta);
                if (oldEvaluacionIdOfPreguntaListPregunta != null) {
                    oldEvaluacionIdOfPreguntaListPregunta.getPreguntaList().remove(preguntaListPregunta);
                    oldEvaluacionIdOfPreguntaListPregunta = em.merge(oldEvaluacionIdOfPreguntaListPregunta);
                }
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitado : evaluacion.getEvaluacionCapacitadoList()) {
                Evaluacion oldEvaluacionIdOfEvaluacionCapacitadoListEvaluacionCapacitado = evaluacionCapacitadoListEvaluacionCapacitado.getEvaluacionId();
                evaluacionCapacitadoListEvaluacionCapacitado.setEvaluacionId(evaluacion);
                evaluacionCapacitadoListEvaluacionCapacitado = em.merge(evaluacionCapacitadoListEvaluacionCapacitado);
                if (oldEvaluacionIdOfEvaluacionCapacitadoListEvaluacionCapacitado != null) {
                    oldEvaluacionIdOfEvaluacionCapacitadoListEvaluacionCapacitado.getEvaluacionCapacitadoList().remove(evaluacionCapacitadoListEvaluacionCapacitado);
                    oldEvaluacionIdOfEvaluacionCapacitadoListEvaluacionCapacitado = em.merge(oldEvaluacionIdOfEvaluacionCapacitadoListEvaluacionCapacitado);
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

    public void edit(Evaluacion evaluacion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Evaluacion persistentEvaluacion = em.find(Evaluacion.class, evaluacion.getId());
            List<Capacitacion> capacitacionListOld = persistentEvaluacion.getCapacitacionList();
            List<Capacitacion> capacitacionListNew = evaluacion.getCapacitacionList();
            List<Pregunta> preguntaListOld = persistentEvaluacion.getPreguntaList();
            List<Pregunta> preguntaListNew = evaluacion.getPreguntaList();
            List<EvaluacionCapacitado> evaluacionCapacitadoListOld = persistentEvaluacion.getEvaluacionCapacitadoList();
            List<EvaluacionCapacitado> evaluacionCapacitadoListNew = evaluacion.getEvaluacionCapacitadoList();
            List<Capacitacion> attachedCapacitacionListNew = new ArrayList<Capacitacion>();
            for (Capacitacion capacitacionListNewCapacitacionToAttach : capacitacionListNew) {
                capacitacionListNewCapacitacionToAttach = em.getReference(capacitacionListNewCapacitacionToAttach.getClass(), capacitacionListNewCapacitacionToAttach.getId());
                attachedCapacitacionListNew.add(capacitacionListNewCapacitacionToAttach);
            }
            capacitacionListNew = attachedCapacitacionListNew;
            evaluacion.setCapacitacionList(capacitacionListNew);
            List<Pregunta> attachedPreguntaListNew = new ArrayList<Pregunta>();
            for (Pregunta preguntaListNewPreguntaToAttach : preguntaListNew) {
                preguntaListNewPreguntaToAttach = em.getReference(preguntaListNewPreguntaToAttach.getClass(), preguntaListNewPreguntaToAttach.getId());
                attachedPreguntaListNew.add(preguntaListNewPreguntaToAttach);
            }
            preguntaListNew = attachedPreguntaListNew;
            evaluacion.setPreguntaList(preguntaListNew);
            List<EvaluacionCapacitado> attachedEvaluacionCapacitadoListNew = new ArrayList<EvaluacionCapacitado>();
            for (EvaluacionCapacitado evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach : evaluacionCapacitadoListNew) {
                evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach = em.getReference(evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach.getClass(), evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach.getId());
                attachedEvaluacionCapacitadoListNew.add(evaluacionCapacitadoListNewEvaluacionCapacitadoToAttach);
            }
            evaluacionCapacitadoListNew = attachedEvaluacionCapacitadoListNew;
            evaluacion.setEvaluacionCapacitadoList(evaluacionCapacitadoListNew);
            evaluacion = em.merge(evaluacion);
            for (Capacitacion capacitacionListOldCapacitacion : capacitacionListOld) {
                if (!capacitacionListNew.contains(capacitacionListOldCapacitacion)) {
                    capacitacionListOldCapacitacion.setEvaluacionId(null);
                    capacitacionListOldCapacitacion = em.merge(capacitacionListOldCapacitacion);
                }
            }
            for (Capacitacion capacitacionListNewCapacitacion : capacitacionListNew) {
                if (!capacitacionListOld.contains(capacitacionListNewCapacitacion)) {
                    Evaluacion oldEvaluacionIdOfCapacitacionListNewCapacitacion = capacitacionListNewCapacitacion.getEvaluacionId();
                    capacitacionListNewCapacitacion.setEvaluacionId(evaluacion);
                    capacitacionListNewCapacitacion = em.merge(capacitacionListNewCapacitacion);
                    if (oldEvaluacionIdOfCapacitacionListNewCapacitacion != null && !oldEvaluacionIdOfCapacitacionListNewCapacitacion.equals(evaluacion)) {
                        oldEvaluacionIdOfCapacitacionListNewCapacitacion.getCapacitacionList().remove(capacitacionListNewCapacitacion);
                        oldEvaluacionIdOfCapacitacionListNewCapacitacion = em.merge(oldEvaluacionIdOfCapacitacionListNewCapacitacion);
                    }
                }
            }
            for (Pregunta preguntaListOldPregunta : preguntaListOld) {
                if (!preguntaListNew.contains(preguntaListOldPregunta)) {
                    preguntaListOldPregunta.setEvaluacionId(null);
                    preguntaListOldPregunta = em.merge(preguntaListOldPregunta);
                }
            }
            for (Pregunta preguntaListNewPregunta : preguntaListNew) {
                if (!preguntaListOld.contains(preguntaListNewPregunta)) {
                    Evaluacion oldEvaluacionIdOfPreguntaListNewPregunta = preguntaListNewPregunta.getEvaluacionId();
                    preguntaListNewPregunta.setEvaluacionId(evaluacion);
                    preguntaListNewPregunta = em.merge(preguntaListNewPregunta);
                    if (oldEvaluacionIdOfPreguntaListNewPregunta != null && !oldEvaluacionIdOfPreguntaListNewPregunta.equals(evaluacion)) {
                        oldEvaluacionIdOfPreguntaListNewPregunta.getPreguntaList().remove(preguntaListNewPregunta);
                        oldEvaluacionIdOfPreguntaListNewPregunta = em.merge(oldEvaluacionIdOfPreguntaListNewPregunta);
                    }
                }
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListOldEvaluacionCapacitado : evaluacionCapacitadoListOld) {
                if (!evaluacionCapacitadoListNew.contains(evaluacionCapacitadoListOldEvaluacionCapacitado)) {
                    evaluacionCapacitadoListOldEvaluacionCapacitado.setEvaluacionId(null);
                    evaluacionCapacitadoListOldEvaluacionCapacitado = em.merge(evaluacionCapacitadoListOldEvaluacionCapacitado);
                }
            }
            for (EvaluacionCapacitado evaluacionCapacitadoListNewEvaluacionCapacitado : evaluacionCapacitadoListNew) {
                if (!evaluacionCapacitadoListOld.contains(evaluacionCapacitadoListNewEvaluacionCapacitado)) {
                    Evaluacion oldEvaluacionIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado = evaluacionCapacitadoListNewEvaluacionCapacitado.getEvaluacionId();
                    evaluacionCapacitadoListNewEvaluacionCapacitado.setEvaluacionId(evaluacion);
                    evaluacionCapacitadoListNewEvaluacionCapacitado = em.merge(evaluacionCapacitadoListNewEvaluacionCapacitado);
                    if (oldEvaluacionIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado != null && !oldEvaluacionIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado.equals(evaluacion)) {
                        oldEvaluacionIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado.getEvaluacionCapacitadoList().remove(evaluacionCapacitadoListNewEvaluacionCapacitado);
                        oldEvaluacionIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado = em.merge(oldEvaluacionIdOfEvaluacionCapacitadoListNewEvaluacionCapacitado);
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
                Integer id = evaluacion.getId();
                if (findEvaluacion(id) == null) {
                    throw new NonexistentEntityException("The evaluacion with id " + id + " no longer exists.");
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
            Evaluacion evaluacion;
            try {
                evaluacion = em.getReference(Evaluacion.class, id);
                evaluacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The evaluacion with id " + id + " no longer exists.", enfe);
            }
            List<Capacitacion> capacitacionList = evaluacion.getCapacitacionList();
            for (Capacitacion capacitacionListCapacitacion : capacitacionList) {
                capacitacionListCapacitacion.setEvaluacionId(null);
                capacitacionListCapacitacion = em.merge(capacitacionListCapacitacion);
            }
            List<Pregunta> preguntaList = evaluacion.getPreguntaList();
            for (Pregunta preguntaListPregunta : preguntaList) {
                preguntaListPregunta.setEvaluacionId(null);
                preguntaListPregunta = em.merge(preguntaListPregunta);
            }
            List<EvaluacionCapacitado> evaluacionCapacitadoList = evaluacion.getEvaluacionCapacitadoList();
            for (EvaluacionCapacitado evaluacionCapacitadoListEvaluacionCapacitado : evaluacionCapacitadoList) {
                evaluacionCapacitadoListEvaluacionCapacitado.setEvaluacionId(null);
                evaluacionCapacitadoListEvaluacionCapacitado = em.merge(evaluacionCapacitadoListEvaluacionCapacitado);
            }
            em.remove(evaluacion);
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

    public List<Evaluacion> findEvaluacionEntities() {
        return findEvaluacionEntities(true, -1, -1);
    }

    public List<Evaluacion> findEvaluacionEntities(int maxResults, int firstResult) {
        return findEvaluacionEntities(false, maxResults, firstResult);
    }

    private List<Evaluacion> findEvaluacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Evaluacion.class));
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

    public Evaluacion findEvaluacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Evaluacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getEvaluacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Evaluacion> rt = cq.from(Evaluacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
