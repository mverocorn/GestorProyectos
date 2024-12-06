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
        alumno = new Alumno(null, "Tomas", "Gutierrez",
                "S22013679", "+52 1234567890", "tomas@correo.com",
                9.5f, "inscrito"
        );

        HashMap<String, Object> resultado = AlumnoDAO.registrarAlumno(alumno);

        int idAlumnoGenerado = (int) resultado.get("idAlumno");

        assertTrue(idAlumnoGenerado > 0);
    }

    @Test(expected = SQLException.class)
    public void testRegistrarAlumnoExcepcionSQL() throws SQLException {
        alumno = new Alumno(
                null, // ID se genera automáticamente
                "Tomas",
                "Gutierrez",
                "S22013679", // Usar una matrícula que ya exista en la base de datos
                "123456789",
                "tomas@correo.com", // Usar un correo que ya exista
                9.5f,
                "inscrito"
        );

        AlumnoDAO.registrarAlumno(alumno); // Debería lanzar SQLException
    }
}
