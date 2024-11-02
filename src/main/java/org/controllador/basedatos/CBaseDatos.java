package org.controllador.basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CBaseDatos {
    private static final String URL = "jdbc:mysql://localhost:3306/gestioninventario"; // Cambia 'nombre_de_tu_base_de_datos' por el nombre real de tu base de datos.
    private static final String USER = "root"; // Cambia 'tu_usuario' por tu nombre de usuario de la base de datos.
    private static final String PASSWORD = "Kristoff_Mora26123009"; // Cambia 'tu_contraseña' por tu contraseña de la base de datos.

    private static Connection connection = null;

    // Método para obtener la conexión
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Establecer la conexión a la base de datos
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo de excepciones, podrías manejarlo mejor en producción.
            }
        }
        return connection;
    }

    // Método para cerrar la conexión
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }
}
