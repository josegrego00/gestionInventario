package org.modelo;

import java.util.Objects;

public class ProductoComprado {
    private int id;
    private String nombre;
    private double precio;
    private int calidadDisponible;
    private String categoria;


    public ProductoComprado() {
    }

    public ProductoComprado(String nombre, double precio, int calidadDisponible, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.calidadDisponible = calidadDisponible;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCalidadDisponible() {
        return calidadDisponible;
    }

    public void setCalidadDisponible(int calidadDisponible) {
        this.calidadDisponible = calidadDisponible;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoComprado that = (ProductoComprado) o;
        return id == that.id && Double.compare(precio, that.precio) == 0 && calidadDisponible == that.calidadDisponible && Objects.equals(nombre, that.nombre) && Objects.equals(categoria, that.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, precio, calidadDisponible, categoria);
    }

    @Override
    public String toString() {
        return "Producto. "+"\n"+
                "Id = " + id +"\n"+
                "Nombre = " + nombre + "\n" +
                "Precio = " + precio +"\n"+
                "Cantidad Disponible = " + calidadDisponible +"\n"+
                "Categoria = " + categoria +"\n";


    }
}
