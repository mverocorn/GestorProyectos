package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.utilidades.Validador;
import gestorproyectos.modelo.pojo.ProyectoSS;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProyectoSSDAO {

	private static final Logger logger = Logger.getLogger(
			ProyectoSSDAO.class.getName());

	public static List<ProyectoSS> obtenerProyectosSS() throws SQLException {
		List<ProyectoSS> proyectosSS = null;
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String consulta = "SELECT idProyectoSS, fechaProyecto, nombreProyecto, "
						+ "objetivoProyecto, descripcionProyecto, cupoProyecto, idResponsable "
						+ "FROM proyectoss;";
				PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
				ResultSet resultado = prepararConsulta.executeQuery();
				proyectosSS = new ArrayList<>();
				while (resultado.next()) {
					proyectosSS.add(serializarProyectoSS(resultado));
				}
			} catch (SQLException e) {
				proyectosSS = null;
				e.printStackTrace();
			} finally {
				conexionBD.close();
			}
		}
		return proyectosSS;
	}

	private static ProyectoSS serializarProyectoSS(ResultSet resultado) throws SQLException {
		ProyectoSS proyectoSS = new ProyectoSS();
		proyectoSS.setIdProyectoSS(resultado.getInt("idProyectoSS"));
		proyectoSS.setFechaProyecto(resultado.getString("fechaProyecto"));
		proyectoSS.setNombreProyecto(resultado.getString("nombreProyecto"));
		proyectoSS.setObjetivoProyecto(resultado.getString("objetivoProyecto"));
		proyectoSS.setDescripcionProyecto(resultado.getString("descripcionProyecto"));
		proyectoSS.setCupoProyecto(resultado.getInt("cupoProyecto"));
		proyectoSS.setIdResponsable(resultado.getInt("idResponsable"));
		return proyectoSS;
	}

	public static void validarProyectoSS(ProyectoSS proyecto) {
		if (proyecto == null) {
			throw new IllegalArgumentException("El objeto ProyectoSS no puede ser nulo.");
		}

		Validador.validarTexto(proyecto.getNombreProyecto(), "nombreProyecto", 150);
		Validador.validarTexto(proyecto.getObjetivoProyecto(), "objetivoProyecto", 255);
		Validador.validarTexto(proyecto.getDescripcionProyecto(), "descripcionProyecto", 1000);
	}

	public static boolean existeProyectoSS(String nombre, String objetivo, String descripcion, int idProyectoActual) throws SQLException {
		boolean existe = false;
		String consulta = "SELECT COUNT(*) AS cantidad FROM ProyectoSS WHERE nombreProyecto = ? "
				+ "AND objetivoProyecto = ? AND descripcionProyecto = ? AND idProyectoSS != ?";

		try (Connection conexion = ConexionBD.abrirConexion(); PreparedStatement sentenciaPreparada = conexion.prepareStatement(consulta)) {
			sentenciaPreparada.setString(1, nombre);
			sentenciaPreparada.setString(2, objetivo);
			sentenciaPreparada.setString(3, descripcion);
			sentenciaPreparada.setInt(4, idProyectoActual);

			try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
				if (resultSet.next() && resultSet.getInt("cantidad") > 0) {
					existe = true;
				}
			}
		} catch (SQLTimeoutException ex) {
			logger.log(Level.SEVERE, "La consulta a la base de datos excedió el tiempo límite", ex);
			throw new SQLException("Tiempo de espera agotado al comprobar la existencia del ProyectoSS.", ex);
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Error al comprobar la existencia del ProyectoSS", ex);
			throw new SQLException("Error al comprobar la existencia del ProyectoSS: "
					+ ex.getMessage(), ex);
		}

		if (existe) {
			throw new SQLException("Ya existe un ProyectoSS con el mismo nombre, objetivo y descripción.");
		}

		return existe;
	}

	public static HashMap<String, Object> registrarProyectoSS(ProyectoSS proyecto) throws SQLException {
		HashMap<String, Object> respuesta = new HashMap<>();

		existeProyectoSS(proyecto.getNombreProyecto(), proyecto.getObjetivoProyecto(),
				proyecto.getDescripcionProyecto(), -1);

		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			PreparedStatement prepararSentencia = null;
			int idProyectoGenerado = -1;
			try {
				validarProyectoSS(proyecto);

				String sentencia = "INSERT INTO ProyectoSS (fechaProyecto, nombreProyecto, "
						+ "objetivoProyecto, descripcionProyecto, cupoProyecto, idResponsable) "
						+ "VALUES (?, ?, ?, ?, ?, ?)";

				prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
				prepararSentencia.setString(1, proyecto.getFechaProyecto());
				prepararSentencia.setString(2, proyecto.getNombreProyecto());
				prepararSentencia.setString(3, proyecto.getObjetivoProyecto());
				prepararSentencia.setString(4, proyecto.getDescripcionProyecto());
				prepararSentencia.setInt(5, proyecto.getCupoProyecto());
				prepararSentencia.setInt(6, proyecto.getIdResponsable());

				int filasAfectadas = prepararSentencia.executeUpdate();

				if (filasAfectadas == 1) {
					try (ResultSet rs = prepararSentencia.getGeneratedKeys()) {
						if (rs.next()) {
							idProyectoGenerado = rs.getInt(1);
						}
					}
				}

				respuesta.put("idProyectoSS", idProyectoGenerado);

			} catch (SQLTimeoutException ex) {
				logger.log(Level.SEVERE, "La inserción en la base de datos excedió el tiempo límite", ex);
				throw new SQLException("Tiempo de espera agotado al registrar el Proyecto SS.", ex);
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, "Error al registrar el Proyecto SS", ex);
				throw new SQLException("Error al registrar el Proyecto SS", ex);
			} finally {
				ConexionBD.cerrarConexion(conexionBD);
			}
		}

		return respuesta;
	}

	public static ProyectoSS obtenerProyectoSSPorIdProyectoSS(int idProyectoSS) throws SQLException {
		ProyectoSS proyectoSS = null;
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String consulta = "SELECT p.*, "
						+ "r.nombreResponsable, r.apellidoResponsable, r.correoResponsable, "
						+ "e.nombreEmpresa, e.correoEmpresa " + "FROM proyectoss p "
						+ "JOIN responsable r ON p.idResponsable = r.idResponsable "
						+ "JOIN empresa e ON r.idEmpresa = e.idEmpresa "
						+ "WHERE p.idProyectoSS = ?;";

				PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
				prepararConsulta.setInt(1, idProyectoSS);
				ResultSet resultado = prepararConsulta.executeQuery();

				if (resultado.next()) {
					// Crear el objeto ProyectoSS con los datos básicos
					proyectoSS = new ProyectoSS(
							resultado.getInt("idProyectoSS"),
							resultado.getString("fechaProyecto"),
							resultado.getString("nombreProyecto"),
							resultado.getString("objetivoProyecto"),
							resultado.getString("descripcionProyecto"),
							resultado.getInt("cupoProyecto"),
							resultado.getInt("idResponsable")
					);

					String nombreResponsable = resultado.getString("nombreResponsable");
					String apellidoResponsable = resultado.getString("apellidoResponsable");
					String correoResponsable = resultado.getString("correoResponsable");
					String nombreEmpresa = resultado.getString("nombreEmpresa");
					String correoEmpresa = resultado.getString("correoEmpresa");

					System.out.println("Responsable: " + nombreResponsable + " "
							+ apellidoResponsable + ", Correo: " + correoResponsable);
					System.out.println("Empresa: " + nombreEmpresa
							+ ", Correo: " + correoEmpresa);
				}
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, "Error al obtener ProyectoSS por ID", ex);
				throw new SQLException("Error al obtener ProyectoSS por ID", ex);
			} finally {
				try {
					conexionBD.close();
				} catch (SQLException ex) {
					logger.log(Level.WARNING, "Error al cerrar la conexión", ex);
				}
			}
		}
		return proyectoSS;
	}

	public List<ProyectoSS> obtenerProyectosDisponiblesPorPeriodoDeEESS(String periodo) throws SQLException {
		List<ProyectoSS> proyectosDisponibles = new ArrayList<>();
		Connection conexionBD = ConexionBD.abrirConexion();

		if (conexionBD != null) {
			try {
				String consulta = "SELECT idProyectoSS, nombreProyecto "
						+ "FROM proyectoss WHERE fechaProyecto = ? AND cupoProyecto > 0";

				try (
						PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta)) {
					prepararConsulta.setString(1, periodo);

					try (ResultSet resultado = prepararConsulta.executeQuery()) {
						if (!resultado.isBeforeFirst()) {
							System.out.println("No se encontraron proyectos con cupo disponible para este periodo.");
							return proyectosDisponibles;
						}

						while (resultado.next()) {
							ProyectoSS proyecto = new ProyectoSS(
									resultado.getInt("idProyectoSS"),
									null,
									resultado.getString("nombreProyecto"),
									null, null,
									0, 0
							);
							proyectosDisponibles.add(proyecto);
						}
					}
				}
			} catch (SQLException ex) {
				throw new SQLException("Error al obtener proyectos disponibles por periodo: "
						+ ex.getMessage(), ex);
			} finally {
				ConexionBD.cerrarConexion(conexionBD);
			}
		}
		return proyectosDisponibles;
	}

	public static List<ProyectoSS> obtenerPriorizacionDeAlumnoProyectoSS(int idAlumno) throws SQLException {
		List<ProyectoSS> resultados = new ArrayList<>();
		Connection conexionBD = ConexionBD.abrirConexion();

		if (conexionBD != null) {
			String consulta = "SELECT ss.idProyectoSS, ss.nombreProyecto, ss.cupoProyecto, "
					+ "p.prioridad "
					+ "FROM priorizacionproyectos p "
					+ "INNER JOIN proyectoss ss ON p.idProyectoSS = ss.idProyectoSS "
					+ "INNER JOIN inscripcionee ie ON p.idInscripcionEE = ie.idInscripcionEE "
					+ "WHERE ie.idAlumno = ?";

			try (
					PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta)) {
				prepararSentencia.setInt(1, idAlumno);

				try (ResultSet resultado = prepararSentencia.executeQuery()) {
					while (resultado.next()) {
						ProyectoSS proyecto = new ProyectoSS();
						proyecto.setIdProyectoSS(resultado.getInt("idProyectoSS"));
						proyecto.setNombreProyecto(resultado.getString("nombreProyecto"));
						proyecto.setCupoProyecto(resultado.getInt("cupoProyecto"));
						proyecto.setPrioridad(resultado.getInt("prioridad"));
						resultados.add(proyecto);
					}
				}
			} catch (SQLException ex) {
				Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE,
						"Error al obtener detalles de priorización de proyectos de servicio social.", ex);
				throw new SQLException("Error al realizar la consulta en la base de datos.", ex);
			} finally {
				ConexionBD.cerrarConexion(conexionBD);
			}
		} else {
			throw new SQLException("No se pudo establecer conexión con la base de datos.");
		}

		return resultados;
	}

}
