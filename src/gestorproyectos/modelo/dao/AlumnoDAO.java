package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.utilidades.Validador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;

public class AlumnoDAO {

    private static final Logger logger = Logger.getLogger(
        AlumnoDAO.class.getName()
    );

    public static List<Alumno> obtenerAlumnos() throws SQLException {
        List<Alumno> alumnos = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idAlumno, nombreAlumno, apellidoAlumno, matricula, "
                    + "telefonoAlumno, correoAlumno, promedio, estadoAlumno "
                    + "FROM alumno;";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararConsulta.executeQuery();
                alumnos = new ArrayList<>();
                while (resultado.next()) {
                    alumnos.add(serializarAlumno(resultado));
                }
            } catch (SQLException e) {
                alumnos = null;
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return alumnos;
    }

    public static Alumno serializarAlumno(ResultSet resultado) throws SQLException {
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(resultado.getInt("idAlumno"));
        alumno.setNombreAlumno(resultado.getString("nombreAlumno"));
        alumno.setApellidoAlumno(resultado.getString("apellidoAlumno"));
        alumno.setMatricula(resultado.getString("matricula"));
        alumno.setTelefonoAlumno(resultado.getString("telefonoAlumno"));
        alumno.setCorreo(resultado.getString("correoAlumno"));
        alumno.setPromedio(resultado.getFloat("promedio"));
        alumno.setEstadoAlumno(resultado.getString("estadoAlumno"));
        return alumno;
    }

    public static boolean validarAlumnoARegistrar(Alumno alumno) {
        try {
            if (alumno == null) {
                throw new IllegalArgumentException("El objeto Alumno no puede ser nulo.");
            }
            Validador.validarTexto(alumno.getNombreAlumno(), "nombreAlumno", 100);
            Validador.validarTexto(alumno.getApellidoAlumno(), "apellidoAlumno", 100);
            Validador.validarMatricula(alumno.getMatricula());
            if (alumno.getTelefonoAlumno() != null
                && !alumno.getTelefonoAlumno().isEmpty()) {
                Validador.validarTelefono(alumno.getTelefonoAlumno());
            }
            Validador.validarCorreo(alumno.getCorreo());
            Validador.validarPromedio(alumno.getPromedio());
            Validador.validarContrasenia(alumno.getContrasenia());
        } catch (IllegalArgumentException ex) {

        }
        return true;
    }

