package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.pojo.IUsuario;
import gestorproyectos.modelo.pojo.Profesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Vero
 */
public class UsuarioDAO {

    public static IUsuario verificarCredencialesUsuario(String tipoUsuario,
        String correo, String contrasenia) throws SQLException {
        if (tipoUsuario == null) {
            return null;
        }

        if (tipoUsuario.equals("alumno")) {
            return verificarCredencialesAlumno(correo, contrasenia);
        } else if (tipoUsuario.equals("profesor")) {
            return verificarCredencialesProfesor(correo, contrasenia);
        }
        return null;
    }

    public static String identificarTipoUsuario(String correo) {
        String tipoUsuario = null;
        Connection conexionBD = null;

        try {
            conexionBD = ConexionBD.abrirConexion();
            if (conexionBD != null) {
                System.out.println("Conexión a la base de datos abierta correctamente.");

                String consulta = "SELECT 'alumno' AS tipoUsuario FROM Alumno WHERE correo = ? "
                    + "UNION "
                    + "SELECT 'profesor' AS tipoUsuario FROM Profesor WHERE correo = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setString(1, correo.trim().toLowerCase());
                prepararSentencia.setString(2, correo.trim().toLowerCase());

                ResultSet resultadoConsulta = prepararSentencia.executeQuery();
                if (resultadoConsulta.next()) {
                    tipoUsuario = resultadoConsulta.getString("tipoUsuario");
                    System.out.println("Tipo de usuario encontrado: "
                        + tipoUsuario);
                } else {
                    System.out.println("No se encontró tipo de usuario para el correo: "
                        + correo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conexionBD != null) {
                try {
                    conexionBD.close();
                    System.out.println("Conexión cerrada correctamente.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return tipoUsuario;
    }

    private static Alumno verificarCredencialesAlumno(String correo, String contrasenia) {
        Alumno alumno = null;
        Connection conexionBD = null;

        try {
            conexionBD = ConexionBD.abrirConexion();

            if (conexionBD != null) {
                System.out.println("Conexión a la base de datos abierta correctamente.");

                String consulta = "SELECT idAlumno, nombreAlumno, apellidoAlumno, "
                    + "correo, matricula, telefonoAlumno, correo, "
                    + "promedio, estadoAlumno "
                    + "FROM Alumno "
                    + "WHERE correo = ? "
                    + "AND contrasenia = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setString(1, correo.trim().toLowerCase());
                prepararSentencia.setString(2, contrasenia.trim()); 
                System.out.println("Verificando credenciales para alumno: " + correo + ", Contraseña: " + contrasenia); 

                ResultSet resultadoConsulta = prepararSentencia.executeQuery();
                if (resultadoConsulta.next()) {
                    alumno = AlumnoDAO.serializarAlumno(resultadoConsulta);
                    System.out.println("Credenciales verificadas correctamente para: "
                        + correo);
                } else {
                    System.out.println("No se encontraron credenciales para el correo: "
                        + correo);
                }
            } else {
                System.out.println("Error al abrir la conexión.");
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: "
                + e.getMessage());
        } finally {
            if (conexionBD != null) {
                try {
                    conexionBD.close();
                    System.out.println("Conexión cerrada correctamente.");
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: "
                        + e.getMessage());
                }
            }
        }

        return alumno;
    }

    private static Profesor verificarCredencialesProfesor(String correo, String contrasenia) {
        Profesor profesor = null;
        try {
            Connection conexionBD = ConexionBD.abrirConexion();

            if (conexionBD != null) {
                try {
                    String consulta = "SELECT idProfesor, nombreProfesor, apellidoProfesor, correo, clave "
                        + "FROM Profesor WHERE correo = ? AND contrasenia = ?";
                    PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                    prepararSentencia.setString(1, correo);
                    prepararSentencia.setString(2, contrasenia);

                    ResultSet resultadoConsulta = prepararSentencia.executeQuery();
                    if (resultadoConsulta.next()) {
                        profesor = ProfesorDAO.serializarProfesor(resultadoConsulta);
                    }
                    conexionBD.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return profesor;
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return profesor;
    }
}
