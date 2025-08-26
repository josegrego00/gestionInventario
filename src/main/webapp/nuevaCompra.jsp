<%@page import="logica.Proveedor"%>
<%@page import="logica.Producto"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registrar Compra</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>

        <jsp:include page="index.jsp" />

        <div class="container mt-4">
            <h2 class="mb-4">Registrar Compra</h2>

            <!-- ===================== FORM PRINCIPAL ===================== -->
            <form id="formCompra" action="SVRegistrarCompraDetalle" method="post" onsubmit="return prepararDatosCompra();">

                <!-- ===================== SELECCIÓN DE PROVEEDOR ===================== -->
                <div class="mb-4">
                    <label class="form-label">Proveedor</label>
                    <select id="proveedorSelect" name="idProveedor" class="form-select w-auto d-inline-block" onchange="cambiarProveedor()">
                        <option value="">Seleccione un proveedor</option>
                        <%
                            List<Proveedor> proveedores = (List<Proveedor>) request.getAttribute("listaProveedoresExistentes");
                            Integer proveedorSeleccionado = (Integer) request.getAttribute("idProveedorSeleccionado");
                            if (proveedores != null) {
                                for (Proveedor pr : proveedores) {
                                    boolean isSelected = (proveedorSeleccionado != null && proveedorSeleccionado.equals(pr.getId()));
                        %>
                        <option value="<%= pr.getId()%>" <%= isSelected ? "selected" : ""%>>
                            <%= pr.getNombreProveedor()%>
                        </option>
                        <%      }
                            }
                        %>
                    </select>
                </div>
                <!-- ===================== PRODUCTOS (solo si ya hay proveedor seleccionado) ===================== -->
                <%
                    List<Producto> productos = (List<Producto>) request.getAttribute("listaProductosExistentes");
                    if (productos != null && !productos.isEmpty()) {
                %>
                <div class="row mb-3">
                    <div class="col-md-5">
                        <label class="form-label">Producto</label>
                        <select id="productoSelect" class="form-select">
                            <option selected disabled value="">Seleccione</option>
                            <%
                                for (Producto p : productos) {
                            %>
                            <option value="<%= p.getCodigoBarra()%>"
                                    data-nombre="<%= p.getNombreProducto()%>"
                                    data-precio="<%= p.getCostoProducto().doubleValue()%>">
                                <%= p.getNombreProducto()%> - Costo: $<%= p.getCostoProducto()%>
                            </option>
                            <% } %>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label">Cantidad</label>
                        <input type="number" id="cantidadInput" class="form-control" min="1" value="1">
                    </div>

                    <div class="col-md-4 d-flex align-items-end">
                        <button type="button" class="btn btn-primary w-100" onclick="agregarProductoCompra()">Agregar al Detalle</button>
                    </div>
                </div>
                <% }%>

                <!-- ===================== TABLA DETALLE ===================== -->
                <div class="table-responsive mb-3">
                    <table class="table table-bordered table-striped text-center" id="detalleCompraTabla">
                        <thead class="table-light">
                            <tr>
                                <th>Producto</th>
                                <th>Cantidad</th>
                                <th>Costo Unitario</th>
                                <th>Subtotal</th>
                                <th>Eliminar</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>

                <!-- Campos ocultos -->
                <input type="hidden" name="detalleJson" id="detalleJsonCompra">

                <!-- ===================== TOTAL ===================== -->
                <div class="mb-3">
                    <label class="form-label">Total: $</label>
                    <input type="text" id="totalCompra" name="totalCompra" class="form-control" readonly>
                </div>

                <!-- ===================== FORMA DE PAGO ===================== -->
                <div class="mb-3">
                    <label class="form-label">Forma de Pago</label>
                    <select id="formaPago" name="formaPago" class="form-select" onchange="mostrarCamposPagoCompra()">
                        <option selected disabled value="">Seleccione</option>
                        <option value="Efectivo">Efectivo</option>
                        <option value="Transferencia">Transferencia</option>
                        <option value="Mixto">Efectivo + Transferencia</option>
                    </select>
                </div>

                <!-- Campos de pago -->
                <div class="mb-3" id="grupoMontoEfectivo" style="display:none;">
                    <label class="form-label">Monto en Efectivo</label>
                    <input type="number" step="0.01" id="montoEfectivo" name="montoEfectivo" class="form-control" min="0" oninput="calcularCambioCompra()">
                </div>

                <div class="mb-3" id="grupoMontoTransferencia" style="display:none;">
                    <label class="form-label">Monto en Transferencia</label>
                    <input type="number" step="0.01" id="montoTransferencia" name="montoTransferencia" class="form-control" min="0" oninput="calcularCambioCompra()">
                </div>

                <div class="mb-3" id="grupoCambio" style="display:none;">
                    <label id="labelCambio" class="form-label">Diferencia: $</label>
                    <input type="text" id="cambioCompra" class="form-control" readonly>
                </div>

                <!-- ===================== BOTONES ===================== -->
                <div class="d-flex justify-content-between mt-4">
                    <button type="submit" class="btn btn-success">Guardar Compra</button>
                    <a href="SVListarComprasReportes" class="btn btn-secondary">Cancelar</a>
                </div>

            </form>
        </div>

        <!-- ===================== JS ===================== -->
        <script>
            const detalleCompra = [];
            function agregarProductoCompra() {
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

                const tabla = document.getElementById("detalleCompraTabla").getElementsByTagName("tbody")[0];
                const fila = tabla.insertRow();
                fila.insertCell().textContent = nombre;
                fila.insertCell().textContent = cantidad;
                fila.insertCell().textContent = '$' + precio.toFixed(2);
                fila.insertCell().textContent = '$' + subtotal.toFixed(2);

                const celdaBoton = fila.insertCell();
                const boton = document.createElement("button");
                boton.textContent = "X";
                boton.className = "btn btn-danger btn-sm";
                boton.type = "button";
                boton.onclick = function () {
                    fila.remove();
                    const index = detalleCompra.findIndex(item => item.productoId === id && item.cantidad === cantidad);
                    if (index > -1)
                        detalleCompra.splice(index, 1);
                    calcularTotalCompra();
                };
                celdaBoton.appendChild(boton);

                detalleCompra.push({productoId: id, nombre, precio, cantidad, subtotal});
                select.selectedIndex = 0;
                document.getElementById("cantidadInput").value = 1;
                calcularTotalCompra();
            }

            function calcularTotalCompra() {
                const total = detalleCompra.reduce((sum, item) => sum + item.subtotal, 0);
                document.getElementById("totalCompra").value = total.toFixed(2);
            }

            function mostrarCamposPagoCompra() {
                const formaPago = document.getElementById("formaPago").value;
                const efectivo = document.getElementById("grupoMontoEfectivo");
                const transferencia = document.getElementById("grupoMontoTransferencia");
                const grupoCambio = document.getElementById("grupoCambio");

                efectivo.style.display = "none";
                transferencia.style.display = "none";
                grupoCambio.style.display = "none";

                if (formaPago === "Efectivo") {
                    efectivo.style.display = "block";
                    grupoCambio.style.display = "block";
                    document.getElementById("labelCambio").textContent = "Vuelto: $";
                } else if (formaPago === "Transferencia") {
                    transferencia.style.display = "block";
                    grupoCambio.style.display = "block";
                    document.getElementById("labelCambio").textContent = "Diferencia: $";
                } else if (formaPago === "Mixto") {
                    efectivo.style.display = "block";
                    transferencia.style.display = "block";
                    grupoCambio.style.display = "block";
                    document.getElementById("labelCambio").textContent = "Vuelto / Diferencia: $";
                }
            }

            function calcularCambioCompra() {
                const total = parseFloat(document.getElementById("totalCompra").value) || 0;
                const montoEfectivo = parseFloat(document.getElementById("montoEfectivo")?.value) || 0;
                const montoTransferencia = parseFloat(document.getElementById("montoTransferencia")?.value) || 0;
                const totalPagado = montoEfectivo + montoTransferencia;
                const resultado = totalPagado - total;
                document.getElementById("cambioCompra").value = resultado.toFixed(2);
            }

            function prepararDatosCompra() {
                if (detalleCompra.length === 0) {
                    alert("Debe agregar al menos un producto.");
                    return false;
                }
                document.getElementById("detalleJsonCompra").value = JSON.stringify(detalleCompra);
                return true;
            }
            function cambiarProveedor() {
                const form = document.getElementById("formCompra");
                form.action = "SVRealizarCompraNueva";  // mandamos al servlet de recarga
                form.method = "get";
                form.submit();

                // Luego, cuando guardes, el action vuelve a ser SVRegistrarCompraDetalle (definido en el JSP)
            }
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 