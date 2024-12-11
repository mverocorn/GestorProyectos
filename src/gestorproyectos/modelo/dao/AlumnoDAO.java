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
                    + "telefonoAlumno, correo, promedio, estadoAlumno "
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
        alumno.setCorreo(resultado.getString("correo"));
        alumno.setPromedio(resultado.getFloat("promedio"));
        alumno.setEstadoAlumno(resultado.getString("estadoAlumno"));
        return alumno;
    }

    public static void validarAlumnoARegistrar(Alumno alumno) {
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
        Validador.validarTexto(alumno.getEstadoAlumno(), "estadoAlumno", 50);
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
                    + "telefonoAlumno, correo, promedio, estadoAlumno) "
                    + "VALUES (?, ?, ?, ?, ?, ?, 'inscrito')";
                prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, alumno.getNombreAlumno());
                prepararSentencia.setString(2, alumno.getApellidoAlumno());
                prepararSentencia.setString(3, alumno.getMatricula());
                prepararSentencia.setString(4, alumno.getTelefonoAlumno());
                prepararSentencia.setString(5, alumno.getCorreo());
                prepararSentencia.setFloat(6, alumno.getPromedio());

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

}
