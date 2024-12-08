package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class PriorizacionProyectosDAO {
    public static void guardarPriorizacion(int idInscripcionEE, Map<Integer, Integer> proyectosYPrioridades) throws SQLException {
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            try {
                String consulta = "INSERT INTO priorizacionproyectos (idInscripcionEE, idProyectoSS, prioridad) VALUES (?, ?, ?)";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);

                for (Map.Entry<Integer, Integer> entrada : proyectosYPrioridades.entrySet()) {
                    prepararConsulta.setInt(1, idInscripcionEE);
                    prepararConsulta.setInt(2, entrada.getKey()); // idProyectoSS
                    prepararConsulta.setInt(3, entrada.getValue()); // prioridad
                    prepararConsulta.addBatch();
                }
                prepararConsulta.executeBatch();
            } catch (SQLException ex) {
                throw new SQLException("Error al guardar priorización de proyectos", ex);
            } finally {
                conexionBD.close();
            }
        }
    }

}
