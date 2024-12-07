package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.utilidades.Validador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InscripcionProyectoDAO {

    private static final Logger logger = Logger.getLogger(
            InscripcionProyectoDAO.class.getName()
    );

    public static void validarPeriodo(String periodo) {
        if (periodo == null || periodo.trim().isEmpty()) {
            throw new IllegalArgumentException("El periodo no puede estar vacío.");
        }

        Validador.validarTexto(periodo, "periodo", 50);

        Validador.validarPeriodo(periodo);
    }

    public static int registrarPeriodoInscripcionProyecto(int idEE, String periodo) throws SQLException {
        Connection conexionBD = ConexionBD.abrirConexion();
        int idInscripcionGenerada = -1;

        if (conexionBD != null) {
            PreparedStatement prepararSentencia = null;
            try { 
                InscripcionProyectoDAO.validarPeriodo(periodo);

                String sentencia = "INSERT INTO inscripcionproyecto (periodo, idEE) VALUES (?, ?)";
                prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, periodo);
                prepararSentencia.setInt(2, idEE);

                int filasAfectadas = prepararSentencia.executeUpdate();
                if (filasAfectadas == 1) {
                    try (ResultSet rs = prepararSentencia.getGeneratedKeys()) {
                        if (rs.next()) {
                            idInscripcionGenerada = rs.getInt(1); // Obtener el ID generado
                        }
                    }
                }

                if (idInscripcionGenerada <= 0) {
                    throw new SQLException("Error al obtener el ID de inscripcionproyecto registrado.");
                }

            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al registrar el periodo", ex);
                throw new SQLException("Error al registrar el periodo", ex);
            } finally {
                if (prepararSentencia != null) {
                    prepararSentencia.close();
                }
                ConexionBD.cerrarConexion(conexionBD);
            }
        }

        return idInscripcionGenerada;
    }

}
