package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.dao.ProyectoSSDAO;
import gestorproyectos.modelo.dao.ProyectoPPDAO;
import gestorproyectos.modelo.pojo.ProyectoSS;
import gestorproyectos.utilidades.MisUtilidades;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

public class PriorizacionProyectosDAO {

	private static final Logger logger = Logger.getLogger(
			InscripcionEEDAO.class.getName()
	);

	public static boolean validarPriorizacion(Map<Integer, Integer> proyectosYPrioridades) throws SQLException {
		Set<Integer> prioridades = new HashSet<>(proyectosYPrioridades.values());
		if (prioridades.size() != proyectosYPrioridades.size()) {
			throw new SQLException("Detectamos la repetición de un mismo número en 2 o más proyectos. "
					+ "Por favor asigne un número a cada proyecto sin repetir.");
		}

		return true;
	}

	public static void guardarPriorizacionProyectosSS(int idInscripcionEE,
			Map<Integer, Integer> proyectosYPrioridades) throws SQLException {
		validarPriorizacion(proyectosYPrioridades);

		Connection conexionBD = ConexionBD.abrirConexion();
		HashMap<String, Object> respuesta = new HashMap<>();

		if (conexionBD != null) {
			try {
				String consulta = "INSERT INTO priorizacionproyectos (idInscripcionEE, "
						+ "idProyectoSS, prioridad) VALUES (?, ?, ?)";
				PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);

				for (Map.Entry<Integer, Integer> entrada : proyectosYPrioridades.entrySet()) {
					prepararConsulta.setInt(1, idInscripcionEE);
					prepararConsulta.setInt(2, entrada.getKey());
					prepararConsulta.setInt(3, entrada.getValue());
					prepararConsulta.addBatch();
				}

				int[] resultado = prepararConsulta.executeBatch();

				if (resultado.length > 0) {
					respuesta.put("error", false);
					respuesta.put("mensaje", "Se ha guardado tu orden de "
							+ "preferencia para proyecto de Servicio Social. Tu "
							+ "coordinador pronto te asignará tu proyecto.");
				} else {
					respuesta.put("error", true);
					respuesta.put("mensaje", "Lo sentimos, no se pudo guardar "
							+ "la priorización de los proyectos. Intenta nuevamente más tarde.");
				}

			} catch (SQLException ex) {
				respuesta.put("error", true);
				respuesta.put("mensaje", "Error al guardar la priorización de proyectos: "
						+ ex.getMessage());
				logger.log(Level.SEVERE, "Error al guardar priorización de proyectos", ex);
				throw new SQLException("Error al guardar priorización de proyectos", ex);
			} finally {
				conexionBD.close();
			}
		} else {
			respuesta.put("error", true);
			respuesta.put("mensaje", "Lo sentimos, por el momento el servicio "
					+ "no está disponible. Intente más tarde.");
		}
	}

	public static void guardarPriorizacionProyectosPP(int idInscripcionEE,
			Map<Integer, Integer> proyectosYPrioridades) throws SQLException {
		validarPriorizacion(proyectosYPrioridades);

		Connection conexionBD = ConexionBD.abrirConexion();
		HashMap<String, Object> respuesta = new HashMap<>();

		if (conexionBD != null) {
			try {
				String consulta = "INSERT INTO priorizacionproyectos (idInscripcionEE, "
						+ "idProyectoPP, prioridad) VALUES (?, ?, ?)";
				PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);

				for (Map.Entry<Integer, Integer> entrada : proyectosYPrioridades.entrySet()) {
					prepararConsulta.setInt(1, idInscripcionEE);
					prepararConsulta.setInt(2, entrada.getKey());
					prepararConsulta.setInt(3, entrada.getValue());
					prepararConsulta.addBatch();
				}

				int[] resultado = prepararConsulta.executeBatch();

				if (resultado.length > 0) {
					respuesta.put("error", false);
					respuesta.put("mensaje", "Se ha guardado tu orden de "
							+ "preferencia para proyecto de Practica Profesional. Tu coordinador pronto te asignará tu proyecto.");
				} else {
					respuesta.put("error", true);
					respuesta.put("mensaje", "Lo sentimos, no se pudo guardar "
							+ "la priorización de los proyectos. Intenta nuevamente más tarde.");
				}

			} catch (SQLException ex) {
				respuesta.put("error", true);
				respuesta.put("mensaje", "Error al guardar la priorización de proyectos: "
						+ ex.getMessage());
				logger.log(Level.SEVERE, "Error al guardar priorización de proyectos", ex);
				throw new SQLException("Error al guardar priorización de proyectos", ex);
			} finally {
				conexionBD.close();
			}
		} else {
			respuesta.put("error", true);
			respuesta.put("mensaje", "Lo sentimos, por el momento el servicio "
					+ "no está disponible. Intente más tarde.");
		}
	}

	public static List<Map<String, Object>> obtenerPriorizacionDeAlumnoProyectoPP(int idAlumno) throws SQLException {
		List<Map<String, Object>> resultados = new ArrayList<>();
		Connection conexionBD = ConexionBD.abrirConexion();

		if (conexionBD != null) {
			String consulta = "SELECT "
					+ "ss.idProyectoPP, "
					+ "ss.nombreProyecto, "
					+ "ss.cupoProyecto, "
					+ "p.prioridad "
					+ "FROM proyectopp pp "
					+ "INNER JOIN priorizacionproyectos p ON p.idProyectoPP = ss.idProyectoPP "
					+ "INNER JOIN inscripcionee ie ON p.idInscripcionEE = ie.idInscripcionEE "
					+ "WHERE ie.idAlumno = ?";

			try (
					PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta)) {
				prepararSentencia.setInt(1, idAlumno);

				try (ResultSet resultado = prepararSentencia.executeQuery()) {
					while (resultado.next()) {
						Map<String, Object> fila = new HashMap<>();
						fila.put("idProyectoSS", resultado.getInt("idProyectoSS"));
						fila.put("nombreProyecto", resultado.getString("nombreProyecto"));
						fila.put("cupoProyecto", resultado.getInt("cupoProyecto"));
						fila.put("prioridad", resultado.getInt("prioridad"));
						resultados.add(fila);
					}
				}
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, "Error al obtener detalles de priorización de proyectos", ex);
				throw ex;
			} finally {
				ConexionBD.cerrarConexion(conexionBD);
			}
		} else {
			throw new SQLException("No se pudo establecer conexión con la base de datos.");
		}

		return resultados;
	}

	public static void asignarProyectoSS(int idAlumno, int idProyectoSS, int idInscripcionEE) throws SQLException {
		Connection conexionBD = ConexionBD.abrirConexion();

		if (conexionBD != null) {
			try {
				conexionBD.setAutoCommit(false);

				String consultaAsignarProyecto = "UPDATE inscripcionee  " +
                                                                 "SET idProyectoSS = ?, " +
                                                                  "estadoInscripcion = 'En curso' " +
                                                                  "WHERE idInscripcionEE = ?;";

				try (
						PreparedStatement prepararSentencia = conexionBD.prepareStatement(consultaAsignarProyecto)) {
					prepararSentencia.setInt(1, idProyectoSS);
					prepararSentencia.setInt(2, idInscripcionEE);

					int filasAfectadas = prepararSentencia.executeUpdate();

					if (filasAfectadas > 0) {                                                
						conexionBD.commit();
						System.out.println("Proyecto asignado y estado actualizado correctamente.");
					} else {
						System.out.println("No se encontró una inscripción activa para el alumno especificado.");
						conexionBD.rollback();
					}
				}
			} catch (SQLException ex) {
				conexionBD.rollback();
				logger.log(Level.SEVERE, "Error en la asignación del proyecto o actualización del estado", ex);
				throw ex;
			} finally {
				ConexionBD.cerrarConexion(conexionBD);
			}
		} else {
			throw new SQLException("No se pudo establecer conexión con la base de datos.");
		}
	}

	public static void asignarProyectoPP(int idAlumno, int idProyectoPP, int idInscripcionEE) throws SQLException {
		Connection conexionBD = ConexionBD.abrirConexion();

		if (conexionBD != null) {
			try {
				conexionBD.setAutoCommit(false);

				String consultaAsignarProyecto = "UPDATE inscripcionee  " +
                                                                 "SET idProyectoPP = ?, " +
                                                                  "estadoInscripcion = 'En curso' " +
                                                                  "WHERE idInscripcionEE = ?;";

				try (
						PreparedStatement prepararSentencia = conexionBD.prepareStatement(consultaAsignarProyecto)) {
					prepararSentencia.setInt(1, idProyectoPP);
					prepararSentencia.setInt(2, idInscripcionEE);

					int filasAfectadas = prepararSentencia.executeUpdate();

					if (filasAfectadas > 0) {

						conexionBD.commit();
						System.out.println("Proyecto PP asignado y estado actualizado correctamente.");
					} else {
						System.out.println("No se encontró una inscripción activa para el alumno especificado.");
						conexionBD.rollback();
					}
				}
			} catch (SQLException ex) {
				conexionBD.rollback();
				logger.log(Level.SEVERE, "Error en la asignación del proyecto PP o actualización del estado", ex);
				throw ex;
			} finally {
				ConexionBD.cerrarConexion(conexionBD);
			}
		} else {
			throw new SQLException("No se pudo establecer conexión con la base de datos.");
		}
	}

	public static void actualizarEstadoInscripcion(int idInscripcionEE) throws SQLException {
		Connection conexionBD = ConexionBD.abrirConexion();

		if (conexionBD != null) {
			String consulta = "UPDATE inscripcionee "
					+ "SET estadoInscripcion = 'En curso' "
					+ "WHERE idInscripcionEE = ? ";

			try (
					PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta)) {
				prepararSentencia.setInt(1, idInscripcionEE);

				int filasAfectadas = prepararSentencia.executeUpdate();

				if (filasAfectadas > 0) {
					System.out.println("Estado de inscripción actualizado automáticamente a 'En curso'.");
				} else {
					System.out.println("No se encontró una inscripción activa para el alumno especificado.");
					throw new SQLException("No se pudo actualizar el estado de inscripción.");
				}
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, "Error al actualizar el estado de inscripción", ex);
				throw ex;
			} finally {
				ConexionBD.cerrarConexion(conexionBD);
			}
		} else {
			throw new SQLException("No se pudo establecer conexión con la base de datos.");
		}
	}

	public static boolean validarPriorizacionPorAlumno(int idAlumno) throws SQLException {
		Connection conexionBD = ConexionBD.abrirConexion();
		boolean priorizacionHecha = false;

		if (conexionBD != null) {
			String consulta = "SELECT COUNT(*) FROM priorizacionproyectos WHERE idInscripcionEE IN ("
					+ "SELECT idInscripcionEE FROM inscripcionee WHERE idAlumno = ?)";

			try (
					PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta)) {
				prepararSentencia.setInt(1, idAlumno);

				try (ResultSet resultado = prepararSentencia.executeQuery()) {
					if (resultado.next()) {
						int count = resultado.getInt(1);
						priorizacionHecha = count > 0;
					}
				}
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, "Error al validar priorización de proyectos", ex);
				throw ex;
			} finally {
				ConexionBD.cerrarConexion(conexionBD);
			}
		} else {
			throw new SQLException("No se pudo establecer conexión con la base de datos.");
		}

		return priorizacionHecha;
	}

}
