package logica;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.CompraDetallada;
import logica.Proveedor;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-08-26T11:51:57")
@StaticMetamodel(Compra.class)
public class Compra_ { 

    public static volatile SingularAttribute<Compra, BigDecimal> totalCompra;
    public static volatile SingularAttribute<Compra, Date> fechaCompra;
    public static volatile SingularAttribute<Compra, String> estadoCompra;
    public static volatile SingularAttribute<Compra, String> descripcionCompra;
    public static volatile SingularAttribute<Compra, Proveedor> idProveedor;
    public static volatile ListAttribute<Compra, CompraDetallada> compraDetalladaList;
    public static volatile SingularAttribute<Compra, Integer> id;
    public static volatile SingularAttribute<Compra, String> nFactura;

}