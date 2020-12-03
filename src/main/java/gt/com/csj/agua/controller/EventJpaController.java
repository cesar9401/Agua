/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.controller;

import gt.com.csj.agua.controller.exceptions.IllegalOrphanException;
import gt.com.csj.agua.controller.exceptions.NonexistentEntityException;
import gt.com.csj.agua.controller.exceptions.PreexistingEntityException;
import gt.com.csj.agua.entity.Event;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.com.csj.agua.entity.SocioEvent;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author julio
 */
public class EventJpaController implements Serializable {

    public EventJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Event event) throws PreexistingEntityException, Exception {
        if (event.getSocioEventList() == null) {
            event.setSocioEventList(new ArrayList<SocioEvent>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SocioEvent> attachedSocioEventList = new ArrayList<SocioEvent>();
            for (SocioEvent socioEventListSocioEventToAttach : event.getSocioEventList()) {
                socioEventListSocioEventToAttach = em.getReference(socioEventListSocioEventToAttach.getClass(), socioEventListSocioEventToAttach.getSocioEventId());
                attachedSocioEventList.add(socioEventListSocioEventToAttach);
            }
            event.setSocioEventList(attachedSocioEventList);
            em.persist(event);
            for (SocioEvent socioEventListSocioEvent : event.getSocioEventList()) {
                Event oldEventIdOfSocioEventListSocioEvent = socioEventListSocioEvent.getEventId();
                socioEventListSocioEvent.setEventId(event);
                socioEventListSocioEvent = em.merge(socioEventListSocioEvent);
                if (oldEventIdOfSocioEventListSocioEvent != null) {
                    oldEventIdOfSocioEventListSocioEvent.getSocioEventList().remove(socioEventListSocioEvent);
                    oldEventIdOfSocioEventListSocioEvent = em.merge(oldEventIdOfSocioEventListSocioEvent);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEvent(event.getEventId()) != null) {
                throw new PreexistingEntityException("Event " + event + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Event event) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Event persistentEvent = em.find(Event.class, event.getEventId());
            List<SocioEvent> socioEventListOld = persistentEvent.getSocioEventList();
            List<SocioEvent> socioEventListNew = event.getSocioEventList();
            List<String> illegalOrphanMessages = null;
            for (SocioEvent socioEventListOldSocioEvent : socioEventListOld) {
                if (!socioEventListNew.contains(socioEventListOldSocioEvent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioEvent " + socioEventListOldSocioEvent + " since its eventId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SocioEvent> attachedSocioEventListNew = new ArrayList<SocioEvent>();
            for (SocioEvent socioEventListNewSocioEventToAttach : socioEventListNew) {
                socioEventListNewSocioEventToAttach = em.getReference(socioEventListNewSocioEventToAttach.getClass(), socioEventListNewSocioEventToAttach.getSocioEventId());
                attachedSocioEventListNew.add(socioEventListNewSocioEventToAttach);
            }
            socioEventListNew = attachedSocioEventListNew;
            event.setSocioEventList(socioEventListNew);
            event = em.merge(event);
            for (SocioEvent socioEventListNewSocioEvent : socioEventListNew) {
                if (!socioEventListOld.contains(socioEventListNewSocioEvent)) {
                    Event oldEventIdOfSocioEventListNewSocioEvent = socioEventListNewSocioEvent.getEventId();
                    socioEventListNewSocioEvent.setEventId(event);
                    socioEventListNewSocioEvent = em.merge(socioEventListNewSocioEvent);
                    if (oldEventIdOfSocioEventListNewSocioEvent != null && !oldEventIdOfSocioEventListNewSocioEvent.equals(event)) {
                        oldEventIdOfSocioEventListNewSocioEvent.getSocioEventList().remove(socioEventListNewSocioEvent);
                        oldEventIdOfSocioEventListNewSocioEvent = em.merge(oldEventIdOfSocioEventListNewSocioEvent);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = event.getEventId();
                if (findEvent(id) == null) {
                    throw new NonexistentEntityException("The event with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Event event;
            try {
                event = em.getReference(Event.class, id);
                event.getEventId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The event with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SocioEvent> socioEventListOrphanCheck = event.getSocioEventList();
            for (SocioEvent socioEventListOrphanCheckSocioEvent : socioEventListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Event (" + event + ") cannot be destroyed since the SocioEvent " + socioEventListOrphanCheckSocioEvent + " in its socioEventList field has a non-nullable eventId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(event);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Event> findEventEntities() {
        return findEventEntities(true, -1, -1);
    }

    public List<Event> findEventEntities(int maxResults, int firstResult) {
        return findEventEntities(false, maxResults, firstResult);
    }

    private List<Event> findEventEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Event.class));
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

    public Event findEvent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Event.class, id);
        } finally {
            em.close();
        }
    }

    public int getEventCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Event> rt = cq.from(Event.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
