<%@page import="java.util.List"%>
<%@page import="logica.Compra"%>
<!-- compras.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Compras</title>
        <link 
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" 
            rel="stylesheet">
    </head>
    <body>
        <jsp:include page="index.jsp" />

        <%-- Mensaje de éxito --%>
        <% if (session.getAttribute("mensajeExito") != null) {%>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= session.getAttribute("mensajeExito")%>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("mensajeExito"); %>
        <% }%>

        <div class="container mt-4">
            <h2 class="mb-3">Historial de Compras</h2>
            <a href="SVRealizarCompraNueva" class="btn btn-primary mb-3">Registrar Compra</a>

            <!-- Botones del filtro -->
            <form method="GET" action="SVListarComprasReportes" class="row g-3 mb-4">
                <div class="col-md-4">
                    <input type="text" name="proveedor" class="form-control" 
                           placeholder="Nombre del proveedor" 
                           value="<%= request.getParameter("proveedor") != null ? request.getParameter("proveedor") : ""%>">
                </div>
                <div class="col-md-4">
                    <input type="date" name="fecha" class="form-control"
                           value="<%= request.getParameter("fecha") != null ? request.getParameter("fecha") : ""%>">
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary">Filtrar</button>
                    <a href="SVListarComprasReportes" class="btn btn-secondary">Limpiar</a>
                </div>
            </form>

            <!-- Tabla de compras -->
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Fecha</th>
                        <th>Nombre Proveedor</th>
                        <th>Total</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Compra> listaCompras = (List<Compra>) request.getAttribute("listaComprasExistentes");
                        if (listaCompras != null && !listaCompras.isEmpty()) {
                            for (Compra compra : listaCompras) {
                    %>
                    <tr>
                        <td><%= compra.getNFactura()%></td>
                        <td>
                            <%
                                String fechaOriginal = compra.getFechaCompra().toString();
                                String fechaFormateada = fechaOriginal.replace("T", " ");
                                if (fechaFormateada.contains(".")) {
                                    fechaFormateada = fechaFormateada.substring(0, fechaFormateada.indexOf("."));
                                }
                            %>
                            <%= fechaFormateada%>
                        </td>
                        <td><%= compra.getIdProveedor().getNombreProveedor()%></td>
                        <td><%= compra.getTotalCompra()%></td>

                        <td><%= compra.getEstadoCompra() ? "Activa" : "Anulada"%></td>
                        <td>
                            <a href="SVGenerarFacturaPDF?idFactura=<%= compra.getId()%>" 
                               class="btn btn-warning btn-sm">Generar PDF</a>
                            <form action="SVAnularCompra" method="POST" style="display:inline;">
                                <input type="hidden" name="idFactura" value="<%= compra.getNFactura()%>">
                                <button type="submit" class="btn btn-danger btn-sm" 
                                        onclick="return confirmarCancelacion()">Anular</button>
                            </form>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="7" class="text-center">No hay Facturas a mostrar.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>

            <!-- Paginación -->
            <div class="d-flex justify-content-center mt-4">
                <nav aria-label="Page navigation">
                    <ul class="pagination">
                        <%
                            Integer paginaActualAttr = (Integer) request.getAttribute("paginaActual");
                            Integer totalPaginasAttr = (Integer) request.getAttribute("totalPaginas");

                            int paginaActual = (paginaActualAttr != null) ? paginaActualAttr : 1;
                            int totalPaginas = (totalPaginasAttr != null) ? totalPaginasAttr : 1;

                            String proveedorParam = request.getParameter("proveedor") != null
                                    ? "&proveedor=" + request.getParameter("proveedor") : "";
                            String fechaParam = request.getParameter("fecha") != null
                                    ? "&fecha=" + request.getParameter("fecha") : "";
                            String extraParams = proveedorParam + fechaParam;
                        %>

                        <!-- Botón Anterior -->
                        <li class="page-item <%= (paginaActual == 1) ? "disabled" : ""%>">
                            <a class="page-link" href="SVListarComprasReportes?page=<%= paginaActual - 1%><%= extraParams%>">Anterior</a>
                        </li>

                        <!-- Números de página -->
                        <%
                            for (int i = 1; i <= totalPaginas; i++) {
                        %>
                        <li class="page-item <%= (i == paginaActual) ? "active" : ""%>">
                            <a class="page-link" href="SVListarComprasReportes?page=<%= i%><%= extraParams%>"><%= i%></a>
                        </li>
                        <% }%>

                        <!-- Botón Siguiente -->
                        <li class="page-item <%= (paginaActual == totalPaginas) ? "disabled" : ""%>">
                            <a class="page-link" href="SVListarComprasReportes?page=<%= paginaActual + 1%><%= extraParams%>">Siguiente</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>

        <script>

            function confirmarCancelacion() {
                return confirm("¿Está seguro que desea anular esta Compra?");
            }
        </script>
    </body>
</html>
