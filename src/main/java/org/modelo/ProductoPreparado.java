package org.modelo;

import java.util.Objects;

public class ProductoPreparado {
    private int id;
    private String nombre;
    private double precio;
    private Receta receta;
    private int cantidadDisponible;
    private String categoria;

    public ProductoPreparado() {
    }

    public ProductoPreparado(int id, String nombre, double precio, Receta receta, int cantidadDisponible, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.receta = receta;
        this.cantidadDisponible = cantidadDisponible;
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

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
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
        ProductoPreparado that = (ProductoPreparado) o;
        return id == that.id && Double.compare(precio, that.precio) == 0 && cantidadDisponible == that.cantidadDisponible && Objects.equals(nombre, that.nombre) && Objects.equals(receta, that.receta) && Objects.equals(categoria, that.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, precio, receta, cantidadDisponible, categoria);
    }

    @Override
    public String toString() {
        return "ProductoPreparado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", receta=" + receta +
                ", cantidadDisponible=" + cantidadDisponible +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
