
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
<div class="container mt-4">
    <h2 class="mb-3">Historial de Ventas</h2>
    <a href="nuevaVenta.jsp" class="btn btn-primary mb-3">Registrar Venta</a>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Fecha</th>
            <th>Producto</th>
            <th>Cantidad</th>
            <th>Total</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="venta" items="${ventas}">
            <tr>
                <td>${venta.id}</td>
                <td>${venta.fecha}</td>
                <td>${venta.producto.nombre}</td>
                <td>${venta.cantidad}</td>
                <td>${venta.total}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
