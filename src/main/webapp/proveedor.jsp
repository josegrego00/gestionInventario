
<%@page import="logica.Proveedor"%>
<%@page import="java.util.List"%>
<!-- proveedores.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Proveedores</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="index.jsp" />
        <div class="container mt-4">
            <h2 class="mb-3">Proveedores</h2>
            <a href="nuevoProveedor.jsp" class="btn btn-primary mb-3">Agregar Proveedor</a>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre Proveedor</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Proveedor> listaProveedor = (List<Proveedor>) request.getAttribute("listaProveedorExistentes");
                        if (listaProveedor != null && !listaProveedor.isEmpty()) {
                            for (Proveedor proveedor : listaProveedor) {
                    %>
                    <tr>
                        <td><%= proveedor.getId()%></td>
                        <td><%= proveedor.getNombreProveedor()%></td>
                        <td>
                            <a href="SVEditarProveedorPorId?idProveedor=<%= proveedor.getId() %>" class="btn btn-warning btn-sm" op>Editar</a>
                            <a href="SVEliminarProveedor?idProveedor=<%= proveedor.getId() %>" class="btn btn-danger btn-sm" onclick="return confirmarCancelacion()">Eliminar</a>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="7" class="text-center">No hay Proveedor a mostrar.</td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </body>
</html>
