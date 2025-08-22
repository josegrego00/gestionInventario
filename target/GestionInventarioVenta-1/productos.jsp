<%@page import="logica.Producto"%>
<%@page import="java.util.List"%>
<!-- productos.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Productos</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="index.jsp" />
        
        <%-- Mensaje de éxito --%>
        <% if (session.getAttribute("mensajeExito") != null) {%>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= session.getAttribute("mensajeExito")%>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("mensajeExito"); // Limpia después de mostrar %>
        <% } %>

        <%-- Mensaje de Error--%>
        <% if (session.getAttribute("mensajeError") != null) {%>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= session.getAttribute("mensajeError")%>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("mensajeError"); // Limpia después de mostrar %>
        <% } %>

        
        
        
        
        <div class="container mt-4">

            
            <h2 class="mb-3">Lista de Productos</h2>
            <a href="SVListarCategoriaYProveedor" class="btn btn-primary mb-3">Agregar Producto</a>
            <form method="GET" action="SVListarProductos" class="row g-3 mb-4">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Codigo de Barra</th>
                            <th>Nombre Producto</th>
                            <th>Categoría</th>
                            <th>Proveedor</th>
                            <th>Precio de Compra</th>
                            <th>Inventario Actual</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Producto> listaProductos = (List<Producto>) request.getAttribute("listaProductosExistentes");
                            if (listaProductos != null && !listaProductos.isEmpty()) {
                                for (Producto producto : listaProductos) {
                        %>
                        <tr>
                            <td><%= producto.getCodigoBarra()%></td>
                            <td><%= producto.getNombreProducto()%></td>
                            <td><%= producto.getIdCategoria().getNombreCategoria()%></td>
                            <td><%= producto.getIdProveedor().getNombreProveedor()%></td>
                            <td><%= producto.getCostoProducto() %></td>
                            <td><%= producto.getInventario()%></td>
                            <td>
                                <a href="SVEditarProductoConCodigoBarra?codigoBarra=<%= producto.getCodigoBarra() %>" class="btn btn-warning btn-sm" op>Editar</a>
                                <a href="SVEliminarProducto?codigoBarra=<%= producto.getCodigoBarra()%>" class="btn btn-danger btn-sm" onclick="return confirmarCancelacion()">Eliminar</a>
                            </td>
                        </tr>
                        <%
                            }
                        } else {
                        %>
                        <tr>
                            <td colspan="7" class="text-center">No hay productos a mostrar.</td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </form>
        </div>
                    
                    <!-- ------------------------- JavaScript.--------------------------------- -->                        
        <!-- Esto es una codigo de Java Script con la idea de que cuando se precione el boton de cancelarar arroje un mensaje de advertencia -->
        <script>
            function confirmarCancelacion() {
                return confirm("¿Está seguro que desea Eliminar este Producto? ");
            }
        </script>
    </body>
</html>