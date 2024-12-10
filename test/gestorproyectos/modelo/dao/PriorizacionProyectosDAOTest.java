/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
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
}