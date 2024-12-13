package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.pojo.ProyectoSS;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProyectoSSDAOTest {

    private ProyectoSSDAO proyectoSSDAO;
    private ProyectoSS proyectoSS;

    public ProyectoSSDAOTest() {
    }

    @Before
    public void setUp() {
        proyectoSSDAO = new ProyectoSSDAO();
        proyectoSS = new ProyectoSS();
    }

    @Test
    public void testValidarProyectoSS() {
    }

    @Test
    public void testExisteProyectoSS() throws Exception {
    }

    @Test
    public void testRegistrarProyectoSSExitoso() throws SQLException {
        int valorEsperado = 9;

        proyectoSS = new ProyectoSS(
                0, "01ENE2024-30JUN2024", "Proyecto Innovador",
                "Desarrollar tecnología innovadora.",
                "Este proyecto tiene como objetivo desarrollar nuevas soluciones tecnológicas.",
                30, 1
        );

        HashMap<String, Object> resultado = ProyectoSSDAO.registrarProyectoSS(proyectoSS);

        int valorObtenido = (int) resultado.get("idProyectoSS");

        assertEquals(valorEsperado, valorObtenido);
    }

    @Test(expected = SQLException.class)
    public void testRegistrarProyectoSSExcepcionSQL() throws SQLException {
        ProyectoSS proyecto = new ProyectoSS(
                0, "01ENE2024-30JUN2024", "Proyecto Innovador",
                "Desarrollar tecnología innovadora.",
                "Este proyecto tiene como objetivo desarrollar nuevas soluciones tecnológicas.",
                30, 1
        );

        ProyectoSSDAO.registrarProyectoSS(proyecto);
    }

    @Test
    public void testObtenerProyectosSS() throws Exception {
    }

    @Test
    public void testRegistrarProyectoSS() throws Exception {
    }

    @Test
    public void testObtenerProyectoSSPorIdProyectoSS() throws SQLException {
        ProyectoSS proyectoEsperado = new ProyectoSS(
                3,"01ENE2024-30JUN2024", 
                "Implementación de ERP",
                "Implementar sistemas ERP en pequeñas empresas.",
                "Optimizar procesos internos en las empresas.",
                4,8
        );

        ProyectoSS proyectoObtenido = ProyectoSSDAO.obtenerProyectoSSPorIdProyectoSS(3);

        assertEquals(proyectoEsperado, proyectoObtenido);
    }

    @Test
    public void testObtenerProyectosDisponiblesPorPeriodo() throws SQLException {
        List<ProyectoSS> listaEsperada = new ArrayList<>();
        listaEsperada.add(new ProyectoSS(3, null, "Implementación de ERP", 
            null, null, 0, 0));
        listaEsperada.add(new ProyectoSS(4, null, "Gestión de Infraestructura", 
            null, null, 0, 0));
        listaEsperada.add(new ProyectoSS(5, null, "Diseño Educativo", 
            null, null, 0, 0));
        listaEsperada.add(new ProyectoSS(9, null, "Proyecto Innovador", 
            null, null, 0, 0));

        List<ProyectoSS> listaObtenida = proyectoSSDAO.obtenerProyectosDisponiblesPorPeriodoDeEESS("AGO2024-ENE2025");

        assertEquals(listaEsperada.size(), listaObtenida.size());

        for (int i = 0; i < listaEsperada.size(); i++) {
            ProyectoSS esperado = listaEsperada.get(i);
            ProyectoSS obtenido = listaObtenida.get(i);
            assertEquals(esperado.getIdProyectoSS(), obtenido.getIdProyectoSS());
            assertEquals(esperado.getNombreProyecto(), obtenido.getNombreProyecto());
        }
    }

    @Test
    public void testObtenerPriorizacionDeAlumnoProyectoSS() throws SQLException {
        List<ProyectoSS> proyectos = ProyectoSSDAO.obtenerPriorizacionDeAlumnoProyectoSS(1);

        assertNotNull(proyectos);
        assertEquals(4, proyectos.size()); 

        ProyectoSS proyecto1 = proyectos.get(0);
        assertEquals(3, proyecto1.getIdProyectoSS());
        assertEquals("Implementación de ERP", proyecto1.getNombreProyecto());
        assertEquals(4, proyecto1.getCupoProyecto());
        assertEquals(1, proyecto1.getPrioridad());

        ProyectoSS proyecto2 = proyectos.get(1);
        assertEquals(4, proyecto2.getIdProyectoSS());
        assertEquals("Gestión de Infraestructura", proyecto2.getNombreProyecto());
        assertEquals(6, proyecto2.getCupoProyecto());
        assertEquals(2, proyecto2.getPrioridad());

        ProyectoSS proyecto3 = proyectos.get(2);
        assertEquals(5, proyecto3.getIdProyectoSS());
        assertEquals("Diseño Educativo", proyecto3.getNombreProyecto());
        assertEquals(12, proyecto3.getCupoProyecto());
        assertEquals(3, proyecto3.getPrioridad());

        ProyectoSS proyecto4 = proyectos.get(3);
        assertEquals(9, proyecto4.getIdProyectoSS());
        assertEquals("Proyecto Innovador", proyecto4.getNombreProyecto());
        assertEquals(30, proyecto4.getCupoProyecto());
        assertEquals(4, proyecto4.getPrioridad());
    }

}
