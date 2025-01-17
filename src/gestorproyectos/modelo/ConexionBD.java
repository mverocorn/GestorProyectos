package gestorproyectos.modelo;

import gestorproyectos.utilidades.Constantes;
import java.sql.*;
import java.util.logging.*;

public class ConexionBD {

	private static final Logger logger = Logger.getLogger(ConexionBD.class.getName());

	public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String NOMBRE_BD = "gestorproyectos";
	public static final String IP = "localhost";
	public static final String PUERTO = "3306";

	static {
		Logger rootLogger = Logger.getLogger("");
		ConsoleHandler consoleHandler = null;

		for (Handler handler : rootLogger.getHandlers()) {
			if (handler instanceof ConsoleHandler) {
				consoleHandler = (ConsoleHandler) handler;
				break;
			}
		}

		if (consoleHandler != null) {
			rootLogger.removeHandler(consoleHandler);
		}
	}

	public static Connection abrirConexion() throws SQLException {
		Connection conexionBD = null;
		String urlConexion = String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", IP, PUERTO, NOMBRE_BD);

		try {
			Class.forName(DRIVER);

			conexionBD = DriverManager.getConnection(urlConexion, Constantes.USER_BD, Constantes.PASSWORD_BD);
		} catch (ClassNotFoundException ex) {
			logger.log(Level.SEVERE, "Driver de MySQL no encontrado.", ex);
			throw new SQLException("Driver de MySQL no encontrado.", ex);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error al establecer la conexión con la base de datos", e);
			throw e;
		}
		return conexionBD;
	}

	public static void cerrarConexion(Connection conexion) {
		if (conexion != null) {
			try {
				conexion.close();
			} catch (SQLException ex) {
				logger.log(Level.SEVERE, "Error al cerrar la conexión con la base de datos", ex);
			}
		}
	}

	public static boolean verificarConexionBD() {
		try (Connection connection = abrirConexion()) {
			return true;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "No hay conexión con la base de datos", e);
			return false;
		}
	}
}
