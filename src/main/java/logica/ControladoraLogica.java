/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public long contarVentas(String cliente, String fecha) {
        return controladoraPersistencia.contarVenta(cliente, fecha);
    }

    //--------------------------- Logica Para todo------------------------------------
    // Este sera un metodo en la cual se le puedo ingersar n texto sea 
    //el q sea, con el fin de evitar errorres y que la base de datos se llene de basura
    public boolean validarTexto(String texto) {

        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        if (contieneInyeccionSQL(texto)) {
            return false;
        }
        return true;

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
        Categoria categoria = buscarCategoriaPorID(categoriaId);
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
        Categoria categoria = buscarCategoriaPorID(categoriaId);
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

    
    private boolean ajusteDescuentoInventarioProductos(Venta facturaVenta) {
        return controladoraPersistencia.ajusteDescuentoInventarioProductos(facturaVenta.getId());
    }

    
    
    
    
    
//---------------------------------------------------------------------------- Logica de Categoria ------------------------------------------------------------------------------
    public List<Categoria> listarCategorias() {
        return controladoraPersistencia.listarCategorias();
    }

    public Categoria buscarCategoriaPorID(int idCategoria) {
        return controladoraPersistencia.buscarCategoriaPorId(idCategoria);
    }

    // se toma toda las categoria porque la verdad el sistema no tendragran cantidad de categorias
    // Asumo que por muuuuucho tendra 1000
    public boolean existeCategoria(String nombreCategoria) {
        List<Categoria> listaCategoriaExistente = listarCategorias();
        for (Categoria categoria : listaCategoriaExistente) {
            if (categoria.getNombreCategoria().equals(nombreCategoria)) {
                return true;
            }
        }
        return false;
    }

    public Categoria buscarCategoriaPorNombre(String nombreCategoria) {
        List<Categoria> listaCategoriasExistentes = listarCategorias();
        for (Categoria categoria : listaCategoriasExistentes) {
            if (categoria.getNombreCategoria().equals(nombreCategoria)) {
                return categoria;
            }
        }
        return null;
    }

    public void guardarActualizacionCategoria(Integer idcategoria, String nombreCategoria, String descripcionCategoria) {
        Categoria categoria = buscarCategoriaPorID(idcategoria);
        if (categoria != null) {
            categoria.setNombreCategoria(nombreCategoria);
            categoria.setDescripcionCategoria(descripcionCategoria);
            controladoraPersistencia.guardarActualizacionCategoria(categoria);
        }
    }

    public void eliminarCategoria(String idCategoria) {

        controladoraPersistencia.eliminarCategoria(Integer.parseInt(idCategoria));

    }

    public void guardarCategoriaNuevo(String nombreCategoria, String descripcionCategoria) {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(nombreCategoria);
        categoria.setDescripcionCategoria(descripcionCategoria);
        controladoraPersistencia.guardarCategoriaNueva(categoria);
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

    public String determinarCliente(String opcionCliente, String nuevoDni, String nuevoNombre) {

        if (opcionCliente == null || opcionCliente.trim().isEmpty()) {
            String clientePorDefecto = "5555555"; // DNI del cliente por defecto
            if (validarClientePorDni(clientePorDefecto)) {
                return clientePorDefecto;
            }
        }

        if ("nuevo".equals(opcionCliente)) {
            // Validar datos del nuevo cliente
            if (!validarDatosIngresadosCliente(nuevoDni, nuevoNombre)) {
                return null;
            }
            // Si el cliente no existe, crearlo
            if (!validarClientePorDni(nuevoDni)) {
                agregarNuevoCliente(nuevoDni, nuevoNombre);
            }
            return nuevoDni;
        } else {
            // Validar que el cliente existente sea válido
            return validarClientePorDni(opcionCliente) ? opcionCliente : null;
        }
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

    private Cliente buscarClientePorDni(String dniCliente) {
        List<Cliente> listaClientEx = listarClientes();
        for (Cliente clien : listaClientEx) {
            if (clien.getDniCliente().equals(dniCliente)) {
                return clien;
            }
        }
        return null;
    }

    //----------------------------------Ventas ---------------------------------------------------------------
    public int crearFactura(String dniCliente, double totalFactura, LocalDateTime horaActualFactura, String formaPago, boolean estado) {

        // Se Crean las VEnta, es decir numero de factura
        Venta venta = new Venta();

        //Se crea  el cliente que estara dentro de la factura y se le agrega el monto total de la facturas
        Cliente cliente = buscarClientePorDni(dniCliente);
        venta.setDniCliente(cliente);
        venta.setFechaVenta(horaActualFactura);
        //se convierte el double a un bigdecimal
        venta.setTotalVenta(BigDecimal.valueOf(totalFactura));
        venta.setTipoPago(formaPago);
        venta.setEstadoFactura(estado);
        controladoraPersistencia.crearFactura(venta);
        return venta.getId();
    }

    public void procesarDetallesVenta(String detalleJson, int idFactura) {
        if (detalleJson == null || detalleJson.trim().isEmpty()) {
            return;
        }

        try {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<DetalleVentaFactura>>() {
            }.getType();
            List<DetalleVentaFactura> detalles = gson.fromJson(detalleJson, tipoLista);
            Venta facturaVenta = buscarVentaFactura(idFactura);
            for (DetalleVentaFactura item : detalles) {
                VentaDetallada ventaDetallada = new VentaDetallada();
                Producto producto = buscarProductoPorCodigoBarra(item.getProductoId());
                ventaDetallada.setIdVenta(facturaVenta);
                ventaDetallada.setIdProducto(producto);
                ventaDetallada.setCantidadVendida(BigDecimal.valueOf(item.getCantidad()));
                ventaDetallada.setPrecioProducto(BigDecimal.valueOf(item.getPrecio()));
                
                controladoraPersistencia.guardarDetalleVenta(ventaDetallada);
            }
            ajusteDescuentoInventarioProductos(facturaVenta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Venta buscarVentaFactura(int idFactura) {
        return controladoraPersistencia.buscarVentaFactura(idFactura);
    }

    public List<Venta> listarVentas() {
        return controladoraPersistencia.listarVentas();
    }

    public List<Venta> listarVentasParaPaginado(String cliente, String fechaStr, int offset, int registrosPorPagina) {
        LocalDate localfecha=null;

        if (fechaStr != null && !fechaStr.trim().isEmpty()) {
            localfecha = LocalDate.parse(fechaStr); // Formato ISO yyyy-MM-dd
            
        }
        
        return controladoraPersistencia.listarVentasParaPaginado(cliente, localfecha, offset, registrosPorPagina);
    }

    public boolean anularFactura(String idFactura) {
        Venta factura = buscarVentaFactura(Integer.parseInt(idFactura));
        if (factura != null) {
            factura.setEstadoFactura(false);
            controladoraPersistencia.actualizarFactura(factura);
            return true;
        }
        return false;

    }

    // ---------------------------------------------Generacion de Factura ----------------------------------------------------------
    public void generarFactura(HttpServletResponse response, int idFactura) throws IOException {

        Venta factura = buscarVentaFactura(idFactura);
        if (factura != null) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"factura.pdf\"");
            try {
                // Configuración inicial del documento
                PdfWriter writer = new PdfWriter(response.getOutputStream());
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);
                document.setMargins(50, 50, 50, 50);

                // 1. ENCABEZADO DE LA FACTURA
                addHeader(document, factura.getId());

                // 2. DATOS DEL CLIENTE
                addClientInfo(document, factura);

                // 3. TABLA DE PRODUCTOS/SERVICIOS
                addProductsTable(document, factura);

                // 4. TOTALES Y DETALLES DE PAGO
                addTotalsSection(document, factura);

                // 5. PIE DE PÁGINA
                addFooter(document);

                document.close();

            } catch (Exception e) {
                if (!response.isCommitted()) { // ← Verifica si la respuesta no se ha enviado
                    response.reset();
                    response.setContentType("text/html");
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<h1>Error al generar factura</h1>");
                        out.println("<p>" + e.getMessage() + "</p>");
                    } catch (IOException ex) {
                        Logger.getLogger(ControladoraLogica.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    // Logear el error, pero no intentar escribir en la respuesta
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al generar factura (respuesta ya comprometida)", e);
                }
                throw new IOException("Error generando factura", e);
            }
        }

    }

//----------------------------------------------------------Metodos Usados para PErsonalizar la factura------------------------------------------------------
    private void addHeader(Document document, int idFactura) {

        // Logo y datos de la empresa
        Paragraph header = new Paragraph()
                .add(new Text("Factura \n").setFontSize(18).setBold())
                .add(new Text("Nit: 12345678901\n").setFontSize(10))
                .add(new Text("N° de Factura : " + idFactura + "\n").setFontSize(10))
                .add(new Text("Cucuta, Norte de Santander\n").setFontSize(10))
                .add(new Text("Teléfono: (05) 555-5555\n").setFontSize(10))
                .setTextAlignment(TextAlignment.CENTER);

        document.add(header);

        Venta factura = buscarVentaFactura(idFactura);

        if (factura.getEstadoFactura() == false) {
            document.add(new Paragraph("\nFACTURA ANULADA")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(22));
        } else {
            document.add(new Paragraph("\nFACTURA ELECTRÓNICA N° : " + idFactura + "\n")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(16));
        }
        // Título Factura

    }

    private void addClientInfo(Document document, Venta factura) {
        // Datos del cliente (puedes obtenerlos de request.getParameter())
        float[] columnWidths = {2, 5};
        Table clientTable = new Table(columnWidths);

        clientTable.addCell(new Cell().add(new Paragraph("Cliente:").setBold()));
        clientTable.addCell(new Cell().add(new Paragraph(factura.getDniCliente().getNombreCliente())));

        clientTable.addCell(new Cell().add(new Paragraph("RUC/DNI:").setBold()));
        clientTable.addCell(new Cell().add(new Paragraph(factura.getDniCliente().getDniCliente())));

        clientTable.addCell(new Cell().add(new Paragraph("Fecha:").setBold()));

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = factura.getFechaVenta().format(formatoFecha);
        clientTable.addCell(new Cell().add(new Paragraph(factura.getFechaVenta().format(formatoFecha))));

        document.add(clientTable);
        document.add(new Paragraph("\n"));
    }

    private void addProductsTable(Document document, Venta factura) {
        // Tabla de productos
        float[] columnWidths = {1, 3, 1, 1, 1};
        Table productsTable = new Table(columnWidths);

        // Encabezados de la tabla
        productsTable.addHeaderCell(new Cell().add(new Paragraph("Código").setBold()));
        productsTable.addHeaderCell(new Cell().add(new Paragraph("Nombre Producto").setBold()));
        productsTable.addHeaderCell(new Cell().add(new Paragraph("Cantidad").setBold()));
        productsTable.addHeaderCell(new Cell().add(new Paragraph("P. Unit").setBold()));
        productsTable.addHeaderCell(new Cell().add(new Paragraph("Total").setBold()));

        // Datos de ejemplo (puedes obtenerlos de una base de datos)
        for (VentaDetallada detalle : factura.getVentaDetalladaList()) {
            addProductRow(productsTable, detalle.getIdProducto().getCodigoBarra(), detalle.getIdProducto().getNombreProducto(), detalle.getCantidadVendida(), detalle.getPrecioProducto());
        }

        document.add(productsTable);
    }

    private void addProductRow(Table table, String code, String nombreProducto, BigDecimal quantity, BigDecimal price) {
        table.addCell(new Cell().add(new Paragraph(code)));
        table.addCell(new Cell().add(new Paragraph(nombreProducto)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(quantity))).setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(new Paragraph(String.format("$ %.2f", price))).setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(new Paragraph(String.format("$ %.2f", quantity.multiply(price)))).setTextAlignment(TextAlignment.RIGHT));
    }

    private void addTotalsSection(Document document, Venta factura) {
        float[] columnWidths = {4, 1, 1};
        Table totalsTable = new Table(columnWidths);

        BigDecimal total = factura.getTotalVenta();
        BigDecimal igvRate = new BigDecimal("0.19"); // Tasa de IGV (19%)
        BigDecimal subtotal = total.divide(igvRate.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
        BigDecimal igv = total.subtract(subtotal);

        // Subtotal
        totalsTable.addCell(new Cell(1, 2).add(new Paragraph("")));
        totalsTable.addCell(new Cell().add(new Paragraph("Subtotal:").setBold()).setTextAlignment(TextAlignment.RIGHT));
        totalsTable.addCell(new Cell().add(new Paragraph(String.format("$ %.2f", subtotal))).setTextAlignment(TextAlignment.RIGHT));

        // IGV (19%)
        totalsTable.addCell(new Cell(1, 2).add(new Paragraph("")));
        totalsTable.addCell(new Cell().add(new Paragraph("IGV (19%):").setBold()).setTextAlignment(TextAlignment.RIGHT));
        totalsTable.addCell(new Cell().add(new Paragraph(String.format("$ %.2f", igv))).setTextAlignment(TextAlignment.RIGHT));

        // Total
        totalsTable.addCell(new Cell(1, 2).add(new Paragraph("")));
        totalsTable.addCell(new Cell().add(new Paragraph("TOTAL:").setBold()).setTextAlignment(TextAlignment.RIGHT));
        totalsTable.addCell(new Cell().add(new Paragraph(String.format("$ %.2f", total)).setBold()).setTextAlignment(TextAlignment.RIGHT));

        document.add(new Paragraph("\n"));
        document.add(totalsTable);
    }

    private void addFooter(Document document) {
        document.add(new Paragraph("\n\n"));
        document.add(new Paragraph("Gracias por su compra!")
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic());

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Condiciones de pago: 7 días netos")
                .setFontSize(10));

        document.add(new Paragraph("Válido como comprobante de pago")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER));
    }

}
