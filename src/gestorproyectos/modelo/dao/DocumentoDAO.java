/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Documento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author jonathan
 */
public class DocumentoDAO {

	public static HashMap<String, Object> subirDocumento(Documento documento) throws SQLException {
		HashMap<String, Object> respuesta = new HashMap<>();
		respuesta.put("error", true);
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String sentencia = "INSERT INTO documento (documento, nombreDocumento, fechaEntrega, validado, idExpediente) "
						+ "VALUES (?, ?, ?, ?, ?)";
				PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
				prepararSentencia.setBytes(1, documento.getDocumento());
				prepararSentencia.setString(2, documento.getNombreDocumento());
				prepararSentencia.setString(3, documento.getFechaEntrega());
				prepararSentencia.setString(4, documento.getValidado());
				prepararSentencia.setInt(5, documento.getIdExpediente());
				int filasAfectadas = prepararSentencia.executeUpdate();
				if (filasAfectadas > 0) {
					respuesta.put("error", false);
					respuesta.put("mensaje", "El documento se ha guardado correctamente");
				} else {
					respuesta.put("mensaje", "Lo sentimos, hubo un error al guardar el documento, por favor revisa la información.");
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

	public static List<Documento> obtenerDocumentos(int idExpediente) throws SQLException {
		List<Documento> documentos = null;
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String consulta = "SELECT idDocumento, nombreDocumento , documento, fechaEntrega, validado "
						+ "FROM Documento "
						+ "WHERE idExpediente = ? ";
				PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
				prepararSentencia.setInt(1, idExpediente);
				ResultSet resultado = prepararSentencia.executeQuery();
				documentos = new ArrayList<>();
				while (resultado.next()) {
					documentos.add(convertirDocumentoResult(resultado));
				}
			} catch (SQLException e) {
				documentos = null;
				e.printStackTrace();
			} finally {
				conexionBD.close();
			}
		}
		return documentos;
	}

	private static Documento convertirDocumentoResult(ResultSet resultado) throws SQLException {
		Documento documento = new Documento();
		documento.setIdDocumento(resultado.getInt("idDocumento"));
		documento.setNombreDocumento(resultado.getString("nombreDocumento"));
		documento.setDocumento(resultado.getBytes("documento"));
		documento.setFechaEntrega(resultado.getString("fechaEntrega"));
		documento.setValidado(resultado.getString("validado"));
		return documento;
	}

	public static HashMap<String, Object> validarDocumento(int idDocumento) throws SQLException {
		HashMap<String, Object> respuesta = new HashMap<>();
		respuesta.put("error", true);
		LocalDate fechaActual = LocalDate.now();
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String sentencia = "UPDATE documento SET validado = 'Validado' "
						+ "WHERE idDocumento = ?;";
				PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
				prepararSentencia.setInt(1, idDocumento);
				int filasAfectadas = prepararSentencia.executeUpdate();
				if (filasAfectadas > 0) {
					respuesta.put("error", false);
					respuesta.put("mensaje", "Informacion del documento actualizada correctamente");
				} else {
					respuesta.put("mensaje", "Lo sentimos, hubo un error al validar el documento, por favor intente mas tarde");
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

	public static HashMap<String, Object> rechazarDocumento(int idDocumento) throws SQLException {
		HashMap<String, Object> respuesta = new HashMap<>();
		respuesta.put("error", true);
		LocalDate fechaActual = LocalDate.now();
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String sentencia = "UPDATE documento SET validado = 'Rechazado' "
						+ "WHERE idDocumento = ?;";
				PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
				prepararSentencia.setInt(1, idDocumento);
				int filasAfectadas = prepararSentencia.executeUpdate();
				if (filasAfectadas > 0) {
					respuesta.put("error", false);
					respuesta.put("mensaje", "Informacion del documento actualizada correctamente");
				} else {
					respuesta.put("mensaje", "Lo sentimos, hubo un error al validar el documento, por favor intente mas tarde");
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

	public static HashMap<String, Object> DarDeBaja(int idExpediente) throws SQLException {
		HashMap<String, Object> respuesta = new HashMap<>();
		respuesta.put("error", true);
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			String sentencia = "DELETE FROM Documento "
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

}