    public boolean existeAlumno(String correo, String telefono, String matricula, int idAlumnoActual) throws SQLException {
        ConexionBD.verificarConexionBD();

        boolean existe = false;
        String consulta = "SELECT COUNT(*) AS cantidad FROM Alumno WHERE (correoAlumno = ? OR "
            + "telefonoAlumno = ? OR matricula = ?) AND idAlumno != ?";

        try (
            Connection conexion = ConexionBD.abrirConexion();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(consulta)) {
            sentenciaPreparada.setString(1, correo);
            sentenciaPreparada.setString(2, telefono);
            sentenciaPreparada.setString(3, matricula);
            sentenciaPreparada.setInt(4, idAlumnoActual);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next() && resultSet.getInt("cantidad") > 0) {
                    existe = true;
                    throw new SQLException(
                        "Ya existe un alumno registrado con el correo, teléfono o matrícula proporcionados."
                    );
                }
            }
        } catch (SQLTimeoutException ex) {
            logger.log(Level.SEVERE, "La consulta a la base de datos excedió el tiempo límite", ex);
            throw new SQLException("Tiempo de espera agotado al comprobar la existencia del alumno.", ex);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al comprobar la existencia del alumno", ex);
            throw new SQLException("Error al comprobar la existencia del alumno: "
                + ex.getMessage(), ex);
        }

        return existe;
    }

    public static HashMap<String, Object> registrarAlumno(Alumno alumno) throws SQLException {
        HashMap<String, Object> respuesta = new HashMap<>();

        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            PreparedStatement prepararSentencia = null;
            int idAlumnoGenerado = -1;
            try {
                validarAlumnoARegistrar(alumno);

                new AlumnoDAO().existeAlumno(
                    alumno.getCorreo(),
                    alumno.getTelefonoAlumno(),
                    alumno.getMatricula(),
                    -1
                );

                String sentencia = "INSERT INTO alumno (nombreAlumno, apellidoAlumno, matricula, "
                    + "telefonoAlumno, correoAlumno, promedio, estadoAlumno, contraseniaAlumno) "
                    + "VALUES (?, ?, ?, ?, ?, ?, 'inscrito', ?)";
                prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, alumno.getNombreAlumno());
                prepararSentencia.setString(2, alumno.getApellidoAlumno());
                prepararSentencia.setString(3, alumno.getMatricula());
                prepararSentencia.setString(4, alumno.getTelefonoAlumno());
                prepararSentencia.setString(5, alumno.getCorreo());
                prepararSentencia.setFloat(6, alumno.getPromedio());
                prepararSentencia.setString(7, alumno.getContrasenia());

                int filasAfectadas = prepararSentencia.executeUpdate();

                if (filasAfectadas > 0) {
                    try (ResultSet rs = prepararSentencia.getGeneratedKeys()) {
                        if (rs.next()) {
                            idAlumnoGenerado = rs.getInt(1);
                        }
                    }

                    respuesta.put("error", false);
                    respuesta.put("mensaje", "El alumno "
                        + alumno.getNombreAlumno()
                        + " fue registrado correctamente.");
                    respuesta.put("idAlumno", idAlumnoGenerado);
                } else {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "No fue posible registrar al alumno. Intente más tarde.");
                }

            } catch (SQLTimeoutException ex) {
                logger.log(Level.SEVERE, "La inserción en la base de datos excedió el tiempo límite", ex);
                respuesta.put("error", true);
                respuesta.put("mensaje", "Tiempo de espera agotado al registrar al alumno.");
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al registrar el alumno", ex);
                respuesta.put("error", true);
                respuesta.put("mensaje", ex.getMessage());
            } finally {
                ConexionBD.cerrarConexion(conexionBD);
            }
        } else {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Lo sentimos, el servicio no está disponible. Intente más tarde.");
        }
        return respuesta;
    }

    public static List<Alumno> obtenerAlumnoYProyecto(String nombreBusqueda, String tipoProyecto) throws SQLException {
        List<Alumno> resultados = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT "
                + "CONCAT(a.nombreAlumno, ' ', a.apellidoAlumno) AS nombreAlumno, "
                + "e.nombreEE, " + "CASE "
                + "  WHEN pp.nombreProyecto IS NOT NULL THEN pp.nombreProyecto "
                + "  WHEN ss.nombreProyecto IS NOT NULL THEN ss.nombreProyecto "
                + "  ELSE 'Sin proyecto asignado' " + "END AS nombreProyecto, "
                + "CASE "
                + "  WHEN pp.nombreProyecto IS NOT NULL THEN 'Práctica Profesional' "
                + "  WHEN ss.nombreProyecto IS NOT NULL THEN 'Servicio Social' "
                + "  ELSE 'Sin proyecto' " + "END AS tipoProyecto "
                + "FROM inscripcionee i "
                + "INNER JOIN alumno a ON i.idAlumno = a.idAlumno "
                + "INNER JOIN ee e ON i.idEE = e.idEE "
                + "LEFT JOIN proyectopp pp ON i.idProyectoPP = pp.idProyectoPP "
                + "LEFT JOIN proyectoss ss ON i.idProyectoSS = ss.idProyectoSS "
                + "WHERE LOWER(a.nombreAlumno) LIKE LOWER(?) ";

            if (tipoProyecto != null && !tipoProyecto.isEmpty()) {
                consulta += "AND CASE "
                    + "  WHEN pp.nombreProyecto IS NOT NULL THEN 'Práctica Profesional' "
                    + "  WHEN ss.nombreProyecto IS NOT NULL THEN 'Servicio Social' "
                    + "  ELSE 'Sin proyecto' " + "END = ?";
            }

            try (
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta)) {
                prepararSentencia.setString(1, "%" + nombreBusqueda + "%");

                if (tipoProyecto != null && !tipoProyecto.isEmpty()) {
                    prepararSentencia.setString(2, tipoProyecto);
                }

                try (
                    ResultSet resultadoConsulta = prepararSentencia.executeQuery()) {
                    if (!resultadoConsulta.isBeforeFirst()) {
                        System.out.println("No se encontraron resultados con el criterio: "
                            + nombreBusqueda);
                    }

                    while (resultadoConsulta.next()) {
                        Alumno alumno = new Alumno();
                        alumno.setNombreAlumno(resultadoConsulta.getString("nombreAlumno"));
                        alumno.setNombreEE(resultadoConsulta.getString("nombreEE"));
                        alumno.setNombreProyecto(resultadoConsulta.getString("nombreProyecto"));
                        alumno.setTipoProyecto(resultadoConsulta.getString("tipoProyecto"));
                        resultados.add(alumno);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE,
                    "Error al obtener los detalles de los alumnos, EE y proyectos.", ex);
                throw new SQLException("Error al realizar la consulta en la base de datos.", ex);
            } finally {
                ConexionBD.cerrarConexion(conexionBD);
            }
        } else {
            throw new SQLException("No se pudo establecer conexión con la base de datos.");
        }

        return resultados;
    }

    public static List<Alumno> obtenerAlumnosNoInscritosEnEE() throws SQLException {
    List<Alumno> resultados = new ArrayList<>();
    Connection conexionBD = ConexionBD.abrirConexion();

    if (conexionBD != null) {
        String consulta = "SELECT "
                + "CONCAT(a.nombreAlumno, ' ', a.apellidoAlumno) AS nombreCompleto, "
                + "a.idAlumno, "
                + "a.matricula "
                + "FROM alumno a "
                + "LEFT JOIN inscripcionee i ON a.idAlumno = i.idAlumno "
                + "WHERE (i.idInscripcionEE IS NULL OR i.estadoInscripcion = 'Finalizada') "
                + "AND (LOWER(a.nombreAlumno) LIKE LOWER(?) "
                + "OR LOWER(a.matricula) LIKE LOWER(?))";
        try (
            PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta)
        ) {


            try (ResultSet resultado = prepararSentencia.executeQuery()) {
                while (resultado.next()) {
                    Alumno alumno = new Alumno();
                    alumno.setIdAlumno(resultado.getInt("idAlumno"));
                    alumno.setNombreAlumno(resultado.getString("nombreAlumno"));
					
                    alumno.setMatricula(resultado.getString("matricula"));
                    resultados.add(alumno);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE,
                    "Error al obtener los alumnos no inscritos en EE o cuya inscripción esté finalizada.", ex);
            throw ex;
        } finally {
            ConexionBD.cerrarConexion(conexionBD);
        }
    } else {
        throw new SQLException("No se pudo establecer conexión con la base de datos.");
    }

    return resultados;
}


}
