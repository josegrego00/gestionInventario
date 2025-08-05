<%@page import="logica.Proveedor"%>
<%@page import="logica.Categoria"%>
<%@page import="java.util.List"%>
<!-- editarCategoria.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Agregar Producto</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="index.jsp" />
        <div class="container mt-4">
            <h2 class="mb-4">Editar Categoria</h2>
            <form action="SVActualizarCategoria" method="post">
                <div class="mb-3">
                    <label for="id" class="form-label">ID de la Categoria</label>
                    <input type="text" class="form-control" id="idCategoria" name="idCategoria" value="${categoria.id}" readonly required>
                </div>
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre de la Categoria</label>
                    <input type="text" class="form-control" id="nombreCategoria" name="nombreCategoria" value="${categoria.nombreCategoria}" required>
                </div>
                
                <div class="mb-3">
                    <label for="descripcion" class="form-label">Descripción</label>
                    <textarea class="form-control" id="descripcionCategoria" name="descripcionCategoria" value="${categoria.descripcionCategoria}" rows="5"></textarea>
                </div>
                <button type="submit" class="btn btn-success">Guardar Categoria</button>
                <a href="SVListarCategoriaCrearCategoria" class="btn btn-secondary" onclick="return confirmarCancelacion()">Cancelar</a>
            </form>
        </div>
                        
                        <<!-- ------------------------- JavaScript.--------------------------------- -->                        
        <!-- Esto es una codigo de Java Script con la idea de que cuando se precione el boton de cancelarar arroje un mensaje de advertencia -->
        <script>
            function confirmarCancelacion() {
                return confirm("¿Está seguro que desea cancelar? Se perderán los cambios y volverá al menú principal.");
            }
        </script>

    </body>
</html>

<!-- Los demás JSPs siguen como antes... -->
