
package gestorproyectos.modelo.pojo;

/**
 *
 * @author jonathan
 */
public class Expediente {

    private int idExpediente;
    private String fechaInicioProyecto;
    private int horasRestantes;
    private String fechaFinProyecto;
    private int horasAcumuladas;
    private int idInscripcionEE;

    public Expediente() {
    }

    public Expediente(int idExpediente, String fechaInicioProyecto, int horasRestantes, String fechaFinProyecto, int horasAcumuladas, int idInscripcionEE) {
        this.idExpediente = idExpediente;
        this.fechaInicioProyecto = fechaInicioProyecto;
        this.horasRestantes = horasRestantes;
        this.fechaFinProyecto = fechaFinProyecto;
        this.horasAcumuladas = horasAcumuladas;
        this.idInscripcionEE = idInscripcionEE;
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public String getFechaInicioProyecto() {
        return fechaInicioProyecto;
    }

    public void setFechaInicioProyecto(String fechaInicioProyecto) {
        this.fechaInicioProyecto = fechaInicioProyecto;
    }

    public int getHorasRestantes() {
        return horasRestantes;
    }

    public void setHorasRestantes(int horasRestantes) {
        this.horasRestantes = horasRestantes;
    }

    public String getFechaFinProyecto() {
        return fechaFinProyecto;
    }

    public void setFechaFinProyecto(String fechaFinProyecto) {
        this.fechaFinProyecto = fechaFinProyecto;
    }

    public int getHorasAcumuladas() {
        return horasAcumuladas;
    }

    public void setHorasAcumuladas(int horasAcumuladas) {
        this.horasAcumuladas = horasAcumuladas;
    }

    public int getIdInscripcionEE() {
        return idInscripcionEE;
    }

    public void setIdInscripcionEE(int idInscripcionEE) {
        this.idInscripcionEE = idInscripcionEE;
    }    
}
