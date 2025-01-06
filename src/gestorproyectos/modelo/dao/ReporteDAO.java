/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Reporte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author jonathan
 */
public class ReporteDAO {

	public static List<Reporte> obtenerReportes(int idExpediente) throws SQLException {
		List<Reporte> reportes = null;
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String consulta = "SELECT idReporte, nombreReporte, reporte, horasReportadas, validado, noReporte "
						+ "FROM Reporte "
						+ "WHERE idExpediente = ? ";
				PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
				prepararSentencia.setInt(1, idExpediente);
				ResultSet resultado = prepararSentencia.executeQuery();
				reportes = new ArrayList<>();
				while (resultado.next()) {
					reportes.add(convertirReporteResult(resultado));
				}
			} catch (SQLException e) {
				reportes = null;
				e.printStackTrace();
			} finally {
				conexionBD.close();
			}
		}
		return reportes;
	}
        
   public static HashMap<String, Object> obtenerReportesExpediente(int idExpediente) throws SQLException {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            try{
                String consulta= "SELECT idReporte, nombreReporte, reporte, horasReportadas, validado, noReporte "
                        + "FROM Reporte "
                        + "WHERE idExpediente = ?";
                PreparedStatement prepararSentencia= conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idExpediente);
                ResultSet resultado = prepararSentencia.executeQuery();
                List<Reporte> reportes = new ArrayList();
                while(resultado.next()){
                    Reporte reporte = new Reporte();
                    reporte.setIdReporte(resultado.getInt("idReporte"));
                    reporte.setReporte(resultado.getBytes("reporte"));
                    reporte.setNombreReporte("nombreReporte");
                    reporte.setHorasReportadas(resultado.getInt("horasReportadas"));
                    reporte.setValidado(resultado.getString("validado"));
                    reporte.setNoReporte(resultado.getInt("noReporte"));
                    reporte.setIdExpediente(idExpediente);
                    reportes.add(reporte);
                }
                respuesta.put("error",false);
                respuesta.put("reportes", reportes);
                conexionBD.close();
            }catch (SQLException e){
                respuesta.put("mensaje", e.getMessage());
            }
        }else{
            respuesta.put("error", "Error de coneccion");
        }
        return respuesta;        
    }        

	private static Reporte convertirReporteResult(ResultSet resultado) throws SQLException {
		Reporte reporte = new Reporte();
		reporte.setIdReporte(resultado.getInt("idReporte"));
		reporte.setNombreReporte(resultado.getString("nombreReporte"));
		reporte.setReporte(resultado.getBytes("reporte"));
		reporte.setHorasReportadas(resultado.getInt("horasReportadas"));
		reporte.setValidado(resultado.getString("validado"));
		reporte.setNoReporte(resultado.getInt("noReporte"));
		return reporte;
	}

	public static HashMap<String, Object> subirReporte(Reporte reporte) throws SQLException {
		HashMap<String, Object> respuesta = new HashMap<>();
		respuesta.put("error", true);
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String sentencia = "INSERT INTO reporte (reporte, nombreReporte, horasReportadas, validado, noReporte, idExpediente) "
						+ "VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
				prepararSentencia.setBytes(1, reporte.getReporte());
				prepararSentencia.setString(2, reporte.getNombreReporte());
				prepararSentencia.setInt(3, reporte.getHorasReportadas());
				prepararSentencia.setString(4, reporte.getValidado());
				prepararSentencia.setInt(5, reporte.getNoReporte());
				prepararSentencia.setInt(6, reporte.getIdExpediente());
				int filasAfectadas = prepararSentencia.executeUpdate();
				if (filasAfectadas > 0) {
					respuesta.put("error", false);
					respuesta.put("mensaje", "El reporte se ha guardado correctamente");
				} else {
					respuesta.put("mensaje", "Lo sentimos, hubo un error al guardar el reporte, por favor revisa la información.");
				}
			} catch (SQLException ex) {
				respuesta.put("mensaje", ex.getMessage());
			} finally {
				conexionBD.close();
			}
		} else {
			respuesta.put("mensaje", "Por el momento el servicio no está disponible, inténtalo más tarde.");
		}
		return respuesta;
	}

	public static HashMap<String, Object> DarDeBaja(int idExpediente) throws SQLException {
		HashMap<String, Object> respuesta = new HashMap<>();
		respuesta.put("error", true);
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			String sentencia = "DELETE FROM Reporte "
					+ "WHERE idExpediente = ?";
			try {
				PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
				prepararSentencia.setInt(1, idExpediente);
				int filasAfectadas = prepararSentencia.executeUpdate();
				if (filasAfectadas > 0) {
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
}
