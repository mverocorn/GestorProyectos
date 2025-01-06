package gestorproyectos.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Empresa;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmpresaDAO {

	private static final Logger logger = Logger.getLogger(
			EmpresaDAO.class.getName()
	);

	public static List<Empresa> obtenerEmpresas() throws SQLException {
		List<Empresa> empresas = null;
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String consulta = "SELECT idEmpresa, nombreEmpresa, correoEmpresa, "
						+ "telefonoEmpresa, calleEmpresa, colonia, numExt, "
						+ "codigoPostal FROM empresa;";

				PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
				ResultSet resultado = prepararConsulta.executeQuery();
				empresas = new ArrayList<>();
				while (resultado.next()) {
					empresas.add(serializarEmpresa(resultado));
				}
			} catch (SQLException e) {
				empresas = null;
				e.printStackTrace();
			} finally {
				conexionBD.close();
			}
		}
		return empresas;
	}

	private static Empresa serializarEmpresa(ResultSet resultado) throws SQLException {
		Empresa empresa = new Empresa();
		empresa.setIdEmpresa(resultado.getInt("idEmpresa"));
		empresa.setNombreEmpresa(resultado.getString("nombreEmpresa"));
		empresa.setCorreoEmpresa(resultado.getString("correoEmpresa"));
		empresa.setTelefonoEmpresa(resultado.getString("telefonoEmpresa"));
		empresa.setCalleEmpresa(resultado.getString("calleEmpresa"));
		empresa.setColonia(resultado.getString("colonia"));
		empresa.setNumExt(resultado.getString("numExt"));
		empresa.setCodigoPostal(resultado.getString("codigoPostal"));
		return empresa;
	}

	public static Empresa obtenerEmpresaPorIdEmpresa(int idEmpresa) throws SQLException {
		Empresa empresa = null;
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String consulta = "SELECT * FROM empresa WHERE idEmpresa = ?";
				PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
				prepararConsulta.setInt(1, idEmpresa);
				ResultSet resultado = prepararConsulta.executeQuery();
				if (resultado.next()) {
					empresa = new Empresa(
							resultado.getInt("idEmpresa"),
							resultado.getString("nombreEmpresa"),
							resultado.getString("correoEmpresa"),
							resultado.getString("telefonoEmpresa"),
							resultado.getString("calleEmpresa"),
							resultado.getString("colonia"),
							resultado.getString("numExt"),
							resultado.getString("codigoPostal")
					);
				}
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, "Error al obtener Empresa por ID", ex);
				throw new SQLException("Error al obtener Empresa por ID", ex);
			} finally {
				conexionBD.close();
			}
		}
		return empresa;
	}

}
