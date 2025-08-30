package logica;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.Compra;
import logica.Producto;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-08-29T20:50:47")
@StaticMetamodel(CompraDetallada.class)
public class CompraDetallada_ { 

    public static volatile SingularAttribute<CompraDetallada, BigDecimal> cantidadComprada;
    public static volatile SingularAttribute<CompraDetallada, Integer> id;
    public static volatile SingularAttribute<CompraDetallada, BigDecimal> precioProductoComprado;
    public static volatile SingularAttribute<CompraDetallada, Producto> idProducto;
    public static volatile SingularAttribute<CompraDetallada, Compra> nFactura;

}