package gestorproyectos.modelo.pojo;

public class EE {
    private int idEE;
    private String nombreEE;
    private int nrc;
    private int bloque;
    private int idProfesor;

    public EE(int idEE, String nombreEE, int nrc, int bloque, int idProfesor) {
        this.idEE = idEE;
        this.nombreEE = nombreEE;
        this.nrc = nrc;
        this.bloque = bloque;
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

    public int getBloque() {
        return bloque;
    }

    public void setBloque(int bloque) {
        this.bloque = bloque;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }
}
