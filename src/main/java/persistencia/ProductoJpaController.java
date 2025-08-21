/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.Categoria;
import logica.Proveedor;
import logica.VentaDetallada;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import logica.Producto;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ProductoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("gestionPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) {
        if (producto.getVentaDetalladaList() == null) {
            producto.setVentaDetalladaList(new ArrayList<VentaDetallada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria idCategoria = producto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getId());
                producto.setIdCategoria(idCategoria);
            }
            Proveedor idProveedor = producto.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getId());
                producto.setIdProveedor(idProveedor);
            }
            List<VentaDetallada> attachedVentaDetalladaList = new ArrayList<VentaDetallada>();
            for (VentaDetallada ventaDetalladaListVentaDetalladaToAttach : producto.getVentaDetalladaList()) {
                ventaDetalladaListVentaDetalladaToAttach = em.getReference(ventaDetalladaListVentaDetalladaToAttach.getClass(), ventaDetalladaListVentaDetalladaToAttach.getId());
                attachedVentaDetalladaList.add(ventaDetalladaListVentaDetalladaToAttach);
            }
            producto.setVentaDetalladaList(attachedVentaDetalladaList);
            em.persist(producto);
            if (idCategoria != null) {
                idCategoria.getProductoList().add(producto);
                idCategoria = em.merge(idCategoria);
            }
            if (idProveedor != null) {
                idProveedor.getProductoList().add(producto);
                idProveedor = em.merge(idProveedor);
            }
            for (VentaDetallada ventaDetalladaListVentaDetallada : producto.getVentaDetalladaList()) {
                Producto oldIdProductoOfVentaDetalladaListVentaDetallada = ventaDetalladaListVentaDetallada.getIdProducto();
                ventaDetalladaListVentaDetallada.setIdProducto(producto);
                ventaDetalladaListVentaDetallada = em.merge(ventaDetalladaListVentaDetallada);
                if (oldIdProductoOfVentaDetalladaListVentaDetallada != null) {
                    oldIdProductoOfVentaDetalladaListVentaDetallada.getVentaDetalladaList().remove(ventaDetalladaListVentaDetallada);
                    oldIdProductoOfVentaDetalladaListVentaDetallada = em.merge(oldIdProductoOfVentaDetalladaListVentaDetallada);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getId());
            Categoria idCategoriaOld = persistentProducto.getIdCategoria();
            Categoria idCategoriaNew = producto.getIdCategoria();
            Proveedor idProveedorOld = persistentProducto.getIdProveedor();
            Proveedor idProveedorNew = producto.getIdProveedor();
            List<VentaDetallada> ventaDetalladaListOld = persistentProducto.getVentaDetalladaList();
            List<VentaDetallada> ventaDetalladaListNew = producto.getVentaDetalladaList();
            List<String> illegalOrphanMessages = null;
            for (VentaDetallada ventaDetalladaListOldVentaDetallada : ventaDetalladaListOld) {
                if (!ventaDetalladaListNew.contains(ventaDetalladaListOldVentaDetallada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VentaDetallada " + ventaDetalladaListOldVentaDetallada + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getId());
                producto.setIdCategoria(idCategoriaNew);
            }
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getId());
                producto.setIdProveedor(idProveedorNew);
            }
            List<VentaDetallada> attachedVentaDetalladaListNew = new ArrayList<VentaDetallada>();
            for (VentaDetallada ventaDetalladaListNewVentaDetalladaToAttach : ventaDetalladaListNew) {
                ventaDetalladaListNewVentaDetalladaToAttach = em.getReference(ventaDetalladaListNewVentaDetalladaToAttach.getClass(), ventaDetalladaListNewVentaDetalladaToAttach.getId());
                attachedVentaDetalladaListNew.add(ventaDetalladaListNewVentaDetalladaToAttach);
            }
            ventaDetalladaListNew = attachedVentaDetalladaListNew;
            producto.setVentaDetalladaList(ventaDetalladaListNew);
            producto = em.merge(producto);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getProductoList().remove(producto);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getProductoList().add(producto);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getProductoList().remove(producto);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getProductoList().add(producto);
                idProveedorNew = em.merge(idProveedorNew);
            }
            for (VentaDetallada ventaDetalladaListNewVentaDetallada : ventaDetalladaListNew) {
                if (!ventaDetalladaListOld.contains(ventaDetalladaListNewVentaDetallada)) {
                    Producto oldIdProductoOfVentaDetalladaListNewVentaDetallada = ventaDetalladaListNewVentaDetallada.getIdProducto();
                    ventaDetalladaListNewVentaDetallada.setIdProducto(producto);
                    ventaDetalladaListNewVentaDetallada = em.merge(ventaDetalladaListNewVentaDetallada);
                    if (oldIdProductoOfVentaDetalladaListNewVentaDetallada != null && !oldIdProductoOfVentaDetalladaListNewVentaDetallada.equals(producto)) {
                        oldIdProductoOfVentaDetalladaListNewVentaDetallada.getVentaDetalladaList().remove(ventaDetalladaListNewVentaDetallada);
                        oldIdProductoOfVentaDetalladaListNewVentaDetallada = em.merge(oldIdProductoOfVentaDetalladaListNewVentaDetallada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getId();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<VentaDetallada> ventaDetalladaListOrphanCheck = producto.getVentaDetalladaList();
            for (VentaDetallada ventaDetalladaListOrphanCheckVentaDetallada : ventaDetalladaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the VentaDetallada " + ventaDetalladaListOrphanCheckVentaDetallada + " in its ventaDetalladaList field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idCategoria = producto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getProductoList().remove(producto);
                idCategoria = em.merge(idCategoria);
            }
            Proveedor idProveedor = producto.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getProductoList().remove(producto);
                idProveedor = em.merge(idProveedor);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {

        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            // 🔥 Forzar a que lea siempre de BD, no de caché
            q.setHint("javax.persistence.cache.storeMode", "REFRESH");

            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
