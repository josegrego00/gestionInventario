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

    public ProductoPreparado(String nombre) {
        this.nombre = nombre;
    }

    public ProductoPreparado(String nombre, Receta receta) {
        this.nombre = nombre;
        this.receta = receta;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Producto Preparado:\n");
        sb.append("Nombre del producto: ").append(nombre).append("\n");
        sb.append("Precio: ").append(precio).append("\n");

        if (receta != null) {
            sb.append("Receta:\n");
            sb.append("   Nombre de la receta: ").append(receta.getNombreReceta()).append("\n");
            sb.append("   Ingredientes:\n");
            sb.append(receta.toString()); // Llama al toString de Receta
        } else {
            sb.append("Receta: No hay receta asignada.\n");
        }

        sb.append("Cantidad Disponible: ").append(cantidadDisponible).append("\n");
        sb.append("Categoría: ").append(categoria != null ? categoria : "Sin categoría").append("\n");

        return sb.toString();
    }
}
