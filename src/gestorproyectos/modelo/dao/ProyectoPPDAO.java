package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import gestorproyectos.modelo.pojo.ProyectoPP;
import java.util.logging.Logger;

public class ProyectoPPDAO {

    private static final Logger logger = Logger.getLogger(
            ProyectoPPDAO.class.getName()
    );

    public static List<ProyectoPP> obtenerProyectosPP() throws SQLException {
        List<ProyectoPP> proyectosPP = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idProyectoPP, fechaProyecto, periodoProyecto, nombreProyecto, objetivoProyecto, descripcionProyecto, cupoProyecto, idEmpresa, idResponsable FROM proyectopp;";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararConsulta.executeQuery();
                proyectosPP = new ArrayList<>();
                while (resultado.next()) {
                    proyectosPP.add(serializarProyectoPP(resultado));
                }
            } catch (SQLException e) {
                proyectosPP = null;
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return proyectosPP;
    }

    private static ProyectoPP serializarProyectoPP(ResultSet resultado) throws SQLException {
        ProyectoPP proyectoPP = new ProyectoPP();

        proyectoPP.setIdProyectoPP(resultado.getInt("idProyectoPP"));
        proyectoPP.setFechaProyecto(resultado.getDate("fechaProyecto"));
        proyectoPP.setPeriodoProyecto(resultado.getString("periodoProyecto"));
        proyectoPP.setNombreProyecto(resultado.getString("nombreProyecto"));
        proyectoPP.setObjetivoProyecto(resultado.getString("objetivoProyecto"));
        proyectoPP.setDescripcionProyecto(resultado.getString("descripcionProyecto"));
        proyectoPP.setCupoProyecto(resultado.getInt("cupoProyecto"));
        proyectoPP.setIdEmpresa(resultado.getInt("idEmpresa"));
        proyectoPP.setIdResponsable(resultado.getInt("idResponsable"));

        return proyectoPP;
    }

}
