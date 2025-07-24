package logica;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.VentaDetallada;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-07-23T20:46:40")
@StaticMetamodel(Venta.class)
public class Venta_ { 

    public static volatile SingularAttribute<Venta, String> descripcionVenta;
    public static volatile SingularAttribute<Venta, BigDecimal> totalVenta;
    public static volatile ListAttribute<Venta, VentaDetallada> ventaDetalladaList;
    public static volatile SingularAttribute<Venta, Integer> id;
    public static volatile SingularAttribute<Venta, Date> fechaVenta;

}