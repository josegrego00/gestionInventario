package logica;
import java.math.BigDecimal;

public class DetalleCompraDTO {
    private String productoCodigo;  // código de barras
    private BigDecimal cantidadComprada;
    private BigDecimal precioProductoComprado;

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
}
