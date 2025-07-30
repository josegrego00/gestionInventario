/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import logica.Categoria;
import logica.Producto;
import logica.Proveedor;
import persistencia.ControladoraPersistencia;

/**
 *
 * @author josepino
 */
public class ControladoraLogica {

    //Declaraciones de Variables
    private ControladoraPersistencia controladoraPersistencia;

    public ControladoraLogica() {

        controladoraPersistencia = new ControladoraPersistencia();
    }

    //-----------------------------Logica de Productos----------------------------------
    //Guardar  un producto
    public void guardarProductoNuevo(String codigoBarra, String nombre, String descripcion, double costoCompra, int inventarioInicial, int categoriaId, int proveedorId) {
        Producto producto = new Producto();
        producto.setCodigoBarra(codigoBarra);
        producto.setNombreProducto(nombre);
        producto.setDescripcionProducto(descripcion);

        //Transformo el costo que me da el HTML para Usarlo en mi Objeto
        BigDecimal costo = BigDecimal.valueOf(costoCompra);
        producto.setCostoProducto(costo);

        //Transformo el inventario que me da el HTML para Usarlo en mi Objeto
        BigDecimal inventario = BigDecimal.valueOf(inventarioInicial);
        producto.setInventario(inventario);

        //obtencion de categoria
        Categoria categoria = buscarCategoriaPorId(categoriaId);
        producto.setIdCategoria(categoria);

        //obtencion de proveedor
        Proveedor proveedor = buscarProveedorPorId(proveedorId);
        producto.setIdProveedor(proveedor);

        LocalDate fecha = LocalDate.now();
        Date fechaDate = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
        producto.setFechaCreacion(fechaDate);

        controladoraPersistencia.guardarProductoNuevo(producto);

    }

