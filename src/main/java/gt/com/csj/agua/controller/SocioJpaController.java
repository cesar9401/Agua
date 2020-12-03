/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.controller;

import gt.com.csj.agua.controller.exceptions.IllegalOrphanException;
import gt.com.csj.agua.controller.exceptions.NonexistentEntityException;
import gt.com.csj.agua.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.com.csj.agua.entity.Socio;
import gt.com.csj.agua.entity.Administrator;
import java.util.ArrayList;
import java.util.Collection;
import gt.com.csj.agua.entity.SocioEvent;
import gt.com.csj.agua.entity.SocioPayment;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author julio
 */
public class SocioJpaController implements Serializable {

    public SocioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Socio socio) throws PreexistingEntityException, Exception {
        if (socio.getAdministratorCollection() == null) {
            socio.setAdministratorCollection(new ArrayList<Administrator>());
        }
        if (socio.getSocioCollection() == null) {
            socio.setSocioCollection(new ArrayList<Socio>());
        }
        if (socio.getSocioEventCollection() == null) {
            socio.setSocioEventCollection(new ArrayList<SocioEvent>());
        }
        if (socio.getSocioPaymentCollection() == null) {
            socio.setSocioPaymentCollection(new ArrayList<SocioPayment>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Socio socioSocioId = socio.getSocioSocioId();
            if (socioSocioId != null) {
                socioSocioId = em.getReference(socioSocioId.getClass(), socioSocioId.getSocioId());
                socio.setSocioSocioId(socioSocioId);
            }
            Collection<Administrator> attachedAdministratorCollection = new ArrayList<Administrator>();
            for (Administrator administratorCollectionAdministratorToAttach : socio.getAdministratorCollection()) {
                administratorCollectionAdministratorToAttach = em.getReference(administratorCollectionAdministratorToAttach.getClass(), administratorCollectionAdministratorToAttach.getAdministratorId());
                attachedAdministratorCollection.add(administratorCollectionAdministratorToAttach);
            }
            socio.setAdministratorCollection(attachedAdministratorCollection);
            Collection<Socio> attachedSocioCollection = new ArrayList<Socio>();
            for (Socio socioCollectionSocioToAttach : socio.getSocioCollection()) {
                socioCollectionSocioToAttach = em.getReference(socioCollectionSocioToAttach.getClass(), socioCollectionSocioToAttach.getSocioId());
                attachedSocioCollection.add(socioCollectionSocioToAttach);
            }
            socio.setSocioCollection(attachedSocioCollection);
            Collection<SocioEvent> attachedSocioEventCollection = new ArrayList<SocioEvent>();
            for (SocioEvent socioEventCollectionSocioEventToAttach : socio.getSocioEventCollection()) {
                socioEventCollectionSocioEventToAttach = em.getReference(socioEventCollectionSocioEventToAttach.getClass(), socioEventCollectionSocioEventToAttach.getSocioEventId());
                attachedSocioEventCollection.add(socioEventCollectionSocioEventToAttach);
            }
            socio.setSocioEventCollection(attachedSocioEventCollection);
            Collection<SocioPayment> attachedSocioPaymentCollection = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentCollectionSocioPaymentToAttach : socio.getSocioPaymentCollection()) {
                socioPaymentCollectionSocioPaymentToAttach = em.getReference(socioPaymentCollectionSocioPaymentToAttach.getClass(), socioPaymentCollectionSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentCollection.add(socioPaymentCollectionSocioPaymentToAttach);
            }
            socio.setSocioPaymentCollection(attachedSocioPaymentCollection);
            em.persist(socio);
            if (socioSocioId != null) {
                socioSocioId.getSocioCollection().add(socio);
                socioSocioId = em.merge(socioSocioId);
            }
            for (Administrator administratorCollectionAdministrator : socio.getAdministratorCollection()) {
                Socio oldSocioIdOfAdministratorCollectionAdministrator = administratorCollectionAdministrator.getSocioId();
                administratorCollectionAdministrator.setSocioId(socio);
                administratorCollectionAdministrator = em.merge(administratorCollectionAdministrator);
                if (oldSocioIdOfAdministratorCollectionAdministrator != null) {
                    oldSocioIdOfAdministratorCollectionAdministrator.getAdministratorCollection().remove(administratorCollectionAdministrator);
                    oldSocioIdOfAdministratorCollectionAdministrator = em.merge(oldSocioIdOfAdministratorCollectionAdministrator);
                }
            }
            for (Socio socioCollectionSocio : socio.getSocioCollection()) {
                Socio oldSocioSocioIdOfSocioCollectionSocio = socioCollectionSocio.getSocioSocioId();
                socioCollectionSocio.setSocioSocioId(socio);
                socioCollectionSocio = em.merge(socioCollectionSocio);
                if (oldSocioSocioIdOfSocioCollectionSocio != null) {
                    oldSocioSocioIdOfSocioCollectionSocio.getSocioCollection().remove(socioCollectionSocio);
                    oldSocioSocioIdOfSocioCollectionSocio = em.merge(oldSocioSocioIdOfSocioCollectionSocio);
                }
            }
            for (SocioEvent socioEventCollectionSocioEvent : socio.getSocioEventCollection()) {
                Socio oldSocioIdOfSocioEventCollectionSocioEvent = socioEventCollectionSocioEvent.getSocioId();
                socioEventCollectionSocioEvent.setSocioId(socio);
                socioEventCollectionSocioEvent = em.merge(socioEventCollectionSocioEvent);
                if (oldSocioIdOfSocioEventCollectionSocioEvent != null) {
                    oldSocioIdOfSocioEventCollectionSocioEvent.getSocioEventCollection().remove(socioEventCollectionSocioEvent);
                    oldSocioIdOfSocioEventCollectionSocioEvent = em.merge(oldSocioIdOfSocioEventCollectionSocioEvent);
                }
            }
            for (SocioPayment socioPaymentCollectionSocioPayment : socio.getSocioPaymentCollection()) {
                Socio oldSocioIdOfSocioPaymentCollectionSocioPayment = socioPaymentCollectionSocioPayment.getSocioId();
                socioPaymentCollectionSocioPayment.setSocioId(socio);
                socioPaymentCollectionSocioPayment = em.merge(socioPaymentCollectionSocioPayment);
                if (oldSocioIdOfSocioPaymentCollectionSocioPayment != null) {
                    oldSocioIdOfSocioPaymentCollectionSocioPayment.getSocioPaymentCollection().remove(socioPaymentCollectionSocioPayment);
                    oldSocioIdOfSocioPaymentCollectionSocioPayment = em.merge(oldSocioIdOfSocioPaymentCollectionSocioPayment);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSocio(socio.getSocioId()) != null) {
                throw new PreexistingEntityException("Socio " + socio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Socio socio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Socio persistentSocio = em.find(Socio.class, socio.getSocioId());
            Socio socioSocioIdOld = persistentSocio.getSocioSocioId();
            Socio socioSocioIdNew = socio.getSocioSocioId();
            Collection<Administrator> administratorCollectionOld = persistentSocio.getAdministratorCollection();
            Collection<Administrator> administratorCollectionNew = socio.getAdministratorCollection();
            Collection<Socio> socioCollectionOld = persistentSocio.getSocioCollection();
            Collection<Socio> socioCollectionNew = socio.getSocioCollection();
            Collection<SocioEvent> socioEventCollectionOld = persistentSocio.getSocioEventCollection();
            Collection<SocioEvent> socioEventCollectionNew = socio.getSocioEventCollection();
            Collection<SocioPayment> socioPaymentCollectionOld = persistentSocio.getSocioPaymentCollection();
            Collection<SocioPayment> socioPaymentCollectionNew = socio.getSocioPaymentCollection();
            List<String> illegalOrphanMessages = null;
            for (Administrator administratorCollectionOldAdministrator : administratorCollectionOld) {
                if (!administratorCollectionNew.contains(administratorCollectionOldAdministrator)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Administrator " + administratorCollectionOldAdministrator + " since its socioId field is not nullable.");
                }
            }
            for (SocioEvent socioEventCollectionOldSocioEvent : socioEventCollectionOld) {
                if (!socioEventCollectionNew.contains(socioEventCollectionOldSocioEvent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioEvent " + socioEventCollectionOldSocioEvent + " since its socioId field is not nullable.");
                }
            }
            for (SocioPayment socioPaymentCollectionOldSocioPayment : socioPaymentCollectionOld) {
                if (!socioPaymentCollectionNew.contains(socioPaymentCollectionOldSocioPayment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioPayment " + socioPaymentCollectionOldSocioPayment + " since its socioId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (socioSocioIdNew != null) {
                socioSocioIdNew = em.getReference(socioSocioIdNew.getClass(), socioSocioIdNew.getSocioId());
                socio.setSocioSocioId(socioSocioIdNew);
            }
            Collection<Administrator> attachedAdministratorCollectionNew = new ArrayList<Administrator>();
            for (Administrator administratorCollectionNewAdministratorToAttach : administratorCollectionNew) {
                administratorCollectionNewAdministratorToAttach = em.getReference(administratorCollectionNewAdministratorToAttach.getClass(), administratorCollectionNewAdministratorToAttach.getAdministratorId());
                attachedAdministratorCollectionNew.add(administratorCollectionNewAdministratorToAttach);
            }
            administratorCollectionNew = attachedAdministratorCollectionNew;
            socio.setAdministratorCollection(administratorCollectionNew);
            Collection<Socio> attachedSocioCollectionNew = new ArrayList<Socio>();
            for (Socio socioCollectionNewSocioToAttach : socioCollectionNew) {
                socioCollectionNewSocioToAttach = em.getReference(socioCollectionNewSocioToAttach.getClass(), socioCollectionNewSocioToAttach.getSocioId());
                attachedSocioCollectionNew.add(socioCollectionNewSocioToAttach);
            }
            socioCollectionNew = attachedSocioCollectionNew;
            socio.setSocioCollection(socioCollectionNew);
            Collection<SocioEvent> attachedSocioEventCollectionNew = new ArrayList<SocioEvent>();
            for (SocioEvent socioEventCollectionNewSocioEventToAttach : socioEventCollectionNew) {
                socioEventCollectionNewSocioEventToAttach = em.getReference(socioEventCollectionNewSocioEventToAttach.getClass(), socioEventCollectionNewSocioEventToAttach.getSocioEventId());
                attachedSocioEventCollectionNew.add(socioEventCollectionNewSocioEventToAttach);
            }
            socioEventCollectionNew = attachedSocioEventCollectionNew;
            socio.setSocioEventCollection(socioEventCollectionNew);
            Collection<SocioPayment> attachedSocioPaymentCollectionNew = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentCollectionNewSocioPaymentToAttach : socioPaymentCollectionNew) {
                socioPaymentCollectionNewSocioPaymentToAttach = em.getReference(socioPaymentCollectionNewSocioPaymentToAttach.getClass(), socioPaymentCollectionNewSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentCollectionNew.add(socioPaymentCollectionNewSocioPaymentToAttach);
            }
            socioPaymentCollectionNew = attachedSocioPaymentCollectionNew;
            socio.setSocioPaymentCollection(socioPaymentCollectionNew);
            socio = em.merge(socio);
            if (socioSocioIdOld != null && !socioSocioIdOld.equals(socioSocioIdNew)) {
                socioSocioIdOld.getSocioCollection().remove(socio);
                socioSocioIdOld = em.merge(socioSocioIdOld);
            }
            if (socioSocioIdNew != null && !socioSocioIdNew.equals(socioSocioIdOld)) {
                socioSocioIdNew.getSocioCollection().add(socio);
                socioSocioIdNew = em.merge(socioSocioIdNew);
            }
            for (Administrator administratorCollectionNewAdministrator : administratorCollectionNew) {
                if (!administratorCollectionOld.contains(administratorCollectionNewAdministrator)) {
                    Socio oldSocioIdOfAdministratorCollectionNewAdministrator = administratorCollectionNewAdministrator.getSocioId();
                    administratorCollectionNewAdministrator.setSocioId(socio);
                    administratorCollectionNewAdministrator = em.merge(administratorCollectionNewAdministrator);
                    if (oldSocioIdOfAdministratorCollectionNewAdministrator != null && !oldSocioIdOfAdministratorCollectionNewAdministrator.equals(socio)) {
                        oldSocioIdOfAdministratorCollectionNewAdministrator.getAdministratorCollection().remove(administratorCollectionNewAdministrator);
                        oldSocioIdOfAdministratorCollectionNewAdministrator = em.merge(oldSocioIdOfAdministratorCollectionNewAdministrator);
                    }
                }
            }
            for (Socio socioCollectionOldSocio : socioCollectionOld) {
                if (!socioCollectionNew.contains(socioCollectionOldSocio)) {
                    socioCollectionOldSocio.setSocioSocioId(null);
                    socioCollectionOldSocio = em.merge(socioCollectionOldSocio);
                }
            }
            for (Socio socioCollectionNewSocio : socioCollectionNew) {
                if (!socioCollectionOld.contains(socioCollectionNewSocio)) {
                    Socio oldSocioSocioIdOfSocioCollectionNewSocio = socioCollectionNewSocio.getSocioSocioId();
                    socioCollectionNewSocio.setSocioSocioId(socio);
                    socioCollectionNewSocio = em.merge(socioCollectionNewSocio);
                    if (oldSocioSocioIdOfSocioCollectionNewSocio != null && !oldSocioSocioIdOfSocioCollectionNewSocio.equals(socio)) {
                        oldSocioSocioIdOfSocioCollectionNewSocio.getSocioCollection().remove(socioCollectionNewSocio);
                        oldSocioSocioIdOfSocioCollectionNewSocio = em.merge(oldSocioSocioIdOfSocioCollectionNewSocio);
                    }
                }
            }
            for (SocioEvent socioEventCollectionNewSocioEvent : socioEventCollectionNew) {
                if (!socioEventCollectionOld.contains(socioEventCollectionNewSocioEvent)) {
                    Socio oldSocioIdOfSocioEventCollectionNewSocioEvent = socioEventCollectionNewSocioEvent.getSocioId();
                    socioEventCollectionNewSocioEvent.setSocioId(socio);
                    socioEventCollectionNewSocioEvent = em.merge(socioEventCollectionNewSocioEvent);
                    if (oldSocioIdOfSocioEventCollectionNewSocioEvent != null && !oldSocioIdOfSocioEventCollectionNewSocioEvent.equals(socio)) {
                        oldSocioIdOfSocioEventCollectionNewSocioEvent.getSocioEventCollection().remove(socioEventCollectionNewSocioEvent);
                        oldSocioIdOfSocioEventCollectionNewSocioEvent = em.merge(oldSocioIdOfSocioEventCollectionNewSocioEvent);
                    }
                }
            }
            for (SocioPayment socioPaymentCollectionNewSocioPayment : socioPaymentCollectionNew) {
                if (!socioPaymentCollectionOld.contains(socioPaymentCollectionNewSocioPayment)) {
                    Socio oldSocioIdOfSocioPaymentCollectionNewSocioPayment = socioPaymentCollectionNewSocioPayment.getSocioId();
                    socioPaymentCollectionNewSocioPayment.setSocioId(socio);
                    socioPaymentCollectionNewSocioPayment = em.merge(socioPaymentCollectionNewSocioPayment);
                    if (oldSocioIdOfSocioPaymentCollectionNewSocioPayment != null && !oldSocioIdOfSocioPaymentCollectionNewSocioPayment.equals(socio)) {
                        oldSocioIdOfSocioPaymentCollectionNewSocioPayment.getSocioPaymentCollection().remove(socioPaymentCollectionNewSocioPayment);
                        oldSocioIdOfSocioPaymentCollectionNewSocioPayment = em.merge(oldSocioIdOfSocioPaymentCollectionNewSocioPayment);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = socio.getSocioId();
                if (findSocio(id) == null) {
                    throw new NonexistentEntityException("The socio with id " + id + " no longer exists.");
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
            Socio socio;
            try {
                socio = em.getReference(Socio.class, id);
                socio.getSocioId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The socio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Administrator> administratorCollectionOrphanCheck = socio.getAdministratorCollection();
            for (Administrator administratorCollectionOrphanCheckAdministrator : administratorCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Socio (" + socio + ") cannot be destroyed since the Administrator " + administratorCollectionOrphanCheckAdministrator + " in its administratorCollection field has a non-nullable socioId field.");
            }
            Collection<SocioEvent> socioEventCollectionOrphanCheck = socio.getSocioEventCollection();
            for (SocioEvent socioEventCollectionOrphanCheckSocioEvent : socioEventCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Socio (" + socio + ") cannot be destroyed since the SocioEvent " + socioEventCollectionOrphanCheckSocioEvent + " in its socioEventCollection field has a non-nullable socioId field.");
            }
            Collection<SocioPayment> socioPaymentCollectionOrphanCheck = socio.getSocioPaymentCollection();
            for (SocioPayment socioPaymentCollectionOrphanCheckSocioPayment : socioPaymentCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Socio (" + socio + ") cannot be destroyed since the SocioPayment " + socioPaymentCollectionOrphanCheckSocioPayment + " in its socioPaymentCollection field has a non-nullable socioId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Socio socioSocioId = socio.getSocioSocioId();
            if (socioSocioId != null) {
                socioSocioId.getSocioCollection().remove(socio);
                socioSocioId = em.merge(socioSocioId);
            }
            Collection<Socio> socioCollection = socio.getSocioCollection();
            for (Socio socioCollectionSocio : socioCollection) {
                socioCollectionSocio.setSocioSocioId(null);
                socioCollectionSocio = em.merge(socioCollectionSocio);
            }
            em.remove(socio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Socio> findSocioEntities() {
        return findSocioEntities(true, -1, -1);
    }

    public List<Socio> findSocioEntities(int maxResults, int firstResult) {
        return findSocioEntities(false, maxResults, firstResult);
    }

    private List<Socio> findSocioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Socio.class));
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

    public Socio findSocio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Socio.class, id);
        } finally {
            em.close();
        }
    }

    public int getSocioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Socio> rt = cq.from(Socio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
