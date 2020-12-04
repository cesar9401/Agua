/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.com.csj.agua.controller;

import gt.com.csj.agua.controller.exceptions.NonexistentEntityException;
import gt.com.csj.agua.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gt.com.csj.agua.entity.Administrator;
import gt.com.csj.agua.entity.Fine;
import gt.com.csj.agua.entity.Socio;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author julio
 */
public class FineJpaController implements Serializable {

    public FineJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fine fine) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrator administratorId = fine.getAdministratorId();
            if (administratorId != null) {
                administratorId = em.getReference(administratorId.getClass(), administratorId.getAdministratorId());
                fine.setAdministratorId(administratorId);
            }
            Socio socioId = fine.getSocioId();
            if (socioId != null) {
                socioId = em.getReference(socioId.getClass(), socioId.getSocioId());
                fine.setSocioId(socioId);
            }
            em.persist(fine);
            if (administratorId != null) {
                administratorId.getFineList().add(fine);
                administratorId = em.merge(administratorId);
            }
            if (socioId != null) {
                socioId.getFineList().add(fine);
                socioId = em.merge(socioId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFine(fine.getFineId()) != null) {
                throw new PreexistingEntityException("Fine " + fine + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fine fine) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fine persistentFine = em.find(Fine.class, fine.getFineId());
            Administrator administratorIdOld = persistentFine.getAdministratorId();
            Administrator administratorIdNew = fine.getAdministratorId();
            Socio socioIdOld = persistentFine.getSocioId();
            Socio socioIdNew = fine.getSocioId();
            if (administratorIdNew != null) {
                administratorIdNew = em.getReference(administratorIdNew.getClass(), administratorIdNew.getAdministratorId());
                fine.setAdministratorId(administratorIdNew);
            }
            if (socioIdNew != null) {
                socioIdNew = em.getReference(socioIdNew.getClass(), socioIdNew.getSocioId());
                fine.setSocioId(socioIdNew);
            }
            fine = em.merge(fine);
            if (administratorIdOld != null && !administratorIdOld.equals(administratorIdNew)) {
                administratorIdOld.getFineList().remove(fine);
                administratorIdOld = em.merge(administratorIdOld);
            }
            if (administratorIdNew != null && !administratorIdNew.equals(administratorIdOld)) {
                administratorIdNew.getFineList().add(fine);
                administratorIdNew = em.merge(administratorIdNew);
            }
            if (socioIdOld != null && !socioIdOld.equals(socioIdNew)) {
                socioIdOld.getFineList().remove(fine);
                socioIdOld = em.merge(socioIdOld);
            }
            if (socioIdNew != null && !socioIdNew.equals(socioIdOld)) {
                socioIdNew.getFineList().add(fine);
                socioIdNew = em.merge(socioIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = fine.getFineId();
                if (findFine(id) == null) {
                    throw new NonexistentEntityException("The fine with id " + id + " no longer exists.");
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
            Fine fine;
            try {
                fine = em.getReference(Fine.class, id);
                fine.getFineId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fine with id " + id + " no longer exists.", enfe);
            }
            Administrator administratorId = fine.getAdministratorId();
            if (administratorId != null) {
                administratorId.getFineList().remove(fine);
                administratorId = em.merge(administratorId);
            }
            Socio socioId = fine.getSocioId();
            if (socioId != null) {
                socioId.getFineList().remove(fine);
                socioId = em.merge(socioId);
            }
            em.remove(fine);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fine> findFineEntities() {
        return findFineEntities(true, -1, -1);
    }

    public List<Fine> findFineEntities(int maxResults, int firstResult) {
        return findFineEntities(false, maxResults, firstResult);
    }

    private List<Fine> findFineEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fine.class));
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

    public Fine findFine(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fine.class, id);
        } finally {
            em.close();
        }
    }

    public int getFineCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fine> rt = cq.from(Fine.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
