package org.controllador;

import org.controllador.repositorio.IngredienteRepositorio;
import org.controllador.repositorio.ProductoCompradoRepositorio;
import org.controllador.repositorio.RecetaRepositorio;
import org.modelo.Ingrediente;
import org.modelo.ProductoComprado;
import org.modelo.Receta;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        RecetaRepositorio rs = new RecetaRepositorio();
        rs.agregarIngredienteAReceta(12, new Ingrediente("matas", 10.5));
        System.out.println(rs.listar());

        /*RecetaRepositorio rs = new RecetaRepositorio();
        rs.eliminarPorID(6);
        System.out.println(rs.listar());
        */

        /*IngredienteRepositorio i = new IngredienteRepositorio();
        /*
        i.crear(new Ingrediente("Cebolla", 4500.0, 12.5, "Vegetal"));
        i.crear(new Ingrediente("Lechuga", 4500.0, 20.0, "Vegetal"));
        i.crear(new Ingrediente("Zanahoria", 4500.0, 1.2, "Vegetal"));

        i.crear(new Ingrediente("Papas", 4500.0, 15.0, "Vegetal"));


       i.eliminarPorID(2);
        System.out.println(i.listar());

        /*
        ProductoCompradoRepositorio o = new ProductoCompradoRepositorio();
        //o.crear(new ProductoComprado("Mouse", 15, 12, "Tecnologia"));
        //System.out.println(o.buscarPorID(3));
        //  System.out.println(o.listar());
       /* ProductoComprado pc = new ProductoComprado("Pc Gamer", 1200, 5, "Tecnologia");
        System.out.println("Buscando producto viejo");
        pc.setId(1);
        System.out.println(o.buscarPorID(1));
        o.actualizar(pc);
        System.out.println("Buscando producto modificado");
        System.out.println(o.buscarPorID(1));

        o.eliminarPorID(2);
        System.out.println(o.listar());
        o.cerrarConexion();
            */

    }
}
