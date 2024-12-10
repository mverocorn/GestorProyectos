package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PriorizacionProyectosDAO {
    
    private static final Logger logger = Logger.getLogger(
            InscripcionEEDAO.class.getName()
    );
    
    public static boolean validarPriorizacion(Map<Integer, Integer> proyectosYPrioridades) throws SQLException {
        Set<Integer> prioridades = new HashSet<>(proyectosYPrioridades.values());
        if (prioridades.size() != proyectosYPrioridades.size()) {
            throw new SQLException("Detectamos la repetición de un mismo número en 2 o más proyectos. Por favor asigne un número a cada proyecto sin repetir.");
        }

        return true;
    }

    public static void guardarPriorizacionProyectosSS(int idInscripcionEE, Map<Integer, Integer> proyectosYPrioridades) throws SQLException {
        validarPriorizacion(proyectosYPrioridades);

        Connection conexionBD = ConexionBD.abrirConexion();
        HashMap<String, Object> respuesta = new HashMap<>();

        if (conexionBD != null) {
            try {
                String consulta = "INSERT INTO priorizacionproyectos (idInscripcionEE, idProyectoSS, prioridad) VALUES (?, ?, ?)";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);

                for (Map.Entry<Integer, Integer> entrada : proyectosYPrioridades.entrySet()) {
                    prepararConsulta.setInt(1, idInscripcionEE);
                    prepararConsulta.setInt(2, entrada.getKey());
                    prepararConsulta.setInt(3, entrada.getValue());
                    prepararConsulta.addBatch();
                }

                int[] resultado = prepararConsulta.executeBatch();

                if (resultado.length > 0) {
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Se ha guardado tu orden de preferencia para proyecto de Servicio Social. Tu coordinador pronto te asignará tu proyecto.");
                } else {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "Lo sentimos, no se pudo guardar la priorización de los proyectos. Intenta nuevamente más tarde.");
                }

            } catch (SQLException ex) {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Error al guardar la priorización de proyectos: " + ex.getMessage());
                logger.log(Level.SEVERE, "Error al guardar priorización de proyectos", ex);
                throw new SQLException("Error al guardar priorización de proyectos", ex);
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Lo sentimos, por el momento el servicio no está disponible. Intente más tarde.");
        }
    }

    public static void guardarPriorizacionProyectosPP(int idInscripcionEE, Map<Integer, Integer> proyectosYPrioridades) throws SQLException {
        validarPriorizacion(proyectosYPrioridades);

        Connection conexionBD = ConexionBD.abrirConexion();
        HashMap<String, Object> respuesta = new HashMap<>();

        if (conexionBD != null) {
            try {
                String consulta = "INSERT INTO priorizacionproyectos (idInscripcionEE, idProyectoPP, prioridad) VALUES (?, ?, ?)";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);

                for (Map.Entry<Integer, Integer> entrada : proyectosYPrioridades.entrySet()) {
                    prepararConsulta.setInt(1, idInscripcionEE);
                    prepararConsulta.setInt(2, entrada.getKey());
                    prepararConsulta.setInt(3, entrada.getValue());
                    prepararConsulta.addBatch();
                }

                int[] resultado = prepararConsulta.executeBatch();

                if (resultado.length > 0) {
                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Se ha guardado tu orden de preferencia para proyecto de Practica Profesional. Tu coordinador pronto te asignará tu proyecto.");
                } else {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "Lo sentimos, no se pudo guardar la priorización de los proyectos. Intenta nuevamente más tarde.");
                }

            } catch (SQLException ex) {
                respuesta.put("error", true);
                respuesta.put("mensaje", "Error al guardar la priorización de proyectos: " + ex.getMessage());
                logger.log(Level.SEVERE, "Error al guardar priorización de proyectos", ex);
                throw new SQLException("Error al guardar priorización de proyectos", ex);
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Lo sentimos, por el momento el servicio no está disponible. Intente más tarde.");
        }
    }
    
}
