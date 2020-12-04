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
        if (payment.getSocioPaymentList() == null) {
            payment.setSocioPaymentList(new ArrayList<SocioPayment>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SocioPayment> attachedSocioPaymentList = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentListSocioPaymentToAttach : payment.getSocioPaymentList()) {
                socioPaymentListSocioPaymentToAttach = em.getReference(socioPaymentListSocioPaymentToAttach.getClass(), socioPaymentListSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentList.add(socioPaymentListSocioPaymentToAttach);
            }
            payment.setSocioPaymentList(attachedSocioPaymentList);
            em.persist(payment);
            for (SocioPayment socioPaymentListSocioPayment : payment.getSocioPaymentList()) {
                Payment oldPaymentIdOfSocioPaymentListSocioPayment = socioPaymentListSocioPayment.getPaymentId();
                socioPaymentListSocioPayment.setPaymentId(payment);
                socioPaymentListSocioPayment = em.merge(socioPaymentListSocioPayment);
                if (oldPaymentIdOfSocioPaymentListSocioPayment != null) {
                    oldPaymentIdOfSocioPaymentListSocioPayment.getSocioPaymentList().remove(socioPaymentListSocioPayment);
                    oldPaymentIdOfSocioPaymentListSocioPayment = em.merge(oldPaymentIdOfSocioPaymentListSocioPayment);
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
            List<SocioPayment> socioPaymentListOld = persistentPayment.getSocioPaymentList();
            List<SocioPayment> socioPaymentListNew = payment.getSocioPaymentList();
            List<String> illegalOrphanMessages = null;
            for (SocioPayment socioPaymentListOldSocioPayment : socioPaymentListOld) {
                if (!socioPaymentListNew.contains(socioPaymentListOldSocioPayment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SocioPayment " + socioPaymentListOldSocioPayment + " since its paymentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SocioPayment> attachedSocioPaymentListNew = new ArrayList<SocioPayment>();
            for (SocioPayment socioPaymentListNewSocioPaymentToAttach : socioPaymentListNew) {
                socioPaymentListNewSocioPaymentToAttach = em.getReference(socioPaymentListNewSocioPaymentToAttach.getClass(), socioPaymentListNewSocioPaymentToAttach.getSocioPaymentId());
                attachedSocioPaymentListNew.add(socioPaymentListNewSocioPaymentToAttach);
            }
            socioPaymentListNew = attachedSocioPaymentListNew;
            payment.setSocioPaymentList(socioPaymentListNew);
            payment = em.merge(payment);
            for (SocioPayment socioPaymentListNewSocioPayment : socioPaymentListNew) {
                if (!socioPaymentListOld.contains(socioPaymentListNewSocioPayment)) {
                    Payment oldPaymentIdOfSocioPaymentListNewSocioPayment = socioPaymentListNewSocioPayment.getPaymentId();
                    socioPaymentListNewSocioPayment.setPaymentId(payment);
                    socioPaymentListNewSocioPayment = em.merge(socioPaymentListNewSocioPayment);
                    if (oldPaymentIdOfSocioPaymentListNewSocioPayment != null && !oldPaymentIdOfSocioPaymentListNewSocioPayment.equals(payment)) {
                        oldPaymentIdOfSocioPaymentListNewSocioPayment.getSocioPaymentList().remove(socioPaymentListNewSocioPayment);
                        oldPaymentIdOfSocioPaymentListNewSocioPayment = em.merge(oldPaymentIdOfSocioPaymentListNewSocioPayment);
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
            List<SocioPayment> socioPaymentListOrphanCheck = payment.getSocioPaymentList();
            for (SocioPayment socioPaymentListOrphanCheckSocioPayment : socioPaymentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Payment (" + payment + ") cannot be destroyed since the SocioPayment " + socioPaymentListOrphanCheckSocioPayment + " in its socioPaymentList field has a non-nullable paymentId field.");
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
