package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.pojo.InscripcionEE;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InscripcionEEDAO {

    private static final Logger logger = Logger.getLogger(
        InscripcionEEDAO.class.getName()
    );

    public static List<Alumno> obtenerAlumnosAsignados(int idEE) throws SQLException {
        List<Alumno> alumnos = new ArrayList<>();
        HashMap<String, Object> respuesta = new HashMap<>();

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

                if (alumnos.isEmpty()) {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "No se encontraron alumnos asignados para el EE especificado.");
                }

            } catch (SQLException ex) {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Error al obtener los alumnos asignados: "
                    + ex.getMessage());
                logger.log(Level.SEVERE, "Error al obtener los alumnos asignados", ex);
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Lo sentimos, por el momento el servicio no está disponible. Intente más tarde.");
        }

        return alumnos;
    }

    public static boolean registrarInscripcionAlumnoEnEE(int idAlumno, int idEE) throws SQLException {
        boolean exito = false;
        HashMap<String, Object> respuesta = new HashMap<>();

        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "INSERT INTO inscripcionee (fechaInscripcion, estadoInscripcion, idAlumno, idEE) "
                    + "VALUES (NOW(), 'inscrito', ?, ?);";

                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                prepararConsulta.setInt(1, idAlumno);
                prepararConsulta.setInt(2, idEE);
                exito = prepararConsulta.executeUpdate() > 0;

                if (exito) {
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Inscripción registrada con éxito.");
                } else {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "No se pudo registrar la inscripción del alumno.");
                }
            } catch (SQLException ex) {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Error al registrar la inscripción: "
                    + ex.getMessage());
                logger.log(Level.SEVERE, "Error al registrar la inscripción", ex);
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Lo sentimos, por el momento el servicio no está disponible. Intente más       tarde.");
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

    public static List<EE> obtenerEEActivaPorAlumno(int idAlumno) throws SQLException {
		List<EE> experienciasEducativas = new ArrayList<>();
		Connection conexionBD = ConexionBD.abrirConexion();

		if (conexionBD != null) {
			try {
				String consulta = "SELECT e.idEE, e.nombreEE, e.nrc, e.seccion, e.periodo, e.idProfesor "
						+ "FROM ee e "
						+ "JOIN inscripcionee i ON e.idEE = i.idEE "
						+ "WHERE i.idAlumno = ? AND i.estadoInscripcion = 'inscrito';";

				PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
				prepararConsulta.setInt(1, idAlumno);

				try (ResultSet resultado = prepararConsulta.executeQuery()) {
					while (resultado.next()) {
						EE experienciaEducativa = new EE(
								resultado.getInt("idEE"),
								resultado.getString("nombreEE"),
								resultado.getInt("nrc"),
								resultado.getInt("seccion"),
								resultado.getString("periodo"),
								resultado.getInt("idProfesor")
						);
						experienciasEducativas.add(experienciaEducativa);
					}
				}
			} catch (SQLException ex) {
				throw new SQLException("Error al obtener las EE activas del alumno con id: " + idAlumno, ex);
			} finally {
				conexionBD.close();
			}
		} else {
			throw new SQLException("No se pudo establecer la conexión con la base de datos.");
		}

		return experienciasEducativas;
	}

	public static List<EE> obtenerNombreTodasEEPorAlumno(int idAlumno) throws SQLException {
		List<EE> nombresEE = new ArrayList<>();
		Connection conexionBD = ConexionBD.abrirConexion();

		if (conexionBD != null) {
			String consulta = "SELECT ee.idEE, ee.nombreEE, ee.nrc, ee.seccion, ee.periodo, ee.idProfesor "
					+ "FROM inscripcionee "
					+ "JOIN ee ON inscripcionee.idEE = ee.idEE "
					+ "WHERE inscripcionee.idAlumno = ?";

			try (PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta)) {
				prepararConsulta.setInt(1, idAlumno);

				try (ResultSet resultados = prepararConsulta.executeQuery()) {
					while (resultados.next()) {
						EE experienciaEducativa = new EE(
								resultados.getInt("idEE"),
								resultados.getString("nombreEE"),
								resultados.getInt("nrc"),
								resultados.getInt("seccion"),
								resultados.getString("periodo"),
								resultados.getInt("idProfesor")
						);
						nombresEE.add(experienciaEducativa);
					}
				}
			} catch (SQLException ex) {
				throw new SQLException("Error al obtener las EE del alumno con id: " + idAlumno, ex);
			} finally {
				conexionBD.close();
			}
		} else {
			throw new SQLException("No se pudo establecer la conexión con la base de datos.");
		}

		return nombresEE;
	}

    public static List<InscripcionEE> obtenerAlumnosSSParaAsignarProyectoSS() throws SQLException {
        List<InscripcionEE> resultados = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT DISTINCT "
                + "a.idAlumno, "
                + "a.nombreAlumno, "
                + "a.promedio, "
                + "e.idEE, "
                + "e.nombreEE, "
                + "e.seccion "
                + "FROM alumno a "
                + "INNER JOIN inscripcionee i ON a.idAlumno = i.idAlumno "
                + "INNER JOIN ee e ON i.idEE = e.idEE "
                + "INNER JOIN priorizacionproyectos p ON i.idInscripcionEE = p.idInscripcionEE "
                + "WHERE i.estadoInscripcion = 'Inscrito' "
                + "AND e.nombreEE = 'Servicio Social' "
                + "AND (p.idProyectoSS IS NOT NULL OR p.idProyectoPP IS NOT NULL)";

            try (
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery()) {
                while (resultado.next()) {
                    InscripcionEE inscripcion = new InscripcionEE();
                    inscripcion.setIdAlumno(resultado.getInt("idAlumno"));
                    inscripcion.setNombreAlumno(resultado.getString("nombreAlumno"));
                    inscripcion.setPromedio(resultado.getFloat("promedio"));
                    inscripcion.setIdEE(resultado.getInt("idEE"));
                    inscripcion.setNombreEE(resultado.getString("nombreEE"));
                    inscripcion.setSeccion(resultado.getString("seccion"));
                    resultados.add(inscripcion);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al obtener detalles de inscripciones", ex);
                throw ex;
            } finally {
                ConexionBD.cerrarConexion(conexionBD);
            }
        } else {
            throw new SQLException("No se pudo establecer conexión con la base de datos.");
        }

        return resultados;
    }

    public static List<InscripcionEE> obtenerAlumnosPPParaAsignarProyectoPP() throws SQLException {
        List<InscripcionEE> resultados = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT DISTINCT "
                + "a.idAlumno, "
                + "a.nombreAlumno, "
                + "a.promedio, "
                + "e.idEE, "
                + "e.nombreEE, "
                + "e.seccion "
                + "FROM alumno a "
                + "INNER JOIN inscripcionee i ON a.idAlumno = i.idAlumno "
                + "INNER JOIN ee e ON i.idEE = e.idEE "
                + "INNER JOIN priorizacionproyectos p ON i.idInscripcionEE = p.idInscripcionEE "
                + "WHERE i.estadoInscripcion = 'Inscrito' "
                + "AND e.nombreEE = 'Práctica Profesional' "
                + "AND (p.idProyectoSS IS NOT NULL OR p.idProyectoPP IS NOT NULL)";

            try (
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery()) {
                while (resultado.next()) {
                    InscripcionEE inscripcion = new InscripcionEE();
                    inscripcion.setIdAlumno(resultado.getInt("idAlumno"));
                    inscripcion.setNombreAlumno(resultado.getString("nombreAlumno"));
                    inscripcion.setPromedio(resultado.getFloat("promedio"));
                    inscripcion.setIdEE(resultado.getInt("idEE"));
                    inscripcion.setNombreEE(resultado.getString("nombreEE"));
                    inscripcion.setSeccion(resultado.getString("seccion"));
                    resultados.add(inscripcion);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al obtener detalles de inscripciones", ex);
                throw ex;
            } finally {
                ConexionBD.cerrarConexion(conexionBD);
            }
        } else {
            throw new SQLException("No se pudo establecer conexión con la base de datos.");
        }

        return resultados;
    }

    public static HashMap<String, Object> DarDeBaja(int idInscripcionEE) throws SQLException {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sentencia = "DELETE FROM InscripcionEE "
                + "WHERE idInscripcionEE = ?";
            try {
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, idInscripcionEE);
                int filasAfectadas = prepararSentencia.executeUpdate();
                if (filasAfectadas == 1) {
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "El alumno fue dado de baja correctamente");
                } else {
                    respuesta.put("mensaje", "Lo sentimos, hubo un error al dar de baja "
                        + "al alumno, por favor intenta mas tarde");
                }
                conexionBD.close();
            } catch (SQLException ex) {
                respuesta.put("mensaje", ex.getMessage());
            }

        } else {
            respuesta.put("mensaje", "Lo sentimos, por el momento el servicio "
                + "no está disponible. Intente más tarde.");
        }
        return respuesta;
    }

    public void actualizarEstadoInscripcion() throws SQLException {
        LocalDate fechaActual = LocalDate.now();
        String fechaActualStr = fechaActual.format(DateTimeFormatter.ofPattern("MMM-yyyy")).toUpperCase();

        Connection conexionBD = ConexionBD.abrirConexion();
        
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idEE, periodo FROM EE";
                try (PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                     ResultSet resultado = prepararConsulta.executeQuery()) {
                    
                    while (resultado.next()) {
                        String periodoEE = resultado.getString("periodo");

                        String mesFinal = periodoEE.substring(8, 11); 
                        int anioFinal = Integer.parseInt(periodoEE.substring(12, 16));

                        LocalDate fechaFinal = LocalDate.of(anioFinal, convertirMesANumero(mesFinal), 1)
                                .withDayOfMonth(LocalDate.of(anioFinal, convertirMesANumero(mesFinal), 1).lengthOfMonth());

                        if (!fechaActual.isBefore(fechaFinal)) {
                            // Actualizar el estado de la inscripción a "Finalizado"
                            String updateQuery = "UPDATE inscripcionee SET estadoInscripcion = 'Finalizado' "
                                                 + "WHERE idEE = ? AND estadoInscripcion != 'Finalizado'";
                            try (PreparedStatement preparedStatement = conexionBD.prepareStatement(updateQuery)) {
                                preparedStatement.setInt(1, resultado.getInt("idEE"));
                                preparedStatement.executeUpdate();
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                throw new SQLException("Error al actualizar el estado de inscripción: " + ex.getMessage(), ex);
            } finally {
                ConexionBD.cerrarConexion(conexionBD);
            }
        }
    }

    private int convertirMesANumero(String mes) {
        switch (mes.toUpperCase()) {
            case "ENE": return 1;
            case "FEB": return 2;
            case "MAR": return 3;
            case "ABR": return 4;
            case "MAY": return 5;
            case "JUN": return 6;
            case "JUL": return 7;
            case "AGO": return 8;
            case "SEP": return 9;
            case "OCT": return 10;
            case "NOV": return 11;
            case "DIC": return 12;
            default: throw new IllegalArgumentException("Mes inválido: " + mes);
        }
    }
}
