
<%@page import="java.util.List"%>
<%@page import="logica.Venta"%>
<!-- ventas.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Ventas</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="index.jsp" />



        <%-- Mensaje de éxito --%>
        <% if (session.getAttribute("mensaje") != null) {%>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= session.getAttribute("mensaje")%>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("mensaje"); // Limpia después de mostrar %>
        <% } %>


        <div class="container mt-4">
            <h2 class="mb-3">Historial de Ventas</h2>
            <a href="SVListarProductosVenta" class="btn btn-primary mb-3">Registrar Venta</a>

            <!-- Botones del filtro.-->
            <form method="GET" action="SVListarVentasReportes" class="row g-3 mb-4">
                <div class="col-md-4">
                    <input type="text" name="cliente" class="form-control" placeholder="Nombre del cliente" 
                           value="<%= request.getParameter("cliente") != null ? request.getParameter("cliente") : ""%>">
                </div>
                <div class="col-md-4">
                    <input type="date" name="fecha" class="form-control"
                           value="<%= request.getParameter("fecha") != null ? request.getParameter("fecha") : ""%>">
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary">Filtrar</button>
                    <a href="SVListarVentasReportes" class="btn btn-secondary">Limpiar</a>
                </div>
            </form>



            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Fecha</th>
                        <th>Nombre Cliente</th>
                        <th>Total</th>
                        <th>Tipo Pago</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Venta> listaVentas = (List<Venta>) request.getAttribute("listaVentasExistentes");
                        if (listaVentas != null && !listaVentas.isEmpty()) {
                            for (Venta venta : listaVentas) {
                    %>
                    <tr  class="<%= venta.getEstadoFactura() ? "" : "table-danger" %>">
                        <td><%= venta.getId()%></td>
                        <td>
                            <%
                                //Esto es para Agrear la fecha con un formato mas agradable a la vista
                                String fechaOriginal = venta.getFechaVenta().toString();
                                String fechaFormateada = fechaOriginal.replace("T", " ");
                                // Si tiene milisegundos, los removemos
                                if (fechaFormateada.contains(".")) {
                                    fechaFormateada = fechaFormateada.substring(0, fechaFormateada.indexOf("."));
                                }
                            %>
                            <%= fechaFormateada%>
                        </td>
                        <td><%= venta.getDniCliente().getNombreCliente()%></td>
                        <td><%= venta.getTotalVenta()%></td>
                        <td><%= venta.getTipoPago()%></td>
                        <td><%=venta.getEstadoFactura() ? "Activo" : "Anulada"%></td>
                        <td>
                            <a href="SVGenerarFacturaPDF?idFactura=<%= venta.getId()%>" class="btn btn-warning btn-sm" op>Generar PDF</a>
                            <form action="SVAnularVenta" method="POST" style="display: inline;">
                                <input type="hidden" name="idFactura" value="<%= venta.getId()%>">
                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirmarCancelacion()">Anular Factura</button>
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
                    <%
                        }
                    %></tbody>
            </table>

            <!--Ajuste con Botones para que se pasen con paginacion -->

            <!--Ajuste con Botones para que se pasen con paginacion -->
            <div class="d-flex justify-content-center mt-4">
                <nav aria-label="Page navigation">
                    <ul class="pagination">

                        <%
                            int paginaActual = (int) request.getAttribute("paginaActual");
                            int totalPaginas = (int) request.getAttribute("totalPaginas");
                            String clienteParam = request.getParameter("cliente") != null ? "&cliente=" + request.getParameter("cliente") : "";
                            String fechaParam = request.getParameter("fecha") != null ? "&fecha=" + request.getParameter("fecha") : "";
                            String extraParams = clienteParam + fechaParam;
                        %>

                        <!-- Botón Anterior -->
                        <li class="page-item <%= (paginaActual == 1) ? "disabled" : ""%>">
                            <a class="page-link" 
                               href="SVListarVentasReportes?page=<%= paginaActual - 1%><%= extraParams%>">
                                Anterior
                            </a>
                        </li>

                        <!-- Números de página -->
                        <%
                            for (int i = 1; i <= totalPaginas; i++) {
                        %>
                        <li class="page-item <%= (i == paginaActual) ? "active" : ""%>">
                            <a class="page-link" href="SVListarVentasReportes?page=<%= i%><%= extraParams%>">
                                <%= i%>
                            </a>
                        </li>
                        <%
                            }
                        %>

                        <!-- Botón Siguiente -->
                        <li class="page-item <%= (paginaActual == totalPaginas) ? "disabled" : ""%>">
                            <a class="page-link" 
                               href="SVListarVentasReportes?page=<%= paginaActual + 1%><%= extraParams%>">
                                Siguiente
                            </a>
                        </li>

                    </ul>
                </nav>
            </div>
            <script>
                function confirmarCancelacion() {
                    return confirm("¿Está seguro que desea Anular esta factura? ");
                }


            </script>        
    </body>
</html>
