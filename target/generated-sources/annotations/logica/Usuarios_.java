package logica;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.Rol;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-08-04T20:37:04")
@StaticMetamodel(Usuarios.class)
public class Usuarios_ { 

    public static volatile SingularAttribute<Usuarios, Integer> idRol;
    public static volatile SingularAttribute<Usuarios, String> contrasenna;
    public static volatile SingularAttribute<Usuarios, String> descripcionUsuario;
    public static volatile SingularAttribute<Usuarios, Integer> id;
    public static volatile SingularAttribute<Usuarios, String> nombreUsuario;
    public static volatile SingularAttribute<Usuarios, Rol> rol;

}