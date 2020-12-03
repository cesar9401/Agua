/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.controller;

import gt.com.csj.agua.controller.exceptions.IllegalOrphanException;
import gt.com.csj.agua.controller.exceptions.NonexistentEntityException;
import gt.com.csj.agua.entity.Administrator;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.com.csj.agua.entity.Socio;
import gt.com.csj.agua.entity.SocioEvent;
import java.util.ArrayList;
import java.util.Collection;
import gt.com.csj.agua.entity.SocioPayment;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author julio
 */
public class AdministratorJpaController implements Serializable {

    public AdministratorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Administrator administrator) {
        if (administrator.getSocioEventCollection() == null) {
            administrator.setSocioEventCollection(new ArrayList<SocioEvent>());
        }
        if (administrator.getSocioPaymentCollection() == null) {
            administrator.setSocioPaymentCollection(new ArrayList<SocioPayment>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Socio socioId = administrator.getSocioId();
            if (socioId != null) {
                socioId = em.getReference(socioId.getClass(), socioId.getSocioId());
                administrator.setSocioId(socioId);
            }
            Collection<SocioEvent> attachedSocioEventCollection = new ArrayList<SocioEvent>();
            for (SocioEvent socioEventCollectionSocioEventToAttach : administrator.getSocioEventCollection()) {
                socioEventCollectionSocioEventToAttach = em.getReference(socioEventCollectionSocioEventToAttach.getClass(), socioEventCollectionSocioEventToAttach.getSocioEventId());
                attachedSocioEventCollection.add(socioEventCollectionSocioEventToAttach);
            }
            administrator.setSocioEventCollection(attachedSocioEventCollection);
            Collection<SocioPayment> attachedSocioPaymentCollection = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentCollectionSocioPaymentToAttach : administrator.getSocioPaymentCollection()) {
                socioPaymentCollectionSocioPaymentToAttach = em.getReference(socioPaymentCollectionSocioPaymentToAttach.getClass(), socioPaymentCollectionSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentCollection.add(socioPaymentCollectionSocioPaymentToAttach);
            }
            administrator.setSocioPaymentCollection(attachedSocioPaymentCollection);
            em.persist(administrator);
            if (socioId != null) {
                socioId.getAdministratorCollection().add(administrator);
                socioId = em.merge(socioId);
            }
            for (SocioEvent socioEventCollectionSocioEvent : administrator.getSocioEventCollection()) {
                Administrator oldAdministratorIdOfSocioEventCollectionSocioEvent = socioEventCollectionSocioEvent.getAdministratorId();
                socioEventCollectionSocioEvent.setAdministratorId(administrator);
                socioEventCollectionSocioEvent = em.merge(socioEventCollectionSocioEvent);
                if (oldAdministratorIdOfSocioEventCollectionSocioEvent != null) {
                    oldAdministratorIdOfSocioEventCollectionSocioEvent.getSocioEventCollection().remove(socioEventCollectionSocioEvent);
                    oldAdministratorIdOfSocioEventCollectionSocioEvent = em.merge(oldAdministratorIdOfSocioEventCollectionSocioEvent);
                }
            }
            for (SocioPayment socioPaymentCollectionSocioPayment : administrator.getSocioPaymentCollection()) {
                Administrator oldAdministratorIdOfSocioPaymentCollectionSocioPayment = socioPaymentCollectionSocioPayment.getAdministratorId();
                socioPaymentCollectionSocioPayment.setAdministratorId(administrator);
                socioPaymentCollectionSocioPayment = em.merge(socioPaymentCollectionSocioPayment);
                if (oldAdministratorIdOfSocioPaymentCollectionSocioPayment != null) {
                    oldAdministratorIdOfSocioPaymentCollectionSocioPayment.getSocioPaymentCollection().remove(socioPaymentCollectionSocioPayment);
                    oldAdministratorIdOfSocioPaymentCollectionSocioPayment = em.merge(oldAdministratorIdOfSocioPaymentCollectionSocioPayment);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Administrator administrator) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrator persistentAdministrator = em.find(Administrator.class, administrator.getAdministratorId());
            Socio socioIdOld = persistentAdministrator.getSocioId();
            Socio socioIdNew = administrator.getSocioId();
            Collection<SocioEvent> socioEventCollectionOld = persistentAdministrator.getSocioEventCollection();
            Collection<SocioEvent> socioEventCollectionNew = administrator.getSocioEventCollection();
            Collection<SocioPayment> socioPaymentCollectionOld = persistentAdministrator.getSocioPaymentCollection();
            Collection<SocioPayment> socioPaymentCollectionNew = administrator.getSocioPaymentCollection();
            List<String> illegalOrphanMessages = null;
            for (SocioEvent socioEventCollectionOldSocioEvent : socioEventCollectionOld) {
                if (!socioEventCollectionNew.contains(socioEventCollectionOldSocioEvent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioEvent " + socioEventCollectionOldSocioEvent + " since its administratorId field is not nullable.");
                }
            }
            for (SocioPayment socioPaymentCollectionOldSocioPayment : socioPaymentCollectionOld) {
                if (!socioPaymentCollectionNew.contains(socioPaymentCollectionOldSocioPayment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioPayment " + socioPaymentCollectionOldSocioPayment + " since its administratorId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (socioIdNew != null) {
                socioIdNew = em.getReference(socioIdNew.getClass(), socioIdNew.getSocioId());
                administrator.setSocioId(socioIdNew);
            }
            Collection<SocioEvent> attachedSocioEventCollectionNew = new ArrayList<SocioEvent>();
            for (SocioEvent socioEventCollectionNewSocioEventToAttach : socioEventCollectionNew) {
                socioEventCollectionNewSocioEventToAttach = em.getReference(socioEventCollectionNewSocioEventToAttach.getClass(), socioEventCollectionNewSocioEventToAttach.getSocioEventId());
                attachedSocioEventCollectionNew.add(socioEventCollectionNewSocioEventToAttach);
            }
            socioEventCollectionNew = attachedSocioEventCollectionNew;
            administrator.setSocioEventCollection(socioEventCollectionNew);
            Collection<SocioPayment> attachedSocioPaymentCollectionNew = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentCollectionNewSocioPaymentToAttach : socioPaymentCollectionNew) {
                socioPaymentCollectionNewSocioPaymentToAttach = em.getReference(socioPaymentCollectionNewSocioPaymentToAttach.getClass(), socioPaymentCollectionNewSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentCollectionNew.add(socioPaymentCollectionNewSocioPaymentToAttach);
            }
            socioPaymentCollectionNew = attachedSocioPaymentCollectionNew;
            administrator.setSocioPaymentCollection(socioPaymentCollectionNew);
            administrator = em.merge(administrator);
            if (socioIdOld != null && !socioIdOld.equals(socioIdNew)) {
                socioIdOld.getAdministratorCollection().remove(administrator);
                socioIdOld = em.merge(socioIdOld);
            }
            if (socioIdNew != null && !socioIdNew.equals(socioIdOld)) {
                socioIdNew.getAdministratorCollection().add(administrator);
                socioIdNew = em.merge(socioIdNew);
            }
            for (SocioEvent socioEventCollectionNewSocioEvent : socioEventCollectionNew) {
                if (!socioEventCollectionOld.contains(socioEventCollectionNewSocioEvent)) {
                    Administrator oldAdministratorIdOfSocioEventCollectionNewSocioEvent = socioEventCollectionNewSocioEvent.getAdministratorId();
                    socioEventCollectionNewSocioEvent.setAdministratorId(administrator);
                    socioEventCollectionNewSocioEvent = em.merge(socioEventCollectionNewSocioEvent);
                    if (oldAdministratorIdOfSocioEventCollectionNewSocioEvent != null && !oldAdministratorIdOfSocioEventCollectionNewSocioEvent.equals(administrator)) {
                        oldAdministratorIdOfSocioEventCollectionNewSocioEvent.getSocioEventCollection().remove(socioEventCollectionNewSocioEvent);
                        oldAdministratorIdOfSocioEventCollectionNewSocioEvent = em.merge(oldAdministratorIdOfSocioEventCollectionNewSocioEvent);
                    }
                }
            }
            for (SocioPayment socioPaymentCollectionNewSocioPayment : socioPaymentCollectionNew) {
                if (!socioPaymentCollectionOld.contains(socioPaymentCollectionNewSocioPayment)) {
                    Administrator oldAdministratorIdOfSocioPaymentCollectionNewSocioPayment = socioPaymentCollectionNewSocioPayment.getAdministratorId();
                    socioPaymentCollectionNewSocioPayment.setAdministratorId(administrator);
                    socioPaymentCollectionNewSocioPayment = em.merge(socioPaymentCollectionNewSocioPayment);
                    if (oldAdministratorIdOfSocioPaymentCollectionNewSocioPayment != null && !oldAdministratorIdOfSocioPaymentCollectionNewSocioPayment.equals(administrator)) {
                        oldAdministratorIdOfSocioPaymentCollectionNewSocioPayment.getSocioPaymentCollection().remove(socioPaymentCollectionNewSocioPayment);
                        oldAdministratorIdOfSocioPaymentCollectionNewSocioPayment = em.merge(oldAdministratorIdOfSocioPaymentCollectionNewSocioPayment);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = administrator.getAdministratorId();
                if (findAdministrator(id) == null) {
                    throw new NonexistentEntityException("The administrator with id " + id + " no longer exists.");
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
            Administrator administrator;
            try {
                administrator = em.getReference(Administrator.class, id);
                administrator.getAdministratorId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The administrator with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SocioEvent> socioEventCollectionOrphanCheck = administrator.getSocioEventCollection();
            for (SocioEvent socioEventCollectionOrphanCheckSocioEvent : socioEventCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Administrator (" + administrator + ") cannot be destroyed since the SocioEvent " + socioEventCollectionOrphanCheckSocioEvent + " in its socioEventCollection field has a non-nullable administratorId field.");
            }
            Collection<SocioPayment> socioPaymentCollectionOrphanCheck = administrator.getSocioPaymentCollection();
            for (SocioPayment socioPaymentCollectionOrphanCheckSocioPayment : socioPaymentCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Administrator (" + administrator + ") cannot be destroyed since the SocioPayment " + socioPaymentCollectionOrphanCheckSocioPayment + " in its socioPaymentCollection field has a non-nullable administratorId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Socio socioId = administrator.getSocioId();
            if (socioId != null) {
                socioId.getAdministratorCollection().remove(administrator);
                socioId = em.merge(socioId);
            }
            em.remove(administrator);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Administrator> findAdministratorEntities() {
        return findAdministratorEntities(true, -1, -1);
    }

    public List<Administrator> findAdministratorEntities(int maxResults, int firstResult) {
        return findAdministratorEntities(false, maxResults, firstResult);
    }

    private List<Administrator> findAdministratorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Administrator.class));
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

    public Administrator findAdministrator(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Administrator.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdministratorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Administrator> rt = cq.from(Administrator.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
