package org.modelo;

public class Ingrediente {

    private int id;
    private String nombre;
    private double precio;
    private double cantidad;
    private String medida;

    public Ingrediente() {
    }

    public Ingrediente(String nombre, double cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public Ingrediente(String nombre, double precio, double cantidad, String medida) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.medida = medida;
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

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    @Override
    public String toString() {
        return "\tIngrediente: " + nombre + "\n" +
                "\tCantidad: " + cantidad + "\n";

    }
}
