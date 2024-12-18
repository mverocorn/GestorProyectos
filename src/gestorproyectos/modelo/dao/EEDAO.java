package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.pojo.ProyectoPP;
import gestorproyectos.modelo.pojo.ProyectoSS;
import gestorproyectos.utilidades.Validador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.SQLTimeoutException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class EEDAO {

    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("MMMyyyy");

    private static final Logger logger = Logger.getLogger(
        EEDAO.class.getName()
    );

    public static List<EE> obtenerEE() throws SQLException {
        List<EE> eeList = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                // Modificamos la consulta para incluir el nombre y apellido del profesor
                String consulta = "SELECT ee.idEE, ee.nombreEE, ee.nrc, ee.seccion, ee.periodo, ee.idProfesor, "
                    + "profesor.nombreProfesor AS nombreProfesor, profesor.apellidoProfesor AS apellidoProfesor "
                    + "FROM ee "
                    + "JOIN profesor ON ee.idProfesor = profesor.idProfesor;";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararConsulta.executeQuery();
                eeList = new ArrayList<>();
                while (resultado.next()) {
                    eeList.add(serializarEE(resultado)); // Llamamos a serializarEE para mapear los resultados
                }
            } catch (SQLException e) {
                eeList = null;
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return eeList;
    }

    private static EE serializarEE(ResultSet resultado) throws SQLException {
        EE ee = new EE();
        ee.setIdEE(resultado.getInt("idEE"));
        ee.setNombreEE(resultado.getString("nombreEE"));
        ee.setNrc(resultado.getInt("nrc"));
        ee.setSeccion(resultado.getInt("seccion"));
        ee.setPeriodo(resultado.getString("periodo"));
        ee.setNombreProfesor(resultado.getString("nombreProfesor"));
        ee.setApellidoProfesor(resultado.getString("apellidoProfesor"));

        return ee;
    }


    public static List<String> obtenerNombresProyectosPorTipo(String tipo) throws SQLException {
        List<String> nombresProyectos = new ArrayList<>();

        if (tipo.equalsIgnoreCase("PP")) {
            List<ProyectoPP> proyectosPP = ProyectoPPDAO.obtenerProyectosPP();
            if (proyectosPP != null) {
                for (ProyectoPP proyecto : proyectosPP) {
                    nombresProyectos.add(proyecto.getNombreProyecto());
                }
            }
        } else if (tipo.equalsIgnoreCase("SS")) {
            List<ProyectoSS> proyectosSS = ProyectoSSDAO.obtenerProyectosSS();
            if (proyectosSS != null) {
                for (ProyectoSS proyecto : proyectosSS) {
                    nombresProyectos.add(proyecto.getNombreProyecto());
                }
            }
        }

        return nombresProyectos;
    }

    public static void validarEE(EE ee) {
        if (ee == null) {
            throw new IllegalArgumentException("El objeto EE no puede ser nulo.");
        }

        Validador.validarTexto(ee.getNombreEE(), "nombreEE", 100);
        Validador.validarNRC(ee.getNrc());
        Validador.validarSeccion(ee.getSeccion());
        Validador.validarPeriodo(ee.getPeriodo());

        if (ee.getIdProfesor() <= 0) {
            throw new IllegalArgumentException("El profesor asociado es inválido.");
        }
    }

    public static boolean existeEE(String nombreEE, int nrc, int seccion, String periodo) throws SQLException {
        boolean existe = false;
        String consulta = "SELECT COUNT(*) AS cantidad FROM EE WHERE (nombreEE = ? AND seccion = ? "
				+ "AND periodo = ?) OR (nrc = ? AND periodo = ?)";

        try (Connection conexion = ConexionBD.abrirConexion();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(consulta)) {
            sentenciaPreparada.setString(1, nombreEE);
            sentenciaPreparada.setInt(2, seccion);
            sentenciaPreparada.setString(3, periodo);
            sentenciaPreparada.setInt(4, nrc);
            sentenciaPreparada.setString(5, periodo);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next() && resultSet.getInt("cantidad") > 0) {
                    existe = true;
                }
            }
        } catch (SQLTimeoutException ex) {
            logger.log(Level.SEVERE, "La consulta a la base de datos excedió el tiempo límite", ex);
            throw new SQLException("Tiempo de espera agotado al comprobar la existencia del EE.", ex);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al comprobar la existencia del EE", ex);
            throw new SQLException("Error al comprobar la existencia del EE: "
                + ex.getMessage(), ex);
        }

        if (existe) {
            throw new SQLException("Ya existe una EE con el mismo nombre, sección, nrc o periodo.");
        }

        return existe;
    }

    public static HashMap<String, Object> registrarEE(EE ee) throws SQLException {
        HashMap<String, Object> respuesta = new HashMap<>();

        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            PreparedStatement prepararSentencia = null;
            int idEEGenerado = -1;
            try {
                existeEE(ee.getNombreEE(), ee.getNrc(), ee.getSeccion(), ee.getPeriodo());

                String sentencia = "INSERT INTO EE (nombreEE, nrc, seccion, periodo, idProfesor) "
                    + "VALUES (?, ?, ?, ?, ?)";

                prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, ee.getNombreEE());
                prepararSentencia.setInt(2, ee.getNrc());
                prepararSentencia.setInt(3, ee.getSeccion());
                prepararSentencia.setString(4, ee.getPeriodo());
                prepararSentencia.setInt(5, ee.getIdProfesor());

                int filasAfectadas = prepararSentencia.executeUpdate();

                if (filasAfectadas == 1) {
                    try (ResultSet rs = prepararSentencia.getGeneratedKeys()) {
                        if (rs.next()) {
                            idEEGenerado = rs.getInt(1);
                        }
                    }

                    respuesta.put("error", false);
                    respuesta.put("mensaje", "El EE " + ee.getNombreEE()
                        + " fue registrado correctamente.");
                    respuesta.put("idEE", idEEGenerado);
                } else {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "No fue posible registrar el EE. Intente más tarde.");
                }

            } catch (SQLTimeoutException ex) {
                logger.log(Level.SEVERE, "La inserción en la base de datos excedió el tiempo límite", ex);
                respuesta.put("error", true);
                respuesta.put("mensaje", "Tiempo de espera agotado al registrar el EE.");
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al registrar el EE", ex);
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

    public static EE obtenerDetalleEE(int idEE) throws SQLException {
        EE ee = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT * FROM experienciaeducativa WHERE idEE = ?";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                prepararConsulta.setInt(1, idEE);
                ResultSet resultado = prepararConsulta.executeQuery();
                if (resultado.next()) {
                    ee = serializarEE(resultado);
                }
            } finally {
                conexionBD.close();
            }
        }
        return ee;
    }

    public static boolean isPeriodoActivo(String periodo) throws ParseException {
        String[] fechas = periodo.split("-");
        String fechaInicio = fechas[0];
        String fechaFin = fechas[1];

        Date fechaInicioDate = formatoFecha.parse(fechaInicio);
        Date fechaFinDate = formatoFecha.parse(fechaFin);

        Date fechaActual = new Date();

        return !fechaActual.before(fechaInicioDate)
            && !fechaActual.after(fechaFinDate);
    }

    public static int obtenerIdEEPorIdAlumno(int idAlumno) throws SQLException {
        int idEE = -1;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idEE FROM inscripcionEE WHERE idAlumno = ?";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                prepararConsulta.setInt(1, idAlumno);
                ResultSet resultado = prepararConsulta.executeQuery();
                if (resultado.next()) {
                    idEE = resultado.getInt("idEE");
                }
            } catch (SQLException ex) {
                throw new SQLException("Error al obtener idEE", ex);
            } finally {
                conexionBD.close();
            }
        }
        return idEE;
    }

}
