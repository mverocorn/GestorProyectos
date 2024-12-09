package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Alumno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InscripcionEEDAO {

    private static final Logger logger = Logger.getLogger(
            InscripcionEEDAO.class.getName()
    );

    public static List<Alumno> obtenerAlumnosAsignados(int idEE) throws SQLException {
        List<Alumno> alumnos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT a.idAlumno, a.nombreAlumno, a.matricula "
                        + "FROM alumno a "
                        + "JOIN inscripcionee i ON a.idAlumno = i.idAlumno "
                        + "WHERE i.idEE = ? AND i.estadoInscripcion = 'inscrito';";

                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                prepararConsulta.setInt(1, idEE);
                ResultSet resultado = prepararConsulta.executeQuery();
                while (resultado.next()) {
                    Alumno alumno = new Alumno();
                    alumno.setIdAlumno(resultado.getInt("idAlumno"));
                    alumno.setNombreAlumno(resultado.getString("nombreAlumno"));
                    alumno.setMatricula(resultado.getString("matricula"));
                    alumnos.add(alumno);
                }
            } finally {
                conexionBD.close();
            }
        }
        return alumnos;
    }

    public static boolean registrarInscripcion(int idAlumno, int idEE) throws SQLException {
        boolean exito = false;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "INSERT INTO inscripcionee (fechaInscripcion, estadoInscripcion, idAlumno, idEE) "
                        + "VALUES (NOW(), 'inscrito', ?, ?);";

                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                prepararConsulta.setInt(1, idAlumno);
                prepararConsulta.setInt(2, idEE);
                exito = prepararConsulta.executeUpdate() > 0;
            } finally {
                conexionBD.close();
            }
        }
        return exito;
    }

    public static int obtenerIdInscripcionEE(int idAlumno) throws SQLException {
        int idInscripcionEE = 0;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idInscripcionEE FROM inscripcionEE WHERE idAlumno = ?";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                prepararConsulta.setInt(1, idAlumno);
                ResultSet resultado = prepararConsulta.executeQuery();
                if (resultado.next()) {
                    idInscripcionEE = resultado.getInt("idInscripcionEE");
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al obtener idInscripcionEE por idAlumno", ex);
                throw new SQLException("Error al obtener idInscripcionEE por idAlumno", ex);
            } finally {
                conexionBD.close();
            }
        }
        return idInscripcionEE;
    }

}
