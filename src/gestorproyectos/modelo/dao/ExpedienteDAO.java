package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Expediente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author jonathan
 */
public class ExpedienteDAO {

	public static int obtenerIdExpedientePorAlumno(int idAlumno) throws SQLException {
		int idExpediente = -1;
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String consulta = "SELECT E.idExpediente "
						+ "FROM Expediente E "
						+ "JOIN InscripcionEE I ON E.idInscripcionEE = I.idInscripcionEE "
						+ "WHERE I.idAlumno = ?";
				PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
				prepararSentencia.setInt(1, idAlumno);
				ResultSet resultado = prepararSentencia.executeQuery();
				if (resultado.next()) {
					idExpediente = resultado.getInt("idExpediente");
				}
				conexionBD.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return idExpediente;
	}

	public static Expediente obtenerExpediente(int idInscripcionEE) throws SQLException {
		Expediente expediente = null;
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String consulta = "SELECT idExpediente, fechaInicioProyecto, horasRestantes , fechaFinProyecto, horasAcumuladas "
						+ "FROM Expediente "
						+ "WHERE idInscripcionEE = ? ";
				PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
				prepararSentencia.setInt(1, idInscripcionEE);
				ResultSet resultado = prepararSentencia.executeQuery();
				while (resultado.next()) {
					expediente = convertirExpedienteResult(resultado);
				}
				conexionBD.close();
			} catch (SQLException e) {
				expediente = null;
				e.printStackTrace();
			}
		}
		return expediente;
	}

	public static HashMap<String, Object> DarDeBaja(int idExpediente) throws SQLException {
		HashMap<String, Object> respuesta = new HashMap<>();
		respuesta.put("error", true);
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			String sentencia = "DELETE FROM Expediente "
					+ "WHERE idExpediente = ?";
			try {
				PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
				prepararSentencia.setInt(1, idExpediente);
				int filasAfectadas = prepararSentencia.executeUpdate();
				if (filasAfectadas == 1) {
					respuesta.put("error", false);
					respuesta.put("mensaje", "Información del expediente eliminada correctamente");
				} else {
					respuesta.put("mensaje", "Lo sentimos, hubo un error al eliminar "
							+ "la informacion del expediente, por favor intenta mas tarde");
				}
				conexionBD.close();
			} catch (SQLException ex) {
				respuesta.put("mensaje", ex.getMessage());
			}

		} else {
			respuesta.put("mensaje", "Por el momento el servicio no esta disponible, intentalo mas tarde.");
		}
		return respuesta;
	}

	private static Expediente convertirExpedienteResult(ResultSet resultado) throws SQLException {
		Expediente expediente = new Expediente();
		expediente.setIdExpediente(resultado.getInt("idExpediente"));
		expediente.setFechaInicioProyecto(resultado.getString("fechaInicioProyecto"));
		expediente.setHorasRestantes(resultado.getInt("horasRestantes"));
		expediente.setFechaFinProyecto(resultado.getString("fechaFinProyecto"));
		expediente.setHorasAcumuladas(resultado.getInt("horasAcumuladas"));

		return expediente;
	}

    public static HashMap<String, Object> CrearExpediente(int idInscripcionEE, String fechaProyecto, int horasPorCubrir) throws SQLException{
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error",true);
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD!=null){
        try{
            String sentencia = "INSERT INTO Expediente (fechaInicioProyecto, horasRestantes, horasAcumuladas, idInscripcionEE) " +
                                "VALUES ( ? , ?, 0, ?);";
            PreparedStatement prepararSentencia= conexionBD.prepareStatement(sentencia);
            prepararSentencia.setString(1, fechaProyecto);
            prepararSentencia.setInt(2, horasPorCubrir);
            prepararSentencia.setInt(3, idInscripcionEE);
            int filasAfectadas = prepararSentencia.executeUpdate();
            if(filasAfectadas > 0){
                respuesta.put("error",false);
                respuesta.put("mensaje","El expediente se ha registrado correctamente");
            }else{
                respuesta.put("mensaje","Lo sentimos, hubo un error al registrar el expediente, por favor revisa la informacion");
            }
            conexionBD.close();
        }catch(SQLException ex){
                respuesta.put("mensaje", ex.getMessage());
            }    
        }else{
            respuesta.put("mensaje","No hay coneccion, intente mas tarde");
        }
        return respuesta; 
    }        
        
}
