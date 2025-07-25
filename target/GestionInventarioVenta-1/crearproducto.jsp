<%@page import="logica.Proveedor"%>
<%@page import="logica.Categoria"%>
<%@page import="java.util.List"%>
<!-- nuevoProducto.jsp -->
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
            <h2 class="mb-4">Agregar Nuevo Producto</h2>
            <form action="SVGuardarProductoNuevo" method="post">
                <div class="mb-3">
                    <label for="codigoBarra" class="form-label">Codigo de Barra</label>
                    <input type="text" class="form-control" id="codigoBarra" name="codigoBarra" required>
                </div>
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre del Producto</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" required>
                </div>
                <div class="mb-3">
                    <label for="descripcion" class="form-label">Descripción</label>
                    <textarea class="form-control" id="descripcion" name="descripcion" rows="3" ></textarea>
                </div>
                <div class="mb-3">
                    <label for="costoCompra" class="form-label">Costo de Compra</label>
                    <input type="number" class="form-control" id="costoCompra" name="costoCompra" step="0.01" required>
                </div>
                <div class="mb-3">
                    <label for="categoria" class="form-label">Categoría</label>
                    <select class="form-select" id="categoria" name="categoriaId" required>
                        <option selected disabled value="">Seleccione una categoría</option>


                        <!-- Aqui coloco la lista de mis Categoria y las imprimo en el Selecet -->
                        <!-- Esto es Solo Codigo JAVA -->
                        <%
                            List<Categoria> listaCategoria = (List<Categoria>) request.getAttribute("categorias");
                            if (listaCategoria != null) {
                                for (Categoria categoria : listaCategoria) {
                        %>
                        <option value="<%= categoria.getId()%>"><%= categoria.getNombreCategoria()%></option>
                        <%
                                }
                            }
                        %>

                    </select>
                </div>
                <div class="mb-3">
                    <label for="proveedor" class="form-label">Proveedor</label>
                    <select class="form-select" id="proveedor" name="proveedorId" required>
                        <option selected disabled value="">Seleccione un proveedor</option>
                       
                        
                        <!-- Aqui coloco mis Proveedores -->
                        <%
                            List<Proveedor> listaProveedor = (List<Proveedor>) request.getAttribute("proveedores");
                            if (listaProveedor != null) {
                                for (Proveedor proveedor : listaProveedor) {
                        %>
                        <option value="<%= proveedor.getId()%>"><%= proveedor.getNombreProveedor()%></option>
                        <%
                                }
                            }
                        %>

                    </select>
                </div>
                <div class="mb-3">
                    <label for="inventario" class="form-label">Inventario Inicial</label>
                    <input type="number" class="form-control" id="inventario" name="inventarioInicial" required>
                </div>
                <button type="submit" class="btn btn-success">Guardar Producto</button>
                <a href="productos.jsp" class="btn btn-secondary">Cancelar</a>
            </form>
        </div>
    </body>
</html>

<!-- Los demás JSPs siguen como antes... -->
