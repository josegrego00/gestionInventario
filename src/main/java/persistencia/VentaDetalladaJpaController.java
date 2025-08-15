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
import logica.Producto;
import logica.Venta;
import logica.VentaDetallada;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class VentaDetalladaJpaController implements Serializable {

    public VentaDetalladaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public VentaDetalladaJpaController() {
    this.emf=Persistence.createEntityManagerFactory("gestionPU");
    }
    
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VentaDetallada ventaDetallada) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto idProducto = ventaDetallada.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getId());
                ventaDetallada.setIdProducto(idProducto);
            }
            Venta idVenta = ventaDetallada.getIdVenta();
            if (idVenta != null) {
                idVenta = em.getReference(idVenta.getClass(), idVenta.getId());
                ventaDetallada.setIdVenta(idVenta);
            }
            em.persist(ventaDetallada);
            if (idProducto != null) {
                idProducto.getVentaDetalladaList().add(ventaDetallada);
                idProducto = em.merge(idProducto);
            }
            if (idVenta != null) {
                idVenta.getVentaDetalladaList().add(ventaDetallada);
                idVenta = em.merge(idVenta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VentaDetallada ventaDetallada) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VentaDetallada persistentVentaDetallada = em.find(VentaDetallada.class, ventaDetallada.getId());
            Producto idProductoOld = persistentVentaDetallada.getIdProducto();
            Producto idProductoNew = ventaDetallada.getIdProducto();
            Venta idVentaOld = persistentVentaDetallada.getIdVenta();
            Venta idVentaNew = ventaDetallada.getIdVenta();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getId());
                ventaDetallada.setIdProducto(idProductoNew);
            }
            if (idVentaNew != null) {
                idVentaNew = em.getReference(idVentaNew.getClass(), idVentaNew.getId());
                ventaDetallada.setIdVenta(idVentaNew);
            }
            ventaDetallada = em.merge(ventaDetallada);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getVentaDetalladaList().remove(ventaDetallada);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getVentaDetalladaList().add(ventaDetallada);
                idProductoNew = em.merge(idProductoNew);
            }
            if (idVentaOld != null && !idVentaOld.equals(idVentaNew)) {
                idVentaOld.getVentaDetalladaList().remove(ventaDetallada);
                idVentaOld = em.merge(idVentaOld);
            }
            if (idVentaNew != null && !idVentaNew.equals(idVentaOld)) {
                idVentaNew.getVentaDetalladaList().add(ventaDetallada);
                idVentaNew = em.merge(idVentaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ventaDetallada.getId();
                if (findVentaDetallada(id) == null) {
                    throw new NonexistentEntityException("The ventaDetallada with id " + id + " no longer exists.");
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
            VentaDetallada ventaDetallada;
            try {
                ventaDetallada = em.getReference(VentaDetallada.class, id);
                ventaDetallada.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventaDetallada with id " + id + " no longer exists.", enfe);
            }
            Producto idProducto = ventaDetallada.getIdProducto();
            if (idProducto != null) {
                idProducto.getVentaDetalladaList().remove(ventaDetallada);
                idProducto = em.merge(idProducto);
            }
            Venta idVenta = ventaDetallada.getIdVenta();
            if (idVenta != null) {
                idVenta.getVentaDetalladaList().remove(ventaDetallada);
                idVenta = em.merge(idVenta);
            }
            em.remove(ventaDetallada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VentaDetallada> findVentaDetalladaEntities() {
        return findVentaDetalladaEntities(true, -1, -1);
    }

    public List<VentaDetallada> findVentaDetalladaEntities(int maxResults, int firstResult) {
        return findVentaDetalladaEntities(false, maxResults, firstResult);
    }

    private List<VentaDetallada> findVentaDetalladaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VentaDetallada.class));
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

    public VentaDetallada findVentaDetallada(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VentaDetallada.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaDetalladaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VentaDetallada> rt = cq.from(VentaDetallada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
