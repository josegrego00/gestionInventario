/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author josepino
 */
@Entity
@Table(name = "compra_detallada")
@NamedQueries({
    @NamedQuery(name = "CompraDetallada.findAll", query = "SELECT c FROM CompraDetallada c"),
    @NamedQuery(name = "CompraDetallada.findById", query = "SELECT c FROM CompraDetallada c WHERE c.id = :id"),
    @NamedQuery(name = "CompraDetallada.findByCantidadComprada", query = "SELECT c FROM CompraDetallada c WHERE c.cantidadComprada = :cantidadComprada"),
    @NamedQuery(name = "CompraDetallada.findByPrecioProductoComprado", query = "SELECT c FROM CompraDetallada c WHERE c.precioProductoComprado = :precioProductoComprado")})
public class CompraDetallada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_comprada")
    private BigDecimal cantidadComprada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_producto_comprado")
    private BigDecimal precioProductoComprado;
    @JoinColumn(name = "n_factura", referencedColumnName = "n_factura")
    @ManyToOne(optional = false)
    private Compra nFactura;
    @JoinColumn(name = "id_producto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Producto idProducto;

    public CompraDetallada() {
    }

    public CompraDetallada(Integer id) {
        this.id = id;
    }

    public CompraDetallada(Integer id, BigDecimal cantidadComprada, BigDecimal precioProductoComprado) {
        this.id = id;
        this.cantidadComprada = cantidadComprada;
        this.precioProductoComprado = precioProductoComprado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Compra getNFactura() {
        return nFactura;
    }

    public void setNFactura(Compra nFactura) {
        this.nFactura = nFactura;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompraDetallada)) {
            return false;
        }
        CompraDetallada other = (CompraDetallada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "logica.CompraDetallada[ id=" + id + " ]";
    }
    
}
