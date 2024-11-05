package org.controllador.repositorio;

import org.controllador.basedatos.CBaseDatos;
import org.modelo.ProductoComprado;
import org.modelo.ProductoPreparado;

import javax.swing.*;
import java.sql.*;
import java.util.List;

public class ProductoPreparadoRepositorio implements Repositorio<ProductoPreparado> {
    private Connection conn;
    private RecetaRepositorio recetaR;

    public ProductoPreparadoRepositorio() {
        this.conn = CBaseDatos.getConnection();
        recetaR = new RecetaRepositorio();
    }

    @Override
    public void crear(ProductoPreparado productoPreparado) {
        if (recetaR.buscarPorID(productoPreparado.getReceta().getId()) == null) {
            System.out.println("No se puede preparar el producto ya que la receta no existe.. puto");
            return;
        }
        String sql = "INSERT INTO productopreparado (nombre, idreceta) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productoPreparado.getNombre());
            stmt.setDouble(2, productoPreparado.getReceta().getId());

            stmt.executeUpdate();  // Ejecuta la inserción
            System.out.println("Se creo un producto Preparado con receta...Puto");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Producto ya creado... no puedes crear otro Producto con el mismo nombre... Idiota");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en Crear un Prodcuto Nuevo...");
        }
    }

    @Override
    public ProductoPreparado buscarPorID(int i) {
        String sql = "SELECT * FROM productopreparado WHERE id =?";
        ProductoPreparado productoPreparado = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, i);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                productoPreparado = mapearProductoPreparado(rs);
            } else {
                System.out.println("no hay producto Preparado con ese ID");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en consultar el Producto");
        }
        return productoPreparado;
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

    private ProductoPreparado mapearProductoPreparado(ResultSet rs) throws SQLException {
        ProductoPreparado productoPreparado = new ProductoPreparado();
        productoPreparado.setId(rs.getInt("id"));
        productoPreparado.setNombre(rs.getString("nombre"));
        int recetaId = rs.getInt("idreceta");
        productoPreparado.setReceta(recetaR.buscarPorID(recetaId));
        return productoPreparado;
    }

}
