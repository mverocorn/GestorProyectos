package gestorproyectos.modelo.dao;

import gestorproyectos.modelo.pojo.EE;
import java.sql.SQLException;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EEDAOTest {

    private EEDAO eeDAO;
    private EE ee;

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
                1);

        HashMap<String, Object> resultado = EEDAO.registrarEE(ee);

        int valorObtenido = (int) resultado.get("idEE");

        assertEquals(valorEsperado, valorObtenido);
    }

    @Test(expected = SQLException.class)
    public void testRegistrarEEExcepcionSQL() throws SQLException {
        ee = new EE(0, "Servicio Social", 12301, 2, 
                999);

        EEDAO.registrarEE(ee);
    }
}
