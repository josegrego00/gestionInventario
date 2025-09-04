package logica;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.Proveedor;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-09-04T11:29:04")
@StaticMetamodel(Compra.class)
public class Compra_ { 

    public static volatile SingularAttribute<Compra, BigDecimal> totalCompra;
    public static volatile SingularAttribute<Compra, LocalDateTime> fechaCompra;
    public static volatile SingularAttribute<Compra, Boolean> estadoCompra;
    public static volatile SingularAttribute<Compra, String> descripcionCompra;
    public static volatile SingularAttribute<Compra, Proveedor> idProveedor;
    public static volatile SingularAttribute<Compra, Integer> id;
    public static volatile SingularAttribute<Compra, String> nFactura;

}