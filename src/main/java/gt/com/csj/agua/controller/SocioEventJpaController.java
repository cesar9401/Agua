/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.controller;

import gt.com.csj.agua.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.com.csj.agua.entity.Administrator;
import gt.com.csj.agua.entity.Event;
import gt.com.csj.agua.entity.Socio;
import gt.com.csj.agua.entity.SocioEvent;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author julio
 */
public class SocioEventJpaController implements Serializable {

    public SocioEventJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SocioEvent socioEvent) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrator administratorId = socioEvent.getAdministratorId();
            if (administratorId != null) {
                administratorId = em.getReference(administratorId.getClass(), administratorId.getAdministratorId());
                socioEvent.setAdministratorId(administratorId);
            }
            Event eventId = socioEvent.getEventId();
            if (eventId != null) {
                eventId = em.getReference(eventId.getClass(), eventId.getEventId());
                socioEvent.setEventId(eventId);
            }
            Socio socioId = socioEvent.getSocioId();
            if (socioId != null) {
                socioId = em.getReference(socioId.getClass(), socioId.getSocioId());
                socioEvent.setSocioId(socioId);
            }
            em.persist(socioEvent);
            if (administratorId != null) {
                administratorId.getSocioEventCollection().add(socioEvent);
                administratorId = em.merge(administratorId);
            }
            if (eventId != null) {
                eventId.getSocioEventCollection().add(socioEvent);
                eventId = em.merge(eventId);
            }
            if (socioId != null) {
                socioId.getSocioEventCollection().add(socioEvent);
                socioId = em.merge(socioId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SocioEvent socioEvent) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SocioEvent persistentSocioEvent = em.find(SocioEvent.class, socioEvent.getSocioEventId());
            Administrator administratorIdOld = persistentSocioEvent.getAdministratorId();
            Administrator administratorIdNew = socioEvent.getAdministratorId();
            Event eventIdOld = persistentSocioEvent.getEventId();
            Event eventIdNew = socioEvent.getEventId();
            Socio socioIdOld = persistentSocioEvent.getSocioId();
            Socio socioIdNew = socioEvent.getSocioId();
            if (administratorIdNew != null) {
                administratorIdNew = em.getReference(administratorIdNew.getClass(), administratorIdNew.getAdministratorId());
                socioEvent.setAdministratorId(administratorIdNew);
            }
            if (eventIdNew != null) {
                eventIdNew = em.getReference(eventIdNew.getClass(), eventIdNew.getEventId());
                socioEvent.setEventId(eventIdNew);
            }
            if (socioIdNew != null) {
                socioIdNew = em.getReference(socioIdNew.getClass(), socioIdNew.getSocioId());
                socioEvent.setSocioId(socioIdNew);
            }
            socioEvent = em.merge(socioEvent);
            if (administratorIdOld != null && !administratorIdOld.equals(administratorIdNew)) {
                administratorIdOld.getSocioEventCollection().remove(socioEvent);
                administratorIdOld = em.merge(administratorIdOld);
            }
            if (administratorIdNew != null && !administratorIdNew.equals(administratorIdOld)) {
                administratorIdNew.getSocioEventCollection().add(socioEvent);
                administratorIdNew = em.merge(administratorIdNew);
            }
            if (eventIdOld != null && !eventIdOld.equals(eventIdNew)) {
                eventIdOld.getSocioEventCollection().remove(socioEvent);
                eventIdOld = em.merge(eventIdOld);
            }
            if (eventIdNew != null && !eventIdNew.equals(eventIdOld)) {
                eventIdNew.getSocioEventCollection().add(socioEvent);
                eventIdNew = em.merge(eventIdNew);
            }
            if (socioIdOld != null && !socioIdOld.equals(socioIdNew)) {
                socioIdOld.getSocioEventCollection().remove(socioEvent);
                socioIdOld = em.merge(socioIdOld);
            }
            if (socioIdNew != null && !socioIdNew.equals(socioIdOld)) {
                socioIdNew.getSocioEventCollection().add(socioEvent);
                socioIdNew = em.merge(socioIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = socioEvent.getSocioEventId();
                if (findSocioEvent(id) == null) {
                    throw new NonexistentEntityException("The socioEvent with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SocioEvent socioEvent;
            try {
                socioEvent = em.getReference(SocioEvent.class, id);
                socioEvent.getSocioEventId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The socioEvent with id " + id + " no longer exists.", enfe);
            }
            Administrator administratorId = socioEvent.getAdministratorId();
            if (administratorId != null) {
                administratorId.getSocioEventCollection().remove(socioEvent);
                administratorId = em.merge(administratorId);
            }
            Event eventId = socioEvent.getEventId();
            if (eventId != null) {
                eventId.getSocioEventCollection().remove(socioEvent);
                eventId = em.merge(eventId);
            }
            Socio socioId = socioEvent.getSocioId();
            if (socioId != null) {
                socioId.getSocioEventCollection().remove(socioEvent);
                socioId = em.merge(socioId);
            }
            em.remove(socioEvent);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SocioEvent> findSocioEventEntities() {
        return findSocioEventEntities(true, -1, -1);
    }

    public List<SocioEvent> findSocioEventEntities(int maxResults, int firstResult) {
        return findSocioEventEntities(false, maxResults, firstResult);
    }

    private List<SocioEvent> findSocioEventEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SocioEvent.class));
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

    public SocioEvent findSocioEvent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SocioEvent.class, id);
        } finally {
            em.close();
        }
    }

    public int getSocioEventCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SocioEvent> rt = cq.from(SocioEvent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
