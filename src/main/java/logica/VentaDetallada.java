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
@Table(name = "venta_detallada")
@NamedQueries({
    @NamedQuery(name = "VentaDetallada.findAll", query = "SELECT v FROM VentaDetallada v"),
    @NamedQuery(name = "VentaDetallada.findById", query = "SELECT v FROM VentaDetallada v WHERE v.id = :id"),
    @NamedQuery(name = "VentaDetallada.findByCantidadVendida", query = "SELECT v FROM VentaDetallada v WHERE v.cantidadVendida = :cantidadVendida"),
    @NamedQuery(name = "VentaDetallada.findByPrecioProducto", query = "SELECT v FROM VentaDetallada v WHERE v.precioProducto = :precioProducto")})
public class VentaDetallada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_vendida")
    private BigDecimal cantidadVendida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_producto")
    private BigDecimal precioProducto;
    @JoinColumn(name = "id_producto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Producto idProducto;
    @JoinColumn(name = "id_venta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Venta idVenta;

    public VentaDetallada() {
    }

    public VentaDetallada(Integer id) {
        this.id = id;
    }

    public VentaDetallada(Integer id, BigDecimal cantidadVendida, BigDecimal precioProducto) {
        this.id = id;
        this.cantidadVendida = cantidadVendida;
        this.precioProducto = precioProducto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(BigDecimal cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public BigDecimal getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(BigDecimal precioProducto) {
        this.precioProducto = precioProducto;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public Venta getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Venta idVenta) {
        this.idVenta = idVenta;
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
        if (!(object instanceof VentaDetallada)) {
            return false;
        }
        VentaDetallada other = (VentaDetallada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "logica.VentaDetallada[ id=" + id + " ]";
    }
    
}
