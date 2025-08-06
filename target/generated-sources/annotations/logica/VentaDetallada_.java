package logica;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.Producto;
import logica.Venta;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-08-05T19:52:22")
@StaticMetamodel(VentaDetallada.class)
public class VentaDetallada_ { 

    public static volatile SingularAttribute<VentaDetallada, BigDecimal> cantidadVendida;
    public static volatile SingularAttribute<VentaDetallada, Integer> id;
    public static volatile SingularAttribute<VentaDetallada, Producto> idProducto;
    public static volatile SingularAttribute<VentaDetallada, BigDecimal> precioProducto;
    public static volatile SingularAttribute<VentaDetallada, Venta> idVenta;

}