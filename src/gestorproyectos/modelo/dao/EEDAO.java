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
                String consulta = "SELECT idEE, nombreEE, nrc, bloque, periodo, idProfesor FROM ee;";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararConsulta.executeQuery();
                eeList = new ArrayList<>();
                while (resultado.next()) {
                    eeList.add(serializarEE(resultado));
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
        ee.setBloque(resultado.getInt("bloque"));
        ee.setPeriodo(resultado.getString("periodo"));
        ee.setIdProfesor(resultado.getInt("idProfesor"));
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

        Validador.validarBloque(ee.getBloque());
        
        Validador.validarPeriodo(ee.getPeriodo());

        if (ee.getIdProfesor() <= 0) {
            throw new IllegalArgumentException("El profesor asociado es inválido.");
        }
    }

    public static boolean existeEE(int nrc, int bloque, int idProfesor) throws SQLException {
        //No se si es encesario
        return false;
    }

    public static HashMap<String, Object> registrarEE(EE ee) throws SQLException {
        HashMap<String, Object> respuesta = new HashMap<>();

        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            PreparedStatement prepararSentencia = null;
            int idEEGenerado = -1;
            try {
                validarEE(ee);

                String sentencia = "INSERT INTO EE (nombreEE, nrc, bloque, periodo, idProfesor) "
                        + "VALUES (?, ?, ?, ?, ?)";

                prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, ee.getNombreEE());
                prepararSentencia.setInt(2, ee.getNrc());
                prepararSentencia.setInt(3, ee.getBloque());
                prepararSentencia.setString(4, ee.getPeriodo());
                prepararSentencia.setInt(5, ee.getIdProfesor());

                int filasAfectadas = prepararSentencia.executeUpdate();

                if (filasAfectadas == 1) {
                    try (ResultSet rs = prepararSentencia.getGeneratedKeys()) {
                        if (rs.next()) {
                            idEEGenerado = rs.getInt(1);
                        }
                    }
                }

                respuesta.put("idEE", idEEGenerado);

            } catch (SQLTimeoutException ex) {
                logger.log(Level.SEVERE, "La inserción en la base de datos excedió el tiempo límite", ex);
                throw new SQLException("Tiempo de espera agotado al registrar el EE.", ex);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al registrar el EE", ex);
                throw new SQLException("Error al registrar el EE", ex);
            } finally {
                ConexionBD.cerrarConexion(conexionBD);
            }
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
        // Extraer fechas de inicio y fin del periodo
        String[] fechas = periodo.split("-");
        String fechaInicio = fechas[0]; // Ejemplo: "AGO2024"
        String fechaFin = fechas[1];    // Ejemplo: "ENE2025"

        // Convertir las fechas a objetos Date
        Date fechaInicioDate = formatoFecha.parse(fechaInicio);
        Date fechaFinDate = formatoFecha.parse(fechaFin);

        // Obtener la fecha actual
        Date fechaActual = new Date();

        // Verificar si la fecha actual está dentro del rango
        return !fechaActual.before(fechaInicioDate) && !fechaActual.after(fechaFinDate);
    }
    
    

}
