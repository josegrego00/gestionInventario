package org.controllador.repositorio;

import org.controllador.basedatos.CBaseDatos;
import org.modelo.Ingrediente;
import org.modelo.ProductoComprado;
import org.modelo.ProductoPreparado;
import org.modelo.Receta;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<ProductoPreparado> listarProductosPreparados = new ArrayList<>();
        String sql = """
                SELECT pp.id AS productoPid, 
                       pp.nombre AS nombre, 
                       r.id AS receta_id, 
                       r.nombre AS nombre_receta, 
                       i.nombre AS ingrediente_nombre, 
                       rd.cantidadingrediente 
                FROM productopreparado pp
                INNER JOIN receta r ON pp.idreceta = r.id
                INNER JOIN recetadetalle rd ON r.id = rd.idreceta
                INNER JOIN ingrediente i ON rd.idingrediente = i.id
                ORDER BY pp.id;
                """;

        Map<Integer, ProductoPreparado> productoMap = new HashMap<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int productoId = rs.getInt("productoPid");

                // Verificar si el producto ya está en el mapa
                ProductoPreparado productoPreparado = productoMap.get(productoId);
                if (productoPreparado == null) {
                    // Si no está, crear el producto y agregarlo al mapa
                    productoPreparado = new ProductoPreparado();
                    productoPreparado.setId(productoId);
                    productoPreparado.setNombre(rs.getString("nombre"));

                    // Crear la receta y asociarla al producto
                    Receta receta = new Receta(rs.getString("nombre_receta"));
                    productoPreparado.setReceta(receta);
                    productoMap.put(productoId, productoPreparado);
                    listarProductosPreparados.add(productoPreparado);
                }

                // Agregar el ingrediente a la receta correspondiente
                Ingrediente ingrediente = new Ingrediente(
                        rs.getString("ingrediente_nombre"),
                        rs.getDouble("cantidadingrediente")
                );
                productoPreparado.getReceta().agregarIngrediente(ingrediente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al consultar los productos preparados: " + e.getMessage());
        }

        return listarProductosPreparados;
    }


    @Override
    public void actualizar(ProductoPreparado productoPreparado) {

    }

    @Override
    public void eliminarPorID(int i) {
        String sql = "DELETE FROM productopreparado WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, i);  // Establece el ID del producto preparado que deseas eliminar
            int filasEliminadas = stmt.executeUpdate();

            if (filasEliminadas > 0) {
                System.out.println("Producto eliminado con éxito.");
            } else {
                System.out.println("No se encontró ningun producto con ese ID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
        }
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
