package org.controllador.repositorio;

import org.controllador.basedatos.CBaseDatos;
import org.modelo.ProductoPreparado;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductoPreparadoRepositorio implements Repositorio<ProductoPreparado> {
    private Connection conn;
    private RecetaRepositorio recetaR;

    public ProductoPreparadoRepositorio() {
        this.conn = CBaseDatos.getConnection();
        recetaR= new RecetaRepositorio();
    }

    @Override
    public void crear(ProductoPreparado productoPreparado) {
        if (recetaR.buscarPorID(productoPreparado.getReceta().getId())==null){
            System.out.println("No se puede preparar el producto ya que la receta no existe.. puto");
            return;
        }
        String sql = "INSERT INTO productopreparado (nombre, idreceta) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productoPreparado.getNombre());
            stmt.setDouble(2, productoPreparado.getReceta().getId());

            stmt.executeUpdate();  // Ejecuta la inserción
            System.out.println("Se creo un producto con receta...Puto");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error en Crear un Prodcuto Nuevo...");
        }
    }

    @Override
    public ProductoPreparado buscarPorID(int i) {
        return null;
    }

    @Override
    public List listar() {
        return List.of();
    }

    @Override
    public void actualizar(ProductoPreparado productoPreparado) {

    }

    @Override
    public void eliminarPorID(int i) {

    }
}
