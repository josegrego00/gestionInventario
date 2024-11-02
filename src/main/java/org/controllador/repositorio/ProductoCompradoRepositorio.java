package org.controllador.repositorio;

import org.controllador.basedatos.CBaseDatos;
import org.modelo.ProductoComprado;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoCompradoRepositorio implements Repositorio<ProductoComprado> {

    private Connection conn;

    public ProductoCompradoRepositorio() {
        this.conn = CBaseDatos.getConnection();  // Obtener la conexión al inicializar el repositorio
    }


    @Override
    public void crear(ProductoComprado productoComprado) {
        String sql = "INSERT INTO productocomprado (nombre, precio, cantidaddisponible, categoria) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productoComprado.getNombre());
            stmt.setDouble(2, productoComprado.getPrecio());
            stmt.setDouble(3, productoComprado.getCalidadDisponible());
            stmt.setString(4, productoComprado.getCategoria());

            stmt.executeUpdate();  // Ejecuta la inserción
            System.out.println("Se creo un producto...Puto");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error en Crear un Prodcuto Nuevo...");
        }
    }

    @Override
    public ProductoComprado buscarPorID(int i) {
        String sql = "SELECT * FROM productocomprado WHERE id =?";
        ProductoComprado productocomprado = null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, i);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                productocomprado = mapearProductoComprado(rs);
            } else {
                System.out.println("no hay producto con ese ID");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en consultar el Producto");
        }
        return productocomprado;
    }

    @Override
    public List<ProductoComprado> listar() {
        List<ProductoComprado> listaProductosComprados = new ArrayList<>();
        String sql = "SELECT * FROM productocomprado";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Recorrer el resultado y agregar cada producto a la lista
            while (rs.next()) {

                listaProductosComprados.add(mapearProductoComprado(rs));
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de excepciones
        }

        return listaProductosComprados;
    }

    @Override
    public void actualizar(ProductoComprado productoComprado) {
        String sql = "UPDATE productocomprado SET nombre = ?, precio = ?, cantidaddisponible = ?, categoria = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asignar los valores a los parámetros de la consulta SQL
            stmt.setString(1, productoComprado.getNombre());
            stmt.setDouble(2, productoComprado.getPrecio());
            stmt.setInt(3, productoComprado.getCalidadDisponible());
            stmt.setString(4, productoComprado.getCategoria());
            stmt.setInt(5, productoComprado.getId());

            int filasActualizadas = stmt.executeUpdate();  // Ejecuta la actualización

            if (filasActualizadas > 0) {
                System.out.println("Producto actualizado con éxito.");
            } else {
                System.out.println("No se encontró el producto con el ID especificado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo de excepciones
        }

    }

    @Override
    public void eliminarPorID(int i) {
        if (buscarPorID(i) != null) {
            String sql = "DELETE FROM productocomprado WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, i);  // Establecer el ID del producto a eliminar
                int filasEliminadas = stmt.executeUpdate();  // Ejecutar la eliminación
                if (filasEliminadas > 0) {
                    System.out.println("Producto eliminado con éxito.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el producto.");
                e.printStackTrace();
            }
        } else {
            System.out.println("No se puede eliminar, el producto con ID " + i + " no existe.");
        }


    }

    private ProductoComprado mapearProductoComprado(ResultSet rs) throws SQLException {
        ProductoComprado productocomprado = new ProductoComprado();
        productocomprado.setId(rs.getInt("id"));
        productocomprado.setNombre(rs.getString("nombre"));
        productocomprado.setPrecio(rs.getDouble("precio"));
        productocomprado.setCalidadDisponible(rs.getInt("cantidaddisponible"));
        productocomprado.setCategoria(rs.getString("categoria"));
        return productocomprado;
    }

    public void cerrarConexion() {
        CBaseDatos.closeConnection();  // Cierra la conexión cuando ya no la necesites
        System.out.println("Se cerro la Base de Datos...");
    }
}
