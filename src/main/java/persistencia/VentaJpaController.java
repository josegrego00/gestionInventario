/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.Cliente;
import logica.VentaDetallada;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
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

        this.emf = Persistence.createEntityManagerFactory("gestionPU");
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
            Cliente dniCliente = venta.getDniCliente();
            if (dniCliente != null) {
                dniCliente = em.getReference(dniCliente.getClass(), dniCliente.getId());
                venta.setDniCliente(dniCliente);
            }
            List<VentaDetallada> attachedVentaDetalladaList = new ArrayList<VentaDetallada>();
            for (VentaDetallada ventaDetalladaListVentaDetalladaToAttach : venta.getVentaDetalladaList()) {
                ventaDetalladaListVentaDetalladaToAttach = em.getReference(ventaDetalladaListVentaDetalladaToAttach.getClass(), ventaDetalladaListVentaDetalladaToAttach.getId());
                attachedVentaDetalladaList.add(ventaDetalladaListVentaDetalladaToAttach);
            }
            venta.setVentaDetalladaList(attachedVentaDetalladaList);
            em.persist(venta);
            if (dniCliente != null) {
                dniCliente.getVentaList().add(venta);
                dniCliente = em.merge(dniCliente);
            }
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
            Cliente dniClienteOld = persistentVenta.getDniCliente();
            Cliente dniClienteNew = venta.getDniCliente();
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
            if (dniClienteNew != null) {
                dniClienteNew = em.getReference(dniClienteNew.getClass(), dniClienteNew.getId());
                venta.setDniCliente(dniClienteNew);
            }
            List<VentaDetallada> attachedVentaDetalladaListNew = new ArrayList<VentaDetallada>();
            for (VentaDetallada ventaDetalladaListNewVentaDetalladaToAttach : ventaDetalladaListNew) {
                ventaDetalladaListNewVentaDetalladaToAttach = em.getReference(ventaDetalladaListNewVentaDetalladaToAttach.getClass(), ventaDetalladaListNewVentaDetalladaToAttach.getId());
                attachedVentaDetalladaListNew.add(ventaDetalladaListNewVentaDetalladaToAttach);
            }
            ventaDetalladaListNew = attachedVentaDetalladaListNew;
            venta.setVentaDetalladaList(ventaDetalladaListNew);
            venta = em.merge(venta);
            if (dniClienteOld != null && !dniClienteOld.equals(dniClienteNew)) {
                dniClienteOld.getVentaList().remove(venta);
                dniClienteOld = em.merge(dniClienteOld);
            }
            if (dniClienteNew != null && !dniClienteNew.equals(dniClienteOld)) {
                dniClienteNew.getVentaList().add(venta);
                dniClienteNew = em.merge(dniClienteNew);
            }
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
            Cliente dniCliente = venta.getDniCliente();
            if (dniCliente != null) {
                dniCliente.getVentaList().remove(venta);
                dniCliente = em.merge(dniCliente);
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

    public List<Venta> listarVentasFiltradas(String cliente, LocalDate fecha, int offset, int registrosPorPagina) {
        EntityManager em = emf.createEntityManager();
        try {
            // 1️⃣ Comenzamos con el JPQL base
            StringBuilder jpql = new StringBuilder("SELECT v FROM Venta v WHERE 1=1");

            // 2️⃣ Añadimos filtros si no son nulos/vacíos
            if (cliente != null && !cliente.trim().isEmpty()) {
                jpql.append(" AND LOWER(v.dniCliente.nombreCliente) LIKE LOWER(:cliente)");
            }
            if (fecha != null) {
                jpql.append(" AND v.fechaVenta BETWEEN :inicioDia AND :finDia");
            }

            // 3️⃣ Ordenamos
            jpql.append(" ORDER BY v.fechaVenta DESC");

            // 4️⃣ Creamos la query
            TypedQuery<Venta> query = em.createQuery(jpql.toString(), Venta.class);

            // 5️⃣ Seteamos los parámetros si existen
            if (cliente != null && !cliente.trim().isEmpty()) {
                query.setParameter("cliente", "%" + cliente + "%");
            }
            if (fecha != null) {
                LocalDateTime inicioDia = fecha.atStartOfDay();
                LocalDateTime finDia = fecha.atTime(23, 59, 59);
                query.setParameter("inicioDia", inicioDia);
                query.setParameter("finDia", finDia);
            }
            // 6️⃣ Paginación
            query.setFirstResult(offset);
            query.setMaxResults(registrosPorPagina);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long contarVentasFiltradas(String cliente, String fecha) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(v) FROM Venta v WHERE 1=1");

            if (cliente != null && !cliente.trim().isEmpty()) {
                jpql.append(" AND LOWER(v.dniCliente.nombreCliente) LIKE LOWER(:cliente)");
            }
            if (fecha != null && !fecha.trim().isEmpty()) {
                jpql.append(" AND FUNCTION('DATE', v.fechaVenta) = :fecha");
            }

            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);

            if (cliente != null && !cliente.trim().isEmpty()) {
                query.setParameter("cliente", "%" + cliente + "%");
            }
            if (fecha != null && !fecha.trim().isEmpty()) {
                query.setParameter("fecha", LocalDate.parse(fecha));
            }

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    public boolean descontarInventarioVenta(int idFactura) {
    EntityManager em = getEntityManager();
    try {
        StoredProcedureQuery query = em.createStoredProcedureQuery("sp_descontar_inventario_venta");
        query.registerStoredProcedureParameter("id_factura", Integer.class, ParameterMode.IN);
        query.setParameter("id_factura", idFactura);
        query.execute();
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
    
    
    
}
    
    
    
    
}
