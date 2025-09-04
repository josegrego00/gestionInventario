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
        <%-- Mensaje de éxito --%>
        <% if (session.getAttribute("mensajeExito") != null) {%>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= session.getAttribute("mensajeExito")%>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("mensajeExito"); // Limpia después de mostrar %>
        <% } %>

        <%-- Mensaje de Error--%>
        <% if (session.getAttribute("mensajeError") != null) {%>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= session.getAttribute("mensajeError")%>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("mensajeError"); // Limpia después de mostrar %>
        <% } %>


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

                <div class="col-md-4">
                    <label for="numeroFactura" class="form-label">Número de Factura</label>
                    <input 
                        type="text" 
                        id="numeroFactura" 
                        name="numeroFactura" 
                        class="form-control" 
                        maxlength="200" 
                        placeholder="Ej: FAC-00123 o A123B"
                        required>
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

                <div class="col-md-6">
                    <label for="descripcionCompra" class="form-label">Descripción</label>
                    <textarea 
                        id="descripcionCompra" 
                        name="descripcionCompra" 
                        class="form-control" 
                        rows="3" 
                        maxlength="500" 
                        placeholder="Escriba aquí una breve descripción de la compra..."></textarea>
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
                    <button type="submit" class="btn btn-success" onclick="return confirmarVenta()">Guardar Compra</button>
                    <a href="SVListarComprasReportes" class="btn btn-secondary" onclick="return confirmarCancelar()">Cancelar</a>
                </div>

            </form>
        </div>

        <!-- ===================== JS ===================== -->
        <script>

            /* Mantener estas variables globales en tu script */
            const detalleCompra = [];
            let nextItemUid = 1; // contador simple para uid (puede ser Date.now() si prefieres)

            // Agregar producto a la tabla y al array con UID único
            function agregarProductoCompra() {
                mostrarCamposPagoCompra();
                const select = document.getElementById("productoSelect");
                const cantidad = parseInt(document.getElementById("cantidadInput").value);
                const opcion = select.options[select.selectedIndex];
                if (!opcion || !opcion.value || isNaN(cantidad) || cantidad <= 0) {
                    alert("Seleccione un producto y una cantidad válida.");
                    return;
                }

                const id = opcion.value;
                const nombre = opcion.getAttribute("data-nombre");
                const precio = parseFloat(opcion.getAttribute("data-precio")) || 0;
                const subtotal = Math.round((precio * cantidad) * 100) / 100;
                const uid = String(nextItemUid++); // uid único por fila

                // insertar fila en la tabla
                const tabla = document.getElementById("detalleCompraTabla").getElementsByTagName("tbody")[0];
                const fila = tabla.insertRow();
                fila.dataset.uid = uid; // guardamos uid en la fila DOM

                fila.insertCell().textContent = nombre; // col 0
                fila.insertCell().textContent = cantidad; // col 1
                fila.insertCell().textContent = '$' + precio.toFixed(2); // col 2 (precio)
                fila.insertCell().textContent = '$' + subtotal.toFixed(2); // col 3 (subtotal)

                // celda botones (col 4)
                const celdaBoton = fila.insertCell();
                // boton eliminar
                const botonEliminar = document.createElement("button");
                botonEliminar.textContent = "X";
                botonEliminar.className = "btn btn-danger btn-sm";
                botonEliminar.type = "button";
                botonEliminar.onclick = function () {
                    mostrarCamposPagoCompra();
                    const uidFila = fila.dataset.uid;
                    const index = detalleCompra.findIndex(item => item.uid === uidFila);
                    if (index > -1)
                        detalleCompra.splice(index, 1);
                    fila.remove();
                    calcularTotalCompra();
                };
                // boton editar (solo precio)
                const botonEditar = document.createElement("button");
                botonEditar.textContent = "Editar";
                botonEditar.className = "btn btn-warning btn-sm ms-1";
                botonEditar.type = "button";
                botonEditar.onclick = function () {
                    const celdaPrecio = fila.cells[2];
                    const celdaSubtotal = fila.cells[3];
                    // parsear precio actual (quitar símbolos)
                    const precioActual = parseFloat(celdaPrecio.textContent.replace(/[^0-9.\-]+/g, '')) || 0;
                    // crear input para editar precio
                    const inputPrecio = document.createElement("input");
                    inputPrecio.type = "number";
                    inputPrecio.min = "0.01";
                    inputPrecio.step = "0.01";
                    inputPrecio.value = precioActual.toFixed(2);
                    inputPrecio.className = "form-control form-control-sm";
                    // reemplazar celda por input
                    celdaPrecio.textContent = "";
                    celdaPrecio.appendChild(inputPrecio);
                    inputPrecio.focus();
                    inputPrecio.select();
                    function aplicarCambioPrecio() {
                        const nuevoPrecio = parseFloat(inputPrecio.value);
                        if (!isNaN(nuevoPrecio) && nuevoPrecio > 0) {
                            // actualizar DOM
                            celdaPrecio.textContent = '$' + nuevoPrecio.toFixed(2);
                            const cantidadFila = parseInt(fila.cells[1].textContent) || 0;
                            const nuevoSubtotal = Math.round((cantidadFila * nuevoPrecio) * 100) / 100;
                            celdaSubtotal.textContent = '$' + nuevoSubtotal.toFixed(2);
                            // actualizar en detalleCompra por uid
                            const uidFila = fila.dataset.uid;
                            const index = detalleCompra.findIndex(item => item.uid === uidFila);
                            if (index > -1) {
                                detalleCompra[index].precio = nuevoPrecio;
                                detalleCompra[index].subtotal = nuevoSubtotal;
                            }

                            calcularTotalCompra();
                        } else {
                            // inválido -> restaurar precio original
                            celdaPrecio.textContent = '$' + precioActual.toFixed(2);
                        }
                    }

                    // confirmar al perder foco
                    inputPrecio.addEventListener("blur", function () {
                        aplicarCambioPrecio();
                    });
                    // confirmar también con Enter, cancelar con Escape
                    inputPrecio.addEventListener("keydown", function (e) {
                        if (e.key === "Enter") {
                            e.preventDefault();
                            inputPrecio.blur();
                        } else if (e.key === "Escape") {
                            // cancelar
                            celdaPrecio.textContent = '$' + precioActual.toFixed(2);
                        }
                    });
                };
                celdaBoton.appendChild(botonEliminar);
                celdaBoton.appendChild(botonEditar);
                // agregar al array con uid
                detalleCompra.push({
                    uid: uid,
                    productoId: id,
                    nombre: nombre,
                    precio: precio,
                    cantidad: cantidad,
                    subtotal: subtotal
                });
                // limpiar UI
                select.selectedIndex = 0;
                document.getElementById("cantidadInput").value = 1;
                calcularTotalCompra();
            }

            // Suma el total usando los subtotales del array (numérico)
            function calcularTotalCompra() {
                const total = detalleCompra.reduce((sum, item) => sum + (Number(item.subtotal) || 0), 0);
                // redondeo a 2 decimales y mostrar con dos dígitos
                document.getElementById("totalCompra").value = (Math.round(total * 100) / 100).toFixed(2);
            }

            function mostrarCamposPagoCompra() {
                const formaPago = document.getElementById("formaPago").value;
                const efectivo = document.getElementById("grupoMontoEfectivo");
                const transferencia = document.getElementById("grupoMontoTransferencia");
                const grupoCambio = document.getElementById("grupoCambio");
                document.getElementById("montoEfectivo").value = "";
                document.getElementById("montoTransferencia").value = "";
                document.getElementById("cambioCompra").value = "";
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

                const formaPago = document.getElementById("formaPago").value;
                const total = parseFloat(document.getElementById("totalCompra").value) || 0;
                const montoEfectivo = parseFloat(document.getElementById("montoEfectivo")?.value) || 0;
                const montoTransferencia = parseFloat(document.getElementById("montoTransferencia")?.value) || 0;

                // Validaciones según forma de pago
                if (formaPago === "Efectivo") {
                    if (montoEfectivo < total) {
                        alert("El monto en efectivo debe ser igual o mayor al total.");
                        return false;
                    }
                } else if (formaPago === "Transferencia") {
                    if (montoTransferencia < total) {
                        alert("El monto en transferencia debe ser igual o mayor al total.");
                        return false;
                    }
                } else if (formaPago === "Mixto") {
                    const suma = montoEfectivo + montoTransferencia;
                    if (suma < total) {
                        alert("La suma de efectivo y transferencia debe cubrir al menos el total.");
                        return false;
                    }
                } else {
                    alert("Debe seleccionar una forma de pago.");
                    return false;
                }

                // Guardar el detalle como JSON para enviar al servlet
                document.getElementById("detalleJsonCompra").value = JSON.stringify(detalleCompra);
                return true;
            }

            function cambiarProveedor() {
                const form = document.getElementById("formCompra");
                form.action = "SVRealizarCompraNueva"; // mandamos al servlet de recarga
                form.method = "get";
                form.submit();
                // Luego, cuando guardes, el action vuelve a ser SVRegistrarCompraDetalle (definido en el JSP)
            }
            function confirmarVenta() {
                return confirm("¿Desea Confirmar la Compra?");
            }
            function confirmarCancelar() {
                return confirm("¿Desea Salir de la Factura de Compra?, se Perdera Todo");
            }

        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 