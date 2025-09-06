/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.Compra;
import logica.Proveedor;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class CompraJpaController implements Serializable {

    public CompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public CompraJpaController() {
        this.emf = Persistence.createEntityManagerFactory("gestionPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compra compra) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor idProveedor = compra.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getId());
                compra.setIdProveedor(idProveedor);
            }
            em.persist(compra);
            if (idProveedor != null) {
                idProveedor.getCompraList().add(compra);
                idProveedor = em.merge(idProveedor);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compra compra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra persistentCompra = em.find(Compra.class, compra.getId());
            Proveedor idProveedorOld = persistentCompra.getIdProveedor();
            Proveedor idProveedorNew = compra.getIdProveedor();
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getId());
                compra.setIdProveedor(idProveedorNew);
            }
            compra = em.merge(compra);
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getCompraList().remove(compra);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getCompraList().add(compra);
                idProveedorNew = em.merge(idProveedorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = compra.getId();
                if (findCompra(id) == null) {
                    throw new NonexistentEntityException("The compra with id " + id + " no longer exists.");
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
            Compra compra;
            try {
                compra = em.getReference(Compra.class, id);
                compra.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compra with id " + id + " no longer exists.", enfe);
            }
            Proveedor idProveedor = compra.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getCompraList().remove(compra);
                idProveedor = em.merge(idProveedor);
            }
            em.remove(compra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Compra> findCompraEntities() {
        return findCompraEntities(true, -1, -1);
    }

    public List<Compra> findCompraEntities(int maxResults, int firstResult) {
        return findCompraEntities(false, maxResults, firstResult);
    }

    private List<Compra> findCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compra.class));
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

    public Compra findCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compra.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compra> rt = cq.from(Compra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    List<Compra> listarComprasFiltradas(String proveedor, LocalDate fecha, int offset, int registrosPorPagina) {
        EntityManager em = emf.createEntityManager();
        try {
            // 1️⃣ Comenzamos con el JPQL base
            StringBuilder jpql = new StringBuilder("SELECT c FROM Compra c WHERE 1=1");

            // 2️⃣ Añadimos filtros si no son nulos/vacíos
            if (proveedor != null && !proveedor.trim().isEmpty()) {
                jpql.append(" AND LOWER(c.idProveedor.nombreProveedor) LIKE LOWER(:proveedor)");
            }
            if (fecha != null) {
                jpql.append(" AND c.fechaCompra BETWEEN :inicioDia AND :finDia");
            }

            // 3️⃣ Ordenamos
            jpql.append(" ORDER BY c.fechaCompra DESC");

            // 4️⃣ Creamos la query
            TypedQuery<Compra> query = em.createQuery(jpql.toString(), Compra.class);

            // 5️⃣ Seteamos los parámetros si existen
            if (proveedor != null && !proveedor.trim().isEmpty()) {
                query.setParameter("proveedor", "%" + proveedor + "%");
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

    public long contarComprasFiltradas(String proveedor, String fecha) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(c) FROM Compra c WHERE 1=1");

            if (proveedor != null && !proveedor.trim().isEmpty()) {
                jpql.append(" AND LOWER(c.idProveedor.nombreProveedor) LIKE LOWER(:proveedor)");
            }
            if (fecha != null && !fecha.trim().isEmpty()) {
                // Convertir LocalDateTime a DATE en JPQL
                jpql.append(" AND FUNCTION('DATE', c.fechaCompra) = :fecha");
            }

            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);

            if (proveedor != null && !proveedor.trim().isEmpty()) {
                query.setParameter("proveedor", "%" + proveedor + "%");
            }
            if (fecha != null && !fecha.trim().isEmpty()) {
                query.setParameter("fecha", LocalDate.parse(fecha));
                // OJO: fecha debe venir en formato "yyyy-MM-dd"
            }

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public void ajusteIncrementoInventarioProductos(String idFacturaCompra) {
        EntityManager em = getEntityManager();

        try {

            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_incrementar_inventario");
            query.registerStoredProcedureParameter("id_factura", String.class, ParameterMode.IN);
            query.setParameter("id_factura", idFacturaCompra);
            query.execute();
            em.clear();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public boolean existeFacturaParaProveedor(Integer idProveedor, String numeroFactura) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT COUNT(c) FROM Compra c WHERE c.idProveedor.id = :idProveedor AND c.nFactura = :nFactura AND c.estadoCompra = true");
        query.setParameter("idProveedor", idProveedor);
        query.setParameter("nFactura", numeroFactura);

        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    public void ajusteDescontarInventarioProductosPorAnularCompraSP(String nFactura) {
        EntityManager em = getEntityManager();

        try {

            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_revertir_inventario");
            query.registerStoredProcedureParameter("id_factura", String.class, ParameterMode.IN);
            query.setParameter("id_factura", nFactura);
            query.execute();
            em.clear();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Compra buscarCompraActivaPorNumeroFactura(String nFactura) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Compra c WHERE c.nFactura = :nFactura AND c.estadoCompra = true ORDER BY c.id DESC",
                    Compra.class
            )
                    .setParameter("nFactura", nFactura)
                    .setMaxResults(1) // solo la última activa
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

}
