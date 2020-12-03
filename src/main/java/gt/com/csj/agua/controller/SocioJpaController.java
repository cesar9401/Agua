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
import java.util.List;
import gt.com.csj.agua.entity.SocioEvent;
import gt.com.csj.agua.entity.SocioPayment;
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
        if (socio.getAdministratorList() == null) {
            socio.setAdministratorList(new ArrayList<Administrator>());
        }
        if (socio.getSocioList() == null) {
            socio.setSocioList(new ArrayList<Socio>());
        }
        if (socio.getSocioEventList() == null) {
            socio.setSocioEventList(new ArrayList<SocioEvent>());
        }
        if (socio.getSocioPaymentList() == null) {
            socio.setSocioPaymentList(new ArrayList<SocioPayment>());
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
            List<Administrator> attachedAdministratorList = new ArrayList<Administrator>();
            for (Administrator administratorListAdministratorToAttach : socio.getAdministratorList()) {
                administratorListAdministratorToAttach = em.getReference(administratorListAdministratorToAttach.getClass(), administratorListAdministratorToAttach.getAdministratorId());
                attachedAdministratorList.add(administratorListAdministratorToAttach);
            }
            socio.setAdministratorList(attachedAdministratorList);
            List<Socio> attachedSocioList = new ArrayList<Socio>();
            for (Socio socioListSocioToAttach : socio.getSocioList()) {
                socioListSocioToAttach = em.getReference(socioListSocioToAttach.getClass(), socioListSocioToAttach.getSocioId());
                attachedSocioList.add(socioListSocioToAttach);
            }
            socio.setSocioList(attachedSocioList);
            List<SocioEvent> attachedSocioEventList = new ArrayList<SocioEvent>();
            for (SocioEvent socioEventListSocioEventToAttach : socio.getSocioEventList()) {
                socioEventListSocioEventToAttach = em.getReference(socioEventListSocioEventToAttach.getClass(), socioEventListSocioEventToAttach.getSocioEventId());
                attachedSocioEventList.add(socioEventListSocioEventToAttach);
            }
            socio.setSocioEventList(attachedSocioEventList);
            List<SocioPayment> attachedSocioPaymentList = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentListSocioPaymentToAttach : socio.getSocioPaymentList()) {
                socioPaymentListSocioPaymentToAttach = em.getReference(socioPaymentListSocioPaymentToAttach.getClass(), socioPaymentListSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentList.add(socioPaymentListSocioPaymentToAttach);
            }
            socio.setSocioPaymentList(attachedSocioPaymentList);
            em.persist(socio);
            if (socioSocioId != null) {
                socioSocioId.getSocioList().add(socio);
                socioSocioId = em.merge(socioSocioId);
            }
            for (Administrator administratorListAdministrator : socio.getAdministratorList()) {
                Socio oldSocioIdOfAdministratorListAdministrator = administratorListAdministrator.getSocioId();
                administratorListAdministrator.setSocioId(socio);
                administratorListAdministrator = em.merge(administratorListAdministrator);
                if (oldSocioIdOfAdministratorListAdministrator != null) {
                    oldSocioIdOfAdministratorListAdministrator.getAdministratorList().remove(administratorListAdministrator);
                    oldSocioIdOfAdministratorListAdministrator = em.merge(oldSocioIdOfAdministratorListAdministrator);
                }
            }
            for (Socio socioListSocio : socio.getSocioList()) {
                Socio oldSocioSocioIdOfSocioListSocio = socioListSocio.getSocioSocioId();
                socioListSocio.setSocioSocioId(socio);
                socioListSocio = em.merge(socioListSocio);
                if (oldSocioSocioIdOfSocioListSocio != null) {
                    oldSocioSocioIdOfSocioListSocio.getSocioList().remove(socioListSocio);
                    oldSocioSocioIdOfSocioListSocio = em.merge(oldSocioSocioIdOfSocioListSocio);
                }
            }
            for (SocioEvent socioEventListSocioEvent : socio.getSocioEventList()) {
                Socio oldSocioIdOfSocioEventListSocioEvent = socioEventListSocioEvent.getSocioId();
                socioEventListSocioEvent.setSocioId(socio);
                socioEventListSocioEvent = em.merge(socioEventListSocioEvent);
                if (oldSocioIdOfSocioEventListSocioEvent != null) {
                    oldSocioIdOfSocioEventListSocioEvent.getSocioEventList().remove(socioEventListSocioEvent);
                    oldSocioIdOfSocioEventListSocioEvent = em.merge(oldSocioIdOfSocioEventListSocioEvent);
                }
            }
            for (SocioPayment socioPaymentListSocioPayment : socio.getSocioPaymentList()) {
                Socio oldSocioIdOfSocioPaymentListSocioPayment = socioPaymentListSocioPayment.getSocioId();
                socioPaymentListSocioPayment.setSocioId(socio);
                socioPaymentListSocioPayment = em.merge(socioPaymentListSocioPayment);
                if (oldSocioIdOfSocioPaymentListSocioPayment != null) {
                    oldSocioIdOfSocioPaymentListSocioPayment.getSocioPaymentList().remove(socioPaymentListSocioPayment);
                    oldSocioIdOfSocioPaymentListSocioPayment = em.merge(oldSocioIdOfSocioPaymentListSocioPayment);
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
            List<Administrator> administratorListOld = persistentSocio.getAdministratorList();
            List<Administrator> administratorListNew = socio.getAdministratorList();
            List<Socio> socioListOld = persistentSocio.getSocioList();
            List<Socio> socioListNew = socio.getSocioList();
            List<SocioEvent> socioEventListOld = persistentSocio.getSocioEventList();
            List<SocioEvent> socioEventListNew = socio.getSocioEventList();
            List<SocioPayment> socioPaymentListOld = persistentSocio.getSocioPaymentList();
            List<SocioPayment> socioPaymentListNew = socio.getSocioPaymentList();
            List<String> illegalOrphanMessages = null;
            for (Administrator administratorListOldAdministrator : administratorListOld) {
                if (!administratorListNew.contains(administratorListOldAdministrator)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Administrator " + administratorListOldAdministrator + " since its socioId field is not nullable.");
                }
            }
            for (SocioEvent socioEventListOldSocioEvent : socioEventListOld) {
                if (!socioEventListNew.contains(socioEventListOldSocioEvent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioEvent " + socioEventListOldSocioEvent + " since its socioId field is not nullable.");
                }
            }
            for (SocioPayment socioPaymentListOldSocioPayment : socioPaymentListOld) {
                if (!socioPaymentListNew.contains(socioPaymentListOldSocioPayment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioPayment " + socioPaymentListOldSocioPayment + " since its socioId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (socioSocioIdNew != null) {
                socioSocioIdNew = em.getReference(socioSocioIdNew.getClass(), socioSocioIdNew.getSocioId());
                socio.setSocioSocioId(socioSocioIdNew);
            }
            List<Administrator> attachedAdministratorListNew = new ArrayList<Administrator>();
            for (Administrator administratorListNewAdministratorToAttach : administratorListNew) {
                administratorListNewAdministratorToAttach = em.getReference(administratorListNewAdministratorToAttach.getClass(), administratorListNewAdministratorToAttach.getAdministratorId());
                attachedAdministratorListNew.add(administratorListNewAdministratorToAttach);
            }
            administratorListNew = attachedAdministratorListNew;
            socio.setAdministratorList(administratorListNew);
            List<Socio> attachedSocioListNew = new ArrayList<Socio>();
            for (Socio socioListNewSocioToAttach : socioListNew) {
                socioListNewSocioToAttach = em.getReference(socioListNewSocioToAttach.getClass(), socioListNewSocioToAttach.getSocioId());
                attachedSocioListNew.add(socioListNewSocioToAttach);
            }
            socioListNew = attachedSocioListNew;
            socio.setSocioList(socioListNew);
            List<SocioEvent> attachedSocioEventListNew = new ArrayList<SocioEvent>();
            for (SocioEvent socioEventListNewSocioEventToAttach : socioEventListNew) {
                socioEventListNewSocioEventToAttach = em.getReference(socioEventListNewSocioEventToAttach.getClass(), socioEventListNewSocioEventToAttach.getSocioEventId());
                attachedSocioEventListNew.add(socioEventListNewSocioEventToAttach);
            }
            socioEventListNew = attachedSocioEventListNew;
            socio.setSocioEventList(socioEventListNew);
            List<SocioPayment> attachedSocioPaymentListNew = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentListNewSocioPaymentToAttach : socioPaymentListNew) {
                socioPaymentListNewSocioPaymentToAttach = em.getReference(socioPaymentListNewSocioPaymentToAttach.getClass(), socioPaymentListNewSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentListNew.add(socioPaymentListNewSocioPaymentToAttach);
            }
            socioPaymentListNew = attachedSocioPaymentListNew;
            socio.setSocioPaymentList(socioPaymentListNew);
            socio = em.merge(socio);
            if (socioSocioIdOld != null && !socioSocioIdOld.equals(socioSocioIdNew)) {
                socioSocioIdOld.getSocioList().remove(socio);
                socioSocioIdOld = em.merge(socioSocioIdOld);
            }
            if (socioSocioIdNew != null && !socioSocioIdNew.equals(socioSocioIdOld)) {
                socioSocioIdNew.getSocioList().add(socio);
                socioSocioIdNew = em.merge(socioSocioIdNew);
            }
            for (Administrator administratorListNewAdministrator : administratorListNew) {
                if (!administratorListOld.contains(administratorListNewAdministrator)) {
                    Socio oldSocioIdOfAdministratorListNewAdministrator = administratorListNewAdministrator.getSocioId();
                    administratorListNewAdministrator.setSocioId(socio);
                    administratorListNewAdministrator = em.merge(administratorListNewAdministrator);
                    if (oldSocioIdOfAdministratorListNewAdministrator != null && !oldSocioIdOfAdministratorListNewAdministrator.equals(socio)) {
                        oldSocioIdOfAdministratorListNewAdministrator.getAdministratorList().remove(administratorListNewAdministrator);
                        oldSocioIdOfAdministratorListNewAdministrator = em.merge(oldSocioIdOfAdministratorListNewAdministrator);
                    }
                }
            }
            for (Socio socioListOldSocio : socioListOld) {
                if (!socioListNew.contains(socioListOldSocio)) {
                    socioListOldSocio.setSocioSocioId(null);
                    socioListOldSocio = em.merge(socioListOldSocio);
                }
            }
            for (Socio socioListNewSocio : socioListNew) {
                if (!socioListOld.contains(socioListNewSocio)) {
                    Socio oldSocioSocioIdOfSocioListNewSocio = socioListNewSocio.getSocioSocioId();
                    socioListNewSocio.setSocioSocioId(socio);
                    socioListNewSocio = em.merge(socioListNewSocio);
                    if (oldSocioSocioIdOfSocioListNewSocio != null && !oldSocioSocioIdOfSocioListNewSocio.equals(socio)) {
                        oldSocioSocioIdOfSocioListNewSocio.getSocioList().remove(socioListNewSocio);
                        oldSocioSocioIdOfSocioListNewSocio = em.merge(oldSocioSocioIdOfSocioListNewSocio);
                    }
                }
            }
            for (SocioEvent socioEventListNewSocioEvent : socioEventListNew) {
                if (!socioEventListOld.contains(socioEventListNewSocioEvent)) {
                    Socio oldSocioIdOfSocioEventListNewSocioEvent = socioEventListNewSocioEvent.getSocioId();
                    socioEventListNewSocioEvent.setSocioId(socio);
                    socioEventListNewSocioEvent = em.merge(socioEventListNewSocioEvent);
                    if (oldSocioIdOfSocioEventListNewSocioEvent != null && !oldSocioIdOfSocioEventListNewSocioEvent.equals(socio)) {
                        oldSocioIdOfSocioEventListNewSocioEvent.getSocioEventList().remove(socioEventListNewSocioEvent);
                        oldSocioIdOfSocioEventListNewSocioEvent = em.merge(oldSocioIdOfSocioEventListNewSocioEvent);
                    }
                }
            }
            for (SocioPayment socioPaymentListNewSocioPayment : socioPaymentListNew) {
                if (!socioPaymentListOld.contains(socioPaymentListNewSocioPayment)) {
                    Socio oldSocioIdOfSocioPaymentListNewSocioPayment = socioPaymentListNewSocioPayment.getSocioId();
                    socioPaymentListNewSocioPayment.setSocioId(socio);
                    socioPaymentListNewSocioPayment = em.merge(socioPaymentListNewSocioPayment);
                    if (oldSocioIdOfSocioPaymentListNewSocioPayment != null && !oldSocioIdOfSocioPaymentListNewSocioPayment.equals(socio)) {
                        oldSocioIdOfSocioPaymentListNewSocioPayment.getSocioPaymentList().remove(socioPaymentListNewSocioPayment);
                        oldSocioIdOfSocioPaymentListNewSocioPayment = em.merge(oldSocioIdOfSocioPaymentListNewSocioPayment);
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
            List<Administrator> administratorListOrphanCheck = socio.getAdministratorList();
            for (Administrator administratorListOrphanCheckAdministrator : administratorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Socio (" + socio + ") cannot be destroyed since the Administrator " + administratorListOrphanCheckAdministrator + " in its administratorList field has a non-nullable socioId field.");
            }
            List<SocioEvent> socioEventListOrphanCheck = socio.getSocioEventList();
            for (SocioEvent socioEventListOrphanCheckSocioEvent : socioEventListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Socio (" + socio + ") cannot be destroyed since the SocioEvent " + socioEventListOrphanCheckSocioEvent + " in its socioEventList field has a non-nullable socioId field.");
            }
            List<SocioPayment> socioPaymentListOrphanCheck = socio.getSocioPaymentList();
            for (SocioPayment socioPaymentListOrphanCheckSocioPayment : socioPaymentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Socio (" + socio + ") cannot be destroyed since the SocioPayment " + socioPaymentListOrphanCheckSocioPayment + " in its socioPaymentList field has a non-nullable socioId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Socio socioSocioId = socio.getSocioSocioId();
            if (socioSocioId != null) {
                socioSocioId.getSocioList().remove(socio);
                socioSocioId = em.merge(socioSocioId);
            }
            List<Socio> socioList = socio.getSocioList();
            for (Socio socioListSocio : socioList) {
                socioListSocio.setSocioSocioId(null);
                socioListSocio = em.merge(socioListSocio);
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
