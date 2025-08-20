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

        <%-- Mensaje de éxito --%>
        <% if (session.getAttribute("mensajeExito") != null) {%>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= session.getAttribute("mensajeExito")%>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("mensajeExito"); // Limpia después de mostrar %>
        <% } %>


        <div class="container mt-4">
            <h2>Registrar Venta</h2>

            <!-- Formulario principal -->
            <form id="formVenta" action="SVRegistrarVentaDetalle" method="post" onsubmit="return prepararDatosParaEnviar();">
                <input type="hidden" name="generarPDF" id="generarPDF">
                <!-- Dentro de tu formulario principal -->
                <input type="hidden" id="idFacturaHidden" name="idFactura">
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

                <!-- Campos ocultos -->
                <input type="hidden" name="detalleJson" id="detalleJson">
                <input type="hidden" name="generarPDF" id="generarPDF">

                <!-- Total -->
                <div class="mb-3">
                    <label>Total: $ </label>
                    <input type="text" id="totalGeneral" name="totalGeneral" class="form-control" readonly>
                </div>

                <!-- Monto efectivo -->
                <div class="mb-3" id="grupoMontoEfectivo" style="display:none;">
                    <label>Monto en Efectivo</label>
                    <input type="number" step="0.01" id="montoEfectivo" name="montoEfectivo" class="form-control" min="0" oninput="calcularCambio()">
                </div>

                <!-- Monto transferencia -->
                <div class="mb-3" id="grupoMontoTransferencia" style="display:none;">
                    <label>Monto en Transferencia</label>
                    <input type="number" step="0.01" id="montoTransferencia" name="montoTransferencia" class="form-control" min="0" oninput="calcularCambio()">
                </div>

                <!-- Forma de pago, aqui se indica como la persona quiere pagar -->
                <select id="formaPago" name="formaPago" class="form-select" onchange="mostrarCamposPago()">
                    <option selected disabled value="">Seleccione</option>
                    <option value="Efectivo">Efectivo</option>
                    <option value="Transferencia">Transferencia</option>
                    <option value="Mixto">Efectivo + Transferencia</option> <!-- Nuevo -->
                </select>

                <!-- Monto entregado -->
                <div class="mb-3" id="grupoMontoEntregado" style="display:none;">
                    <label>Monto Entregado por el Cliente</label>
                    <input type="number" step="0.01" id="montoEntregado" name="montoEntregado" class="form-control" min="0" oninput="calcularCambio()">
                </div>


                <!-- Vuelto / Diferencia -->
                <div class="mb-3" id="grupoCambio" style="display:none;">
                    <label id="labelCambio">Vuelto: $</label>
                    <input type="text" id="cambio" class="form-control" readonly>
                </div>

                <!-- Botones -->
                <div class="d-flex justify-content-between mt-4">
                    <button type="submit" name="accion" value="guardar" class="btn btn-primary" onclick="return confirmarVenta()">
                        <i class="bi bi-save" ></i> Guardar Factura
                    </button>
                    <a href="SVListarVentasReportes" class="btn btn-secondary" onclick="return confirmarCancelar()">
                        <i class="bi bi-x-circle"></i> Cancelar
                    </a>
                </div>
            </form>

        </div>
        <!-- Grupo de botones secundarios -->
    </div>
</div>

<!-- --------------------- JavaScript -------------------- -->
<script>
//Aqui es donde Agrego el detalle de mi venta   
// En este Array
    const detalle = [];

    function agregarProducto() {
        mostrarCamposPago();
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
        mostrarCamposPago();
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

        const formaPago = document.getElementById("formaPago").value;
        const total = parseFloat(document.getElementById("totalGeneral").value) || 0;
        const entregado = parseFloat(document.getElementById("montoEntregado").value) || 0;
        // Validar monto entregado cuando es efectivo
        if (formaPago === "efectivo") {
            if (entregado < total) {
                alert("El monto entregado debe ser igual o mayor al total.");
                return false;
            }
        }


        document.getElementById("detalleJson").value = JSON.stringify(detalle);
        return true;
    }

    function confirmarCancelacion() {
        return confirm("¿Está seguro que desea Eliminar este Producto de la factura?         ");
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

    function mostrarCamposPago() {
        const formaPago = document.getElementById("formaPago").value;
        const efectivo = document.getElementById("grupoMontoEfectivo");
        const transferencia = document.getElementById("grupoMontoTransferencia");
        const grupoCambio = document.getElementById("grupoCambio");
        // Limpiar siempre los valores al cambiar forma de pago
        document.getElementById("montoEfectivo").value = "";
        document.getElementById("montoTransferencia").value = "";
        document.getElementById("cambio").value = "";
        
        if (formaPago === "Efectivo") {
            efectivo.style.display = "block";
            transferencia.style.display = "none";
            grupoCambio.style.display = "block";
            document.getElementById("labelCambio").textContent = "Vuelto: $";
        } else if (formaPago === "Transferencia") {
            efectivo.style.display = "none";
            transferencia.style.display = "block";
            grupoCambio.style.display = "block";
            document.getElementById("labelCambio").textContent = "Diferencia: $";
        } else if (formaPago === "Mixto") { // Nuevo tipo de pago
            efectivo.style.display = "block";
            transferencia.style.display = "block";
            grupoCambio.style.display = "block";
            document.getElementById("labelCambio").textContent = "Vuelto / Diferencia: $";
        } else {
            efectivo.style.display = "none";
            transferencia.style.display = "none";
            grupoCambio.style.display = "none";
        }
    }

    function calcularCambio() {
        const total = parseFloat(document.getElementById("totalGeneral").value) || 0;
        const montoEfectivo = parseFloat(document.getElementById("montoEfectivo")?.value) || 0;
        const montoTransferencia = parseFloat(document.getElementById("montoTransferencia")?.value) || 0;
        const totalPagado = montoEfectivo + montoTransferencia;
        const resultado = totalPagado - total;
        document.getElementById("cambio").value = resultado.toFixed(2);
    }

    function prepararDatosParaEnviar() {
        if (detalle.length === 0) {
            alert("Debe agregar al menos un producto.");
            return false;
        }

        const total = parseFloat(document.getElementById("totalGeneral").value) || 0;
        const montoEfectivo = parseFloat(document.getElementById("montoEfectivo")?.value) || 0;
        const montoTransferencia = parseFloat(document.getElementById("montoTransferencia")?.value) || 0;
        const totalPagado = montoEfectivo + montoTransferencia;
        if (totalPagado < total) {
            alert("El monto total pagado no puede ser menor al total de la venta.");
            return false;
        }

        document.getElementById("detalleJson").value = JSON.stringify(detalle);
        return true;
    }

    function confirmarGuardar(generar) {
        const modal = bootstrap.Modal.getInstance(document.getElementById('modalConfirmarPDF'));
        modal.hide();
        if (generar) {
            const idFactura = document.getElementById('idFacturaHidden').value;
            window.location.href = "SVGenerarFacturaPDF?idFactura=" + idFactura;
        } else {
            // Recargar limpia para nueva venta
            window.location.href = "SVListarProductosVenta";
        }
    }
    function confirmarVenta() {
        return confirm("¿Desea Confirmar la Compra?");
    }
    function confirmarCancelar() {
        return confirm("¿Desea Salir de la Factura?, se Perdera Todo");
    }

</script>

<!-- Bootstrap JS (necesario para modal) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>