package org.controllador.repositorio;

import org.controllador.basedatos.CBaseDatos;
import org.modelo.Ingrediente;
import org.modelo.ProductoComprado;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredienteRepositorio implements  Repositorio<Ingrediente>{
    private Connection conn;

    public IngredienteRepositorio() {
        this.conn = CBaseDatos.getConnection();
    }

    @Override
    public void crear(Ingrediente ingrediente) {
        String sql = "INSERT INTO ingrediente (nombre, precio, cantidad, medida) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ingrediente.getNombre());
            stmt.setDouble(2, ingrediente.getPrecio());
            stmt.setDouble(3, ingrediente.getCantidad());
            stmt.setString(4, ingrediente.getMedida());

            stmt.executeUpdate();  // Ejecuta la inserción
            System.out.println("Se creo un Ingrediente...Puto");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error en Crear un ingrediente Nuevo...");        }
    }

    @Override
    public Ingrediente buscarPorID(int i) {
        String sql = "SELECT * FROM ingrediente WHERE id =?";
        Ingrediente ingrediente= null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, i);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ingrediente = mapearIngrediente(rs);
            } else {
                System.out.println("no hay Ingrediente con ese ID");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en consultar el Ingrediente");
        }
        return ingrediente;
    }

    @Override
    public List<Ingrediente> listar() {
        List<Ingrediente> listarIngredientes = new ArrayList<>();
        String sql = "SELECT * FROM ingrediente";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Recorrer el resultado y agregar cada ingrediente a la lista
            while (rs.next()) {

                listarIngredientes.add(mapearIngrediente(rs));
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de excepciones
        }

        return listarIngredientes;
    }

    @Override
    public void actualizar(Ingrediente ingrediente) {
        String sql = "UPDATE ingrediente SET nombre = ?, precio = ?, cantidad= ?, medida = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asignar los valores a los parámetros de la consulta SQL
            stmt.setString(1, ingrediente.getNombre());
            stmt.setDouble(2, ingrediente.getPrecio());
            stmt.setDouble(3, ingrediente.getCantidad());
            stmt.setString(4, ingrediente.getMedida());
            stmt.setInt(5, ingrediente.getId());

            int filasActualizadas = stmt.executeUpdate();  // Ejecuta la actualización

            if (filasActualizadas > 0) {
                System.out.println("Ingrediente actualizado con éxito.");
            } else {
                System.out.println("No se encontró el Ingrediente con el ID especificado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de excepciones
        }
    }

    @Override
    public void eliminarPorID(int i) {
        if (buscarPorID(i) != null) {
            String sql = "DELETE FROM ingrediente WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, i);  // Establecer el ID del producto a eliminar
                int filasEliminadas = stmt.executeUpdate();  // Ejecutar la eliminación
                if (filasEliminadas > 0) {
                    System.out.println("Ingrediente eliminado con éxito.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el Ingrediente.");
                e.printStackTrace();
            }
        } else {
            System.out.println("No se puede eliminar, el INgrediente con ID " + i + " no existe.");
        }


    }
    private Ingrediente mapearIngrediente(ResultSet rs) throws SQLException {
        Ingrediente ingrediente= new Ingrediente();
        ingrediente.setId(rs.getInt("id"));
        ingrediente.setNombre(rs.getString("nombre"));
        ingrediente.setPrecio(rs.getDouble("precio"));
        ingrediente.setCantidad(rs.getDouble("cantidad"));
        ingrediente.setMedida(rs.getString("medida"));
        return ingrediente;
    }


    public Ingrediente buscarPorNombre(String nombreIngrediente) {
        String sql = "SELECT * FROM ingrediente WHERE nombre =?";
        Ingrediente ingrediente= null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreIngrediente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ingrediente = mapearIngrediente(rs);
            } else {
                System.out.println("no hay Ingrediente con ese nombre");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en consultar el Ingrediente");
        }
        return ingrediente;
    }
    public void cerrarConexion() {
        CBaseDatos.closeConnection();  // Cierra la conexión cuando ya no la necesites
        System.out.println("Se cerro la Base de Datos...");
    }


}
