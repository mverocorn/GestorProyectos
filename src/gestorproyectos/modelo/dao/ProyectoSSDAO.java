package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.utilidades.Validador;
import gestorproyectos.modelo.pojo.ProyectoSS;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
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
        proyectoSS.setFechaProyecto(resultado.getDate("fechaProyecto"));
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

        // Validar nombreProyecto
        Validador.validarTexto(proyecto.getNombreProyecto(), "nombreProyecto", 150);

        // Validar objetivoProyecto
        Validador.validarTexto(proyecto.getObjetivoProyecto(), "objetivoProyecto", 255);

        // Validar descripcionProyecto
        Validador.validarTexto(proyecto.getDescripcionProyecto(), "descripcionProyecto", 1000);

        // Validar fechaProyecto
        Validador.validarFechaProyecto(proyecto.getFechaProyecto());

        // Validar idResponsable
        Validador.validarResponsable(proyecto.getIdResponsable());
    }

    public boolean existeProyectoSS(String nombre, String objetivo, String descripcion, int idProyectoActual) throws SQLException {
        ConexionBD.verificarConexionBD();

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
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al comprobar la existencia del ProyectoSS", ex);
            throw new SQLException("Error al comprobar la existencia del ProyectoSS", ex);
        }

        return existe;
    }

    public static HashMap<String, Object> registrarProyectoSS(ProyectoSS proyecto) throws SQLException {
        HashMap<String, Object> respuesta = new HashMap<>();

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
                            idProyectoGenerado = rs.getInt(1); // Obtener el id generado
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
                String consulta = "SELECT * FROM proyectoss WHERE idProyectoSS = ?";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                prepararConsulta.setInt(1, idProyectoSS);
                ResultSet resultado = prepararConsulta.executeQuery();
                if (resultado.next()) {
                    proyectoSS = new ProyectoSS(
                            resultado.getInt("idProyectoSS"),
                            resultado.getDate("fechaProyecto"),
                            resultado.getString("nombreProyecto"),
                            resultado.getString("objetivoProyecto"),
                            resultado.getString("descripcionProyecto"),
                            resultado.getInt("cupoProyecto"),
                            resultado.getInt("idResponsable")
                    );
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al obtener ProyectoSS por ID", ex);
                throw new SQLException("Error al obtener ProyectoSS por ID", ex);
            } finally {
                conexionBD.close();
            }
        }
        return proyectoSS;
    }
    
    public List<ProyectoSS> obtenerProyectosDisponiblesPorPeriodo(String periodo) throws SQLException {
        List<ProyectoSS> proyectosDisponibles = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            try {
                // Obtener las fechas de inicio y fin del periodo
                java.sql.Date fechaInicio = obtenerFechaInicioSQL(periodo);
                java.sql.Date fechaFin = obtenerFechaFinSQL(periodo);

                // Modificar la consulta SQL para seleccionar solo los campos necesarios
                String consulta = "SELECT idProyectoSS, nombreProyecto FROM proyectoss WHERE fechaProyecto BETWEEN ? AND ? AND cupoProyecto > 0";
                try (PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta)) {
                    prepararConsulta.setDate(1, fechaInicio);
                    prepararConsulta.setDate(2, fechaFin);

                    try (ResultSet resultado = prepararConsulta.executeQuery()) {
                        if (!resultado.isBeforeFirst()) {
                            System.out.println("No se encontraron proyectos en el rango de fechas.");
                            return proyectosDisponibles;
                        }

                        while (resultado.next()) {
                            // Crear un objeto ProyectoSS con solo los campos necesarios
                            ProyectoSS proyecto = new ProyectoSS(
                                    resultado.getInt("idProyectoSS"),
                                    null, // No necesitamos la fecha en este caso
                                    resultado.getString("nombreProyecto"),
                                    null, // Los otros campos no son necesarios
                                    null,
                                    0, // El cupoProyecto no es necesario
                                    0 // El idResponsable tampoco es necesario
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
                return 1; // Enero
            case "FEB":
                return 2; // Febrero
            case "MAR":
                return 3; // Marzo
            case "ABR":
                return 4; // Abril
            case "MAY":
                return 5; // Mayo
            case "JUN":
                return 6; // Junio
            case "JUL":
                return 7; // Julio
            case "AGO":
                return 8; // Agosto
            case "SEP":
                return 9; // Septiembre
            case "OCT":
                return 10; // Octubre
            case "NOV":
                return 11; // Noviembre
            case "DIC":
                return 12; // Diciembre
            default:
                throw new IllegalArgumentException("Mes inválido: " + mes);
        }
    }

}
