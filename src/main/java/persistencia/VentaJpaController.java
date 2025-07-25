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
import logica.VentaDetallada;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import logica.Venta;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class VentaJpaController implements Serializable {

    public VentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public VentaJpaController() {
    this.emf=Persistence.createEntityManagerFactory("gestionPU");
    }
    
    
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venta venta) {
        if (venta.getVentaDetalladaList() == null) {
            venta.setVentaDetalladaList(new ArrayList<VentaDetallada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<VentaDetallada> attachedVentaDetalladaList = new ArrayList<VentaDetallada>();
            for (VentaDetallada ventaDetalladaListVentaDetalladaToAttach : venta.getVentaDetalladaList()) {
                ventaDetalladaListVentaDetalladaToAttach = em.getReference(ventaDetalladaListVentaDetalladaToAttach.getClass(), ventaDetalladaListVentaDetalladaToAttach.getId());
                attachedVentaDetalladaList.add(ventaDetalladaListVentaDetalladaToAttach);
            }
            venta.setVentaDetalladaList(attachedVentaDetalladaList);
            em.persist(venta);
            for (VentaDetallada ventaDetalladaListVentaDetallada : venta.getVentaDetalladaList()) {
                Venta oldIdVentaOfVentaDetalladaListVentaDetallada = ventaDetalladaListVentaDetallada.getIdVenta();
                ventaDetalladaListVentaDetallada.setIdVenta(venta);
                ventaDetalladaListVentaDetallada = em.merge(ventaDetalladaListVentaDetallada);
                if (oldIdVentaOfVentaDetalladaListVentaDetallada != null) {
                    oldIdVentaOfVentaDetalladaListVentaDetallada.getVentaDetalladaList().remove(ventaDetalladaListVentaDetallada);
                    oldIdVentaOfVentaDetalladaListVentaDetallada = em.merge(oldIdVentaOfVentaDetalladaListVentaDetallada);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Venta venta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta persistentVenta = em.find(Venta.class, venta.getId());
            List<VentaDetallada> ventaDetalladaListOld = persistentVenta.getVentaDetalladaList();
            List<VentaDetallada> ventaDetalladaListNew = venta.getVentaDetalladaList();
            List<String> illegalOrphanMessages = null;
            for (VentaDetallada ventaDetalladaListOldVentaDetallada : ventaDetalladaListOld) {
                if (!ventaDetalladaListNew.contains(ventaDetalladaListOldVentaDetallada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VentaDetallada " + ventaDetalladaListOldVentaDetallada + " since its idVenta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<VentaDetallada> attachedVentaDetalladaListNew = new ArrayList<VentaDetallada>();
            for (VentaDetallada ventaDetalladaListNewVentaDetalladaToAttach : ventaDetalladaListNew) {
                ventaDetalladaListNewVentaDetalladaToAttach = em.getReference(ventaDetalladaListNewVentaDetalladaToAttach.getClass(), ventaDetalladaListNewVentaDetalladaToAttach.getId());
                attachedVentaDetalladaListNew.add(ventaDetalladaListNewVentaDetalladaToAttach);
            }
            ventaDetalladaListNew = attachedVentaDetalladaListNew;
            venta.setVentaDetalladaList(ventaDetalladaListNew);
            venta = em.merge(venta);
            for (VentaDetallada ventaDetalladaListNewVentaDetallada : ventaDetalladaListNew) {
                if (!ventaDetalladaListOld.contains(ventaDetalladaListNewVentaDetallada)) {
                    Venta oldIdVentaOfVentaDetalladaListNewVentaDetallada = ventaDetalladaListNewVentaDetallada.getIdVenta();
                    ventaDetalladaListNewVentaDetallada.setIdVenta(venta);
                    ventaDetalladaListNewVentaDetallada = em.merge(ventaDetalladaListNewVentaDetallada);
                    if (oldIdVentaOfVentaDetalladaListNewVentaDetallada != null && !oldIdVentaOfVentaDetalladaListNewVentaDetallada.equals(venta)) {
                        oldIdVentaOfVentaDetalladaListNewVentaDetallada.getVentaDetalladaList().remove(ventaDetalladaListNewVentaDetallada);
                        oldIdVentaOfVentaDetalladaListNewVentaDetallada = em.merge(oldIdVentaOfVentaDetalladaListNewVentaDetallada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = venta.getId();
                if (findVenta(id) == null) {
                    throw new NonexistentEntityException("The venta with id " + id + " no longer exists.");
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
            Venta venta;
            try {
                venta = em.getReference(Venta.class, id);
                venta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<VentaDetallada> ventaDetalladaListOrphanCheck = venta.getVentaDetalladaList();
            for (VentaDetallada ventaDetalladaListOrphanCheckVentaDetallada : ventaDetalladaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Venta (" + venta + ") cannot be destroyed since the VentaDetallada " + ventaDetalladaListOrphanCheckVentaDetallada + " in its ventaDetalladaList field has a non-nullable idVenta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(venta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Venta> findVentaEntities() {
        return findVentaEntities(true, -1, -1);
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return findVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> findVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venta.class));
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

    public Venta findVenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venta> rt = cq.from(Venta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
