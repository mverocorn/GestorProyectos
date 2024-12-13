package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import gestorproyectos.modelo.pojo.ProyectoPP;
import gestorproyectos.utilidades.Validador;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProyectoPPDAO {

    private static final Logger logger = Logger.getLogger(
            ProyectoPPDAO.class.getName()
    );

    public static List<ProyectoPP> obtenerProyectosPP() throws SQLException {
        List<ProyectoPP> proyectosPP = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idProyectoPP, fechaProyecto, nombreProyecto, "
                    + "objetivoProyecto, descripcionProyecto, cupoProyecto, "
                    + "idResponsable FROM proyectopp;";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararConsulta.executeQuery();
                proyectosPP = new ArrayList<>();
                while (resultado.next()) {
                    proyectosPP.add(serializarProyectoPP(resultado));
                }
            } catch (SQLException e) {
                proyectosPP = null;
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return proyectosPP;
    }

    private static ProyectoPP serializarProyectoPP(ResultSet resultado) throws SQLException {
        ProyectoPP proyectoPP = new ProyectoPP();

        proyectoPP.setIdProyectoPP(resultado.getInt("idProyectoPP"));
        proyectoPP.setFechaProyecto(resultado.getDate("fechaProyecto"));
        proyectoPP.setNombreProyecto(resultado.getString("nombreProyecto"));
        proyectoPP.setObjetivoProyecto(resultado.getString("objetivoProyecto"));
        proyectoPP.setDescripcionProyecto(resultado.getString("descripcionProyecto"));
        proyectoPP.setCupoProyecto(resultado.getInt("cupoProyecto"));
        proyectoPP.setIdResponsable(resultado.getInt("idResponsable"));

        return proyectoPP;
    }
    
    public static void validarProyectoSS(ProyectoPP proyecto) {
        if (proyecto == null) {
            throw new IllegalArgumentException("El objeto ProyectoSS no puede ser nulo.");
        }

        Validador.validarTexto(proyecto.getNombreProyecto(), "nombreProyecto", 150);
        Validador.validarTexto(proyecto.getObjetivoProyecto(), "objetivoProyecto", 255);
        Validador.validarTexto(proyecto.getDescripcionProyecto(), "descripcionProyecto", 1000);
        Validador.validarFechaProyecto(proyecto.getFechaProyecto());
        Validador.validarResponsable(proyecto.getIdResponsable());
    }

    public static boolean existeProyectoPP(String nombre, String objetivo, String descripcion, int idProyectoActual) throws SQLException {
        boolean existe = false;
        String consulta = "SELECT COUNT(*) AS cantidad FROM ProyectoPP WHERE nombreProyecto = ? "
                + "AND objetivoProyecto = ? AND descripcionProyecto = ? AND idProyectoPP != ?";

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
            throw new SQLException("Error al comprobar la existencia del ProyectoSS: " + ex.getMessage(), ex);
        }

        if (existe) {
            throw new SQLException("Ya existe un ProyectoPP con el mismo nombre, objetivo y descripción.");
        }

        return existe;
    }

    public static HashMap<String, Object> registrarProyectoSS(ProyectoPP proyecto) throws SQLException {
        HashMap<String, Object> respuesta = new HashMap<>();

        existeProyectoPP(proyecto.getNombreProyecto(), proyecto.getObjetivoProyecto(),
                proyecto.getDescripcionProyecto(), -1);

        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            PreparedStatement prepararSentencia = null;
            int idProyectoGenerado = -1;
            try {
                validarProyectoSS(proyecto);

                String sentencia = "INSERT INTO ProyectoPP (fechaProyecto, nombreProyecto, "
                        + "objetivoProyecto, descripcionProyecto, cupoProyecto, idResponsable) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

                prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setDate(1, proyecto.getFechaProyecto());
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

                respuesta.put("idProyectoPP", idProyectoGenerado);

            } catch (SQLTimeoutException ex) {
                logger.log(Level.SEVERE, "La inserción en la base de datos excedió el tiempo límite", ex);
                throw new SQLException("Tiempo de espera agotado al registrar el Proyecto PP.", ex);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al registrar el Proyecto PP", ex);
                throw new SQLException("Error al registrar el Proyecto PP", ex);
            } finally {
                ConexionBD.cerrarConexion(conexionBD);
            }
        }

        return respuesta;
    }

    public static ProyectoPP obtenerProyectoPPPorIdProyectoPP(int idProyectoPP) throws SQLException {
        ProyectoPP proyectoPP = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT p.*, "
                    + "r.nombreResponsable, r.apellidoResponsable, r.correoResponsable, "
                    + "e.nombreEmpresa, e.correoEmpresa " + "FROM proyectopp p "
                    + "JOIN responsable r ON p.idResponsable = r.idResponsable "
                    + "JOIN empresa e ON r.idEmpresa = e.idEmpresa "
                    + "WHERE p.idProyectoSS = ?;";

                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                prepararConsulta.setInt(1, idProyectoPP);
                ResultSet resultado = prepararConsulta.executeQuery();

                if (resultado.next()) {
                    // Crear el objeto ProyectoSS con los datos básicos
                    proyectoPP= new ProyectoPP(
                        resultado.getInt("idProyectoPP"),
                        resultado.getDate("fechaProyecto"),
                        resultado.getString("nombreProyecto"),
                        resultado.getString("objetivoProyecto"),
                        resultado.getString("descripcionProyecto"),
                        resultado.getInt("cupoProyecto"),
                        resultado.getInt("idResponsable")
                    );

                    // Obtener datos adicionales
                    String nombreResponsable = resultado.getString("nombreResponsable");
                    String apellidoResponsable = resultado.getString("apellidoResponsable");
                    String correoResponsable = resultado.getString("correoResponsable");
                    String nombreEmpresa = resultado.getString("nombreEmpresa");
                    String correoEmpresa = resultado.getString("correoEmpresa");

                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al obtener ProyectoPP por ID", ex);
                throw new SQLException("Error al obtener ProyectoPP por ID", ex);
            } finally {
                try {
                    conexionBD.close();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "Error al cerrar la conexión", ex);
                }
            }
        }
        return proyectoPP;
    }
    
    public List<ProyectoPP> obtenerProyectosDisponiblesPorPeriodo(String periodo) throws SQLException {
        List<ProyectoPP> proyectosDisponibles = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            try {
                java.sql.Date fechaInicio = obtenerFechaInicioSQL(periodo);
                java.sql.Date fechaFin = obtenerFechaFinSQL(periodo);

                String consulta = "SELECT idProyectoPP, nombreProyecto "
                    + "FROM proyectopp WHERE fechaProyecto BETWEEN ? AND ? AND cupoProyecto > 0";
                try (PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta)) {
                    prepararConsulta.setDate(1, fechaInicio);
                    prepararConsulta.setDate(2, fechaFin);

                    try (ResultSet resultado = prepararConsulta.executeQuery()) {
                        if (!resultado.isBeforeFirst()) {
                            System.out.println("No se encontraron proyectos asignados a esta EE.");
                            return proyectosDisponibles;
                        }

                        while (resultado.next()) {
                            ProyectoPP proyecto = new ProyectoPP(
                                    resultado.getInt("idProyectoPP"),
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
                throw new SQLException("Error al obtener proyectos disponibles por periodo: " + ex.getMessage(), ex);
            } finally {
                ConexionBD.cerrarConexion(conexionBD);
            }
        }
        return proyectosDisponibles;
    }

    private java.sql.Date obtenerFechaInicioSQL(String periodo) {
        String mesInicio = periodo.substring(0, 3).toUpperCase();
        int anioInicio = Integer.parseInt(periodo.substring(3, 7));
        return java.sql.Date.valueOf(obtenerPrimerDiaMes(mesInicio, anioInicio));
    }

    private java.sql.Date obtenerFechaFinSQL(String periodo) {
        String mesFin = periodo.substring(8, 11).toUpperCase();
        int anioFin = Integer.parseInt(periodo.substring(11, 15));
        return java.sql.Date.valueOf(obtenerUltimoDiaMes(mesFin, anioFin));
    }

    private String obtenerPrimerDiaMes(String mes, int anio) {
        int numeroMes = convertirMesANumero(mes);
        return LocalDate.of(anio, numeroMes, 1).toString();
    }

    private String obtenerUltimoDiaMes(String mes, int anio) {
        int numeroMes = convertirMesANumero(mes);
        return LocalDate.of(anio, numeroMes, 1).withDayOfMonth(LocalDate.of(anio, numeroMes, 1).lengthOfMonth()).toString();
    }

    private int convertirMesANumero(String mes) {
        switch (mes.toUpperCase()) {
            case "ENE":
                return 1;
            case "FEB":
                return 2;
            case "MAR":
                return 3;
            case "ABR":
                return 4;
            case "MAY":
                return 5;
            case "JUN":
                return 6;
            case "JUL":
                return 7;
            case "AGO":
                return 8;
            case "SEP":
                return 9;
            case "OCT":
                return 10;
            case "NOV":
                return 11;
            case "DIC":
                return 12;
            default:
                throw new IllegalArgumentException("Mes inválido: " + mes);
        }
    }
    
    public static List<ProyectoPP> obtenerPriorizacionDeAlumnoProyectoPP(int idAlumno) throws SQLException {
        List<ProyectoPP> resultados = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT pp.idProyectoPP, pp.nombreProyecto, pp.cupoProyecto, "
                + "p.prioridad "
                + "FROM priorizacionproyectos p "
                + "INNER JOIN proyectopp pp ON p.idProyectoPP = pp.idProyectoPP "
                + "INNER JOIN inscripcionee ie ON p.idInscripcionEE = ie.idInscripcionEE "
                + "WHERE ie.idAlumno = ?";

            try (
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta)) {
                prepararSentencia.setInt(1, idAlumno);

                try (ResultSet resultado = prepararSentencia.executeQuery()) {
                    while (resultado.next()) {
                        ProyectoPP proyecto = new ProyectoPP();
                        proyecto.setIdProyectoPP(resultado.getInt("idProyectoPP"));
                        proyecto.setNombreProyecto(resultado.getString("nombreProyecto"));
                        proyecto.setCupoProyecto(resultado.getInt("cupoProyecto"));
                        proyecto.setPrioridad(resultado.getInt("prioridad"));
                        resultados.add(proyecto);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE,
                    "Error al obtener detalles de priorización de proyectos de práctica profesional.", ex);
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
