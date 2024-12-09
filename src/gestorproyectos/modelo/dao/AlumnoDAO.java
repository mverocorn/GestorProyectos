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
                String consulta = "SELECT idAlumno, nombreAlumno, apellidoAlumno, matricula, telefonoAlumno, correoAlumno, promedio, estadoAlumno FROM alumno;";
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

    public static void validarAlumnoARegistrar(Alumno alumno) {
        if (alumno == null) {
            throw new IllegalArgumentException("El objeto Alumno no puede ser nulo.");
        }

        // Validar nombre
        Validador.validarTexto(alumno.getNombreAlumno(), "nombreAlumno", 100);

        // Validar apellido
        Validador.validarTexto(alumno.getApellidoAlumno(), "apellidoAlumno", 100);

        // Validar matrícula
        Validador.validarMatricula(alumno.getMatricula());

        // Validar teléfono (opcional si puede ser nulo)
        if (alumno.getTelefonoAlumno() != null && !alumno.getTelefonoAlumno().isEmpty()) {
            Validador.validarTelefono(alumno.getTelefonoAlumno());
        }

        // Validar correo
        Validador.validarCorreo(alumno.getCorreo());

        // Validar promedio
        Validador.validarPromedio(alumno.getPromedio());

        // Validar estado
        Validador.validarTexto(alumno.getEstadoAlumno(), "estadoAlumno", 50);
    }

    public boolean existeAlumno(String correo, String telefono, String matricula, int idAlumnoActual) throws SQLException {
        ConexionBD.verificarConexionBD();

        boolean existe = false;
        String consulta
                = "SELECT COUNT(*) AS cantidad FROM Alumno WHERE (correoAlumno = ? OR telefonoAlumno = ? OR matricula = ?) "
                + "AND idAlumno != ?";

        try (
                Connection conexion = ConexionBD.abrirConexion(); PreparedStatement sentenciaPreparada = conexion.prepareStatement(consulta)) {
            sentenciaPreparada.setString(1, correo);
            sentenciaPreparada.setString(2, telefono);
            sentenciaPreparada.setString(3, matricula);
            sentenciaPreparada.setInt(4, idAlumnoActual);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next() && resultSet.getInt("cantidad") > 0) {
                    existe = true;
                }
            }
        } catch (SQLTimeoutException ex) {
            logger.log(Level.SEVERE, "La consulta a la base de datos excedió el tiempo límite", ex);
            throw new SQLException("Tiempo de espera agotado al comprobar la existencia del alumno", ex);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al comprobar la existencia del alumno", ex);
            throw new SQLException("Error al comprobar la existencia del alumno", ex);
        }

        return existe;
    }

    public static HashMap<String, Object> registrarAlumno(Alumno alumno) throws SQLException {
        HashMap<String, Object> respuesta = new HashMap<>();

        // Abrir conexión a la base de datos
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            PreparedStatement prepararSentencia = null;
            int idAlumnoGenerado = -1; // Variable para almacenar el id generado
            try {
                // Validar los datos del alumno
                validarAlumnoARegistrar(alumno);

                // Consulta para insertar un alumno
                String sentencia = "INSERT INTO alumno (nombreAlumno, apellidoAlumno, matricula, "
                        + "telefonoAlumno, correoAlumno, promedio, estadoAlumno) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";

                prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, alumno.getNombreAlumno());
                prepararSentencia.setString(2, alumno.getApellidoAlumno());
                prepararSentencia.setString(3, alumno.getMatricula());
                prepararSentencia.setString(4, alumno.getTelefonoAlumno());
                prepararSentencia.setString(5, alumno.getCorreo());
                prepararSentencia.setFloat(6, alumno.getPromedio());
                prepararSentencia.setString(7, alumno.getEstadoAlumno());

                // Ejecutar la sentencia
                int filasAfectadas = prepararSentencia.executeUpdate();

                // Obtener el idAlumno generado
                if (filasAfectadas == 1) {
                    try (ResultSet rs = prepararSentencia.getGeneratedKeys()) {
                        if (rs.next()) {
                            idAlumnoGenerado = rs.getInt(1); // Obtener el id generado
                        }
                    }
                }

                // Agregar el idAlumno generado al HashMap
                respuesta.put("idAlumno", idAlumnoGenerado);

            } catch (SQLTimeoutException ex) {
                logger.log(Level.SEVERE, "La inserción en la base de datos excedió el tiempo límite", ex);
                throw new SQLException("Tiempo de espera agotado al registrar el estudiante.", ex);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al registrar el estudiante", ex);
                throw new SQLException("Error al registrar el estudiante", ex);
            } finally {
                // Cerrar la conexión después de usarla
                ConexionBD.cerrarConexion(conexionBD);
            }
        }

        return respuesta;
    }

}
