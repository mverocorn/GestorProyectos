package gestorproyectos.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Responsable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponsableDAO {

    private static final Logger logger = Logger.getLogger(
        ResponsableDAO.class.getName()
    );

    public static List<Responsable> obtenerResponsables() throws SQLException {
        List<Responsable> responsables = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idResponsable, nombreResponsable, apellidoResponsable, correoResponsable, cargoResponsable FROM responsable;";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararConsulta.executeQuery();
                responsables = new ArrayList<>();
                while (resultado.next()) {
                    responsables.add(serializarResponsable(resultado));
                }
            } catch (SQLException e) {
                responsables = null;
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return responsables;
    }

    private static Responsable serializarResponsable(ResultSet resultado) throws SQLException {
        Responsable responsable = new Responsable();
        responsable.setIdResponsable(resultado.getInt("idResponsable"));
        responsable.setNombreResponsable(resultado.getString("nombreResponsable"));
        responsable.setApellidoResponsable(resultado.getString("apellidoResponsable"));
        responsable.setCorreoResponsable(resultado.getString("correoResponsable"));
        responsable.setCargoResponsable(resultado.getString("cargoResponsable"));
        return responsable;
    }

    public static Responsable obtenerResponsablePorIdResponsable(int idResponsable) throws SQLException {
        Responsable responsable = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT * FROM responsable WHERE idResponsable = ?";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                prepararConsulta.setInt(1, idResponsable);
                ResultSet resultado = prepararConsulta.executeQuery();
                if (resultado.next()) {
                    responsable = new Responsable(
                        resultado.getInt("idResponsable"),
                        resultado.getString("nombreResponsable"),
                        resultado.getString("apellidoResponsable"),
                        resultado.getString("correoResponsable"),
                        resultado.getString("cargoResponsable")
                    );
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error al obtener Responsable por ID", ex);
                throw new SQLException("Error al obtener Responsable por ID", ex);
            } finally {
                conexionBD.close();
            }
        }
        return responsable;
    }

}
