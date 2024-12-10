package gestorproyectos.modelo.pojo;

public class PriorizacionProyectos {

    private int idPriorizacionProyectos;
    private int idInscripcionEE;
    private Integer idProyectoPP;
    private Integer idProyectoSS;
    private int prioridad;

    public PriorizacionProyectos(int idPriorizacionProyectos, int idInscripcionEE, 
        Integer idProyectoPP, Integer idProyectoSS, int prioridad) {
        this.idPriorizacionProyectos = idPriorizacionProyectos;
        this.idInscripcionEE = idInscripcionEE;
        this.idProyectoPP = idProyectoPP;
        this.idProyectoSS = idProyectoSS;
        this.prioridad = prioridad;
    }

    public int getIdPriorizacionProyectos() {
        return idPriorizacionProyectos;
    }

    public void setIdPriorizacionProyectos(int idPriorizacionProyectos) {
        this.idPriorizacionProyectos = idPriorizacionProyectos;
    }

    public int getIdInscripcionEE() {
        return idInscripcionEE;
    }

    public void setIdInscripcionEE(int idInscripcionEE) {
        this.idInscripcionEE = idInscripcionEE;
    }

    public Integer getIdProyectoPP() {
        return idProyectoPP;
    }

    public void setIdProyectoPP(Integer idProyectoPP) {
        this.idProyectoPP = idProyectoPP;
    }

    public Integer getIdProyectoSS() {
        return idProyectoSS;
    }

    public void setIdProyectoSS(Integer idProyectoSS) {
        this.idProyectoSS = idProyectoSS;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }
}
