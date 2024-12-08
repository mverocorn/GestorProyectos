/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InscripcionEEDAOTest {
    
    public InscripcionEEDAOTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testObtenerAlumnosAsignados() throws Exception {
    }

    @Test
    public void testRegistrarInscripcion() {
        int idAlumno = 1;  // ID del alumno a asignar
        int idEE = 1;      // ID de la experiencia educativa

        try {
            // Realizar la asignación
            InscripcionEEDAO.registrarInscripcion(idAlumno, idEE);

            // Verificar que el alumno está asignado a la EE
            Connection conexionBD = ConexionBD.abrirConexion();
            String consulta = "SELECT * FROM inscripcionee WHERE idAlumno = ? AND idEE = ?";
            PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
            prepararConsulta.setInt(1, idAlumno);
            prepararConsulta.setInt(2, idEE);
            ResultSet resultado = prepararConsulta.executeQuery();

            assertTrue(resultado.next());
            assertEquals(idAlumno, resultado.getInt("idAlumno"));
            assertEquals(idEE, resultado.getInt("idEE"));
            assertEquals("inscrito", resultado.getString("estadoInscripcion"));

            // Cerramos la conexión
            conexionBD.close();

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error en la base de datos: " + e.getMessage());
        }
    }
    
}
