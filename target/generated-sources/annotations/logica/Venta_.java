package logica;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.Cliente;
import logica.VentaDetallada;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-08-27T11:46:28")
@StaticMetamodel(Venta.class)
public class Venta_ { 

    public static volatile SingularAttribute<Venta, BigDecimal> totalVenta;
    public static volatile SingularAttribute<Venta, Boolean> estadoFactura;
    public static volatile SingularAttribute<Venta, String> tipoPago;
    public static volatile ListAttribute<Venta, VentaDetallada> ventaDetalladaList;
    public static volatile SingularAttribute<Venta, Integer> id;
    public static volatile SingularAttribute<Venta, Cliente> dniCliente;
    public static volatile SingularAttribute<Venta, LocalDateTime> fechaVenta;

}