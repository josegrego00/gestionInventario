<!-- index.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Inventario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="index.jsp">Inventario</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="SVListarProductos">Productos</a></li>
                <li class="nav-item"><a class="nav-link" href="categoria.jsp">Categorías</a></li>
                <li class="nav-item"><a class="nav-link" href="proveedor.jsp">Proveedores</a></li>
                <li class="nav-item"><a class="nav-link" href="ventas.jsp">Ventas</a></li>
                <li class="nav-item"><a class="nav-link" href="compras.jsp">Compras</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container mt-5">
    <h1 class="text-center">Bienvenido al Sistema de Gestión de Inventario</h1>
    <p class="text-center">Utilice el menú para gestionar los recursos</p>
</div>
</body>
</html>
