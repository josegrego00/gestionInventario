package org.modelo;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Receta {

    private int id;
    private String nombreReceta;
    private List<Ingrediente> listaIngrediente;

    public Receta() {
        this.listaIngrediente = new ArrayList<>();
    }

    public Receta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
        this.listaIngrediente = new ArrayList<>();
    }

    public List<Ingrediente> getListaIngrediente() {
        return listaIngrediente;
    }


    public void agregarIngrediente(Ingrediente ingrediente) {
        listaIngrediente.add(ingrediente); // Añadimos el ingrediente a la lista
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Receta: ").append(nombreReceta).append("\n");
        sb.append("Ingredientes:\n");

        if (listaIngrediente != null && !listaIngrediente.isEmpty()) {
            for (Ingrediente ingrediente : listaIngrediente) {
                sb.append(ingrediente.toString()); // Llama al toString de Ingrediente
            }
        } else {
            sb.append("No hay ingredientes.\n");
        }

        return sb.toString();
    }

}