package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Alumno;
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

	public static Object verificarCredencialesUsuario(String tipoUsuario, 
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
		try {
			Connection conexionBD = ConexionBD.abrirConexion();
			if (conexionBD != null) {
				try {
					String consulta = "SELECT 'alumno' AS tipoUsuario FROM Alumno WHERE correoAlumno = ? "
							+ "UNION "
							+ "SELECT 'profesor' AS tipoUsuario FROM Profesor WHERE correoProfesor = ?";
					PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
					prepararSentencia.setString(1, correo);
					prepararSentencia.setString(2, correo);

					ResultSet resultadoConsulta = prepararSentencia.executeQuery();
					if (resultadoConsulta.next()) {
						tipoUsuario = resultadoConsulta.getString("tipoUsuario");
					}
					conexionBD.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return tipoUsuario;
		} catch (SQLException ex) {
			ex.getMessage();
		}
		return tipoUsuario;
	}

	private static Alumno verificarCredencialesAlumno(String correo, String contrasenia) {
		Alumno alumno = null;
		try {
			Connection conexionBD = ConexionBD.abrirConexion();

			if (conexionBD != null) {
				try {
					String consulta = "SELECT idAlumno, nombreAlumno, apellidoAlumno, "
							+ "correoAlumno, matricula, promedio "
							+ "FROM Alumno WHERE correoAlumno = ? AND contrasenia = ?";
					PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
					prepararSentencia.setString(1, correo);
					prepararSentencia.setString(2, contrasenia);

					ResultSet resultadoConsulta = prepararSentencia.executeQuery();
					if (resultadoConsulta.next()) {
						alumno = serializarAlumno(resultadoConsulta);
					}
					conexionBD.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return alumno;
		} catch (SQLException ex) {
			ex.getMessage();
		}
		return alumno;
	}

	private static Profesor verificarCredencialesProfesor(String correo, String contrasenia) {
		Profesor profesor = null;
		try {
			Connection conexionBD = ConexionBD.abrirConexion();

			if (conexionBD != null) {
				try {
					String consulta = "SELECT idProfesor, nombreProfesor, apellidoProfesor, correoProfesor, clave "
							+ "FROM Profesor WHERE correoProfesor = ? AND contrasenia = ?";
					PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
					prepararSentencia.setString(1, correo);
					prepararSentencia.setString(2, contrasenia);

					ResultSet resultadoConsulta = prepararSentencia.executeQuery();
					if (resultadoConsulta.next()) {
						profesor = serializarProfesor(resultadoConsulta);
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

	private static Alumno serializarAlumno(ResultSet resultadoConsulta) throws SQLException {
		Alumno alumno = new Alumno();
		alumno.setIdAlumno(resultadoConsulta.getInt("idAlumno"));
		alumno.setNombreAlumno(resultadoConsulta.getString("nombre"));
		alumno.setApellidoAlumno(resultadoConsulta.getString("apellido"));
		alumno.setCorreoAlumno(resultadoConsulta.getString("correo"));
		alumno.setMatricula(resultadoConsulta.getString("matricula"));
		alumno.setPromedio(resultadoConsulta.getFloat("promedio"));
		return alumno;
	}

	private static Profesor serializarProfesor(ResultSet resultadoConsulta) throws SQLException {
		Profesor profesor = new Profesor();
		profesor.setIdProfesor(resultadoConsulta.getInt("idProfesor"));
		profesor.setNombreProfesor(resultadoConsulta.getString("nombre"));
		profesor.setApellidoProfesor(resultadoConsulta.getString("apellido"));
		profesor.setCorreoProfesor(resultadoConsulta.getString("correo"));
		profesor.setClave(resultadoConsulta.getString("numeroEmpleado"));
		return profesor;
	}
}
