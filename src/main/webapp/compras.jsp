<!-- compras.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Compras</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="index.jsp" />
<div class="container mt-4">
    <h2 class="mb-3">Historial de Compras</h2>
    <a href="nuevaCompra.jsp" class="btn btn-primary mb-3">Registrar Compra</a>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Fecha</th>
            <th>Producto</th>
            <th>Cantidad</th>
            <th>Proveedor</th>
            <th>Total</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="compra" items="${compras}">
            <tr>
                <td>${compra.id}</td>
                <td>${compra.fecha}</td>
                <td>${compra.producto.nombre}</td>
                <td>${compra.cantidad}</td>
                <td>${compra.proveedor.nombre}</td>
                <td>${compra.total}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
