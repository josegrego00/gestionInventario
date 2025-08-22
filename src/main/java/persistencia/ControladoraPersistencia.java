/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.TypedQuery;
import logica.Categoria;
import logica.Cliente;
import logica.Producto;
import logica.Proveedor;
import logica.Venta;
import logica.VentaDetallada;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author josepino
 */
public class ControladoraPersistencia {

    private CategoriaJpaController categoriaJpaController;
    private ProveedorJpaController proveedorJpaController;
    private ProductoJpaController productoJpaController;
    private ClienteJpaController clienteJpaController;
    private VentaJpaController ventaJpaController;
    private VentaDetalladaJpaController ventaDetalladaJpaController;

    public ControladoraPersistencia() {

        categoriaJpaController = new CategoriaJpaController();
        proveedorJpaController = new ProveedorJpaController();
        productoJpaController = new ProductoJpaController();
        clienteJpaController = new ClienteJpaController();
        ventaJpaController = new VentaJpaController();
        ventaDetalladaJpaController = new VentaDetalladaJpaController();
    }

    public long contarVenta(String cliente, String fecha) {
        return ventaJpaController.contarVentasFiltradas(cliente, fecha);
    }

    //------------------------------Persistencia de Categoria -------------------------------
    public Categoria buscarCategoriaPorId(int categoriaId) {
        return categoriaJpaController.findCategoria(categoriaId);
    }

    public List<Categoria> listarCategorias() {
        return categoriaJpaController.findCategoriaEntities();
    }

    public void guardarCategoriaNueva(Categoria categoria) {
        categoriaJpaController.create(categoria);
    }

    public void guardarActualizacionCategoria(Categoria categoria) {
        try {
            categoriaJpaController.edit(categoria);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarCategoria(int idCategoria) {
        try {
            categoriaJpaController.destroy(idCategoria);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //-------------------------------Persistencia de Proveedor--------------------------------
    public Proveedor buscarProveedorPorId(int proveedorId) {
        return proveedorJpaController.findProveedor(proveedorId);
    }

    public List<Proveedor> listarProveedor() {
        return proveedorJpaController.findProveedorEntities();
    }

    //--------------------------------PErsistencia para PRoductos------------------------------
    public void guardarProductoNuevo(Producto producto) {

        productoJpaController.create(producto);
    }

    public List<Producto> listarProductos() {
        return productoJpaController.findProductoEntities();
    }

    public void guardarActualizacionProducto(Producto productoActualizado) {
        try {
            productoJpaController.edit(productoActualizado);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarProducto(Integer id) throws IllegalOrphanException, NonexistentEntityException, Exception {
        productoJpaController.destroy(id);
    }

    public void ajusteDescuentoInventarioProductos(Integer idFactura) {
        ventaJpaController.descontarInventarioVentaSP(idFactura);
    }

    //---------------------------- Persistencia de Clientes-------------------------------------------
    public List<Cliente> listarClientes() {
        return clienteJpaController.findClienteEntities();
    }

    public void crearNuevoCliente(Cliente clienteNuevo) {
        clienteJpaController.create(clienteNuevo);
    }

    //-------------------------Persistencia de Venta o Facturas-----------------------------------------
    public void crearFactura(Venta venta) {
        ventaJpaController.create(venta);
    }

    public void guardarDetalleVenta(VentaDetallada ventaDetalle) {
        ventaDetalladaJpaController.create(ventaDetalle);
    }

    public Venta buscarVentaFactura(int idFactura) {
        return ventaJpaController.findVenta(idFactura);
    }

    public List<Venta> listarVentas() {
        return ventaJpaController.findVentaEntities();
    }

    public List<Venta> listarVentasParaPaginado(String cliente, LocalDate fecha, int offset, int registrosPorPagina) {
        return ventaJpaController.listarVentasFiltradas(cliente, fecha, offset, registrosPorPagina);
    }

    public void actualizarFactura(Venta venta) {
        try {
            ventaJpaController.edit(venta);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