    //  -------------- Verificar si el Producto Existe para poder Agregarlo a la base de datos---------------- 
    public boolean existeProducto(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        List<Producto> listaProductoExistentes = listarProductos();
        if (listaProductoExistentes.isEmpty() || listaProductoExistentes == null) {
            return false;
        }
        for (Producto productos : listaProductoExistentes) {
            if (productos.getNombreProducto().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public List<Producto> listarProductos() {
        return controladoraPersistencia.listarProductos();
    }

    public boolean validarDatosProducto(String codigoBarra, String nombre, String descripcion, Double costoCompra, Integer inventarioInicial, Integer categoriaId, Integer proveedorId) {

        // Validar campos obligatorios esto es para generar un producto
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        if (codigoBarra == null || codigoBarra.trim().isEmpty()) {
            return false;
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return false;
        }
        if (costoCompra == null || costoCompra <= 0) {
            return false;
        }

        // Puede ser 0, pero no negativo
        if (inventarioInicial == null || inventarioInicial < 0) {
            return false;
        }

        if (categoriaId == null || categoriaId <= 0) {
            return false;
        }
        if (proveedorId == null || proveedorId <= 0) {
            return false;
        }

        // ------------------- Validar contenido de strings para evitar SQLi ---------------------------
        if (contieneInyeccionSQL(nombre)) {
            return false;
        }

        if (contieneInyeccionSQL(descripcion)) {
            return false;
        }

        if (contieneInyeccionSQL(codigoBarra)) {
            return false;
        }

        return true; // Todos los datos son válidos
    }

    // Función auxiliar para detectar patrones comunes de SQL Injection
    //importante para Evitar Errores en Seguridad..
    private boolean contieneInyeccionSQL(String datos) {
        if (datos == null) {
            return false;
        }

        String lowerInput = datos.toLowerCase();

        // Buscar palabras clave peligrosas o caracteres especiales
        String[] patronesPeligrosos = {
            "select", "insert", "update", "delete", "drop", "truncate",
            "--", ";", "'", "\"", " or ", " and ", "="
        };

        for (String patron : patronesPeligrosos) {
            if (lowerInput.contains(patron)) {
                return true;
            }
        }

        return false;
    }

    //codigo para verificar que no existe otro producto con este codigo
    public boolean existeProductoPorCodigoBarra(String codigoBarra) {
        List<Producto> listaProductos = listarProductos();
        for (Producto producto : listaProductos) {
            if (producto.getCodigoBarra().equals(codigoBarra)) {
                return true;
            }

        }
        return false;
    }

    //esto es para obtener el producto con codigo de Barra
    public Producto buscarProductoPorCodigoBarra(String codigoBarra) {
        List<Producto> listarProducto = listarProductos();
        for (Producto producto : listarProducto) {
            if (producto.getCodigoBarra().equals(codigoBarra)) {
                return producto;
            }
        }
        return null;
    }

    public void guardarActualizacionProducto(String codigoBarra, String nombre, String descripcion, double costoCompra, int categoriaId, int proveedorId) {

        Producto productoActualizado = buscarProductoPorCodigoBarra(codigoBarra);
        productoActualizado.setCodigoBarra(codigoBarra);
        productoActualizado.setNombreProducto(nombre);
        productoActualizado.setDescripcionProducto(descripcion);

        //Transformo el costo que me da el HTML para Usarlo en mi Objeto
        BigDecimal costo = BigDecimal.valueOf(costoCompra);
        productoActualizado.setCostoProducto(costo);

        //obtencion de categoria
        Categoria categoria = buscarCategoriaPorId(categoriaId);
        productoActualizado.setIdCategoria(categoria);

        //obtencion de proveedor
        Proveedor proveedor = buscarProveedorPorId(proveedorId);
        productoActualizado.setIdProveedor(proveedor);

        controladoraPersistencia.guardarActualizacionProducto(productoActualizado);
    }

    public boolean validarDatosProductoParaActualizar(String nombre, String descripcion, Double costoCompra, Integer categoriaId, Integer proveedorId) {
        // Validar campos obligatorios esto es para generar un producto
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return false;
        }
        if (costoCompra == null || costoCompra <= 0) {
            return false;
        }

        if (categoriaId == null || categoriaId <= 0) {
            return false;
        }
        if (proveedorId == null || proveedorId <= 0) {
            return false;
        }

        // ------------------- Validar contenido de strings para evitar SQLi ---------------------------
        if (contieneInyeccionSQL(nombre)) {
            return false;
        }

        if (contieneInyeccionSQL(descripcion)) {
            return false;
        }

        return true; // Todos los datos son válidos
    }

    public void eliminarProducto(Producto producto) {
        controladoraPersistencia.eliminarProducto(producto.getId());
    }

    //--------------------------------- Logica de Categoria -----------------------------------
    private Categoria buscarCategoriaPorId(int categoriaId) {
        return controladoraPersistencia.buscarCategoriaPorId(categoriaId);
    }

    public List<Categoria> listarCategorias() {
        return controladoraPersistencia.listarCategorias();
    }

    //---------------------------------- Logica de Proveedor ----------------------------------
    private Proveedor buscarProveedorPorId(int proveedorId) {
        return controladoraPersistencia.buscarProveedorPorId(proveedorId);
    }

    public List<Proveedor> listarProveedor() {
        return controladoraPersistencia.listarProveedor();
    }

    //-------------------------------- Logica de Clientes------------------------------------------
    public List<Cliente> listarClientes() {
        return controladoraPersistencia.listarClientes();
    }

    public boolean validarClientePorDni(String dniCliente) {
        List<Cliente> listaClientesExistentes = listarClientes();
        for (Cliente cliente : listaClientesExistentes) {
            if (cliente.getDniCliente().equals(dniCliente)) {
                return true;
            }
        }
        return false;
    }

    public void agregarNuevoCliente(String clienteId, String nombreCliente) {
        Cliente clienteNuevo = new Cliente();
        clienteNuevo.setDniCliente(clienteId);
        clienteNuevo.setNombreCliente(nombreCliente);
        controladoraPersistencia.crearNuevoCliente(clienteNuevo);
    }

    public boolean validarDatosIngresadosCliente(String clienteId, String nombreCliente) {
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            return false;
        }
        if (clienteId == null || clienteId.trim().isEmpty()) {
            return false;
        }
        if (contieneInyeccionSQL(nombreCliente)) {
            return false;
        }

        if (contieneInyeccionSQL(clienteId)) {
            return false;
        }
        return true;
    }

    public void crearFactura(String dniCliente, double totalFactura, LocalDateTime horaActualFactura) {
        
        // Se Crean las VEnta, es decir numero de factura
        
        Venta venta= new Venta();
        
        //Se crea  el cliente que estara dentro de la factura y se le agrega el monto total de la facturas
        
        Cliente cliente=buscarClientePorDni(dniCliente);
        venta.setDniCliente(cliente);
        venta.setFechaVenta(horaActualFactura);
        //se convierte el double a un bigdecimal
        venta.setTotalVenta(BigDecimal.valueOf(totalFactura));
        
        controladoraPersistencia.crearFactura(venta);
    }

    private Cliente buscarClientePorDni(String dniCliente) {
        List<Cliente> listaClientEx=listarClientes();
        for(Cliente clien: listaClientEx){
            if(clien.getDniCliente().equals(dniCliente)){
                return clien;
            }
        }
        return null;        
    }

}
