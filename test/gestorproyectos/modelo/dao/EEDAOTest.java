package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.pojo.InscripcionProyecto;
import java.sql.SQLException;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EEDAOTest {

    private EEDAO eeDAO;
    private EE ee;
    private InscripcionProyecto inscripcionProyecto;

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
    public void testRegistrarEEConPeriodoExitoso() throws SQLException {
        int valorEsperadoEE = 1;
        int valorEsperadoInscripcion = 1;
        String periodo = "ENE2024-JUL2024";
        EE ee = new EE(0, "Servicio Social", 12301, 2, 1);

        HashMap<String, Object> resultado = EEDAO.registrarEEConPeriodo(ee, periodo);

        int idEEObtenido = (int) resultado.get("idEE");
        assertEquals(valorEsperadoEE, idEEObtenido);

        int idInscripcionObtenido = (int) resultado.get("idInscripcion");
        assertEquals(valorEsperadoInscripcion, idInscripcionObtenido);
    }

}
