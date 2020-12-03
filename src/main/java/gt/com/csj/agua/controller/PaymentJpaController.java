/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.controller;

import gt.com.csj.agua.controller.exceptions.IllegalOrphanException;
import gt.com.csj.agua.controller.exceptions.NonexistentEntityException;
import gt.com.csj.agua.entity.Payment;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.com.csj.agua.entity.SocioPayment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author julio
 */
public class PaymentJpaController implements Serializable {

    public PaymentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Payment payment) {
        if (payment.getSocioPaymentCollection() == null) {
            payment.setSocioPaymentCollection(new ArrayList<SocioPayment>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SocioPayment> attachedSocioPaymentCollection = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentCollectionSocioPaymentToAttach : payment.getSocioPaymentCollection()) {
                socioPaymentCollectionSocioPaymentToAttach = em.getReference(socioPaymentCollectionSocioPaymentToAttach.getClass(), socioPaymentCollectionSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentCollection.add(socioPaymentCollectionSocioPaymentToAttach);
            }
            payment.setSocioPaymentCollection(attachedSocioPaymentCollection);
            em.persist(payment);
            for (SocioPayment socioPaymentCollectionSocioPayment : payment.getSocioPaymentCollection()) {
                Payment oldPaymentIdOfSocioPaymentCollectionSocioPayment = socioPaymentCollectionSocioPayment.getPaymentId();
                socioPaymentCollectionSocioPayment.setPaymentId(payment);
                socioPaymentCollectionSocioPayment = em.merge(socioPaymentCollectionSocioPayment);
                if (oldPaymentIdOfSocioPaymentCollectionSocioPayment != null) {
                    oldPaymentIdOfSocioPaymentCollectionSocioPayment.getSocioPaymentCollection().remove(socioPaymentCollectionSocioPayment);
                    oldPaymentIdOfSocioPaymentCollectionSocioPayment = em.merge(oldPaymentIdOfSocioPaymentCollectionSocioPayment);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Payment payment) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Payment persistentPayment = em.find(Payment.class, payment.getPaymentId());
            Collection<SocioPayment> socioPaymentCollectionOld = persistentPayment.getSocioPaymentCollection();
            Collection<SocioPayment> socioPaymentCollectionNew = payment.getSocioPaymentCollection();
            List<String> illegalOrphanMessages = null;
            for (SocioPayment socioPaymentCollectionOldSocioPayment : socioPaymentCollectionOld) {
                if (!socioPaymentCollectionNew.contains(socioPaymentCollectionOldSocioPayment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioPayment " + socioPaymentCollectionOldSocioPayment + " since its paymentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SocioPayment> attachedSocioPaymentCollectionNew = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentCollectionNewSocioPaymentToAttach : socioPaymentCollectionNew) {
                socioPaymentCollectionNewSocioPaymentToAttach = em.getReference(socioPaymentCollectionNewSocioPaymentToAttach.getClass(), socioPaymentCollectionNewSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentCollectionNew.add(socioPaymentCollectionNewSocioPaymentToAttach);
            }
            socioPaymentCollectionNew = attachedSocioPaymentCollectionNew;
            payment.setSocioPaymentCollection(socioPaymentCollectionNew);
            payment = em.merge(payment);
            for (SocioPayment socioPaymentCollectionNewSocioPayment : socioPaymentCollectionNew) {
                if (!socioPaymentCollectionOld.contains(socioPaymentCollectionNewSocioPayment)) {
                    Payment oldPaymentIdOfSocioPaymentCollectionNewSocioPayment = socioPaymentCollectionNewSocioPayment.getPaymentId();
                    socioPaymentCollectionNewSocioPayment.setPaymentId(payment);
                    socioPaymentCollectionNewSocioPayment = em.merge(socioPaymentCollectionNewSocioPayment);
                    if (oldPaymentIdOfSocioPaymentCollectionNewSocioPayment != null && !oldPaymentIdOfSocioPaymentCollectionNewSocioPayment.equals(payment)) {
                        oldPaymentIdOfSocioPaymentCollectionNewSocioPayment.getSocioPaymentCollection().remove(socioPaymentCollectionNewSocioPayment);
                        oldPaymentIdOfSocioPaymentCollectionNewSocioPayment = em.merge(oldPaymentIdOfSocioPaymentCollectionNewSocioPayment);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = payment.getPaymentId();
                if (findPayment(id) == null) {
                    throw new NonexistentEntityException("The payment with id " + id + " no longer exists.");
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
            Payment payment;
            try {
                payment = em.getReference(Payment.class, id);
                payment.getPaymentId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The payment with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SocioPayment> socioPaymentCollectionOrphanCheck = payment.getSocioPaymentCollection();
            for (SocioPayment socioPaymentCollectionOrphanCheckSocioPayment : socioPaymentCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Payment (" + payment + ") cannot be destroyed since the SocioPayment " + socioPaymentCollectionOrphanCheckSocioPayment + " in its socioPaymentCollection field has a non-nullable paymentId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(payment);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Payment> findPaymentEntities() {
        return findPaymentEntities(true, -1, -1);
    }

    public List<Payment> findPaymentEntities(int maxResults, int firstResult) {
        return findPaymentEntities(false, maxResults, firstResult);
    }

    private List<Payment> findPaymentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Payment.class));
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

    public Payment findPayment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Payment.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaymentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Payment> rt = cq.from(Payment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
