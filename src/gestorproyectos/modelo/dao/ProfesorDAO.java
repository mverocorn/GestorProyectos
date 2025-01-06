package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Profesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {

	public static List<Profesor> obtenerProfesores() throws SQLException {
		List<Profesor> profesores = null;
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String consulta = "SELECT idProfesor, nombreProfesor, apellidoProfesor, clave, correo FROM profesor;";
				PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
				ResultSet resultado = prepararConsulta.executeQuery();
				profesores = new ArrayList<>();
				while (resultado.next()) {
					profesores.add(serializarProfesor(resultado));
				}
			} catch (SQLException e) {
				profesores = null;
				e.printStackTrace();
			} finally {
				conexionBD.close();
			}
		}
		return profesores;
	}

	public static Profesor serializarProfesor(ResultSet resultado) throws SQLException {
		Profesor profesor = new Profesor();
		profesor.setIdProfesor(resultado.getInt("idProfesor"));
		profesor.setNombreProfesor(resultado.getString("nombreProfesor"));
		profesor.setApellidoProfesor(resultado.getString("apellidoProfesor"));
		profesor.setClave(resultado.getString("clave"));
		profesor.setCorreo(resultado.getString("correo"));
		return profesor;
	}

}
