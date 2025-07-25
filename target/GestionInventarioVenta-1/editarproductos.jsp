<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Producto</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="index.jsp" />

<div class="container mt-4">
    <h2 class="mb-4">Editar Producto</h2>

    <form action="actualizarProducto" method="post">
        <input type="hidden" name="codigoBarra" value="${producto.codigoBarra}" />

        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre del Producto</label>
            <input type="text" class="form-control" id="nombre" name="nombre" value="${producto.nombreProducto}" required>
        </div>

        <div class="mb-3">
            <label for="descripcion" class="form-label">Descripción</label>
            <textarea class="form-control" id="descripcion" name="descripcion" rows="3" required>${producto.descripcionProducto}</textarea>
        </div>

        <div class="mb-3">
            <label for="costoCompra" class="form-label">Costo de Compra</label>
            <input type="number" class="form-control" id="costoCompra" name="costoCompra" step="0.01" value="${producto.costoProducto}" required>
        </div>

        <div class="mb-3">
            <label for="categoria" class="form-label">Categoría</label>
            <select class="form-select" id="categoria" name="categoriaId" required>
                <c:forEach var="cat" items="${categorias}">
                    <option value="${cat.id}" <c:if test="${cat.id == producto.idCategoria.id}">selected</c:if>>${cat.nombreCategoria}</option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label for="proveedor" class="form-label">Proveedor</label>
            <select class="form-select" id="proveedor" name="proveedorId" required>
                <c:forEach var="prov" items="${proveedores}">
                    <option value="${prov.id}" <c:if test="${prov.id == producto.idProveedor.id}">selected</c:if>>${prov.nombreProveedor}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Actualizar Producto</button>
        <a href="productos.jsp" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

</body>
</html>
