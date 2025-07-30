<%@page import="logica.Cliente"%>
<%@page import="logica.Producto"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registrar Venta</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>

        <jsp:include page="index.jsp" />

        <div class="container mt-4">
            <h2>Registrar Venta</h2>

            <!-- Formulario principal -->
            <form action="SVRegistrarVentaDetalle" method="post" onsubmit="return prepararDatosParaEnviar();">



                <!-- Selección o registro de cliente -->
                <div class="mb-4">
                    <label>Cliente</label>
                    <select id="clienteSelect" name="clienteId" class="form-select" onchange="mostrarFormularioCliente()">
                        <option selected disabled value="">Seleccione un cliente</option>
                        <%
                            List<Cliente> clientes = (List<Cliente>) request.getAttribute("listaClientesExistentes");
                            if (clientes != null) {
                                for (Cliente c : clientes) {
                        %>
                        <option value="<%= c.getDniCliente()%>"><%= c.getNombreCliente()%> - <%= c.getDniCliente()%></option>
                        <% }
                            } %>
                        <option value="nuevo">Nuevo cliente</option>
                    </select>
                </div>

                <!-- Formulario para nuevo cliente -->
                <div id="nuevoClienteForm" style="display:none;" class="border p-3 mb-4">
                    <h5>Nuevo Cliente</h5>
                    <div class="row mb-2">
                        <div class="col-md-6">
                            <label>Nombre</label>
                            <input type="text" name="nuevoNombre" class="form-control">
                        </div>
                        <div class="col-md-6">
                            <label>DNI</label>
                            <input type="text" name="nuevoDni" class="form-control">
                        </div>
                    </div>
                </div>
                <!-- Sección para agregar productos -->
                <div class="row mb-3">
                    <div class="col-md-5">
                        <label>Producto</label>
                        <select id="productoSelect" class="form-select">
                            <option selected disabled value="">Seleccione</option>
                            <%
                                List<Producto> productos = (List<Producto>) request.getAttribute("listaProductosExistentes");
                                if (productos != null) {
                                    for (Producto p : productos) {
                            %>
                            <option value="<%= p.getCodigoBarra()%>" data-nombre="<%= p.getNombreProducto()%>" data-precio="<%= p.getCostoProducto().doubleValue()%>">
                                <%= p.getNombreProducto()%> - Valor - $<%= p.getCostoProducto()%>
                            </option>
                            <% }
                                }%>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label>Cantidad</label>
                        <input type="number" id="cantidadInput" class="form-control" min="1" value="1">
                    </div>

                    <div class="col-md-4 d-flex align-items-end">
                        <button type="button" class="btn btn-primary w-100" onclick="agregarProducto()">Agregar a Detalle</button>
                    </div>
                </div>

                <!-- Tabla de detalles -->
                <table class="table table-bordered" id="detalleTabla">
                    <thead class="table-light">
                        <tr>
                            <th>Producto</th>
                            <th>Cantidad</th>
                            <th>Precio Unitario</th>
                            <th>Subtotal</th>
                            <th>Eliminar</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>

                <!-- Campo para enviar datos serializados -->
                <input type="hidden" name="detalleJson" id="detalleJson">

                <!-- Total -->
                <div class="mb-3">
                    <label>Total: $ </label>
                    <input type="text" id="totalGeneral" name="totalGeneral" class="form-control" readonly>
                </div>

                <!-- Botones -->
                <button type="submit" class="btn btn-success">Registrar Venta</button>
                <a href="ventas.jsp" class="btn btn-secondary">Cancelar</a>
            </form>
        </div>

        <!-- --------------------- JavaScript -------------------- -->
        <script>
            const detalle = [];

            function agregarProducto() {
                const select = document.getElementById("productoSelect");
                const cantidad = parseInt(document.getElementById("cantidadInput").value);

                const opcion = select.options[select.selectedIndex];
                if (!opcion.value || isNaN(cantidad) || cantidad <= 0) {
                    alert("Seleccione un producto y una cantidad válida.");
                    return;
                }

                const id = opcion.value;
                const nombre = opcion.getAttribute("data-nombre");
                const precio = parseFloat(opcion.getAttribute("data-precio"));
                const subtotal = precio * cantidad;

                console.log("Producto agregado:", {id, nombre, precio, cantidad, subtotal});

                // Agregar a tabla visual
                const tabla = document.getElementById("detalleTabla").getElementsByTagName("tbody")[0];
                const fila = tabla.insertRow();
                fila.insertCell().textContent = nombre;
                fila.insertCell().textContent = cantidad;
                fila.insertCell().textContent = '$' + precio.toFixed(2);
                fila.insertCell().textContent = '$' + subtotal.toFixed(2);

                // Botón eliminar
                const celdaBoton = fila.insertCell();
                const boton = document.createElement("button");
                boton.textContent = "X";
                boton.className = "btn btn-danger btn-sm";
                boton.type = "button";
                boton.onclick = function () {
                    eliminarFila(this);
                };
                celdaBoton.appendChild(boton);

                // Guardar en array
                detalle.push({productoId: id, nombre, precio, cantidad, subtotal});

                // Limpiar selección
                document.getElementById("productoSelect").selectedIndex = 0;
                document.getElementById("cantidadInput").value = 1;

                calcularTotal();
            }

            function eliminarFila(btn) {
                if (!confirmarCancelacion()) {
                    return;
                }
                const fila = btn.closest("tr");
                const index = fila.rowIndex - 1;
                fila.remove();
                detalle.splice(index, 1);
                calcularTotal();
            }

            function calcularTotal() {
                const total = detalle.reduce((sum, item) => sum + item.subtotal, 0);
                document.getElementById("totalGeneral").value = total.toFixed(2);
            }

            function prepararDatosParaEnviar() {
                if (detalle.length === 0) {
                    alert("Debe agregar al menos un producto.");
                    return false;
                }

                document.getElementById("detalleJson").value = JSON.stringify(detalle);
                return true;
            }
            function confirmarCancelacion() {
                return confirm("¿Está seguro que desea Eliminar este Producto de la factura? ");
            }

            function mostrarFormularioCliente() {
                const select = document.getElementById("clienteSelect");
                const nuevoForm = document.getElementById("nuevoClienteForm");

                if (select.value === "nuevo") {
                    nuevoForm.style.display = "block";
                } else {
                    nuevoForm.style.display = "none";
                }
            }


        </script>

    </body>
</html>
