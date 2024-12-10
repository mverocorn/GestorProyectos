package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.pojo.Alumno;
import java.sql.SQLException;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AlumnoDAOTest {

    private AlumnoDAO alumnoDAO;
    private Alumno alumno;

    public AlumnoDAOTest() {
    }

    @Before
    public void setUp() {
        alumnoDAO = new AlumnoDAO();
        alumno = new Alumno();
    }

    @Test
    public void testValidarAlumnoARegistrar() {
    }

    @Test
    public void testExisteAlumno() throws Exception {
    }

    @Test
    public void testRegistrarAlumnoExitoso() throws SQLException {
        int valorEsperado = 1;

        alumno = new Alumno(null, "Tomas", "Gutierrez",
                "S22013079", "+52 1234567890", "tomas@correo.com",
                9.5f, "inscrito", "Password1."
        );

        HashMap<String, Object> resultado = AlumnoDAO.registrarAlumno(alumno);

        int valorObtenido = (int) resultado.get("idAlumno");

        assertEquals(valorEsperado, valorObtenido);
    }

    @Test(expected = SQLException.class)
    public void testRegistrarAlumnoExcepcionSQL() throws SQLException {
        alumno = new Alumno(null, "Tomas", "Gutierrez",
                "S22013079", "+52 1234567890", "tomas@correo.com",
                9.5f, "inscrito", "Password1."
        );

        AlumnoDAO.registrarAlumno(alumno);
    }

    @Test
    public void testObtenerAlumnos() throws Exception {
    }

    @Test
    public void testSerializarAlumno() throws Exception {
    }

}
