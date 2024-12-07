package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.utilidades.Validador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EEDAO {

    private static final Logger logger = Logger.getLogger(
            EEDAO.class.getName()
    );

    public static List<EE> obtenerEE() throws SQLException {
        List<EE> eeList = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idEE, nombreEE, nrc, bloque, idProfesor FROM ee;";
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
        ee.setIdProfesor(resultado.getInt("idProfesor"));
        return ee;
    }


    public static void validarEE(EE ee) {
        if (ee == null) {
            throw new IllegalArgumentException("El objeto EE no puede ser nulo.");
        }

        Validador.validarTexto(ee.getNombreEE(), "nombreEE", 100);

        Validador.validarNRC(ee.getNrc());

        Validador.validarBloque(ee.getBloque());

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
                if (existeEE(ee.getNrc(), ee.getBloque(), ee.getIdProfesor())) {
                    throw new IllegalArgumentException("Ya existe un EE con el mismo NRC, bloque y profesor.");
                }

                String sentencia = "INSERT INTO EE (nombreEE, nrc, bloque, idProfesor) "
                        + "VALUES (?, ?, ?, ?)";

                prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setString(1, ee.getNombreEE());
                prepararSentencia.setInt(2, ee.getNrc());
                prepararSentencia.setInt(3, ee.getBloque());
                prepararSentencia.setInt(4, ee.getIdProfesor());

                int filasAfectadas = prepararSentencia.executeUpdate();

                if (filasAfectadas == 1) {
                    try (ResultSet rs = prepararSentencia.getGeneratedKeys()) {
                        if (rs.next()) {
                            idEEGenerado = rs.getInt(1); // Obtener el id generado
                        }
                    }
                }

                respuesta.put("idEE", idEEGenerado);

            } catch (SQLTimeoutException ex) {
                logger.log(Level.SEVERE, "La inserción en la base de datos excedió el tiempo límite", ex);
                throw new SQLException("Tiempo de espera agotado al registrar EE.", ex);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al registrar EE", ex);
                throw new SQLException("Error al registrar EE", ex);
            } finally {
                ConexionBD.cerrarConexion(conexionBD);
            }
        }

        return respuesta;
    }

}
