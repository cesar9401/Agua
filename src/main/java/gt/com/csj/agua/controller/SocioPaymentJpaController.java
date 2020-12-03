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
import gt.com.csj.agua.entity.Payment;
import gt.com.csj.agua.entity.Socio;
import gt.com.csj.agua.entity.SocioPayment;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author julio
 */
public class SocioPaymentJpaController implements Serializable {

    public SocioPaymentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SocioPayment socioPayment) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrator administratorId = socioPayment.getAdministratorId();
            if (administratorId != null) {
                administratorId = em.getReference(administratorId.getClass(), administratorId.getAdministratorId());
                socioPayment.setAdministratorId(administratorId);
            }
            Payment paymentId = socioPayment.getPaymentId();
            if (paymentId != null) {
                paymentId = em.getReference(paymentId.getClass(), paymentId.getPaymentId());
                socioPayment.setPaymentId(paymentId);
            }
            Socio socioId = socioPayment.getSocioId();
            if (socioId != null) {
                socioId = em.getReference(socioId.getClass(), socioId.getSocioId());
                socioPayment.setSocioId(socioId);
            }
            em.persist(socioPayment);
            if (administratorId != null) {
                administratorId.getSocioPaymentCollection().add(socioPayment);
                administratorId = em.merge(administratorId);
            }
            if (paymentId != null) {
                paymentId.getSocioPaymentCollection().add(socioPayment);
                paymentId = em.merge(paymentId);
            }
            if (socioId != null) {
                socioId.getSocioPaymentCollection().add(socioPayment);
                socioId = em.merge(socioId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SocioPayment socioPayment) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SocioPayment persistentSocioPayment = em.find(SocioPayment.class, socioPayment.getSocioPaymentId());
            Administrator administratorIdOld = persistentSocioPayment.getAdministratorId();
            Administrator administratorIdNew = socioPayment.getAdministratorId();
            Payment paymentIdOld = persistentSocioPayment.getPaymentId();
            Payment paymentIdNew = socioPayment.getPaymentId();
            Socio socioIdOld = persistentSocioPayment.getSocioId();
            Socio socioIdNew = socioPayment.getSocioId();
            if (administratorIdNew != null) {
                administratorIdNew = em.getReference(administratorIdNew.getClass(), administratorIdNew.getAdministratorId());
                socioPayment.setAdministratorId(administratorIdNew);
            }
            if (paymentIdNew != null) {
                paymentIdNew = em.getReference(paymentIdNew.getClass(), paymentIdNew.getPaymentId());
                socioPayment.setPaymentId(paymentIdNew);
            }
            if (socioIdNew != null) {
                socioIdNew = em.getReference(socioIdNew.getClass(), socioIdNew.getSocioId());
                socioPayment.setSocioId(socioIdNew);
            }
            socioPayment = em.merge(socioPayment);
            if (administratorIdOld != null && !administratorIdOld.equals(administratorIdNew)) {
                administratorIdOld.getSocioPaymentCollection().remove(socioPayment);
                administratorIdOld = em.merge(administratorIdOld);
            }
            if (administratorIdNew != null && !administratorIdNew.equals(administratorIdOld)) {
                administratorIdNew.getSocioPaymentCollection().add(socioPayment);
                administratorIdNew = em.merge(administratorIdNew);
            }
            if (paymentIdOld != null && !paymentIdOld.equals(paymentIdNew)) {
                paymentIdOld.getSocioPaymentCollection().remove(socioPayment);
                paymentIdOld = em.merge(paymentIdOld);
            }
            if (paymentIdNew != null && !paymentIdNew.equals(paymentIdOld)) {
                paymentIdNew.getSocioPaymentCollection().add(socioPayment);
                paymentIdNew = em.merge(paymentIdNew);
            }
            if (socioIdOld != null && !socioIdOld.equals(socioIdNew)) {
                socioIdOld.getSocioPaymentCollection().remove(socioPayment);
                socioIdOld = em.merge(socioIdOld);
            }
            if (socioIdNew != null && !socioIdNew.equals(socioIdOld)) {
                socioIdNew.getSocioPaymentCollection().add(socioPayment);
                socioIdNew = em.merge(socioIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = socioPayment.getSocioPaymentId();
                if (findSocioPayment(id) == null) {
                    throw new NonexistentEntityException("The socioPayment with id " + id + " no longer exists.");
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
            SocioPayment socioPayment;
            try {
                socioPayment = em.getReference(SocioPayment.class, id);
                socioPayment.getSocioPaymentId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The socioPayment with id " + id + " no longer exists.", enfe);
            }
            Administrator administratorId = socioPayment.getAdministratorId();
            if (administratorId != null) {
                administratorId.getSocioPaymentCollection().remove(socioPayment);
                administratorId = em.merge(administratorId);
            }
            Payment paymentId = socioPayment.getPaymentId();
            if (paymentId != null) {
                paymentId.getSocioPaymentCollection().remove(socioPayment);
                paymentId = em.merge(paymentId);
            }
            Socio socioId = socioPayment.getSocioId();
            if (socioId != null) {
                socioId.getSocioPaymentCollection().remove(socioPayment);
                socioId = em.merge(socioId);
            }
            em.remove(socioPayment);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SocioPayment> findSocioPaymentEntities() {
        return findSocioPaymentEntities(true, -1, -1);
    }

    public List<SocioPayment> findSocioPaymentEntities(int maxResults, int firstResult) {
        return findSocioPaymentEntities(false, maxResults, firstResult);
    }

    private List<SocioPayment> findSocioPaymentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SocioPayment.class));
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

    public SocioPayment findSocioPayment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SocioPayment.class, id);
        } finally {
            em.close();
        }
    }

    public int getSocioPaymentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SocioPayment> rt = cq.from(SocioPayment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
