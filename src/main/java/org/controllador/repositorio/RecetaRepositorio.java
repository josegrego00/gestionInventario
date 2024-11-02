package org.controllador.repositorio;

import org.controllador.basedatos.CBaseDatos;
import org.modelo.Ingrediente;
import org.modelo.Receta;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecetaRepositorio implements Repositorio<Receta> {
    private Connection conn;
    IngredienteRepositorio ir;

    public RecetaRepositorio() {
        this.conn = CBaseDatos.getConnection();
        ir = new IngredienteRepositorio();
    }

    public boolean validarIngredientes(List<Ingrediente> ingredientes) {
        for (Ingrediente ingrediente : ingredientes) {
            if (ir.buscarPorNombre(ingrediente.getNombre()) == null) {
                System.out.println("Ingrediente no encontrado: " + ingrediente.getNombre());
                return false;  // Retorna false si algún ingrediente no existe
            }
        }
        return true;  // Retorna true si todos los ingredientes existen
    }

    @Override
    public void crear(Receta receta) {
        if (!validarIngredientes(receta.getListaIngrediente())) {
            System.out.println("Error: No se puede crear la receta porque uno o más ingredientes no existen.");
            return;  // Salimos del método si algún ingrediente no existe
        }

        String sql = "INSERT INTO recetanombre (nombre) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, receta.getNombreReceta());
            stmt.executeUpdate();  // Ejecuta la inserción

            System.out.println("Se creo una receta...Puto");
            ResultSet rs = stmt.getGeneratedKeys();
            int recetaId = 0;
            if (rs.next()) {
                recetaId = rs.getInt(1);
            }
            for (Ingrediente ingrediente : receta.getListaIngrediente()) {
                Ingrediente ingredienteExistente = ir.buscarPorNombre(ingrediente.getNombre());
                ingredienteExistente.setCantidad(ingrediente.getCantidad());
                if (ingredienteExistente != null) {
                    // Inserta en recetadetalle usando el ID del ingrediente existente
                    String sqlRelacion = "INSERT INTO recetadetalle (idreceta, idingrediente, cantidadingrediente) VALUES (?, ?, ?)";
                    try (PreparedStatement stmtRelacion = conn.prepareStatement(sqlRelacion)) {
                        stmtRelacion.setInt(1, recetaId);

                        stmtRelacion.setInt(2, ingredienteExistente.getId());
                        stmtRelacion.setDouble(3, ingredienteExistente.getCantidad());
                        stmtRelacion.executeUpdate();             // Insertar la relación
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }else{
                    System.out.println("Se Agrego la Receta sin Ingredientes");
                }


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al crear una nueva receta...");
        }
    }

    @Override
    public Receta buscarPorID(int i) {
        String sql = "SELECT rn.id AS receta_id, rn.nombre AS nombre, i.nombre AS ingrediente_nombre, rd.cantidadingrediente "+
        "FROM recetanombre rn "+
        "INNER JOIN recetadetalle rd ON rn.id = rd.idreceta "+
        "INNER JOIN ingrediente i ON rd.idingrediente = i.id "+
        "WHERE rn.id = ?";
        Receta receta= null;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, i);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (receta == null) {
                    // Solo se crea la receta la primera vez que encontramos un resultado
                    receta = new Receta(rs.getString("nombre"));
                }
                // Crear un ingrediente y añadirlo a la receta
                Ingrediente ingrediente = new Ingrediente(
                        rs.getString("ingrediente_nombre"),
                        rs.getDouble("cantidadingrediente")
                );
                receta.agregarIngrediente(ingrediente);
            }

            if (receta == null) {
                System.out.println("No hay Receta con ese ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error en consultar el Ingrediente");
        }
        return receta;
    }

    @Override
    public List<Receta> listar() {
        List<Receta> listarReceta = new ArrayList<>();
        String sql = "  SELECT rn.id AS receta_id, \n" +
                "               rn.nombre AS nombre_receta, \n" +
                "               i.nombre AS ingrediente_nombre, \n" +
                "               rd.cantidadingrediente \n" +
                "        FROM recetanombre rn\n" +
                "        INNER JOIN recetadetalle rd ON rn.id = rd.idreceta\n" +
                "        INNER JOIN ingrediente i ON rd.idingrediente = i.id\n" +
                "        ORDER BY rn.id;";

        List<Receta> recetas = new ArrayList<>();
        Map<Integer, Receta> recetaMap = new HashMap<>();  // Para evitar duplicados de receta

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int recetaId = rs.getInt("receta_id");

                // Revisar si la receta ya está en el mapa
                Receta receta = recetaMap.get(recetaId);
                if (receta == null) {
                    // Si no está en el mapa, crear la receta y agregarla a la lista y al mapa
                    receta = new Receta(rs.getString("nombre_receta"));
                    recetas.add(receta);
                    recetaMap.put(recetaId, receta);
                }

                // Crear el ingrediente y añadirlo a la receta actual
                Ingrediente ingrediente = new Ingrediente(
                        rs.getString("ingrediente_nombre"),
                        rs.getDouble("cantidadingrediente")
                );
                receta.agregarIngrediente(ingrediente);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar las recetas: " + e.getMessage());
        }

        return recetas;
    }

    @Override
    public void actualizar(Receta receta) {
        String sql = "UPDATE recetanombre SET nombre = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asignar los valores a los parámetros de la consulta SQL
            stmt.setString(1, receta.getNombreReceta());

            int filasActualizadas = stmt.executeUpdate();  // Ejecuta la actualización

            if (filasActualizadas > 0) {
                System.out.println("Nombre Receta actualizado con éxito.");
            } else {
                System.out.println("No se encontró receta con el ID especificado.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());  // Manejo de excepciones
            e.printStackTrace();  // Manejo de excepciones
        }
    }

    @Override
    public void eliminarPorID(int i) {
        String sql = "DELETE FROM recetanombre WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, i);  // Establece el ID de la receta que deseas eliminar
            int filasEliminadas = stmt.executeUpdate();

            if (filasEliminadas > 0) {
                System.out.println("Receta eliminada con éxito.");
            } else {
                System.out.println("No se encontró ninguna receta con ese ID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar la receta: " + e.getMessage());
        }

    }
    public void agregarIngredienteAReceta(int recetaId, Ingrediente ingrediente) {
        // Verificar si el ingrediente existe en la base de datos
        Ingrediente ingredienteExistente = ir.buscarPorNombre(ingrediente.getNombre());

        if (ingredienteExistente == null) {
            System.out.println("Error: El ingrediente " + ingrediente.getNombre() + " no existe en la base de datos.");
            return;
        }

        // Actualizar la cantidad en el ingrediente encontrado
        ingredienteExistente.setCantidad(ingrediente.getCantidad());

        // Insertar el ingrediente en la receta en la tabla recetadetalle
        String sql = "INSERT INTO recetadetalle (idreceta, idingrediente, cantidadingrediente) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, recetaId);
            stmt.setInt(2, ingredienteExistente.getId());
            stmt.setDouble(3, ingredienteExistente.getCantidad());
            stmt.executeUpdate();  // Ejecuta la inserción

            System.out.println("Ingrediente agregado exitosamente a la receta.");
        } catch (SQLException e) {
            System.out.println("Error al agregar ingrediente a la receta: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al agregar el ingrediente a la receta.");
        }
    }



}
