package logica;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.Categoria;
import logica.Proveedor;
import logica.VentaDetallada;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-08-11T11:34:08")
@StaticMetamodel(Producto.class)
public class Producto_ { 

    public static volatile SingularAttribute<Producto, String> codigoBarra;
    public static volatile SingularAttribute<Producto, String> descripcionProducto;
    public static volatile SingularAttribute<Producto, Proveedor> idProveedor;
    public static volatile SingularAttribute<Producto, Date> fechaCreacion;
    public static volatile SingularAttribute<Producto, BigDecimal> costoProducto;
    public static volatile ListAttribute<Producto, VentaDetallada> ventaDetalladaList;
    public static volatile SingularAttribute<Producto, Integer> id;
    public static volatile SingularAttribute<Producto, Categoria> idCategoria;
    public static volatile SingularAttribute<Producto, BigDecimal> inventario;
    public static volatile SingularAttribute<Producto, String> nombreProducto;

}