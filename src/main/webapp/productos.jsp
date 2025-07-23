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
<div class="container mt-4">
    <h2 class="mb-3">Lista de Productos</h2>
    <a href="SVListarCategoriaYProveedor" class="btn btn-primary mb-3">Agregar Producto</a>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Categoría</th>
            <th>Proveedor</th>
            <th>Precio</th>
            <th>Stock</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="producto" items="${productos}">
            <tr>
                <td>${producto.id}</td>
                <td>${producto.nombre}</td>
                <td>${producto.categoria.nombre}</td>
                <td>${producto.proveedor.nombre}</td>
                <td>${producto.precio}</td>
                <td>${producto.stock}</td>
                <td>
                    <a href="editarproductos.jsp?id=${producto.id}" class="btn btn-warning btn-sm">Editar</a>
                    <a href="eliminarProducto?id=${producto.id}" class="btn btn-danger btn-sm">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>