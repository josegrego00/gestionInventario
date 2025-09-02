package logica;

import java.math.BigDecimal;
import com.google.gson.annotations.SerializedName;

public class DetalleCompraDTO {
    
    @SerializedName("productoId")
    private String productoCodigo;  // código de barras en JSON viene como "productoId"

    @SerializedName("cantidad")
    private BigDecimal cantidadComprada;

    @SerializedName("precio")
    private BigDecimal precioProductoComprado;

    // Opcional: si quieres también capturar "nombre" y "subtotal"
    private String nombre;
    private BigDecimal subtotal;

    // Getters y setters
    public String getProductoCodigo() {
        return productoCodigo;
    }
    public void setProductoCodigo(String productoCodigo) {
        this.productoCodigo = productoCodigo;
    }

    public BigDecimal getCantidadComprada() {
        return cantidadComprada;
    }
    public void setCantidadComprada(BigDecimal cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    public BigDecimal getPrecioProductoComprado() {
        return precioProductoComprado;
    }
    public void setPrecioProductoComprado(BigDecimal precioProductoComprado) {
        this.precioProductoComprado = precioProductoComprado;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
