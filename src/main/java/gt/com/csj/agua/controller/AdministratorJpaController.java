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
import gt.com.csj.agua.entity.Fine;
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
public class AdministratorJpaController implements Serializable {

    public AdministratorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Administrator administrator) {
        if (administrator.getFineList() == null) {
            administrator.setFineList(new ArrayList<Fine>());
        }
        if (administrator.getSocioEventList() == null) {
            administrator.setSocioEventList(new ArrayList<SocioEvent>());
        }
        if (administrator.getSocioPaymentList() == null) {
            administrator.setSocioPaymentList(new ArrayList<SocioPayment>());
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
            List<Fine> attachedFineList = new ArrayList<Fine>();
            for (Fine fineListFineToAttach : administrator.getFineList()) {
                fineListFineToAttach = em.getReference(fineListFineToAttach.getClass(), fineListFineToAttach.getFineId());
                attachedFineList.add(fineListFineToAttach);
            }
            administrator.setFineList(attachedFineList);
            List<SocioEvent> attachedSocioEventList = new ArrayList<SocioEvent>();
            for (SocioEvent socioEventListSocioEventToAttach : administrator.getSocioEventList()) {
                socioEventListSocioEventToAttach = em.getReference(socioEventListSocioEventToAttach.getClass(), socioEventListSocioEventToAttach.getSocioEventId());
                attachedSocioEventList.add(socioEventListSocioEventToAttach);
            }
            administrator.setSocioEventList(attachedSocioEventList);
            List<SocioPayment> attachedSocioPaymentList = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentListSocioPaymentToAttach : administrator.getSocioPaymentList()) {
                socioPaymentListSocioPaymentToAttach = em.getReference(socioPaymentListSocioPaymentToAttach.getClass(), socioPaymentListSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentList.add(socioPaymentListSocioPaymentToAttach);
            }
            administrator.setSocioPaymentList(attachedSocioPaymentList);
            em.persist(administrator);
            if (socioId != null) {
                socioId.getAdministratorList().add(administrator);
                socioId = em.merge(socioId);
            }
            for (Fine fineListFine : administrator.getFineList()) {
                Administrator oldAdministratorIdOfFineListFine = fineListFine.getAdministratorId();
                fineListFine.setAdministratorId(administrator);
                fineListFine = em.merge(fineListFine);
                if (oldAdministratorIdOfFineListFine != null) {
                    oldAdministratorIdOfFineListFine.getFineList().remove(fineListFine);
                    oldAdministratorIdOfFineListFine = em.merge(oldAdministratorIdOfFineListFine);
                }
            }
            for (SocioEvent socioEventListSocioEvent : administrator.getSocioEventList()) {
                Administrator oldAdministratorIdOfSocioEventListSocioEvent = socioEventListSocioEvent.getAdministratorId();
                socioEventListSocioEvent.setAdministratorId(administrator);
                socioEventListSocioEvent = em.merge(socioEventListSocioEvent);
                if (oldAdministratorIdOfSocioEventListSocioEvent != null) {
                    oldAdministratorIdOfSocioEventListSocioEvent.getSocioEventList().remove(socioEventListSocioEvent);
                    oldAdministratorIdOfSocioEventListSocioEvent = em.merge(oldAdministratorIdOfSocioEventListSocioEvent);
                }
            }
            for (SocioPayment socioPaymentListSocioPayment : administrator.getSocioPaymentList()) {
                Administrator oldAdministratorIdOfSocioPaymentListSocioPayment = socioPaymentListSocioPayment.getAdministratorId();
                socioPaymentListSocioPayment.setAdministratorId(administrator);
                socioPaymentListSocioPayment = em.merge(socioPaymentListSocioPayment);
                if (oldAdministratorIdOfSocioPaymentListSocioPayment != null) {
                    oldAdministratorIdOfSocioPaymentListSocioPayment.getSocioPaymentList().remove(socioPaymentListSocioPayment);
                    oldAdministratorIdOfSocioPaymentListSocioPayment = em.merge(oldAdministratorIdOfSocioPaymentListSocioPayment);
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
            List<Fine> fineListOld = persistentAdministrator.getFineList();
            List<Fine> fineListNew = administrator.getFineList();
            List<SocioEvent> socioEventListOld = persistentAdministrator.getSocioEventList();
            List<SocioEvent> socioEventListNew = administrator.getSocioEventList();
            List<SocioPayment> socioPaymentListOld = persistentAdministrator.getSocioPaymentList();
            List<SocioPayment> socioPaymentListNew = administrator.getSocioPaymentList();
            List<String> illegalOrphanMessages = null;
            for (Fine fineListOldFine : fineListOld) {
                if (!fineListNew.contains(fineListOldFine)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Fine " + fineListOldFine + " since its administratorId field is not nullable.");
                }
            }
            for (SocioEvent socioEventListOldSocioEvent : socioEventListOld) {
                if (!socioEventListNew.contains(socioEventListOldSocioEvent)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioEvent " + socioEventListOldSocioEvent + " since its administratorId field is not nullable.");
                }
            }
            for (SocioPayment socioPaymentListOldSocioPayment : socioPaymentListOld) {
                if (!socioPaymentListNew.contains(socioPaymentListOldSocioPayment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioPayment " + socioPaymentListOldSocioPayment + " since its administratorId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (socioIdNew != null) {
                socioIdNew = em.getReference(socioIdNew.getClass(), socioIdNew.getSocioId());
                administrator.setSocioId(socioIdNew);
            }
            List<Fine> attachedFineListNew = new ArrayList<Fine>();
            for (Fine fineListNewFineToAttach : fineListNew) {
                fineListNewFineToAttach = em.getReference(fineListNewFineToAttach.getClass(), fineListNewFineToAttach.getFineId());
                attachedFineListNew.add(fineListNewFineToAttach);
            }
            fineListNew = attachedFineListNew;
            administrator.setFineList(fineListNew);
            List<SocioEvent> attachedSocioEventListNew = new ArrayList<SocioEvent>();
            for (SocioEvent socioEventListNewSocioEventToAttach : socioEventListNew) {
                socioEventListNewSocioEventToAttach = em.getReference(socioEventListNewSocioEventToAttach.getClass(), socioEventListNewSocioEventToAttach.getSocioEventId());
                attachedSocioEventListNew.add(socioEventListNewSocioEventToAttach);
            }
            socioEventListNew = attachedSocioEventListNew;
            administrator.setSocioEventList(socioEventListNew);
            List<SocioPayment> attachedSocioPaymentListNew = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentListNewSocioPaymentToAttach : socioPaymentListNew) {
                socioPaymentListNewSocioPaymentToAttach = em.getReference(socioPaymentListNewSocioPaymentToAttach.getClass(), socioPaymentListNewSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentListNew.add(socioPaymentListNewSocioPaymentToAttach);
            }
            socioPaymentListNew = attachedSocioPaymentListNew;
            administrator.setSocioPaymentList(socioPaymentListNew);
            administrator = em.merge(administrator);
            if (socioIdOld != null && !socioIdOld.equals(socioIdNew)) {
                socioIdOld.getAdministratorList().remove(administrator);
                socioIdOld = em.merge(socioIdOld);
            }
            if (socioIdNew != null && !socioIdNew.equals(socioIdOld)) {
                socioIdNew.getAdministratorList().add(administrator);
                socioIdNew = em.merge(socioIdNew);
            }
            for (Fine fineListNewFine : fineListNew) {
                if (!fineListOld.contains(fineListNewFine)) {
                    Administrator oldAdministratorIdOfFineListNewFine = fineListNewFine.getAdministratorId();
                    fineListNewFine.setAdministratorId(administrator);
                    fineListNewFine = em.merge(fineListNewFine);
                    if (oldAdministratorIdOfFineListNewFine != null && !oldAdministratorIdOfFineListNewFine.equals(administrator)) {
                        oldAdministratorIdOfFineListNewFine.getFineList().remove(fineListNewFine);
                        oldAdministratorIdOfFineListNewFine = em.merge(oldAdministratorIdOfFineListNewFine);
                    }
                }
            }
            for (SocioEvent socioEventListNewSocioEvent : socioEventListNew) {
                if (!socioEventListOld.contains(socioEventListNewSocioEvent)) {
                    Administrator oldAdministratorIdOfSocioEventListNewSocioEvent = socioEventListNewSocioEvent.getAdministratorId();
                    socioEventListNewSocioEvent.setAdministratorId(administrator);
                    socioEventListNewSocioEvent = em.merge(socioEventListNewSocioEvent);
                    if (oldAdministratorIdOfSocioEventListNewSocioEvent != null && !oldAdministratorIdOfSocioEventListNewSocioEvent.equals(administrator)) {
                        oldAdministratorIdOfSocioEventListNewSocioEvent.getSocioEventList().remove(socioEventListNewSocioEvent);
                        oldAdministratorIdOfSocioEventListNewSocioEvent = em.merge(oldAdministratorIdOfSocioEventListNewSocioEvent);
                    }
                }
            }
            for (SocioPayment socioPaymentListNewSocioPayment : socioPaymentListNew) {
                if (!socioPaymentListOld.contains(socioPaymentListNewSocioPayment)) {
                    Administrator oldAdministratorIdOfSocioPaymentListNewSocioPayment = socioPaymentListNewSocioPayment.getAdministratorId();
                    socioPaymentListNewSocioPayment.setAdministratorId(administrator);
                    socioPaymentListNewSocioPayment = em.merge(socioPaymentListNewSocioPayment);
                    if (oldAdministratorIdOfSocioPaymentListNewSocioPayment != null && !oldAdministratorIdOfSocioPaymentListNewSocioPayment.equals(administrator)) {
                        oldAdministratorIdOfSocioPaymentListNewSocioPayment.getSocioPaymentList().remove(socioPaymentListNewSocioPayment);
                        oldAdministratorIdOfSocioPaymentListNewSocioPayment = em.merge(oldAdministratorIdOfSocioPaymentListNewSocioPayment);
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
            List<Fine> fineListOrphanCheck = administrator.getFineList();
            for (Fine fineListOrphanCheckFine : fineListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Administrator (" + administrator + ") cannot be destroyed since the Fine " + fineListOrphanCheckFine + " in its fineList field has a non-nullable administratorId field.");
            }
            List<SocioEvent> socioEventListOrphanCheck = administrator.getSocioEventList();
            for (SocioEvent socioEventListOrphanCheckSocioEvent : socioEventListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Administrator (" + administrator + ") cannot be destroyed since the SocioEvent " + socioEventListOrphanCheckSocioEvent + " in its socioEventList field has a non-nullable administratorId field.");
            }
            List<SocioPayment> socioPaymentListOrphanCheck = administrator.getSocioPaymentList();
            for (SocioPayment socioPaymentListOrphanCheckSocioPayment : socioPaymentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Administrator (" + administrator + ") cannot be destroyed since the SocioPayment " + socioPaymentListOrphanCheckSocioPayment + " in its socioPaymentList field has a non-nullable administratorId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Socio socioId = administrator.getSocioId();
            if (socioId != null) {
                socioId.getAdministratorList().remove(administrator);
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
