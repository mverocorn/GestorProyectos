package gestorproyectos.modelo.pojo;

public class EE {
    private int idEE;
    private String nombreEE;
    private int nrc;
    private int seccion;
    private String periodo;
    private int idProfesor;

    public EE(int idEE, String nombreEE, int nrc, int seccion, String periodo, int idProfesor) {
        this.idEE = idEE;
        this.nombreEE = nombreEE;
        this.nrc = nrc;
        this.seccion = seccion;
        this.periodo = periodo;
        this.idProfesor = idProfesor;
    }

    public EE() {
    }

    public int getIdEE() {
        return idEE;
    }

    public void setIdEE(int idEE) {
        this.idEE = idEE;
    }

    public String getNombreEE() {
        return nombreEE;
    }

    public void setNombreEE(String nombreEE) {
        this.nombreEE = nombreEE;
    }

    public int getNrc() {
        return nrc;
    }

    public void setNrc(int nrc) {
        this.nrc = nrc;
    }

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }
}
