
<%@page import="java.util.List"%>
<%@page import="logica.Categoria"%>
<!-- categorias.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Categorías</title>
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
            <h2 class="mb-3">Categorías</h2>
            <a href="crearCategoria.jsp" class="btn btn-primary mb-3">Agregar Categoría</a>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre Categoria</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%

                        List<Categoria> listaCategoria = (List<Categoria>) request.getAttribute("listaCategoriaExistentes");
                        if (listaCategoria != null && !listaCategoria.isEmpty()) {
                            for (Categoria categoria : listaCategoria) {
                    %>
                    <tr>
                        <td><%= categoria.getId()%></td>
                        <td><%= categoria.getNombreCategoria()%></td>
                        <td>
                            <a href="SVEditarCategoriasPorCodigo?idCategoria=<%= categoria.getId()%>" class="btn btn-warning btn-sm" op>Editar</a>
                            <a href="SVEliminarCategoria?idCategoria=<%= categoria.getId()%>" class="btn btn-danger btn-sm" onclick="return confirmarCancelacion()">Eliminar</a>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="7" class="text-center">No hay Categorias para mostrar.</td>
                    </tr>
                    <%
                        }
                    %>

                </tbody>
            </table>
        </div>
        <!-- ------------------------- JavaScript.--------------------------------- -->                        
        <!-- Esto es una codigo de Java Script con la idea de que cuando se precione el boton de cancelarar arroje un mensaje de advertencia -->
        <script>
            function confirmarCancelacion() {
                return confirm("¿Está seguro que desea Eliminar esta Categoria? ");
            }
        </script>
    </body>
</html>
