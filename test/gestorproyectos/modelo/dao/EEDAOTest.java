package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.pojo.InscripcionEE;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class EEDAOTest {

    private EEDAO eeDAO;
    private EE ee;
    private InscripcionEE inscripcionProyecto;

    public EEDAOTest() {
    }

    @Before
    public void setUp() {
        eeDAO = new EEDAO();
        ee = new EE();
    }

    @Test
    public void testValidarEE() {
    }

    @Test
    public void testExisteEE() throws Exception {
    }

    @Test
    public void testRegistrarEEExitoso() throws SQLException {
        int valorEsperado = 1;

        ee = new EE(0, "Servicio Social", 12301, 2, 
                "FEB2024-JUL2024", 1);

        HashMap<String, Object> resultado = EEDAO.registrarEE(ee);

        int valorObtenido = (int) resultado.get("idEE");

        assertEquals(valorEsperado, valorObtenido);
    }

    @Test
    public void testObtenerEE() throws Exception {
    }

    @Test
    public void testObtenerNombresProyectosPorTipo() throws Exception {
    }

    @Test
    public void testRegistrarEE() throws Exception {
    }

    @Test
    public void testObtenerDetalleEE() throws Exception {
    }

    @Test
    public void testIsPeriodoActivo() throws Exception {
    }

    @Test
    public void testObtenerIdEEPorIdAlumno() throws SQLException {
        int idAlumno = 1;
        int valorEsperado = 1;

        
        int valorObtenido = EEDAO.obtenerIdEEPorIdAlumno(idAlumno);

        assertEquals(valorEsperado, valorObtenido);
    }

}
