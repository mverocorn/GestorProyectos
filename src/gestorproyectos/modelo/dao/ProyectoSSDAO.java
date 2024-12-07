package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import static gestorproyectos.modelo.dao.EmpresaDAO.obtenerEmpresaPorIdEmpresa;
import static gestorproyectos.modelo.dao.ResponsableDAO.obtenerResponsablePorIdResponsable;
import gestorproyectos.utilidades.Validador;
import gestorproyectos.modelo.pojo.ProyectoSS;
import java.sql.*;
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
                String consulta = "SELECT idProyectoSS, fechaProyecto, periodoProyecto, nombreProyecto, objetivoProyecto, descripcionProyecto, cupoProyecto, idEmpresa, idResponsable FROM proyectoss;";
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
        proyectoSS.setPeriodoProyecto(resultado.getString("periodoProyecto"));
        proyectoSS.setNombreProyecto(resultado.getString("nombreProyecto"));
        proyectoSS.setObjetivoProyecto(resultado.getString("objetivoProyecto"));
        proyectoSS.setDescripcionProyecto(resultado.getString("descripcionProyecto"));
        proyectoSS.setCupoProyecto(resultado.getInt("cupoProyecto"));
        proyectoSS.setIdEmpresa(resultado.getInt("idEmpresa"));
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

        // Validar periodoProyecto
        Validador.validarPeriodo(proyecto.getPeriodoProyecto());

        // Validar fechaProyecto
        Validador.validarFechaProyecto(proyecto.getFechaProyecto());

        // Validar idEmpresa
        Validador.validarEmpresa(proyecto.getIdEmpresa());

        // Validar idResponsable
        Validador.validarResponsable(proyecto.getIdResponsable());
    }

    public boolean existeProyectoSS(String nombre, String objetivo, String descripcion, String periodo, int idProyectoActual) throws SQLException {
        ConexionBD.verificarConexionBD();

        boolean existe = false;
        String consulta = "SELECT COUNT(*) AS cantidad FROM ProyectoSS WHERE nombreProyecto = ? "
                + "AND objetivoProyecto = ? AND descripcionProyecto = ? AND periodoProyecto = ? "
                + "AND idProyectoSS != ?";

        try (Connection conexion = ConexionBD.abrirConexion(); PreparedStatement sentenciaPreparada = conexion.prepareStatement(consulta)) {
            sentenciaPreparada.setString(1, nombre);
            sentenciaPreparada.setString(2, objetivo);
            sentenciaPreparada.setString(3, descripcion);
            sentenciaPreparada.setString(4, periodo);
            sentenciaPreparada.setInt(5, idProyectoActual);

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

                String sentencia = "INSERT INTO ProyectoSS (fechaProyecto, periodoProyecto, nombreProyecto, "
                        + "objetivoProyecto, descripcionProyecto, cupoProyecto, idEmpresa, idResponsable) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                prepararSentencia = conexionBD.prepareStatement(sentencia, Statement.RETURN_GENERATED_KEYS);
                prepararSentencia.setDate(1, proyecto.getFechaProyecto());
                prepararSentencia.setString(2, proyecto.getPeriodoProyecto());
                prepararSentencia.setString(3, proyecto.getNombreProyecto());
                prepararSentencia.setString(4, proyecto.getObjetivoProyecto());
                prepararSentencia.setString(5, proyecto.getDescripcionProyecto());
                prepararSentencia.setInt(6, proyecto.getCupoProyecto());
                prepararSentencia.setInt(7, proyecto.getIdEmpresa());
                prepararSentencia.setInt(8, proyecto.getIdResponsable());

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
                            resultado.getString("periodoProyecto"),
                            resultado.getString("nombreProyecto"),
                            resultado.getString("objetivoProyecto"),
                            resultado.getString("descripcionProyecto"),
                            resultado.getInt("cupoProyecto"),
                            resultado.getInt("idEmpresa"),
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


}
