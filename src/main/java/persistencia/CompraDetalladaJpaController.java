/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.CompraDetallada;
import logica.Producto;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class CompraDetalladaJpaController implements Serializable {

    public CompraDetalladaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public CompraDetalladaJpaController() {
    this.emf=Persistence.createEntityManagerFactory("gestionPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CompraDetallada compraDetallada) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto idProducto = compraDetallada.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getId());
                compraDetallada.setIdProducto(idProducto);
            }
            em.persist(compraDetallada);
            if (idProducto != null) {
                idProducto.getCompraDetalladaList().add(compraDetallada);
                idProducto = em.merge(idProducto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CompraDetallada compraDetallada) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CompraDetallada persistentCompraDetallada = em.find(CompraDetallada.class, compraDetallada.getId());
            Producto idProductoOld = persistentCompraDetallada.getIdProducto();
            Producto idProductoNew = compraDetallada.getIdProducto();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getId());
                compraDetallada.setIdProducto(idProductoNew);
            }
            compraDetallada = em.merge(compraDetallada);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getCompraDetalladaList().remove(compraDetallada);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getCompraDetalladaList().add(compraDetallada);
                idProductoNew = em.merge(idProductoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = compraDetallada.getId();
                if (findCompraDetallada(id) == null) {
                    throw new NonexistentEntityException("The compraDetallada with id " + id + " no longer exists.");
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
            CompraDetallada compraDetallada;
            try {
                compraDetallada = em.getReference(CompraDetallada.class, id);
                compraDetallada.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compraDetallada with id " + id + " no longer exists.", enfe);
            }
            Producto idProducto = compraDetallada.getIdProducto();
            if (idProducto != null) {
                idProducto.getCompraDetalladaList().remove(compraDetallada);
                idProducto = em.merge(idProducto);
            }
            em.remove(compraDetallada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CompraDetallada> findCompraDetalladaEntities() {
        return findCompraDetalladaEntities(true, -1, -1);
    }

    public List<CompraDetallada> findCompraDetalladaEntities(int maxResults, int firstResult) {
        return findCompraDetalladaEntities(false, maxResults, firstResult);
    }

    private List<CompraDetallada> findCompraDetalladaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CompraDetallada.class));
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

    public CompraDetallada findCompraDetallada(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CompraDetallada.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompraDetalladaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CompraDetallada> rt = cq.from(CompraDetallada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
