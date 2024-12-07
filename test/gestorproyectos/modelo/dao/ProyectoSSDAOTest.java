package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.pojo.ProyectoSS;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;

public class ProyectoSSDAOTest {
    
    private ProyectoSSDAO proyectoSSDAO;
    private ProyectoSS proyectoSS;
    
    public ProyectoSSDAOTest() {
    }
    
    @Before
    public void setUp() {
        proyectoSSDAO = new ProyectoSSDAO();
        // Inicializar el proyectoSS con valores de prueba que serán reutilizados en los tests
        proyectoSS = new ProyectoSS(
                0, Date.valueOf("2024-12-25"),
                "ENE2024-JUL2024", "Proyecto Innovador",
                "Desarrollar tecnología innovadora.",
                "Este proyecto tiene como objetivo desarrollar nuevas soluciones tecnológicas.",
                30, 1, 1
        );
    }

    @Test
    public void testValidarProyectoSS() {
    }

    @Test
    public void testExisteProyectoSS() throws Exception {
    }

    @Test
    public void testRegistrarProyectoSSExitoso() throws SQLException {
        int valorEsperado = 1;

        // Usamos el objeto proyectoSS ya inicializado en el método @Before
        HashMap<String, Object> resultado = ProyectoSSDAO.registrarProyectoSS(proyectoSS);

        int valorObtenido = (int) resultado.get("idProyectoSS");

        assertEquals(valorEsperado, valorObtenido);
    }

    @Test(expected = SQLException.class)
    public void testRegistrarProyectoSSExcepcionSQL() throws SQLException {
        ProyectoSS proyecto = new ProyectoSS(
                0, Date.valueOf("2024-01-01"),
                "ENE2024-JUL2024", "Proyecto Innovador",
                "Desarrollar tecnología innovadora.",
                "Este proyecto tiene como objetivo desarrollar nuevas soluciones tecnológicas.",
                30,1, 1 
        );

        ProyectoSSDAO.registrarProyectoSS(proyecto);
    }
}
