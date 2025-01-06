
package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import static org.junit.Assert.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class PriorizacionProyectosDAOTest {
    
    public PriorizacionProyectosDAOTest() {
    }
    
    @Before
    public void setUp() {
    }


    @Test
    public void testGuardarPriorizacionProyectosSS() throws Exception {
        int idAlumno = 2;

        int idEE = EEDAO.obtenerIdEEPorIdAlumno(idAlumno);

        Map<Integer, Integer> proyectosYPrioridades = new HashMap<>();
        proyectosYPrioridades.put(3, 1);
        proyectosYPrioridades.put(4, 2);
        proyectosYPrioridades.put(5, 3); 
        proyectosYPrioridades.put(9, 4); 

        PriorizacionProyectosDAO.guardarPriorizacionProyectosSS(idEE, proyectosYPrioridades);

        Connection conexionBD = ConexionBD.abrirConexion();
        String consulta = "SELECT * FROM priorizacionproyectos WHERE idInscripcionEE = ?";
        PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
        prepararConsulta.setInt(1, idEE);

        ResultSet resultado = prepararConsulta.executeQuery();
        int count = 0;

        while (resultado.next()) {
            count++;
        }
        assertEquals(4, count);

        conexionBD.close();
    }
    
    @Test
    public void testGuardarPriorizacionProyectosPP() throws Exception {
        int idAlumno = 2;

        int idEE = EEDAO.obtenerIdEEPorIdAlumno(idAlumno);

        Map<Integer, Integer> proyectosYPrioridades = new HashMap<>();
        proyectosYPrioridades.put(2, 1);
        proyectosYPrioridades.put(3, 2);
        proyectosYPrioridades.put(7, 3);

        PriorizacionProyectosDAO.guardarPriorizacionProyectosPP(idEE, proyectosYPrioridades);

        Connection conexionBD = ConexionBD.abrirConexion();
        String consulta = "SELECT * FROM priorizacionproyectos WHERE idInscripcionEE = ?";
        PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
        prepararConsulta.setInt(1, idEE);

        ResultSet resultado = prepararConsulta.executeQuery();
        int count = 0;

        while (resultado.next()) {
            count++;
        }
        assertEquals(3, count);

        conexionBD.close();
    }

    @Test
    public void testValidarPriorizacion() throws Exception {
    }

    @Test
    public void testObtenerPriorizacionDeAlumnoProyectoSS() throws Exception {
    }

    @Test
    public void testObtenerPriorizacionDeAlumnoProyectoPP() throws Exception {
    }

    @Test
    public void testAsignarProyectoSS() throws SQLException {
        int idAlumno = 1;
        int idProyectoSS = 3;
	int idInscripcionEE = 1;

        PriorizacionProyectosDAO.asignarProyectoSS(idAlumno, idProyectoSS, idInscripcionEE);

        Connection conexionBD = ConexionBD.abrirConexion();
        String consulta = "SELECT idProyectoSS FROM inscripcionee WHERE idAlumno = ? AND estadoInscripcion = 'inscrito'";
        PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
        prepararConsulta.setInt(1, idAlumno);

        ResultSet resultado = prepararConsulta.executeQuery();

        boolean proyectoAsignado = false;
        while (resultado.next()) {
            if (resultado.getInt("idProyectoSS") == idProyectoSS) {
                proyectoAsignado = true;
                break;
            }
        }

        assertTrue(proyectoAsignado);

        conexionBD.close();
    }

    @Test
    public void testAsignarProyectoPP() throws Exception {
    }
}
